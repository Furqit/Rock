package dev.furq.rock.world

import com.mojang.serialization.Codec
import dev.furq.rock.PebbleManager
import net.minecraft.entity.EntityType
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.world.Heightmap
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext

class PebbleFeature(codec: Codec<DefaultFeatureConfig>) : Feature<DefaultFeatureConfig>(codec) {

    override fun generate(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        val world = context.world
        val origin = context.origin
        val random = context.random
        val topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, origin)

        if (!world.getBlockState(topPos.down()).isSolidBlock(world, topPos.down())) {
            return false
        }

        val entity = InteractionEntity(EntityType.INTERACTION, world.toServerWorld())
        entity.refreshPositionAndAngles(
            topPos.x + 0.5,
            topPos.y.toDouble(),
            topPos.z + 0.5,
            random.nextFloat() * 360f,
            0f
        )

        entity.interactionWidth = 0.8f
        entity.interactionHeight = 0.2f

        entity.addCommandTag(PebbleManager.TAG_PEBBLE)

        world.spawnEntity(entity)

        return true
    }
}
