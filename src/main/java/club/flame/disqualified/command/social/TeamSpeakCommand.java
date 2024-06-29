package club.flame.disqualified.command.social;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.utils.lang.Lang;
import org.bukkit.entity.Player;

public class TeamSpeakCommand extends BaseCommand {
    
    @Command(name = "teamspeak", aliases = {"ts", "ts3"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        
        if (cmd.getArgs().length == 0) {
            String message = Disqualified.getInstance()
                                        .getMessagesConfig()
                                        .getConfiguration()
                                        .getString("COMMANDS.SOCIAL.MESSAGES");
            if (message != null) {
                message = message.replace("<command>", "TeamSpeak")
                                 .replace("<social>", Lang.TS);
                player.sendMessage(CC.translate(message));
            }
        }
    }
}
