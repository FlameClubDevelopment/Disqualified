package club.flame.disqualified.command.essentials;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealCommand extends BaseCommand {
    @Command(name = "heal", permission = "core.essentials.heal", aliases = {"curar"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "COMMANDS.HEAL-MESSAGES");
        if (args.length == 0) {
            p.setHealth(p.getMaxHealth());
            p.sendMessage(CC.translate(messages.getString("DEFAULT")));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.setHealth(target.getMaxHealth());
                p.sendMessage(CC.translate(messages.getString("OTHER").replace("<target>", target.getDisplayName())));
            }
        }
    }
}
