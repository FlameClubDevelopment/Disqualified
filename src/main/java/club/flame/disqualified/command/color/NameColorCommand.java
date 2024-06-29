package club.flame.disqualified.command.color;

import club.flame.disqualified.menu.color.namecolor.NameColorMenu;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.bukkit.entity.Player;

public class NameColorCommand extends BaseCommand {
    @Command(name = "color", permission = "core.nameColor", aliases = {"nameColor"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            new NameColorMenu().openMenu(p);
        }
    }
}
