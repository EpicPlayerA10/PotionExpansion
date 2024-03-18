package com.epicplayera10.potionexpansion.configuration;

import com.epicplayera10.potionexpansion.api.effects.XRayMode;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;

@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class PluginConfiguration extends OkaeriConfig {

    public Options options = new Options();

    public static class Options extends OkaeriConfig {
        public boolean autoUpdate = true;
    }

    @Comment("")
    @Comment("Main plugin configuration")
    public PotionsSight potionsSight = new PotionsSight();

    public static class PotionsSight extends OkaeriConfig {
        @Comment("Duration of potion (in seconds).")
        public int duration = 120;

        @Comment("")
        @Comment("Radius of detecting ores (in blocks).")
        public int radius = 10;

        @Comment("")
        @Comment("Specifies the XRay mode that plugin can use. There are some modes:")
        @Comment("- PARTICLES_PATH (default) - Plugin spawns particles path to an ore")
        @Comment("- GLOWING_SHULKER - Plugin spawns glowing shulkers inside ores.")
        public XRayMode xRayMode = XRayMode.PARTICLES_PATH;

        @Comment("")
        @Comment("A glowing block entity tracker implementation. This option only")
        @Comment("applies when x-ray-mode is set to GLOWING_SHULKER")
        @Comment("Options:")
        @Comment("- BUKKIT - This implementation uses only bukkit api. This have likely have good integration with")
        @Comment("           other plugins. Generally it has BAD PERFORMANCE, so this option is not good for big servers.")
        @Comment("           Only works on server version 1.17.1 or above")
        @Comment("- PACKET_BASED - This implementation uses mainly packets to send glowing shulkers. This option has")
        @Comment("                 VERY GOOD PERFORMANCE because server does not tick shulkers at all. It might NOT be")
        @Comment("                 compatible with some anticheats. Requires ProtocolLib")
        public GlowingTrackerImpl glowingTrackerImpl = GlowingTrackerImpl.BUKKIT;
    }

    public enum GlowingTrackerImpl {
        BUKKIT,
        PACKET_BASED
    }
}
