package virtualsystems.com.br.financial;

import java.security.MessageDigest;

public class MD5 {
    public static String md5(String email) {
        final MessageDigest messageDigest;
        StringBuilder sb = new StringBuilder();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            byte[] digest = messageDigest.digest(email.getBytes("UTF-8"));
            for (byte b : digest) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
        } catch (Exception e) {
            sb.setLength(0);
            sb.append(email);
        }
        return sb.toString();
    }
}
