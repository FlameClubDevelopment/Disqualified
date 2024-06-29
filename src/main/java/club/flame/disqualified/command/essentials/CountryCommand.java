package club.flame.disqualified.command.essentials;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CountryCommand extends BaseCommand {
    @SneakyThrows
    @Command(name = "getcountry", permission = "core.essentials.geoip", aliases = {"geoip"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "COMMANDS");

        if (args.length == 0) {
            p.sendMessage("§cUsage /" + cmd.getLabel() + " <player>");
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            p.sendMessage(CC.RED + player.getName() + " isn't online.");
            return;
        }
        String ip = player.getAddress().toString().replaceAll("/", "");
        String country = Utils.getCountry(ip);
        try {
            p.sendMessage(CC.translate(configCursor.getString("GEO-IP-MESSAGE")
                    .replace("<player>", player.getName())
                    .replace("<country>", (country == null ? "No Found" : country)))
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            p.sendMessage("§eError in get player country");
        }
    }
}
