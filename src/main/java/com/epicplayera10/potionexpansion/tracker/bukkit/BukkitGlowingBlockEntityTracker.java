package com.epicplayera10.potionexpansion.tracker.bukkit;

import com.epicplayera10.potionexpansion.PotionExpansion;
import com.epicplayera10.potionexpansion.tracker.GlowingBlockEntityOptions;
import com.epicplayera10.potionexpansion.tracker.GlowingBlockEntityTracker;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.scoreboard.Team;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class BukkitGlowingBlockEntityTracker implements GlowingBlockEntityTracker {
    private final Map<GlowingBlockEntityOptions, Shulker> glowingEntityBlocksLocations = new HashMap<>(); // Shulker riding armorstand to disable ticking
    private final Multimap<GlowingBlockEntityOptions, UUID> glowingEntityBlocksTracker = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new); // shulker location -> Player UUID
    private final Multimap<GlowingBlockEntityOptions, UUID> unflushedTracker = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new); // shulker location -> Player UUID

    public BukkitGlowingBlockEntityTracker() {
        // Tick tracker
        Bukkit.getScheduler().runTaskTimer(PotionExpansion.instance(), () -> {
            // Flush changes
            flush();

            unflushedTracker.clear();
        }, 1L, 1L);
    }

    /**
     * Basically this function looks for changes between
     * two multimaps: unflushedTracker (new) and glowingEntityBlocksTracker (old)
     */
    private void flush() {
        // Check differences in glowingEntityBlocksTracker
        for (var entry : unflushedTracker.entries()) {
            GlowingBlockEntityOptions glowingBlockEntityOptions = entry.getKey();
            UUID playerUUID = entry.getValue();

            // Show tracked shulker
            if (!glowingEntityBlocksTracker.containsEntry(glowingBlockEntityOptions, playerUUID)) {
                Player player = Bukkit.getPlayer(playerUUID);
                if (player == null) continue;

                Shulker glowingBlockEntity = glowingEntityBlocksLocations.computeIfAbsent(glowingBlockEntityOptions, options -> {
                    return createGlowingBlockEntity(options, player);
                });

                player.showEntity(PotionExpansion.instance(), glowingBlockEntity);

                glowingEntityBlocksLocations.put(glowingBlockEntityOptions, glowingBlockEntity);
            }
        }

        // Check differences in unflushedTracker
        for (var entry : glowingEntityBlocksTracker.entries()) {
            GlowingBlockEntityOptions glowingBlockEntityOptions = entry.getKey();
            UUID playerUUID = entry.getValue();

            // Remove untracked shulker
            if (!unflushedTracker.containsKey(glowingBlockEntityOptions)) {
                Shulker shulker = glowingEntityBlocksLocations.remove(glowingBlockEntityOptions);
                if (shulker != null) {
                    shulker.remove();
                }

                continue;
            }

            // Hide untracked shulker
            if (!unflushedTracker.containsEntry(glowingBlockEntityOptions, playerUUID)) {
                Player player = Bukkit.getPlayer(playerUUID);
                if (player == null) continue;

                Shulker shulker = glowingEntityBlocksLocations.get(glowingBlockEntityOptions);
                if (shulker != null) {
                    player.hideEntity(PotionExpansion.instance(), shulker);
                }
            }
        }

        // Set changes
        glowingEntityBlocksTracker.clear();
        glowingEntityBlocksTracker.putAll(unflushedTracker);
    }

    private Shulker createGlowingBlockEntity(GlowingBlockEntityOptions options, Player forPlayer) {
        // Create new shulker
        Shulker shulker = options.location().getWorld().spawn(options.location(), Shulker.class, shulker1 -> {
            shulker1.setInvulnerable(true);
            shulker1.setInvisible(true);
            shulker1.setGlowing(true);
            shulker1.setAI(false);
            shulker1.setGravity(false);
        });

        // Hide shulkers from other players
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(forPlayer.getUniqueId())) continue;

            onlinePlayer.hideEntity(PotionExpansion.instance(), shulker);
        }

        // Glow color
        String teamName = "potionexpansion-"+options.glowColor().name();
        Team colorTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        if (colorTeam == null) {
            // Lazy load team
            colorTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName);
            colorTeam.setColor(options.glowColor());
        }

        colorTeam.addEntry(shulker.getUniqueId().toString());

        return shulker;
    }

    public Collection<Shulker> getEntities() {
        return glowingEntityBlocksLocations.values();
    }

    /**
     * Start tracking glowing block entity
     */
    @ParametersAreNonnullByDefault
    @Override
    public void track(GlowingBlockEntityOptions glowingBlockEntityOptions, Player trackedBy) {
        unflushedTracker.put(glowingBlockEntityOptions, trackedBy.getUniqueId());
    }
}
