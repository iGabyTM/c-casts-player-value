package me.gabytm.minecraftc.castsplayervalue.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Map;

@ConfigSerializable
public record PluginConfig(
        Integer startingValue,
        Boolean loseValueFromAllDeathCauses,
        Map<String, String> messages
) {
}
