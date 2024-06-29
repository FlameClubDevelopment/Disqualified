package club.flame.disqualified.lib.number;

public class NumberUtils {

    public static boolean checkInt(String s) {
        try {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean checkDouble(String s) {
        try {
            double i = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean checkLong(String s) {
        try {
            long i = Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}