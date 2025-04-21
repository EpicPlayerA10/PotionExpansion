package com.epicplayera10.potionexpansion;

import com.epicplayera10.potionexpansion.items.PotionItems;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;

public class ResearchSetup {
    public static void setup(@Nonnull PotionExpansion plugin) {
        new Research(new NamespacedKey(plugin, "potions_sight"), 31001, "Potions Sight", 28)
                .addItems(
                        PotionItems.COAL_SIGHT.item(),
                        PotionItems.IRON_SIGHT.item(),
                        PotionItems.DIAMOND_SIGHT.item(),
                        PotionItems.GOLD_SIGHT.item(),
                        PotionItems.LAPIS_SIGHT.item(),
                        PotionItems.REDSTONE_SIGHT.item(),
                        PotionItems.EMERALD_SIGHT.item(),
                        PotionItems.QUARTZ_SIGHT.item(),
                        PotionItems.ANCIENT_DEBRIS_SIGHT.item(),
                        PotionItems.COPPER_SIGHT.item()
                )
                .register();

        new Research(new NamespacedKey(plugin, "powders"), 31002, "Powders!", 6)
                .addItems(
                        PotionItems.COAL_POWDER.item(),
                        PotionItems.IRON_POWDER.item(),
                        PotionItems.DIAMOND_POWDER.item(),
                        PotionItems.GOLD_POWDER.item(),
                        PotionItems.LAPIS_POWDER.item(),
                        PotionItems.REDSTONE_POWDER.item(),
                        PotionItems.EMERALD_POWDER.item(),
                        PotionItems.QUARTZ_POWDER.item(),
                        PotionItems.ANCIENT_DEBRIS_POWDER.item(),
                        PotionItems.COPPER_POWDER.item()
                )
                .register();

        new Research(new NamespacedKey(plugin, "alchemic_station"), 31003, "Alchemic Station", 15)
                .addItems(PotionItems.ALCHEMIC_STATION.item(), PotionItems.PESTLE.item())
                .register();

        plugin.getLogger().info("Loaded researches!");
    }
}
