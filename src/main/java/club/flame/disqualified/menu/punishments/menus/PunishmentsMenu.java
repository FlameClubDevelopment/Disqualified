package club.flame.disqualified.menu.punishments.menus;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.punishments.PunishmentType;
import club.flame.disqualified.menu.punishments.button.PlayerAltsButton;
import club.flame.disqualified.menu.punishments.button.PlayerInfoButton;
import club.flame.disqualified.menu.punishments.button.PunishmentTypeButton;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@AllArgsConstructor
public class PunishmentsMenu extends Menu {

    private PlayerData targetData;

    @Override
    public String getTitle(Player player) {
        return ChatColor.valueOf(targetData.getNameColor()) + targetData.getName() + "'s Punishments";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(10, new PlayerInfoButton(this.targetData));
        buttonMap.put(12, new PunishmentTypeButton(targetData, PunishmentType.BLACKLIST));
        buttonMap.put(14, new PunishmentTypeButton(targetData, PunishmentType.BAN));
        buttonMap.put(28, new PlayerAltsButton(targetData));
        buttonMap.put(30, new PunishmentTypeButton(targetData, PunishmentType.MUTE));
        buttonMap.put(32, new PunishmentTypeButton(targetData, PunishmentType.WARN));
        buttonMap.put(34, new PunishmentTypeButton(targetData, PunishmentType.KICK));

        return buttonMap;
    }

    @Override
    public boolean isUpdateAfterClick() {
        return false;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
