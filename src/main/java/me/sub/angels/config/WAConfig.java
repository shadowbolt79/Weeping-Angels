package me.sub.angels.config;

import me.sub.angels.WeepingAngels;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = WeepingAngels.MODID)
public class WAConfig {
	
	@Config.LangKey("category.weeping-angels.angels")
	public static final Angels angels = new Angels();
	
	@Config.LangKey("category.weeping-angels.spawn")
	public static final Spawn spawn = new Spawn();
	
	@Config.LangKey("category.weeping-angels.worldgen")
	public static final WorldGen worldGen = new WorldGen();

    @Config.LangKey("category.weeping-angels.mod_intergrations")
    public static Intergrations integrations = new Intergrations();

	public static class WorldGen {
		
		@Config.LangKey("config.weeping-angels.gen_arms")
		@Config.Comment("Config to toggle the generation of arms in snow biomes")
		public boolean arms = true;
		
		@Config.LangKey("config.weeping-angels.genCatacombs")
		@Config.Comment("Generate catacombs?")
		public boolean genCatacombs = false;
		
	}
	
	public static class Spawn {
		
		@Config.LangKey("config.weeping-angels.max_spawn")
		@Config.Comment("The maximum amount of angels per biome")
		@Config.RangeInt(max = 25)
		public int maximumSpawn = 4;
		
		@Config.LangKey("config.weeping-angels.spawn_probability")
		@Config.Comment("The angel spawn probability rate")
		public int spawnProbability = 50;
		
		@Config.LangKey("config.weeping-angels.min_spawn")
		@Config.Comment("The minimum amount of angels per biome")
		@Config.RangeInt(max = 24)
		public int minimumSpawn = 2;
		
		@Config.LangKey("config.weeping-angels.spawntype")
		@Config.Comment("This will only accept: WATER_CREATURE, AMBIENT, CREATURE, MONSTER || Anything else WILL crash your game.")
		@Config.RequiresMcRestart
		public EnumCreatureType spawnType = EnumCreatureType.MONSTER;
		
		@Config.LangKey("config.weeping-angels.disallowed_spawn_biomes")
		@Config.Comment("Note: A list of biomes where angels should NOT spawn.")
		public String[] notAllowedBiomes = { "minecraft:void", "minecraft:sky", "minecraft:hell", "minecraft:deep_ocean", "minecraft:ocean" };
		
		@Config.LangKey("config.weeping-angels.allowed_spawn_dimensions")
		@Config.Comment("Note: A list of dimension ids where angels should spawn.")
		public int[] dimensionWhitelist = { -1, 0, 1 };
		
	}
	
	public static class Angels {
		
		@Config.LangKey("config.weeping-angels.update_checker")
		@Config.Comment("Config to toggle the update available checker")
		public boolean enableUpdateChecker = true;
		
		@Config.LangKey("config.weeping-angels.angel_move_sound")
		@Config.Comment("Non-child angels play scraping sounds when moving, this toggles that")
		public boolean playScrapSounds = true;
		
		@Config.LangKey("config.weeping-angels.angel_seen_sound")
		@Config.Comment("Toggle seen sounds")
		public boolean playSeenSounds = true;
		
		@Config.LangKey("config.weeping-angels.angel_damage")
		@Config.Comment("The damage dealt by an angel")
		public double damage = 8.0D;
		
		@Config.LangKey("config.weeping-angels.angel_xp_value")
		@Config.Comment("XP gained from angels")
		public int xpGained = 25;
		
		@Config.LangKey("config.weeping-angels.teleport_instant")
		@Config.Comment("just teleport. no damage.")
		public boolean justTeleport = false;
		
		@Config.LangKey("config.weeping-angels.teleportRange")
		@Config.Comment("The maximum range a user can be teleported by the Angels")
		public int teleportRange = 450;
		
		@Config.LangKey("config.weeping-angels.angeldimteleport")
		@Config.Comment("If this is enabled, angel teleporting can also tp the player to other dimensions")
		public boolean angelDimTeleport = true;
		
		@Config.LangKey("config.weeping-angels.angel.block_break")
		@Config.Comment("If this is enabled, angels will break blocks (If gamerules allow)")
		public boolean blockBreaking = true;
		
		@Config.LangKey("config.weeping-angels.block_break_range")
		@Config.Comment("The maximum range a angel can break blocks within")
		public int blockBreakRange = 25;
		
		@Config.LangKey("config.weeping-angels.chicken_go_boom")
		@Config.Comment("If this is enabled, the timey wimey detector can blow up chickens when in use randomly")
		public boolean chickenGoboom = true;
		
		@Config.LangKey("config.weeping-angels.blowout_torch")
		@Config.Comment("If this is enabled, baby angels will blow out torches")
		public boolean torchBlowOut = true;
		
		@Config.LangKey("config.weeping-angels.disallowed_blocks")
		public String[] disAllowedBlocks = { Blocks.AIR.getRegistryName().toString(), "thedalekmod:tardis", "tardis:tardis", "tardis:tardisblocktop" };
		
		@Config.LangKey("config.weeping-angels.disallowed_dimensions")
		@Config.Comment("Note: This a list of dimensions that angels should NOT teleport you to.")
		public int[] notAllowedDimensions = { 1 };
		
		@Config.LangKey("config.weeping-angels.ql")
		@Config.Comment("if enabled, angels will freeze when they see one another.")
		public boolean freezeOnAngel = false;
		
		@Config.LangKey("config.weeping-angels.pickaxe_only")
		@Config.Comment("if enabled, Only pickaxes and generators will work on the angels")
		public boolean pickaxeOnly = true;
	}

    public static class Intergrations {
		public boolean vivecraftSupport = false;

        public String[] keyStrings = new String[]{"thedalekmod:tardisKey", "tardis:key"};
	}

	@Mod.EventBusSubscriber
	public static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(WeepingAngels.MODID)) {
				ConfigManager.sync(WeepingAngels.MODID, Config.Type.INSTANCE);
			}
		}
	}

}
