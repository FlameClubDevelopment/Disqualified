package club.flame.disqualified.command.essentials;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import club.flame.disqualified.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FlyCommand extends BaseCommand {
    @Command(name = "fly", permission = "core.essentials.fly", aliases = {"flying"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(),"COMMANDS.FLY-MESSAGE");

        if (args.length == 0) {
            p.setAllowFlight(!p.getAllowFlight());
            p.sendMessage(CC.translate(Lang.PREFIX + configCursor.getString("DEFAULT")
                    .replace("<status>",(p.getAllowFlight() ? "&aenabled" : "&cdisabled"))));
            return;
        }

        if (args.length == 1) {
            p.sendMessage(CC.translate("&cUsage: /fly <target>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            target.setAllowFlight(!target.getAllowFlight());
            p.sendMessage(CC.translate(Lang.PREFIX + configCursor.getString("OTHER.SENDER")
                    .replace("<status>",(target.getAllowFlight() ? "&aenabled" : "&cdisabled"))
                    .replace("<target>",target.getName())));
            target.sendMessage(CC.translate(Lang.PREFIX + configCursor.getString("OTHER.SENDER")
                    .replace("<status>",(target.getAllowFlight() ? "&aenabled" : "&cdisabled"))
                    .replace("<sender>",p.getName())));
        } else {
            p.sendMessage(CC.translate(Lang.PREFIX + "&cPlayer not found."));
        }
    }
}
