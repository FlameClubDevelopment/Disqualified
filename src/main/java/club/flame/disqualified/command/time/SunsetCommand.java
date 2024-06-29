package club.flame.disqualified.command.time;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import org.bukkit.entity.Player;

public class SunsetCommand extends BaseCommand {
    @Command(name = "sunset", permission = "core.command.sunset", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "COMMANDS");

        if (args.length == 0) {
            p.setPlayerTime(12500L, false);
            p.sendMessage(CC.translate(messages.getString("TIME-MESSAGE").replace("<timeSetting>", String.valueOf(p.getPlayerTime()))));
        }
    }
}
