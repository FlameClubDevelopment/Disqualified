package club.flame.disqualified.command.coins;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CoinsCommand extends BaseCommand {
    @Command(name = "coins",aliases = {"monedas"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        PlayerData playerData = PlayerData.getPlayerData(p.getUniqueId());
        String[] args = cmd.getArgs();

        if (args.length == 0){
            p.sendMessage(CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.COINS-MESSAGE.DEFAULT")
                    .replace("<amount>",String.valueOf(playerData.getCoins()))));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null){
            p.sendMessage(Lang.OFFLINE_PLAYER);
            return;
        }
        p.sendMessage(CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.COINS-MESSAGE.TARGET")
                .replace("<target>",target.getName())
                .replace("<amount>",String.valueOf(playerData.getCoins()))));
    }
}
