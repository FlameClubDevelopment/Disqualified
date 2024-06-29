package club.flame.disqualified;

public enum Permissions {

    // Update Checker
    UPDATE_NOTIFICATION("alert.updates");

    private final String perm;

    Permissions(String perm) {
        this.perm = perm;
    }

    public final String getPermission() {
        return "core." + this.perm;
    }

}
