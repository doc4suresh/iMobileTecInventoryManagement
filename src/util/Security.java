
package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    public static String getHash(String password) {
        StringBuffer sb = new StringBuffer();
        byte[] hashedPasseword = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(password.getBytes("UTF-8"));
            hashedPasseword = digest.digest();

            for (int i = 0; i < hashedPasseword.length; i++) {
                sb.append(Integer.toString((hashedPasseword[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        }
        return sb.toString();
    }

}
