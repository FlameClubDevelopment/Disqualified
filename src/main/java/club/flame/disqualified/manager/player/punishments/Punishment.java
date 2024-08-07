package club.flame.disqualified.manager.player.punishments;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.utils.time.DateUtils;
import club.flame.disqualified.utils.time.TimeUtil;
import club.flame.disqualified.lib.config.ConfigCursor;
import club.flame.disqualified.lib.item.ItemCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@Getter
@RequiredArgsConstructor
public class Punishment {

    private final UUID uniqueId;

    private final PunishmentType type;

    @Setter
    private UUID addedBy;
    private final long addedAt;

    private final String reason;
    private final long duration;

    @Setter
    private UUID pardonedBy;
    @Setter
    private long pardonedAt;
    @Setter
    private String pardonedReason;
    @Setter
    private boolean pardoned;

    public Punishment(Document document) {
        this.uniqueId = UUID.fromString(document.getString("uuid"));
        this.type = PunishmentType.valueOf(document.getString("type"));

        if (document.containsKey("addedBy") && document.get("addedBy") != null) {
            this.addedBy = document.getString("addedBy").equals("null") ? null : UUID.fromString(document.getString("addedBy"));
            ;
        }

        this.addedAt = document.getLong("addedAt");

        this.reason = document.getString("reason");
        this.duration = document.getLong("duration");

        if (document.containsKey("pardonedBy") && document.get("pardonedBy") != null) {
            this.pardonedBy = document.getString("pardonedBy").equals("null") ? null : UUID.fromString(document.getString("pardonedBy"));
        }

        if (document.containsKey("pardonedAt") && document.get("pardonedAt") != null) {
            this.pardonedAt = document.getLong("pardonedAt");
        }

        if (document.containsKey("pardonedReason") && document.get("pardonedReason") != null) {
            this.pardonedReason = document.getString("pardonedReason");
        }

        if (document.containsKey("pardoned") && document.get("pardoned") != null) {
            this.pardoned = document.getBoolean("pardoned");
        }
    }

    public boolean isLifetime() {
        return type == PunishmentType.BLACKLIST || duration == -5L;
    }

    public boolean hasExpired() {
        return !(!pardoned && (isLifetime() || getRemaining() < 0));
    }

    public long getRemaining() {
        return System.currentTimeMillis() - (this.addedAt + this.duration);
    }

    public String getTimeLeft(boolean simple) {
        if (this.pardoned) return "Pardoned";
        if (this.hasExpired()) return "Expired";
        if (this.isLifetime()) return "Never";

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.setTime(new Date(System.currentTimeMillis()));
        to.setTime(new Date(this.addedAt + this.duration));
        to.add(Calendar.SECOND, 1);
        return simple ? DateUtils.formatSimplifiedDateDiff(from, to) : DateUtils.formatDateDiff(from, to);
    }

    public String getContext() {
        if (!type.isPardonable()) {
            return this.pardoned ? type.getUndoContext() : type.getContext();
        }

        return this.pardoned ? this.type.getUndoContext() : (this.type == PunishmentType.WARN || !this.type.isPardonable() ? "" : (this.isLifetime() ? "" : "temporarily ")) + this.type.getContext();
    }

    private String getTime() {
        String time;

        if (this.isPardoned() || this.isLifetime() || this.type == PunishmentType.WARN || this.type == PunishmentType.KICK) {
            time = "";
        } else {
            time = CC.translate(Disqualified.getInstance().getPunishmentConfig().getConfiguration().getString("PUNISHMENT-MESSAGES.TIME")).replace("<punish-time>", this.getTimeLeft(false));
        }
        return time;
    }

    public void broadcast(String senderName, String targetName, boolean silent) {
        ConfigCursor configCursor = new ConfigCursor(Disqualified.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES");
//        if (silent) {
//            Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("core.punishments.silent.see")).forEach(player -> {
//                Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENT-MESSAGES.SILENT").forEach(text ->
//                        player.sendMessage(CC.translate(text)
//                                .replace("<player>", targetName)
//                                .replace("<sender>", senderName)
//                                .replace("<context>", getContext())
//                                .replace("<reason>", this.pardoned ?
//                                        (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason.equals("") ? "No reason provided" : this.pardonedReason)
//                                        : (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
//                                .replace("<time>", CC.translate(getTime()))
//                        ));
//            });
//            Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENT-MESSAGES.SILENT").forEach(text -> {
//                Bukkit.getConsoleSender().sendMessage(CC.translate(text)
//                        .replace("<player>", targetName)
//                        .replace("<sender>", senderName)
//                        .replace("<context>", getContext())
//                        .replace("<reason>", this.pardoned ?
//                                (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason.equals("") ? "No reason provided" : this.pardonedReason)
//                                : (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
//                        .replace("<time>", CC.translate(getTime())));
//            });
//        } else {
        if (Disqualified.getInstance().getPunishmentConfig().getConfiguration().getBoolean("PUNISHMENT-MESSAGES.MESSAGE-IN-CONSOLE")) {
            getPunishmentMessages().forEach(text ->
                    Bukkit.getConsoleSender().sendMessage((silent ? CC.translate(configCursor.getString("SILENT")) : "") + CC.translate(text)
                            .replace("<player>", targetName)
                            .replace("<target>", targetName)
                            .replace("<sender>", senderName)
                            .replace("<staff>", senderName)
                            .replace("<context>", getContext())
                            .replace("<reason>", this.pardoned ?
                                    (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason.equals("") ? "No reason provided" : this.pardonedReason)
                                    : (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                            .replace("<time>", getTimeLeft(false))
                            .replace("<duration>", getTimeLeft(false))
                    ));
            senPlayersMSG(silent, configCursor, targetName, senderName);
        } else {
            senPlayersMSG(silent, configCursor, targetName, senderName);
        }
    }

    private void senPlayersMSG(boolean silent, ConfigCursor configCursor, String targetName, String senderName) {
        getPunishmentMessages().forEach(text -> {
            getPlayers(silent).forEach(player -> {
                player.sendMessage((silent ? CC.translate(configCursor.getString("SILENT")) : "") + CC.translate(text)
                        .replace("<player>", targetName)
                        .replace("<target>", targetName)
                        .replace("<sender>", senderName)
                        .replace("<staff>", senderName)
                        .replace("<context>", getContext())
                        .replace("<reason>", this.pardoned ?
                                (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason.equals("") ? "No reason provided" : this.pardonedReason)
                                : (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                        .replace("<time>", getTimeLeft(false))
                        .replace("<duration>", getTimeLeft(false))
                );
            });
        });
    }

    private List<Player> getPlayers(boolean silent) {
        if (silent) {
            return Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("core.punishments.silent.see")).collect(Collectors.toList());
        }
        return new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
    }

    public ItemStack toItemStack() {
        List<String> lore = new ArrayList<>();
        Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENTS-MENU.ITEM.LORE").forEach(text -> {
            switch (text) {
                case "<time-left>":
                    if (!this.hasExpired() && !this.isLifetime())
                        Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENTS-MENU.ITEM.TIME-LEFT").forEach(textExpired ->
                                lore.add(CC.translate(textExpired)
                                        .replace("<time>", this.getTimeLeft(true))));
                    break;
                case "<punishment-pardoned>":
                    if (isPardoned()) {
                        Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENTS-MENU.ITEM.PARDONED.EXPIRED").forEach(textRemoved ->
                                lore.add(CC.translate(textRemoved)
                                        .replace("<pardonedDate>", TimeUtil.formatIntoCalendarString(new Date(this.pardonedAt)))
                                        .replace("<pardonedBy>", Utils.getDisplayName(this.pardonedBy))
                                        .replace("<pardonedReason>", (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason == "" ? "No reason provided" : this.pardonedReason))));
                    } else if (this.hasExpired() && !this.isLifetime()) {
                        Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENTS-MENU.ITEM.PARDONED.ACTIVE").forEach(textRemoved ->
                                lore.add(CC.translate(textRemoved)
                                        .replace("<expiredIn>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt + this.duration)))));
                    }
                    break;
                default:
                    lore.add(translate(text));
                    break;
            }
        });
        String iD = this.uniqueId.toString().split("-")[0];
        return new ItemCreator(Material.INK_SACK)
                .setName(Disqualified.getInstance().getPunishmentConfig().getConfiguration().getString("PUNISHMENTS-MENU.ITEM.NAME")
                        .replace("<id>", iD.toUpperCase())
                        .replace("<status>", (this.hasExpired() ? "&7Inactive" : "&aActive"))
                )
                .setDurability((byte) (this.hasExpired() ? 8 : this.isLifetime() ? 5 : 13))
                .setLore(lore)
                .get();
    }

    private String translate(String text) {
        text = CC.translate(text);

        text = text
                .replace("<addedBy>", Utils.getDisplayName(this.addedBy))
                .replace("<addedDate>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt)))
                .replace("<reason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
        ;
        return text;
    }

    public List<String> getPunishmentMessages() {
        ConfigCursor banMessages = new ConfigCursor(Disqualified.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES.BAN");
        ConfigCursor blacklistMessages = new ConfigCursor(Disqualified.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES.BLACKLIST");
        ConfigCursor muteMessages = new ConfigCursor(Disqualified.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES.MUTE");
        ConfigCursor warnMessages = new ConfigCursor(Disqualified.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES");
        List<String> messages = new ArrayList<>();
        switch (this.type) {
            case BAN:
                if (this.pardoned) {
                    messages = CC.translate(banMessages.getStringList("UN-BAN"));
                } else if (!isLifetime()) {
                    messages = CC.translate(banMessages.getStringList("TEMPORARILY"));
                } else {
                    messages = CC.translate(banMessages.getStringList("PERMANENT"));
                }
                break;
            case BLACKLIST:
                if (this.pardoned) {
                    messages = CC.translate(blacklistMessages.getStringList("UN-BLACKLIST"));
                } else {
                    messages = CC.translate(blacklistMessages.getStringList("PERMANENT"));
                }
                break;
            case MUTE:
                if (this.pardoned) {
                    messages = CC.translate(muteMessages.getStringList("UN-MUTE"));
                } else if (!isLifetime()) {
                    messages = CC.translate(muteMessages.getStringList("TEMPORARILY"));
                } else {
                    messages = CC.translate(muteMessages.getStringList("PERMANENT"));
                }
                break;
            case KICK:
                messages = CC.translate(warnMessages.getStringList("KICK"));
                break;
            case WARN:
                messages = CC.translate(warnMessages.getStringList("WARN"));
                break;
            default:
                messages = Arrays.asList("Nothing type");
        }
        return messages;
    }

    public String toKickMessage(String alternativeAccount) {
        List<String> kickMessage = new ArrayList<>();
        switch (this.type) {
            case KICK:
                Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.KICK").forEach(text -> {
                    kickMessage.add(CC.translate(text)
                            .replace("<punishReason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                            .replace("<punishSender>", Utils.getDisplayName(this.addedBy)))
                    ;
                });
                break;
            case BAN:
                if (!this.isLifetime() && this.type.isBannable()) {
                    Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.TEMP-BAN").forEach(text -> {
                        switch (text) {
                            case "<alt-relation>":
                                if (alternativeAccount != null) {
                                    Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.ALT-RELATION").forEach(textExpired ->
                                            kickMessage.add(CC.translate(textExpired)
                                                    .replace("<alt-name>", alternativeAccount)));
                                }
                                break;
                            default:
                                kickMessage.add(CC.translate(text)
                                        .replace("<punishDate>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt)))
                                        .replace("<punishRemain>", this.getTimeLeft(false))
                                        .replace("<punishSender>", Utils.getDisplayName(this.addedBy))
                                        .replace("<punishReason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                                );
                                break;
                        }
                    });
                } else {
                    Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.BAN").forEach(text -> {
                        switch (text) {
                            case "<alt-relation>":
                                if (alternativeAccount != null) {
                                    Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.ALT-RELATION").forEach(textExpired ->
                                            kickMessage.add(CC.translate(textExpired)
                                                    .replace("<alt-name>", alternativeAccount)));
                                }
                                break;
                            default:
                                kickMessage.add(CC.translate(text)
                                        .replace("<punishDate>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt)))
                                        .replace("<punishRemain>", this.getTimeLeft(false))
                                        .replace("<punishSender>", Utils.getDisplayName(this.addedBy))
                                        .replace("<punishReason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                                );
                                break;
                        }
                    });
                }
                break;
            case BLACKLIST:
                Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.IP-BAN").forEach(text -> {
                    switch (text) {
                        case "<alt-relation>":
                            if (alternativeAccount != null) {
                                Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.ALT-RELATION").forEach(textExpired ->
                                        kickMessage.add(CC.translate(textExpired)
                                                .replace("<alt-name>", alternativeAccount)));
                            }
                            break;
                        default:
                            kickMessage.add(CC.translate(text)
                                    .replace("<punishDate>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt)))
                                    .replace("<punishRemain>", this.getTimeLeft(false))
                                    .replace("<punishSender>", Utils.getDisplayName(this.addedBy))
                                    .replace("<punishReason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                            );
                            break;
                    }
                });
                break;
            default:
                Disqualified.getInstance().getLogger().info("[Punishment] No found a any message for this punishment");
                break;
        }
        return StringUtils.join(kickMessage, "\n");
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof Punishment && ((Punishment) object).uniqueId.equals(this.uniqueId);
    }
}
