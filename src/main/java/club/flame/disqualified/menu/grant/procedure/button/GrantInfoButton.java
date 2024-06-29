package club.flame.disqualified.menu.grant.procedure.button;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.ranks.Rank;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@AllArgsConstructor
public class GrantInfoButton extends Button {

    private PlayerData data;

    @Override
    public ItemStack getButtonItem(Player player) {
        Rank rank = Rank.getRankByName(data.getGrantProcedure().getRankName());
        if (rank == null) return new ItemStack(Material.SKULL_ITEM);
        ItemCreator itemStack = new ItemCreator(Material.NETHER_STAR);
        itemStack.glow();
        itemStack.setName(rank.getColor() + "Grant information");

        List<String> lines = new ArrayList<>();
        lines.add(CC.MENU_BAR);
        lines.add(CC.translate(rank.getColor() + "Rank &7» &f" + data.getGrantProcedure().getRankName()));
        lines.add(CC.translate(rank.getColor() + "Player &7» &f" + data.getGrantProcedure().getPlayerData().getName()));
        lines.add(CC.translate(rank.getColor() + "Current Player Rank &7» &f" + data.getGrantProcedure().getPlayerData().getHighestRank().getColor() + data.getGrantProcedure().getPlayerData().getHighestRank().getName()));
        lines.add(CC.translate(rank.getColor() + "Duration &7» &f" + (data.getGrantProcedure().isPermanent() ? "Permanent" : data.getGrantProcedure().getNiceDuration())));
        lines.add(CC.translate(rank.getColor() + "Reason &7» &f" + data.getGrantProcedure().getEnteredReason()));
        lines.add(CC.MENU_BAR);

        itemStack.setLore(lines);

        return itemStack.get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
