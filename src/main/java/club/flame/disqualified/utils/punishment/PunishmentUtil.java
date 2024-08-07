package club.flame.disqualified.utils.punishment;

import club.flame.disqualified.manager.player.punishments.Punishment;
import org.apache.commons.codec.binary.Base64;
import org.bson.Document;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class PunishmentUtil {

    public static Punishment getPunishmentToString(String string) {
        Document document = deserializePunishment(string);

        return new Punishment(document);
    }

    public static String serializePunishment(Punishment punishment) {
        StringBuilder builder = new StringBuilder();
        if (punishment == null) {
            return builder.toString();
        }

        builder.append("uuid@").append(punishment.getUniqueId().toString())
                .append(":type@").append(punishment.getType().name())
                .append(":addedBy@").append(punishment.getAddedBy() == null ? null : punishment.getAddedBy().toString())
                .append(":addedAt@").append(punishment.getAddedAt())
                .append(":reason@").append(punishment.getReason())
                .append(":duration@").append(punishment.getDuration())
                .append(":pardonedBy@").append(punishment.getPardonedBy() == null ? null : punishment.getPardonedBy().toString())
                .append(":pardonedAt@").append(punishment.getPardonedAt())
                .append(":pardonedReason@").append(punishment.getPardonedReason())
                .append(":pardoned@").append(punishment.isPardoned()
        );

        return builder.toString();
    }

    public static Document deserializePunishment(String string) {
        Document document = new Document();
        final String[] split = string.split(":");
        for (String str : split) {
            String[] itemAttribute = str.split("@");
            String s2 = itemAttribute[0];
            String data = itemAttribute[1];

            switch (s2) {
                case "uuid":
                    if (data != null) {
                        document.put("uuid", data);
                    }
                    break;
                case "type":
                    if (data != null) {
                        document.put("type", data);
                    }
                    break;
                case "addedBy":
                    if (data != null) {
                        document.put("addedBy", data);
                    }
                    break;
                case "addedAt":
                    if (data != null) {
                        document.put("addedAt", Long.valueOf(data));
                    }
                    break;
                case "reason":
                    if (data != null) {
                        document.put("reason", data);
                    }
                    break;
                case "duration":
                    if (data != null) {
                        document.put("duration", Long.valueOf(data));
                    }
                    break;
                case "pardonedBy":
                    if (data != null) {
                        document.put("pardonedBy", data);
                    }
                    break;
                case "pardonedAt":
                    if (data != null) {
                        document.put("pardonedAt", Long.valueOf(data));
                    }
                    break;
                case "pardonedReason":
                    if (data != null) {
                        document.put("pardonedReason", data);
                    }
                    break;
                case "pardoned":
                    if (data != null) {
                        document.put("pardoned", Boolean.valueOf(data));
                    }
                    break;
            }
        }

        return document;
    }

    public static List<String> savePlayerPunishments(List<Punishment> punishments) {
        List<String> strings = new ArrayList<>();
        for (Punishment punishment : punishments) {
            strings.add(serializePunishment(punishment));
        }

        return strings;
    }

    private static final byte[] SALT = {(byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, (byte) 0x9F, (byte) 0x5A, (byte) 0x75};

    private final static int ITERATION_COUNT = 31;

    public static String encode(String input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }
        try {

            KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            byte[] enc = ecipher.doFinal(input.getBytes());

            String res = new String(Base64.encodeBase64(enc));
            // escapes for url
            res = res.replace('+', '-').replace('/', '_').replace("%", "%25").replace("\n", "%0A");

            return res;

        } catch (Exception ignored) {
        }

        return "";

    }

    public static List<Punishment> getPlayerPunishments(List<String> strings) {
        List<Punishment> punishments = new ArrayList<>();

        for (String string : strings) {
            punishments.add(getPunishmentToString(string));
        }

        return punishments;
    }

    public static String reasonBuilder(String[] args, int reasonStart) {
        StringBuilder reasonBuilder = new StringBuilder();

        for (int i = reasonStart; i < args.length; ++i) {
            reasonBuilder.append(args[i]).append(" ");
        }
        if (reasonBuilder.length() == 0) {
            reasonBuilder.append("No reason provided");
        }

        return reasonBuilder.toString().trim();
    }

    public static String getReasonAndRemoveSilent(String string) {
        string = string
                .replace("-S", "")
                .replace("-s", "")
                .replace("-silent", "")
                .replace("-SILENT", "");

        if (string.equalsIgnoreCase("")) {
            string = "No reason provided.";
        }

        return string;
    }

    public static String getPunishReason(Punishment punishment) {
        return punishment.isPardoned() ?
                punishment.getPardonedReason() == null || punishment.getPardonedReason().isEmpty() ? "No reason provided" : punishment.getPardonedReason()
                : punishment.getReason() == null || punishment.getReason().isEmpty() ? "No reason provided"
                : punishment.getReason();
    }
}
