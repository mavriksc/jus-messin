package org.mavriksc.messin.random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

public class HashPass {


    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    private HashPass(){}

    public static byte[] getNextSalt(){
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
        byte[] pwdHash = hash(password, salt);

        String s = Arrays.toString(pwdHash);
        System.out.println("Encryptor pwdHash: " + s);

        String s2 = Arrays.toString(expectedHash);
        System.out.println("Encryptor expectedHash: " + s2);

        Arrays.fill(password, Character.MIN_VALUE);
        if (pwdHash.length != expectedHash.length) return false;
        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHash[i] != expectedHash[i]) return false;
        }
        return true;
    }

    public static void main(String[] args){

        char[] passCharArray = "password".toCharArray();

        byte[] salt = HashPass.getNextSalt();
        byte[] hashedPass = HashPass.hash(passCharArray, salt);

        String saltString = Arrays.toString(salt);
        System.out.println("SALT: " + saltString);

        String hashedPassString = Arrays.toString(hashedPass);
        System.out.println("HASHED PASS: " + hashedPassString);

        System.out.println("Passwords match: " + HashPass.isExpectedPassword(passCharArray, salt, hashedPass));


    }


}
