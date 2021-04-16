package hr.fer.oprpp1.hw05.crypto;

public class Util {

    public static String bytetohex(byte[] bytearray) {

        if (bytearray.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (byte b : bytearray) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16))
                    .append(Character.forDigit((b & 0xF), 16));
        }
        return sb.toString();
    }

    public static byte[] hextobyte(String keyText) {
        int length = keyText.length();
        byte[] result = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            result[i / 2] = (byte) ((Character.digit(keyText.charAt(i), 16) << 4) + Character.digit(keyText.charAt(i + 1), 16));
        }

        return result;
    }
}