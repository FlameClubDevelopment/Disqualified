package club.flame.disqualified.command.social.namemc;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class VoteCommand extends BaseCommand {
    @Command(name = "vote", aliases = {"namemc"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        List<String> msg = CC.translate(Disqualified.getInstance().getSettingsConfig().getConfiguration().getStringList("SETTINGS.NAME-MC-CHECK.JOIN-MSG"));
        String sound = Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.NAME-MC-CHECK.SOUND");

        if (args.length == 0) {
            if (Disqualified.getInstance().getSettingsConfig().getConfiguration().getBoolean("SETTINGS.NAME-MC-CHECK.ENABLED")) {
                p.sendMessage(StringUtils.join(msg, "\n"));
                if (sound != null || !sound.equalsIgnoreCase("none")) {
                    p.playSound(p.getLocation(), Sound.valueOf(sound), 2F, 2F);
                }
            } else {
                p.sendMessage("§cName-MC Verification isn't active");
            }
        }
    }
}
