package club.flame.disqualified.command.chat;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.chat.ChatManager;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.commands.Completer;
import club.flame.disqualified.lib.config.ConfigCursor;
import club.flame.disqualified.lib.number.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatManagerCommand extends BaseCommand {

    @Completer(name = "chat", aliases = {"chatmanager"})
    public List<String> ChatManagerComplete(CommandArgs args) {
        List<String> list = new ArrayList<>();
        list.add("clear");
        list.add("mute");
        list.add("unmute");
        list.add("delay");

        return list;
    }

    @Command(name = "chat", permission = "core.manager", aliases = {"chatmanager"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "NETWORK.CHAT-MANAGER");
        ChatManager chatManager = Disqualified.getInstance().getChatManager();

        if (args.length == 0) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&4&lDisqualified&7 &4✖ &8- &7Chat Management"));
            player.sendMessage(CC.translate(" "));
            player.sendMessage(CC.translate("&4 ▸ &f/chat clear"));
            player.sendMessage(CC.translate(" "));
            player.sendMessage(CC.translate("&4 ▸ &f/chat mute"));
            player.sendMessage(CC.translate("&4 ▸ &f/chat unmute"));
            player.sendMessage(CC.translate(" "));
            player.sendMessage(CC.translate("&4 ▸ &f/chat delay <time>"));
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        switch (args[0]) {
            case "clear":
                for (int i = 0; i < 120; i++) {
                    Utils.sendAllMsg("");
                }
                Utils.sendAllMsg(CC.translate(messages.getString("CLEAR").replace("<player>", player.getDisplayName())));
                break;
            case "mute":
                if (!chatManager.isMute()) {
                    chatManager.setMute(true);
                    Bukkit.broadcastMessage(CC.translate(messages.getString("MUTE").replace("<player>", player.getName())));
                } else {
                    player.sendMessage(CC.translate(messages.getString("ALREADY").replace("<label>", "muted")));
                }
                chatManager.save();
                break;
            case "unmute":
                if (chatManager.isMute()) {
                    chatManager.setMute(false);
                    Bukkit.broadcastMessage(CC.translate(messages.getString("UNMUTE").replace("<player>", player.getName())));
                } else {
                    player.sendMessage(CC.translate(messages.getString("ALREADY").replace("<label>", "unmuted")));
                }
                chatManager.save();
                break;
            case "delay":
                if (!NumberUtils.checkInt(args[1])) {
                    player.sendMessage("§cIt must be a number");
                    return;
                }
                chatManager.setDelay(Integer.parseInt(args[1]));
                Bukkit.broadcastMessage(CC.translate(messages.getString("DELAY")
                        .replace("<delay>", args[1])
                        .replace("<player>", player.getName()))
                );
                chatManager.save();
                break;
            default:
                break;
        }
    }
}
