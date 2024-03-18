package com.epicplayera10.potionexpansion;

import com.epicplayera10.potionexpansion.api.effects.XRayMode;
import com.epicplayera10.potionexpansion.commands.PotionExpansionCommand;
import com.epicplayera10.potionexpansion.commands.PotionExpansionTab;
import com.epicplayera10.potionexpansion.configuration.ConfigurationFactory;
import com.epicplayera10.potionexpansion.configuration.PluginConfiguration;
import com.epicplayera10.potionexpansion.listeners.DrinkMilkListener;
import com.epicplayera10.potionexpansion.tasks.EffectsTask;

import com.epicplayera10.potionexpansion.tracker.GlowingBlockEntityTracker;
import com.epicplayera10.potionexpansion.tracker.bukkit.BukkitGlowingBlockEntityTracker;
import com.epicplayera10.potionexpansion.tracker.bukkit.ShulkerListener;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;

import org.bukkit.Bukkit;
import org.bukkit.entity.Shulker;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class PotionExpansion extends JavaPlugin implements SlimefunAddon {
    private final File pluginConfigurationFile = new File(this.getDataFolder(), "config.yml");

    private PluginConfiguration pluginConfiguration;

    private static PotionExpansion instance;
    private EffectsTask effectsTask;

    private GlowingBlockEntityTracker glowingBlockEntityTracker;

    @Override
    public void onEnable() {
        instance = this;

        this.pluginConfiguration = ConfigurationFactory.createPluginConfiguration(this.pluginConfigurationFile);

        if (pluginConfiguration.options.autoUpdate && this.getPluginVersion().startsWith("DEV - ")) {
            GitHubBuildsUpdater updater = new GitHubBuildsUpdater(this, this.getFile(), "EpicPlayerA10/PotionExpansion/master");
            updater.start();
        }

        PotionsItemSetup.setup(this);
        ResearchSetup.setup(this);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DrinkMilkListener(), this);

        if (pluginConfiguration.potionsSight.xRayMode == XRayMode.GLOWING_SHULKER) {
            glowingBlockEntityTracker = switch (pluginConfiguration.potionsSight.glowingTrackerImpl) {
                case BUKKIT -> {
                    pm.registerEvents(new ShulkerListener(), this);
                    yield new BukkitGlowingBlockEntityTracker();
                }
                case PACKET_BASED -> throw new UnsupportedOperationException("Not implemented yet");
            };
        }

        effectsTask = new EffectsTask(this);

        getCommand("potionexpansion").setExecutor(new PotionExpansionCommand());
        getCommand("pe").setExecutor(new PotionExpansionCommand());

        getCommand("potionexpansion").setTabCompleter(new PotionExpansionTab());
    }

    @Override
    public void onDisable() {
        if (pluginConfiguration.potionsSight.xRayMode == XRayMode.GLOWING_SHULKER && glowingBlockEntityTracker instanceof BukkitGlowingBlockEntityTracker bukkitGlowingBlockEntityTracker) {
            // Remove all glowing shulkers
            for (Shulker shulker : bukkitGlowingBlockEntityTracker.getEntities()) {
                shulker.remove();
            }
        }
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/EpicPlayerA10/PotionExpansion/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    public static PotionExpansion instance() {
        return instance;
    }

    public EffectsTask getEffectsTask() {
        return effectsTask;
    }

    public GlowingBlockEntityTracker getGlowingBlockEntityTracker() {
        return glowingBlockEntityTracker;
    }

    public PluginConfiguration getPluginConfiguration() {
        return pluginConfiguration;
    }

    public static @Nullable BukkitTask runSync(@Nonnull Runnable runnable, long delay) {
        assert delay >= 0;

        if (instance == null || !instance.isEnabled()) {
            return null;
        }

        return instance.getServer().getScheduler().runTaskLater(instance, runnable, delay);
    }
}
