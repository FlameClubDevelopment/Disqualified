package club.flame.disqualified.command.network;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Clickable;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoCommand extends BaseCommand {
    @Command(name = "playerinfo", aliases = {"pinfo", "info", "getplayer"}, permission = "core.network.playerinfo", usage = "Usage: /playerinfo <playerName>")

    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&c" + command.getCommand().getUsage()));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        PlayerData playerData = PlayerData.getPlayerData(target.getName());
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&4&lDisqualified &4✖ &7- &fPlayer Info"));
        player.sendMessage(CC.translate("&8 ▸ &7Player&f: &4" + target.getName()));
        player.sendMessage(CC.translate("&8 ▸ &7Highest Rank&f: &4" + playerData.getHighestRank().getName()));
        //player.sendMessage(CC.translate("&8 ▸ &7Permissions&f: &e" + playerData.getPermissions().size()));

        Clickable showPermsList = new Clickable();
        List<String> playerPerms = new ArrayList<>();
        playerData.getPermissions().forEach(perm -> playerPerms.add(CC.translate("&f ● &4" + perm)));
        showPermsList.add(CC.translate("&8 ▸ &7Permissions&f: &4" + playerData.getPermissions().size() + " &7(&fHover&7)"), StringUtils.join(playerPerms, "\n"), null);
        showPermsList.sendToPlayer(player);

        player.sendMessage(CC.translate("&8 ▸ &7Last Server&f: &4" + playerData.getLastServer()));
        player.sendMessage(CC.translate("&8 ▸ &7Chat Channel&f: &4" + (playerData.isStaffChat() ? "Staff Chat" : "Global Chat")));
        player.sendMessage(CC.translate("&8 ▸ &7Private Messages&f: " + (playerData.isTogglePrivateMessages() ? "&aEnabled" : "&cDisabled")));
        player.sendMessage(CC.CHAT_BAR);
    }
}
