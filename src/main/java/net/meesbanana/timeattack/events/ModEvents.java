package net.meesbanana.timeattack.events;

import net.meesbanana.timeattack.TimeAttack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

public class ModEvents {

    private static final int TIME_LIMIT = 300; // 5 minutes
    private static final Timer timer = new Timer();

    @Mod.EventBusSubscriber(modid = TimeAttack.MOD_ID)
    public static class CommonModEvents {

        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            // check if the entity is a player
            if (!(event.getEntity() instanceof ServerPlayer player)) {
                return;
            }
            // notify player that the timer has started
            player.sendSystemMessage(Component.literal("The curse of time takes effect..."));
            player.sendSystemMessage(Component.literal(TIME_LIMIT/60 + " minutes remaining."));
            // kill the player after TIME_LIMIT seconds
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    event.getEntity().kill();
                }
            }, TIME_LIMIT * 1000);
        }

        @SubscribeEvent
        public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            // check if the entity is a player
            if (!(event.getEntity() instanceof ServerPlayer)) {
                return;
            }
            // cancel the timer if the player logged out
            timer.cancel();
        }
    }
}
