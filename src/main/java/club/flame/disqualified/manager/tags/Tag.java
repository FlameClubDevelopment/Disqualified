package club.flame.disqualified.manager.tags;

import club.flame.disqualified.lib.chat.CC;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class Tag {

    private String tagName;
    private String tagDisplayName;
    private String tagPrefix;
    private ItemStack tagIcon;
    private List<String> tagLore;
    private String tagPermission;
    private ChatColor chatColor;

    public Tag(String tagName, String tagDisplayName, String tagPrefix, ItemStack tagIcon, List<String> tagLore, String tagPermission, ChatColor chatColor) {
        this.tagName = tagName;
        this.tagDisplayName = CC.translate(tagDisplayName);
        this.tagPrefix = CC.translate(tagPrefix);
        this.tagIcon = tagIcon;
        this.tagLore = tagLore;
        this.tagPermission = tagPermission;
        this.chatColor = chatColor;
    }
}
