package club.flame.disqualified.command.essentials;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.utils.lang.Lang;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BroadcastCommand extends BaseCommand {
    @Command(name = "broadcast", permission = "core.essentials.broadcast", aliases = {"bc", "alerta"}, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender p = cmd.getSender();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage("§cUsage: /" + cmd.getLabel() + " <text>");
            return;
        }

        List<String> text = new ArrayList<>();
        Collections.addAll(text, args);
        String message = StringUtils.join(text, " ");

        // Retrieve the broadcast message template from the configuration and check for null
        String template = Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.BROADCAST", "Broadcast: <text>");

        // Check for null or missing placeholders and replace them appropriately
        if (template == null) {
            template = "Broadcast: <text>";
        }

        if (message == null) {
            message = "";
        }

        // Replace the placeholder with the actual message
        String broadcastMessage = CC.translate(template.replace("<text>", message));
        Bukkit.broadcastMessage(broadcastMessage);
    }
}
