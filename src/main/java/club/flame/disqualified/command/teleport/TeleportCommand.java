package club.flame.disqualified.command.teleport;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportCommand extends BaseCommand {
    @Command(name = "teleport", permission = "core.command.teleport", aliases = {"tp"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "COMMANDS.TELEPORT-MESSAGES");

        if (args.length == 0) {
            p.sendMessage("§cUsage: /teleport <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendMessage("§cPlayer isn't online");
            return;
        }

        p.teleport(target.getLocation());
        p.sendMessage(CC.translate(messages.getString("TP").replace("<target>", target.getDisplayName())));
    }
}
