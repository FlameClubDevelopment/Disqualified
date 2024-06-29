package club.flame.disqualified.command.tags;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.menu.tags.TagsMenu;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.commands.Completer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TagsCommand extends BaseCommand {
    @Completer(name = "tags", aliases = {"tag", "prefix"})

    public List<String> gamemodeCompleter(CommandArgs args) {
        if (args.length() == 1) {
            List<String> list = new ArrayList<String>();
            if (args.getPlayer().isOp()) {
                list.add("reload");
                return list;
            }
        }
        return null;
    }

    @Command(name = "tags", permission = "core.tags", aliases = {"tag", "prefix"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            new TagsMenu().openMenu(p);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.isOp()) return;
            try {
                Disqualified.getInstance().getTagManager().deleteTags();
                Disqualified.getInstance().reloadTags();
                Disqualified.getInstance().getTagManager().registerTags();
                p.sendMessage("§aSuccessfully reloaded tags");
            } catch (Exception exception) {
                p.sendMessage("§cAn error occurred while reloading the tags!");
            }
        }
    }
}