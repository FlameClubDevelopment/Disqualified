package club.flame.disqualified.menu.chat;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.menu.chat.buttons.IgnoreListButton;
import club.flame.disqualified.menu.chat.buttons.TogglePrivateMessagesButton;
import club.flame.disqualified.menu.chat.buttons.ToggleSoundButton;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class MessagesSettingsMenu extends Menu {

    private final ChatColor chatColor;

    public MessagesSettingsMenu(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    @Override
    public String getTitle(Player player) {
        return chatColor + "Chat Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ToggleSoundButton(playerData));
        buttons.put(4, new IgnoreListButton(playerData));
        buttons.put(8, new TogglePrivateMessagesButton(playerData));

        setPlaceholder(true);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9;
    }
}
