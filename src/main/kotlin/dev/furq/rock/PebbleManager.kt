package dev.furq.rock

import dev.furq.bdengineimport.api.BDEngineImportAPI
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import kotlin.math.abs

object PebbleManager {
    private const val PEBBLE_MODEL_ID = "pebble_model"
    const val PEBBLE_SPAWN_ID_PREFIX = "pebble_"
    const val TAG_PEBBLE = "rock:pebble"

    fun init() {
        val stream = Rock::class.java.getResourceAsStream("/pebbles.bdi")
        if (stream != null) {
            BDEngineImportAPI.get("rock").loadBdiModel(PEBBLE_MODEL_ID, stream)
        }

        ServerEntityEvents.ENTITY_LOAD.register { entity, world ->
            if (world is ServerWorld) onEntityLoad(entity, world)
        }

        ServerEntityEvents.ENTITY_UNLOAD.register { entity, world ->
            if (world is ServerWorld) onEntityUnload(entity)
        }

        AttackEntityCallback.EVENT.register { _, world, hand, entity, _ ->
            if (world is ServerWorld && hand == Hand.MAIN_HAND) {
                onInteraction(entity)
            } else {
                ActionResult.PASS
            }
        }
    }

    private fun onEntityLoad(entity: Entity, world: ServerWorld) {
        if (entity is InteractionEntity && entity.commandTags.contains(TAG_PEBBLE)) {
            val spawnId = PEBBLE_SPAWN_ID_PREFIX + entity.uuidAsString

            val x = entity.x.toFloat()
            val y = entity.y.toFloat()
            val z = entity.z.toFloat()
            val ry = -entity.yaw
            val scale = 0.7f + (abs(entity.uuid.hashCode()) % 60) / 100f
            val worldId = world.registryKey.value.toString()

            BDEngineImportAPI.get("rock").spawn(
                spawnId,
                PEBBLE_MODEL_ID,
                worldId,
                x, y, z,
                0f, ry, 0f,
                scale,
                64.0
            )
        }
    }

    private fun onEntityUnload(entity: Entity) {
        if (entity is InteractionEntity && entity.commandTags.contains(TAG_PEBBLE)) {
            val spawnId = PEBBLE_SPAWN_ID_PREFIX + entity.uuidAsString
            BDEngineImportAPI.get("rock").despawn(spawnId, PEBBLE_MODEL_ID)
        }
    }

    private fun onInteraction(entity: Entity): ActionResult {
        if (entity is InteractionEntity && entity.commandTags.contains(TAG_PEBBLE)) {
            entity.discard()
            val spawnId = PEBBLE_SPAWN_ID_PREFIX + entity.uuidAsString
            BDEngineImportAPI.get("rock").despawn(spawnId, PEBBLE_MODEL_ID)
            
            entity.world.playSound(null, entity.blockPos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1f, 1f)

            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
    }
}
