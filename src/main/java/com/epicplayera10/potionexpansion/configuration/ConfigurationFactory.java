package com.epicplayera10.potionexpansion.configuration;

import com.epicplayera10.potionexpansion.PotionExpansion;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;

import java.io.File;

public class ConfigurationFactory {

    private ConfigurationFactory(){
    }

    public static PluginConfiguration createPluginConfiguration(File pluginConfigurationFile) {
        return ConfigManager.create(PluginConfiguration.class, (it) -> {
            it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer(), true));
            it.withSerdesPack(registry -> {
                registry.register(new SerdesCommons());
                registry.register(new SerdesBukkit());
            });

            it.withBindFile(pluginConfigurationFile);
            it.withLogger(PotionExpansion.instance().getLogger());
            it.saveDefaults();
            it.load(true);
        });
    }
}
