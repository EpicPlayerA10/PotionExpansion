package com.epicplayera10.potionexpansion.tracker;

import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;

public interface GlowingBlockEntityTracker {
    /**
     * Start tracking glowing block entity
     */
    @ParametersAreNonnullByDefault
    void track(GlowingBlockEntityOptions glowingBlockEntityOptions, Player trackedBy);
}
