package club.flame.disqualified.lib.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface BoardAdapter {

    String getTitle(Player player);

    List<String> getLines(Player player);

    BoardStyle getBoardStyle(Player player);
}

