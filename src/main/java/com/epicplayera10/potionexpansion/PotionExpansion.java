package com.epicplayera10.potionexpansion;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;

public class PotionExpansion extends JavaPlugin implements SlimefunAddon {

    @Override
    public void onEnable() {
        Config cfg = new Config(this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/EpicPlayerA10/PotionExpansion/issues";
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
