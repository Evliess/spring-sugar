package evliess.io.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class TokenUtils {

    private static final char[] ENCODING_MAP = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    private static String encode(String input) {
        StringBuilder encoded = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                int digit = Character.getNumericValue(c);
                encoded.append(ENCODING_MAP[digit]);
            } else {
                throw new IllegalArgumentException("Invalid input");
            }
        }
        return encoded.toString();
    }

    private static String decode(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            int index = c - 'A';
            if (index >= 0 && index < ENCODING_MAP.length) {
                sb.append(index);
            } else {
                throw new IllegalArgumentException("Invalid input");
            }
        }
        return sb.toString();
    }

    private static String getNowPlusDaysToken(int days) {
        return encode(Instant.now().plus(Duration.ofDays(days)).toEpochMilli() + "");
    }

    public static String generateToken(int days) {
        return UUID.randomUUID() + "--" + getNowPlusDaysToken(days);
    }

    public static boolean isValidToken(String token) {
        String[] parts = token.split("--");
        if (parts.length != 2) {
            return false;
        }
        String encodedTime = parts[1];
        String decodedTime = decode(encodedTime);
        long time = Long.parseLong(decodedTime);
        return Instant.ofEpochMilli(time).isAfter(Instant.now());
    }
}
