package com.epicplayera10.potionexpansion.items;

import com.epicplayera10.potionexpansion.Settings;
import com.epicplayera10.potionexpansion.api.effects.PotionSightEffect;
import com.epicplayera10.potionexpansion.api.effects.PotionSightType;

import com.epicplayera10.potionexpansion.api.effects.EffectsManager;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemConsumptionHandler;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

import org.bukkit.inventory.ItemStack;

public class PotionSightItem extends SlimefunItem {
    private PotionSightType potionSightType;

    public PotionSightItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PotionSightType potionSightType) {
        super(category, item, recipeType, recipe);

        this.potionSightType = potionSightType;
    }

    @Override
    public void preRegister() {
        addItemHandler(onConsume());
    }

    private ItemConsumptionHandler onConsume() {
        return (e, p, item) -> {
            EffectsManager.addEffect(p, new PotionSightEffect(potionSightType, Settings.getPotionDuration()));
        };
    }
}
