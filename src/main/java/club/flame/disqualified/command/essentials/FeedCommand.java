package club.flame.disqualified.command.essentials;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import org.bukkit.entity.Player;

public class FeedCommand extends BaseCommand {
    @Command(name = "feed", permission = "core.essentials.feed", aliases = {"comer", "tragar"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "COMMANDS");

        if (args.length == 0) {
            p.setFoodLevel(20);
            p.sendMessage(CC.translate(messages.getString("FEED-MESSAGE")));
        }
    }
}