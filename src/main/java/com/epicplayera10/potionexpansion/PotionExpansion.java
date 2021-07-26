package com.epicplayera10.potionexpansion;

import com.epicplayera10.potionexpansion.commands.PotionExpansionCommand;
import com.epicplayera10.potionexpansion.commands.PotionExpansionTab;
import com.epicplayera10.potionexpansion.listeners.DrinkMilkListener;
import com.epicplayera10.potionexpansion.tasks.EffectsTask;

import org.apache.commons.lang.Validate;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.Updater;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PotionExpansion extends JavaPlugin implements SlimefunAddon {
    private static PotionExpansion instance;
    private EffectsTask effectsTask;

    @Override
    public void onEnable() {
        instance = this;

        Config cfg = new Config(this);

        if (cfg.getBoolean("auto-update")) {
            Updater updater = new GitHubBuildsUpdater(this, this.getFile(), "EpicPlayerA10/PotionExpansion/master");
            updater.start();
        }

        Settings.load(cfg);

        PotionsItemSetup.setup(this);
        ResearchSetup.setup(this);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DrinkMilkListener(), this);

        effectsTask = new EffectsTask(this);

        getCommand("potionexpansion").setExecutor(new PotionExpansionCommand());
        getCommand("pe").setExecutor(new PotionExpansionCommand());

        getCommand("potionexpansion").setTabCompleter(new PotionExpansionTab());
        getCommand("pe").setTabCompleter(new PotionExpansionTab());
    }

    @Override
    public void onDisable() {
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

    public static PotionExpansion getInstance() {
        return instance;
    }

    public EffectsTask getEffectsTask() {
        return effectsTask;
    }

    public static @Nullable BukkitTask runSync(@Nonnull Runnable runnable, long delay) {
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        if (instance == null || !instance.isEnabled()) {
            return null;
        }

        return instance.getServer().getScheduler().runTaskLater(instance, runnable, delay);
    }
}
