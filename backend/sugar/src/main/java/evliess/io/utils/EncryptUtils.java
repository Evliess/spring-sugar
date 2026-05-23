package evliess.io.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

public class EncryptUtils {

    private static final Logger log = LoggerFactory.getLogger(EncryptUtils.class);



    private static byte[] encrypt(PrivateKey privateKey, String text) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(text.getBytes());
    }

    private static String decrypt(PublicKey publicKey, byte[] encryptedBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private static Path createTmpFile(String content) throws IOException {
        Path path = Files.createFile(Path.of("./" + UUID.randomUUID()));
        Files.write(path, content.getBytes());
        return path;
    }


}
