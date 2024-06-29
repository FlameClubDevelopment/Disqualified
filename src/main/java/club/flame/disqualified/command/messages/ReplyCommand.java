package club.flame.disqualified.command.messages;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.messages.PlayerMessage;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.task.TaskUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReplyCommand extends BaseCommand {
    @Command(name = "reply", aliases = {"r"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage("§cUsage: /" + cmd.getLabel() + " <text>");
            return;
        }

        TaskUtil.runAsync(() -> {
            UUID lastReply = Disqualified.getInstance().getMessageManager().getLastReplied().get(p.getUniqueId());
            Player target = Bukkit.getPlayer(lastReply);
            if (target == null) {
                p.sendMessage("§cThere is no player to reply to");
                return;
            }
                String text = StringUtils.join(args, ' ', 0, args.length);
            PlayerMessage playerMessage = new PlayerMessage(p, target, text, true);
            playerMessage.send();
        });
    }
}
