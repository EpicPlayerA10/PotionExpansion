package com.epicplayera10.potionexpansion.multiblocks.alchemic;

import com.epicplayera10.potionexpansion.PotionExpansion;
import com.epicplayera10.potionexpansion.items.PotionItems;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlchemicStation extends MultiBlockMachine {
    private List<AlchemicRecipe> recipes = new ArrayList<>();
    private final int INGREDIENT_SLOT = 1;
    private final int POTION_1_SLOT = 6;
    private final int POTION_2_SLOT = 7;
    private final int POTION_3_SLOT = 8;

    @ParametersAreNonnullByDefault
    public AlchemicStation(Category category, SlimefunItemStack item) {
        super(category, item, new ItemStack[]{null, new ItemStack(Material.WHITE_STAINED_GLASS), null,
                new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.GLOWSTONE), new ItemStack(Material.WHITE_STAINED_GLASS),
                new ItemStack(Material.CAULDRON), new ItemStack(Material.DISPENSER), new ItemStack(Material.CAULDRON)}, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player player, Block block) {
        Block dispBlock = block.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(dispBlock, false).getState();

        if (state instanceof Dispenser) {
            Dispenser disp = (Dispenser) state;
            Inventory inv = disp.getInventory();

            List<Block> glassBlocks = getWhiteGlassBlocks(block);

            AlchemicRecipe recipe = checkRecipe(inv);

            if (recipe != null) {
                if (SlimefunUtils.canPlayerUseItem(player, recipe.getOutput(), true)) {
                    ItemUtils.consumeItem(inv.getItem(INGREDIENT_SLOT), true);
                    startAnimation(recipe, inv, block, glassBlocks);
                }
            } else {
                SlimefunPlugin.getLocalization().sendMessage(player, "machines.pattern-not-found", true);
            }
        }
    }

    public List<AlchemicRecipe> getAlchemicRecipes() {
        return recipes;
    }

    @Nullable
    private AlchemicRecipe checkRecipe(@Nonnull List<ItemStack> input) {
        ItemStack ingredient = input.get(INGREDIENT_SLOT);
        ItemStack potion1 = input.get(POTION_1_SLOT);
        ItemStack potion2 = input.get(POTION_2_SLOT);
        ItemStack potion3 = input.get(POTION_3_SLOT);

        if (ingredient != null) {
            for (AlchemicRecipe recipe : recipes) {
                if (SlimefunUtils.isItemSimilar(ingredient, recipe.getIngredient(), true)) {
                    if (checkPotionSlot(recipe, potion1) || checkPotionSlot(recipe, potion2) || checkPotionSlot(recipe, potion3)) {
                        return recipe;
                    }
                }

            }
        }
        return null;
    }

    @Nullable
    private AlchemicRecipe checkRecipe(@Nonnull Inventory inv) {
        return checkRecipe(Arrays.asList(inv.getContents()));
    }

    private List<Block> getWhiteGlassBlocks(@Nonnull Block block) {
        List<Block> list = new ArrayList<>();
        if (block.getRelative(BlockFace.NORTH).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.NORTH));
        } else if (block.getRelative(BlockFace.EAST).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.EAST));
        }

        if (block.getRelative(BlockFace.UP).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.UP));
        }

        if (block.getRelative(BlockFace.SOUTH).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.SOUTH));
        } else if (block.getRelative(BlockFace.WEST).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.WEST));
        }

        return list;
    }

    @ParametersAreNonnullByDefault
    private void startAnimation(AlchemicRecipe recipe, Inventory dispInv, Block b, List<Block> glassBlocks) {
        for (int i = 0; i < 10; i++) {
            int j = i;
            PotionExpansion.runSync(() -> {
                if (j < 9) {
                    World world = b.getWorld();
                    world.playSound(b.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1f, 2f);
                    glassBlocks.forEach(glassBlock -> {
                        world.playEffect(glassBlock.getLocation().clone().add(0.5, 0, 0.5), Effect.SMOKE, BlockFace.UP);
                    });
                } else {
                    b.getWorld().playSound(b.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1f, 0.1f);
                    brew(recipe, dispInv);
                }
            }, i * 20L);
        }
    }

    @ParametersAreNonnullByDefault
    private void brew(AlchemicRecipe recipe, Inventory dispInv) {
        for (int i = POTION_1_SLOT; i < POTION_3_SLOT + 1; i++) {
            if (checkPotionSlot(recipe, dispInv.getItem(i))) {
                dispInv.setItem(i, recipe.getOutput());
            }
        }
    }

    private boolean checkPotionSlot(@Nonnull AlchemicRecipe recipe, @Nullable ItemStack item) {
        if (item != null && item.getType() == Material.POTION && item.hasItemMeta()) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            return meta.getBasePotionData().getType() == recipe.getPotion();
        }
        return false;
    }
}
