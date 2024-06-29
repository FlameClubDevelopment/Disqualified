package club.flame.disqualified.command.essentials;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PingCommand extends BaseCommand {
    @Command(name = "ping", aliases = {"ms", "conexion"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "COMMANDS");
        String sound = messages.getString("PING-MESSAGES.SOUND");

        int ping = Utils.getPing(p);

        if (args.length == 0) {
            p.sendMessage(CC.translate(messages.getString("PING-MESSAGES.DEFAULT").replace("<ping>", String.valueOf(ping))));
            if (!(sound.equalsIgnoreCase("none") || sound == null)) {
                p.playSound(p.getLocation(), Sound.valueOf(messages.getString("PING-MESSAGES.SOUND")), 2F, 2F);
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage("§cCouldn't find player.");
                return;
            }

            int pingTarget = Utils.getPing(target);
            p.sendMessage(CC.translate(messages.getString("PING-MESSAGES.OTHER")
                    .replace("<ping>", String.valueOf(pingTarget))
                    .replace("<target>", target.getName())));
            Utils.sendPlayerSound(p, sound);
        }
    }
}
