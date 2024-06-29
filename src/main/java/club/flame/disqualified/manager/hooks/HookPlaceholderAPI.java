package club.flame.disqualified.manager.hooks;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class HookPlaceholderAPI extends PlaceholderExpansion {

    private Disqualified plugin;

    public HookPlaceholderAPI(Disqualified plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "disqualified";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getAuthor() {
        return ("themanfurious");
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        // %Disqualified_rank_name%
        if (identifier.equalsIgnoreCase("rank_name")) {
            if (Objects.requireNonNull(PlayerData.getPlayerData(player.getName())).getHighestRank().getName() == null) {
                return Disqualified.getInstance().getRankManager().getDefaultRank().getName();
            }
            return Objects.requireNonNull(PlayerData.getPlayerData(player.getName())).getHighestRank().getName();
        }

        // %Disqualified_rank_color%
        if (identifier.equalsIgnoreCase("rank_color")) {
            return "&" + Objects.requireNonNull(PlayerData.getPlayerData(player.getName())).getHighestRank().getColor().getChar();
        }

        // %Disqualified_rank_prefix%
        if (identifier.equalsIgnoreCase("rank_prefix")) {
            if (Objects.requireNonNull(PlayerData.getPlayerData(player.getName())).getHighestRank().getPrefix() == null) {
                return Disqualified.getInstance().getRankManager().getDefaultRank().getPrefix();
            }
            return Objects.requireNonNull(PlayerData.getPlayerData(player.getName())).getHighestRank().getPrefix();
        }

        // %Disqualified_rank_name_color%
        if (identifier.equalsIgnoreCase("name_color")) {
            return ChatColor.valueOf(PlayerData.getPlayerData(player.getUniqueId()).getNameColor()).toString();
        }

        // %Disqualified_tag%
        if (identifier.equalsIgnoreCase("tag")) {
            return PlayerData.getPlayerData(player.getUniqueId()).getTag() == null ? "" : PlayerData.getPlayerData(player.getUniqueId()).getTag();
        }

        return null;
    }
}
