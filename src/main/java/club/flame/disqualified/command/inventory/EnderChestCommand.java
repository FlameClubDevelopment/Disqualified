package club.flame.disqualified.command.inventory;

import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.bukkit.entity.Player;

public class EnderChestCommand extends BaseCommand {
    @Command(name = "enderchest", permission = "core.command.enderchest", aliases = {"ec"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        p.openInventory(p.getEnderChest());
    }
}