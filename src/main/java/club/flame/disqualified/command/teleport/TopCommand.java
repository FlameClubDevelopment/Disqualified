package club.flame.disqualified.command.teleport;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import org.bukkit.entity.Player;

public class TopCommand extends BaseCommand {
    @Command(name = "teleporttop", permission = "core.command.top", aliases = {"top"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "COMMANDS.TELEPORT-MESSAGES");
        if (args.length == 0) {
            p.teleport(Utils.teleportToTop(p.getLocation()));
            p.sendMessage(CC.translate(messages.getString("TOP")));
            return;
        }
    }
}
