package me.sub.angels.combat.vivecraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public abstract class VivecraftReflector {
    public abstract boolean init();

    public abstract boolean isVRPlayer(EntityPlayer player);

    public abstract Vec3d getHMDPos(EntityPlayer player);
    public abstract Vec3d getHMDRot(EntityPlayer player);
}
