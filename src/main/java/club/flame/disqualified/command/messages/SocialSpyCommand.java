package club.flame.disqualified.command.messages;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.utils.lang.Lang;
import org.bukkit.entity.Player;

public class SocialSpyCommand extends BaseCommand {
    @Command(name = "socialSpy", permission = "core.chat.socialSpy", aliases = {"ssspy"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData data = PlayerData.getPlayerData(p.getUniqueId());

        if (data == null) return;
        String status = !data.isSocialSpy() ? "§aenabled" : "§cdisabled";

        if (args.length == 0) {
            Lang.playSound(p, !data.isSocialSpy());
            data.setSocialSpy(!data.isSocialSpy());
            p.sendMessage("§eYou " + status + " §esocial spy.");
        }
    }
}
