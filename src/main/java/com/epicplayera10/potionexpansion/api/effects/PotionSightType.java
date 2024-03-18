package com.epicplayera10.potionexpansion.api.effects;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

import javax.annotation.Nullable;

public enum PotionSightType {
    COAL_SIGHT(ChatColor.BLACK, Material.COAL_ORE, Material.getMaterial("DEEPSLATE_COAL_ORE")),
    IRON_SIGHT(ChatColor.GRAY, Material.IRON_ORE, Material.getMaterial("DEEPSLATE_IRON_ORE")),
    DIAMOND_SIGHT(ChatColor.AQUA, Material.DIAMOND_ORE, Material.getMaterial("DEEPSLATE_DIAMOND_ORE")),
    GOLD_SIGHT(ChatColor.YELLOW, Material.GOLD_ORE, Material.getMaterial("DEEPSLATE_GOLD_ORE"), Material.getMaterial("NETHER_GOLD_ORE")),
    LAPIS_SIGHT(ChatColor.BLUE, Material.LAPIS_ORE, Material.getMaterial("DEEPSLATE_LAPIS_ORE")),
    REDSTONE_SIGHT(ChatColor.RED, Material.REDSTONE_ORE, Material.getMaterial("DEEPSLATE_REDSTONE_ORE")),
    EMERALD_SIGHT(ChatColor.GREEN, Material.EMERALD_ORE, Material.getMaterial("DEEPSLATE_EMERALD_ORE")),
    QUARTZ_SIGHT(ChatColor.WHITE, Material.NETHER_QUARTZ_ORE),
    ANCIENT_DEBRIS_SIGHT(ChatColor.DARK_RED, Material.getMaterial("ANCIENT_DEBRIS")),
    COPPER_SIGHT(ChatColor.GOLD, Material.getMaterial("COPPER_ORE"), Material.getMaterial("DEEPSLATE_COPPER_ORE"));

    private final Material[] ores;
    private final ChatColor color;

    PotionSightType(ChatColor color, @Nullable Material... ores) {
        this.color = color;
        this.ores = ores;
    }

    public ChatColor getChatColor() {
        return color;
    }

    public Color getBukkitColor() {
        java.awt.Color awtColor = color.asBungee().getColor();
        return Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
    }

    public Material[] getOres() {
        return ores;
    }
}
