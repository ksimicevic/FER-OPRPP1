package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

public class Crypto {

    public static void checksha(String path, String sha) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        InputStream s = Files.newInputStream(Paths.get(path));

        byte[] arr = new byte[4096];
        int readBytes;
        while ((readBytes = s.read(arr)) > -1) {
            messageDigest.update(arr, 0, readBytes);
        }
        s.close();
        byte[] digest = messageDigest.digest();
        String generatedSha = Util.bytetohex(digest);

        if (generatedSha.equals(sha)) {
            System.out.println("Digesting completed. Digest of " + path + " matches expected digest.");
        } else {
            System.out.println("Digesting completed. Digest of " + path + " does not match the expect digest. Digest was: " + generatedSha);
        }
    }

    public static void encrypt(String path, String newPath, String password, String initializationVector) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(password), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(initializationVector));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);

        InputStream inputStream = Files.newInputStream(Paths.get(path));
        OutputStream outputStream = Files.newOutputStream(Paths.get(newPath));

        byte[] arr = new byte[4096];
        int readBytes;
        while ((readBytes = inputStream.read(arr)) > -1) {
            outputStream.write(cipher.update(arr, 0, readBytes));
        }
        outputStream.write(cipher.doFinal());

        inputStream.close();
        outputStream.close();

        System.out.println("Encryption completed. Generated file " + newPath + " based on file " + path + ".");
    }


    public static void decrypt(String path, String newPath, String password, String initializationVector) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(password), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(initializationVector));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);

        InputStream inputStream = Files.newInputStream(Paths.get(path));
        OutputStream outputStream = Files.newOutputStream(Paths.get(newPath));

        byte[] arr = new byte[4096];
        int readBytes;
        while ((readBytes = inputStream.read(arr)) != -1) {
            outputStream.write(cipher.update(arr, 0, readBytes));
        }
        outputStream.write(cipher.doFinal());

        inputStream.close();
        outputStream.close();

        System.out.println("Decryption completed. Generated file " + newPath + " based on file " + path + ".");
    }


    public static void main(String... args) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        String operation = args[0];
        Scanner sc = new Scanner(System.in);

        if (operation.equals("checksha")) {
            System.out.println("Please provide expected sha-256 digest for " + args[1] + " :");
            checksha(args[1], sc.next());
        } else if (operation.equals("encrypt")) {
            System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
            String password = sc.next();
            System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
            encrypt(args[1], args[2], password, sc.next());
        } else if (operation.equals("decrypt")) {
            System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
            String password = sc.next();
            System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
            decrypt(args[1], args[2], password, sc.next());
        }
    }
}
