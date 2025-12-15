package dev.furq.rock

import dev.furq.rock.world.PebbleFeature
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.DefaultFeatureConfig

class Rock : ModInitializer {

    companion object {
        val PEBBLE_FEATURE = PebbleFeature(DefaultFeatureConfig.CODEC)
        val PEBBLE_FEATURE_ID: Identifier? = Identifier.of("rock", "pebble")
    }

    override fun onInitialize() {
        PebbleManager.init()
        Registry.register(Registries.FEATURE, PEBBLE_FEATURE_ID, PEBBLE_FEATURE)
        val placedFeatureKey = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of("rock", "pebble_placed"))

        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.VEGETAL_DECORATION,
            placedFeatureKey
        )
    }
}
