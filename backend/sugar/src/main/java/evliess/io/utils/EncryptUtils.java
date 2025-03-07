package evliess.io.utils;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.UUID;

public class EncryptUtils {

    private static PrivateKey readPrivateKey(String filename) throws Exception {
        PEMParser pemParser = new PEMParser(new FileReader(filename));
        Object object = pemParser.readObject();
        if (object instanceof PrivateKeyInfo privateKeyInfo) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            return converter.getPrivateKey(privateKeyInfo);
        }
        throw new IllegalArgumentException("Provided PEM file does not contain a private key");
    }

    private static PublicKey readPublicKey(String filename) throws Exception {
        PEMParser pemParser = new PEMParser(new FileReader(filename));
        Object object = pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        return converter.getPublicKey((org.bouncycastle.asn1.x509.SubjectPublicKeyInfo) object);
    }

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

    public static boolean verifyUser(String privateKeyContent, String userName, String accessKey) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            Path privateKeyPath = createTmpFile(privateKeyContent);
            PrivateKey privateKey = readPrivateKey(privateKeyPath.toString());
            byte[] encryptedBytes = encrypt(privateKey, userName);
            PublicKey publicKey = readPublicKey(System.getenv("PUBLIC_KEY_PATH"));
            String decryptedText = decrypt(publicKey, encryptedBytes);
            File tmp = privateKeyPath.toFile();
            tmp.deleteOnExit();
            return decryptedText.equals(accessKey);
        } catch (Exception e) {
            System.err.println("Failed to verify user: " + e.getMessage());
        }
        return false;
    }
}
