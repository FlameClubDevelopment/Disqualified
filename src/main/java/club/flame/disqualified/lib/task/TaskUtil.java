package club.flame.disqualified.lib.task;

import club.flame.disqualified.lib.DisqualifiedLib;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void run(Runnable runnable) {
        DisqualifiedLib.INSTANCE.getPlugin().getServer().getScheduler().runTask(DisqualifiedLib.INSTANCE.getPlugin(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        DisqualifiedLib.INSTANCE.getPlugin().getServer().getScheduler().runTaskTimer(DisqualifiedLib.INSTANCE.getPlugin(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(DisqualifiedLib.INSTANCE.getPlugin(), delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously(DisqualifiedLib.INSTANCE.getPlugin(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        DisqualifiedLib.INSTANCE.getPlugin().getServer().getScheduler().runTaskLater(DisqualifiedLib.INSTANCE.getPlugin(), runnable, delay);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        DisqualifiedLib.INSTANCE.getPlugin().getServer().getScheduler().runTaskLaterAsynchronously(DisqualifiedLib.INSTANCE.getPlugin(), runnable, delay);
    }

    public static void runTaskTimerAsynchronously(Runnable runnable, int delay) {
        DisqualifiedLib.INSTANCE.getPlugin().getServer().getScheduler().runTaskTimerAsynchronously(DisqualifiedLib.INSTANCE.getPlugin(), runnable, 20 * delay, 20 * delay);
    }

    public static void runAsync(Runnable runnable) {
        DisqualifiedLib.INSTANCE.getPlugin().getServer().getScheduler().runTaskAsynchronously(DisqualifiedLib.INSTANCE.getPlugin(), runnable);
    }
}