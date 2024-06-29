package club.flame.disqualified.command.teleport;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpallCommand extends BaseCommand {
    @Command(name = "teleportall", permission = "core.command.tpall", aliases = {"tpall"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor message = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "COMMANDS.TELEPORT-MESSAGES");

        for (Player player : Bukkit.getOnlinePlayers()) player.teleport(p.getLocation());
        Utils.sendAllMsg(CC.translate(message.getString("TPALL").replace("<player>", p.getDisplayName())));
    }
}
