package club.flame.disqualified.menu.punishments;

import club.flame.disqualified.lib.chat.CC;
import lombok.Getter;
import org.apache.commons.lang.WordUtils;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public enum PunishmentFilter {

    NONE(CC.WHITE),
    ACTIVE(CC.GREEN),
    INACTIVE(CC.RED),
    LIFETIME(CC.GOLD),
    TEMPORARILY(CC.YELLOW);

    @Getter private final String name;
    @Getter private final String color;

    private static PunishmentFilter[] values = values();

    PunishmentFilter(String color) {
        this.name = WordUtils.capitalizeFully(this.name());
        this.color = color;
    }

    public String getDisplayName() {
        return this.color + name;
    }

    public PunishmentFilter next() {
        return values[(this.ordinal() + 1) % values.length];
    }
}
