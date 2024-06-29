package club.flame.disqualified.command.social.namemc;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CheckVoteCommand extends BaseCommand {
    @Command(name = "checkvote", aliases = {"namemccheck"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData data = PlayerData.getPlayerData(p.getUniqueId());
        String command = Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.NAME-MC-CHECK.EXECUTE-CMD").replace("<player>", p.getName());

        if (args.length == 0) {
            if (data == null) return;
            if (data.isVote()) {
                p.sendMessage("§cYou already voted for the server!");
            } else {
                p.sendMessage("§aChecking.....");
                if (Utils.checkPlayerVote(p.getUniqueId())) {
                    p.sendMessage("§aCorrect verification!");
                    data.setVote(true);
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                } else {
                    p.sendMessage("§cAre you sure you have voted for the server?");
                }
            }
        }
    }
}
