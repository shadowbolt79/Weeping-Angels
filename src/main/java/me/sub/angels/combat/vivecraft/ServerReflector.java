package me.sub.angels.combat.vivecraft;

import me.sub.angels.WeepingAngels;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerReflector extends VivecraftReflector {

    private int enabled = -1;

    //Vivecraft Client/Non-VR
    private Field fVivePlayers;

    private Method mGetControllerDir;
    private Method mGetControllerPos;
    private Method mGetHMDDir;
    private Method mGetHMDPos;
    private Method isVR;

    //Vivecraft Forge Extensions
    private Field fVRPlayers;
    private Field fEntities;
    private Field fRotW, fRotX, fRotY, fRotZ;
    private Field fPosition;
    
    private Constructor<?> conQuaternion;
    
    private Method mIsVRPlayer;
    private Method mVecMultiply;

    @Override
    public boolean init() {
        enabled = 0;
        WeepingAngels.LOGGER.info("Checking for Vivecraft Client...");
        try {
            //Vivecraft Client/Non-VR
            Class<?> cNetworkHelper = Class.forName("org.vivecraft.api.NetworkHelper");
            Class<?> cVivePlayer = Class.forName("org.vivecraft.api.VivePlayer");

            fVivePlayers = cNetworkHelper.getDeclaredField("vivePlayers");

            mGetControllerDir = cVivePlayer.getDeclaredMethod("getControllerDir",int.class);
            mGetHMDDir = cVivePlayer.getDeclaredMethod("getHMDDir");
            mGetControllerPos = cVivePlayer.getDeclaredMethod("getControllerPos",int.class);
            mGetHMDPos = cVivePlayer.getDeclaredMethod("getHMDPos");
            isVR = cVivePlayer.getMethod("isVR");

            WeepingAngels.LOGGER.info("Vivecraft Client detected! Enabling compatibility features.");

        }catch (Exception e){
            enabled = -1;
        }

        if(enabled<0)
        {
            //Vivecraft Forge Extensions
            enabled = 1;
            try{
                Class<?> cVRPlayerData = Class.forName("com.techjar.vivecraftforge.util.VRPlayerData");
                Class<?> cEntityVRObject =  Class.forName("com.techjar.vivecraftforge.entity.EntityVRObject");
                Class<?> cProxyServer = Class.forName("com.techjar.vivecraftforge.proxy.ProxyServer");
                
                fVRPlayers = cProxyServer.getDeclaredField("vrPlayers");
                fEntities = cVRPlayerData.getDeclaredField("entities");
                
                fRotW = cEntityVRObject.getDeclaredField("rotW");
                fRotX = cEntityVRObject.getDeclaredField("rotX");
                fRotY = cEntityVRObject.getDeclaredField("rotY");
                fRotZ = cEntityVRObject.getDeclaredField("rotZ");
                fPosition = cEntityVRObject.getDeclaredField("position");
                
                mIsVRPlayer = cProxyServer.getDeclaredMethod("isVRPlayer", EntityPlayer.class);
                
                Class<?> cQuaternion = Class.forName("com.techjar.vivecraftforge.util.Quaternion");

                conQuaternion = cQuaternion.getConstructor(float.class,float.class,float.class,float.class);
                mVecMultiply = cQuaternion.getMethod("multiply", Vec3d.class);

                WeepingAngels.LOGGER.info("Vivecraft Forge Extensions detected! Enabling compatability features.");
            }catch (Exception e)
            {
                enabled = -1;
            }
        }

        if(enabled<0)
            WeepingAngels.LOGGER.info("Vivecraft not detected!");

        return enabled>=0;
    }

    @Override
    public boolean isVRPlayer(EntityPlayer player) {
        if(enabled<0)return false;
        try {
            UUID uuid = player.getUniqueID();
            if(enabled==0) {
                Map<UUID,?> vivePlayers = (Map<UUID,? extends Object>)fVivePlayers.get(null);
                if (vivePlayers.containsKey(uuid)) {
                    Object vivePlayer = vivePlayers.get(uuid);
                    return (boolean) isVR.invoke(vivePlayer);
                }
            }
            else if(enabled==1) {
                return (boolean)mIsVRPlayer.invoke(null,player);
            }

        } catch (Exception e) {
            WeepingAngels.LOGGER.warn("Vivecraft Server: Unknown Error Parsing isVRPlayer", e);
        }

        return false;
    }

    @Override
    public Vec3d getHMDPos(EntityPlayer player) {
        try {
            if(enabled==0) {
                UUID uuid = player.getUniqueID();
                //Network Character - attempt to get from NetworkHelper
                Map<UUID,?> vivePlayers = (Map<UUID,? extends Object>)fVivePlayers.get(null);
                if (vivePlayers.containsKey(uuid)) {
                    Object vivePlayer = vivePlayers.get(uuid);
                    return (Vec3d) mGetHMDPos.invoke(vivePlayer);
                }
            }
            else if(enabled==1)
            {
                Map<EntityPlayer, ?> vrPlayers = (Map<EntityPlayer,? extends Object>)fVRPlayers.get(null);
                if(vrPlayers.containsKey(player)) {
                    Object playerHead = ((List<? extends Object>)fEntities.get(vrPlayers.get(player))).get(0);
                    if(playerHead!=null)
                        return fPosition.get(playerHead);
                }
            }

        } catch (Exception e) {
            WeepingAngels.LOGGER.warn("Vivecraft Server: Unknown Error Parsing getHMDPos", e);
        }
        return player.getPositionVector().add(0, 1.62, 0);
    }

    @Override
    public Vec3d getHMDRot(EntityPlayer player) {
        try {
            UUID uuid = player.getUniqueID();
            if(enabled==0) {
                //Network Character - attempt to get from NetworkHelper
                Map<UUID,?> vivePlayers = (Map<UUID,? extends Object>)fVivePlayers.get(null);
                if (vivePlayers.containsKey(uuid)) {
                    Object vivePlayer = vivePlayers.get(uuid);
                    return (Vec3d) mGetHMDDir.invoke(vivePlayer);
                }
            }
            else if(enabled==1)
            {
                Map<EntityPlayer, ?> vrPlayers = (Map<EntityPlayer,? extends Object>)fVRPlayers.get(null);
                if(vrPlayers.containsKey(player)) {
                    Object playerHead = ((List<? extends Object>)fEntities.get(vrPlayers.get(player))).get(0);
                    if(playerHead!=null){
                        float W = fRotW.getFloat(playerHead);
                        float X = fRotX.getFloat(playerHead);
                        float Y = fRotY.getFloat(playerHead);
                        float Z = fRotZ.getFloat(playerHead);

                        Object quaternion = conQuaternion.newInstance(W,X,Y,Z);

                        return (Vec3d)mVecMultiply.invoke(quaternion,new Vec3d(0,0,-1));
                    }
                }
            }
        } catch (Exception e) {
            WeepingAngels.LOGGER.warn("Vivecraft Server: Unknown Error Parsing getHMDRot", e);
        }
        return player.getLookVec();
    }
}
