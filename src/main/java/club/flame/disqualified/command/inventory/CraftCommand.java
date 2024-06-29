package club.flame.disqualified.command.inventory;

import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.bukkit.entity.Player;

public class CraftCommand extends BaseCommand {
    @Command(name = "craft", permission = "core.command.craft", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        p.openWorkbench(p.getLocation(), true);
    }
}
