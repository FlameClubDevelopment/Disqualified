package club.flame.disqualified.lib.scoreboard;

public enum BoardStyle {
    MODERN(false, 1),
    KOHI(true, 15),
    VIPER(true, -1),
    TEAMSHQ(true, 0);

    private int start;

    private boolean descending;

    BoardStyle(boolean descending, int start) {
        this.descending = descending;
        this.start = start;
    }

    public boolean isDescending() {
        return this.descending;
    }

    public int getStart() {
        return this.start;
    }
}
