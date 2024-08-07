package club.flame.disqualified.lib.scoreboard;

import club.flame.disqualified.lib.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

@Getter
public class Board {

    private List<BoardEntry> entries = new ArrayList<>();

    private List<String> strings = new ArrayList<>();

    private Scoreboard scoreboard;

    private Objective objective;

    private UUID id;

    private BoardManager board;

    public Board(Player player, BoardManager board) {
        this.id = player.getUniqueId();
        this.board = board;
        setUp(player);
    }

    public void setUp(Player player) {
        if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
            this.scoreboard = player.getScoreboard();
        } else {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        this.objective = this.scoreboard.registerNewObjective("Default", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        if (this.board.getAdapter().getTitle(player) == null){
            this.objective.setDisplayName(CC.translate("&bDefault Tittle"));
        } else {
            this.objective.setDisplayName(this.board.getAdapter().getTitle(player));
        }
        player.setScoreboard(this.scoreboard);
    }

    String getUniqueString() {
        String string = getRandomColor();
        while (this.strings.contains(string))
            string = string + getRandomColor();
        if (string.length() > 16)
            return getUniqueString();
        this.strings.add(string);
        return string;
    }

    BoardEntry getEntryAtPosition(int position) {
        if (position >= this.entries.size())
            return null;
        return this.entries.get(position);
    }

    private static String getRandomColor() {
        Random random = new Random();
        return colors().get(random.nextInt(colors().size() - 1)).toString();
    }

    private static List<ChatColor> colors(){
        List<ChatColor> chatColors = new ArrayList<>();
        Arrays.stream(ChatColor.values()).filter(ChatColor::isColor).forEach(cc -> chatColors.add(cc));
        return chatColors;
    }
}

