package club.flame.disqualified.lib.scoreboard;

import club.flame.disqualified.lib.DisqualifiedLib;
import club.flame.disqualified.lib.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

@Getter
public class BoardManager {
    private BoardAdapter adapter;

    private Map<UUID, Board> boardMap;

    public BoardManager(BoardAdapter adapter, int updateTick) {
        this.adapter = adapter;
        this.boardMap = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(new BoardListener(this), DisqualifiedLib.INSTANCE.getPlugin());
        Bukkit.getScheduler().runTaskTimer(DisqualifiedLib.INSTANCE.getPlugin(), this::sendScoreboard,0,updateTick);
    }

    public void sendScoreboard() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Board board = this.boardMap.get(player.getUniqueId());
            if (board != null) {
                Scoreboard scoreboard = board.getScoreboard();
                Objective objective = board.getObjective();
                String title = CC.translate(this.adapter.getTitle(player));
                if (!objective.getDisplayName().equals(title))
                    objective.setDisplayName(title);
                List<String> lines = this.adapter.getLines(player);
                if (lines == null || lines.isEmpty()) {
                    board.getEntries().forEach(BoardEntry::quit);
                    board.getEntries().clear();
                } else {
                    if (!this.adapter.getBoardStyle(player).isDescending()) Collections.reverse(lines);
                    if (board.getEntries().size() > lines.size())
                        for (int j = lines.size(); j < board.getEntries().size(); j++) {
                            BoardEntry entry = board.getEntryAtPosition(j);
                            if (entry != null)
                                entry.quit();
                        }
                    int cache = this.adapter.getBoardStyle(player).getStart();
                    for (int i = 0; i < lines.size(); i++) {
                        BoardEntry entry = board.getEntryAtPosition(i);
                        String line = CC.translate(lines.get(i));
                        if (entry == null)
                            entry = new BoardEntry(board, line);
                        entry.setText(line);
                        entry.setUp();
                        entry.send(this.adapter.getBoardStyle(player).isDescending() ? cache-- : cache++);
                    }
                }
                if (player.getScoreboard() == scoreboard)
                    continue;
                player.setScoreboard(scoreboard);
            }
        }
    }
}
