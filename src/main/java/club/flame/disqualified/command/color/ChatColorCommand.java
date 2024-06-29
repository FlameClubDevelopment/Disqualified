package club.flame.disqualified.command.color;

import club.flame.disqualified.menu.color.chatcolor.ChatColorMenu;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.bukkit.entity.Player;

public class ChatColorCommand extends BaseCommand {
    @Command(name = "chatcolor", aliases = {"cc"}, permission = "core.chatcolor", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            new ChatColorMenu().openMenu(p);
        }
    }
}
