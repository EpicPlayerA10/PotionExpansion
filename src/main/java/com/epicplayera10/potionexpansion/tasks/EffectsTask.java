package com.epicplayera10.potionexpansion.tasks;

import com.epicplayera10.potionexpansion.PotionExpansion;
import com.epicplayera10.potionexpansion.api.effects.EffectsManager;
import com.epicplayera10.potionexpansion.api.effects.PotionSightEffect;
import com.epicplayera10.potionexpansion.api.effects.PotionSightType;
import com.epicplayera10.potionexpansion.api.effects.XRayMode;
import com.epicplayera10.potionexpansion.tracker.GlowingBlockEntityOptions;
import com.epicplayera10.potionexpansion.utils.XRayUtil;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EffectsTask extends BukkitRunnable {
    private long tickCounter = 0;

    public EffectsTask(@Nonnull PotionExpansion plugin) {
        this.runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (EffectsManager.hasAnyEffect(player)) {
                List<PotionSightEffect> effects = EffectsManager.getPlayerEffects(player);

                // Every second
                if (this.tickCounter % 20 == 0) {
                    effects.forEach(effect -> effect.setTime(effect.getTime() - 1));

                    // Show action bar
                    showEffects(player, true);
                }

                tick(player, effects);

                // Every second
                if (this.tickCounter % 20 == 0) {
                    effects.removeIf(effect -> effect.getTime() <= 0);

                    if (effects.size() == 0) {
                        EffectsManager.removePlayerEffects(player.getUniqueId());
                    }
                }
            }
        }

        this.tickCounter++;
    }

    public void showEffects(@Nonnull Player player, boolean actionBar) {
        StringBuilder message = new StringBuilder();
        message.append(ChatColors.color("&aEffects: &e"));

        boolean first = true;
        for (PotionSightEffect potionSightEffect : EffectsManager.getPlayerEffects(player)) {
            SimpleDateFormat formatter = new SimpleDateFormat("mm'm' ss's'");
            String formattedTime = formatter.format(new Date(potionSightEffect.getTime() * 1000L));
            if (first) {
                message.append(ChatUtils.humanize(potionSightEffect.getType().toString())+" ");
                message.append(ChatColors.color("&aTime: &e"+formattedTime));
                first = false;
            } else {
                message.append(ChatColors.color("&a, &e"+ChatUtils.humanize(potionSightEffect.getType().toString())+" "));
                message.append(ChatColors.color("&aTime: &e"+formattedTime));
            }
        }

        if (actionBar) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message.toString()));
        } else {
            player.sendMessage(message.toString());
        }
    }

    @ParametersAreNonnullByDefault
    private void tick(Player player, List<PotionSightEffect> effects) {
        XRayMode xRayMode = PotionExpansion.instance().getPluginConfiguration().potionsSight.xRayMode;

        for (PotionSightEffect effect : effects) {
            PotionSightType type = effect.getType();

            List<Block> ores = XRayUtil.getOresInRadius(player.getLocation(), PotionExpansion.instance().getPluginConfiguration().potionsSight.radius, Arrays.asList(type.getOres()));

            if (xRayMode == XRayMode.PARTICLES_PATH) {
                if (this.tickCounter % 20 == 0) {
                    XRayUtil.showPathsToBlocks(player, ores, type.getBukkitColor());
                }
            } else if (xRayMode == XRayMode.GLOWING_SHULKER) {
                if (effect.getTime() <= 5) {
                    // Blink effect
                    if (this.tickCounter % 20 >= 10) {
                        for (Block ore : ores) {
                            PotionExpansion.instance().getGlowingBlockEntityTracker().track(
                                    new GlowingBlockEntityOptions(ore.getLocation(), effect.getType().getChatColor()),
                                    player
                            );
                        }
                    }
                } else {
                    for (Block ore : ores) {
                        PotionExpansion.instance().getGlowingBlockEntityTracker().track(
                                new GlowingBlockEntityOptions(ore.getLocation(), effect.getType().getChatColor()),
                                player
                        );
                    }
                }
            }
        }
    }
}
