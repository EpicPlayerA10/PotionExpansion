package com.epicplayera10.potionexpansion.tracker.bukkit;

import com.epicplayera10.potionexpansion.PotionExpansion;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ShulkerListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (PotionExpansion.instance().getGlowingBlockEntityTracker() instanceof BukkitGlowingBlockEntityTracker bukkitTracker) {
            for (Shulker shulker : bukkitTracker.getEntities()) {
                if (event.getEntity().getUniqueId().equals(shulker.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    // Player listeners

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (PotionExpansion.instance().getGlowingBlockEntityTracker() instanceof BukkitGlowingBlockEntityTracker bukkitTracker) {
            for (Shulker shulker : bukkitTracker.getEntities()) {
                event.getPlayer().hideEntity(PotionExpansion.instance(), shulker);
            }
        }
    }
}
