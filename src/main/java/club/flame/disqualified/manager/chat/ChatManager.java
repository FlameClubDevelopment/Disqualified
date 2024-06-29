package club.flame.disqualified.manager.chat;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.config.ConfigCursor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatManager {

    private boolean mute = false;
    private int delay = 3;

    public void load() {
        ConfigCursor configCursor = new ConfigCursor(Disqualified.getInstance().getSettingsConfig(), "CHAT");
        this.mute = configCursor.getBoolean("MUTE");
        this.delay = configCursor.getInt("DELAY");
    }

    public void save() {
        ConfigCursor configCursor = new ConfigCursor(Disqualified.getInstance().getSettingsConfig(), "CHAT");
        configCursor.set("MUTE", mute);
        configCursor.set("DELAY", delay);
        configCursor.save();
    }

    public void setChatDelay(int delay) {
    }
}
