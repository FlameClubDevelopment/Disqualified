package club.flame.disqualified.command;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.commands.Completer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DisqualifiedCommand extends BaseCommand {
    @Completer(name = "core", aliases = {"disqualifiedcore", "disqualified"})

    public List<String> gamemodeCompleter(CommandArgs args) {
        List<String> list = new ArrayList<>();
        if (!args.getPlayer().isOp()) {
            return null;
        }
        if (args.length() == 1) {
            list.add("reload");
            return list;
        }
        return null;
    }

    @Command(name = "core", aliases = {"disqualifiedcore", "disqualified"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage(CC.translate("&4Disqualified Core ✖ &7- &fv" + Disqualified.getInstance().getDescription().getVersion()));
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage(CC.translate("&4Author&f: " + Disqualified.getInstance().getAuthors()));
            p.sendMessage(CC.translate("&4Description&f: " + Disqualified.getInstance().getDescription().getDescription()));
            p.sendMessage(CC.translate("&4Discord&f: " + Disqualified.getInstance().getDescription().getWebsite()));
            p.sendMessage(CC.CHAT_BAR);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.isOp()) return;
            try {
                Disqualified.getInstance().reloadFile();
                p.sendMessage(CC.CHAT_BAR);
                p.sendMessage(CC.translate("&4Disqualified Core ✖ &7- &fv" + Disqualified.getInstance().getDescription().getVersion()));
                p.sendMessage(CC.translate("&a&oSuccesfully reloaded: &7messages, database and settings"));
                p.sendMessage(CC.CHAT_BAR);
            } catch (Exception exception) {
                p.sendMessage(CC.translate("&cThere has been an error while reloading Disqualified Core files!"));
            }
        }
    }
}
