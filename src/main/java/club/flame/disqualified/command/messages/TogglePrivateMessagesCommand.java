package club.flame.disqualified.command.messages;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.utils.lang.Lang;
import org.bukkit.entity.Player;

public class TogglePrivateMessagesCommand extends BaseCommand {
    @Command(name = "togglemessages", aliases = {"togglepm", "tpm"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        PlayerData data = PlayerData.getPlayerData(p.getUniqueId());
        if (data == null) return;

        String status = !data.isTogglePrivateMessages() ? "§aenabled" : "§cdisabled";
        if (args.length == 0) {
            Lang.playSound(p, !data.isTogglePrivateMessages());
            data.setTogglePrivateMessages(!data.isTogglePrivateMessages());
            p.sendMessage("§eYou " + status + " §eprivate messages.");
        }
    }
}
