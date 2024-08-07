package club.flame.disqualified.utils;

import club.flame.disqualified.Permissions;
import club.flame.disqualified.lib.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/*
   Credits: https://www.spigotmc.org/threads/async-update-checker-for-premium-and-regular-plugins.327921/
 */

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final String localPluginVersion;
    private String spigotPluginVersion;

    private static final int ID = 117632;
    private static final Permission UPDATE_PERM = new Permission(Permissions.UPDATE_NOTIFICATION.getPermission(), PermissionDefault.TRUE);
    private static final long CHECK_INTERVAL = 12_000;

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
        this.localPluginVersion = plugin.getDescription().getVersion();
    }

    public void checkForUpdate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        final HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + ID).openConnection();
                        connection.setRequestMethod("GET");
                        spigotPluginVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        cancel();
                        return;
                    }

                    if (localPluginVersion.equals(spigotPluginVersion)) return;

                    plugin.getLogger().info("An update for Disqualified (v%VERSION%) is available at:".replace("%VERSION%", spigotPluginVersion));
                    plugin.getLogger().info("https://www.spigotmc.org/resources/" + ID);

                    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().registerEvents(new Listener() {
                        @EventHandler(priority = EventPriority.MONITOR)
                        public void onPlayerJoin(final PlayerJoinEvent event) {
                            final Player player = event.getPlayer();
                            if (!player.hasPermission(UPDATE_PERM)) return;
                            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&7An update (v%VERSION%) for &4Disqualified &7is available at:".replace("%VERSION%", spigotPluginVersion)));
                            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&6https://www.spigotmc.org/resources/" + ID));
                        }
                    }, plugin));

                    cancel();
                });
            }
        }.runTaskTimer(plugin, 0, CHECK_INTERVAL);
    }
}