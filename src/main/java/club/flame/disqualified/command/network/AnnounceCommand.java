package club.flame.disqualified.command.network;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.utils.lang.Lang;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.bukkit.entity.Player;

public class AnnounceCommand extends BaseCommand {
    @Command(name = "announce", aliases = {"announceserver", "alertserver"}, permission = "core.network.announce", inGameOnly = true)

    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());

        Utils.globalBroadcast(player, CC.translate(Disqualified.getInstance().getSettingsConfig().getString("SETTINGS.SERVER-ANNOUNCE"))
                .replace("<name>", player.getName())
                .replace("<rank>", playerData.getHighestRank().getPrefix())
                .replace("<server_name>", Lang.SERVER_NAME)
        );
    }
}
