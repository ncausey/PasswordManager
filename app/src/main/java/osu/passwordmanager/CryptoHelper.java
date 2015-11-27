package osu.passwordmanager;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A helper class that handles encryption/decryption of data along with hashing. Encryption uses
 * AES-128 and hashing uses PBKDF2
 * Created by Jacob on 11/21/2015.
 */
public class CryptoHelper {

    //AES-128 encryption using a randomly generated IV
    public static String encrypt(String key, String data){
        try {
            //Convert key to MD5 hash since MD5 is 128-bit
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(Base64.decode(key, Base64.DEFAULT));

            //Generate 128-bit initialization vector
            byte[] initVector = generateSalt(16);

            //Set up cipher
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec sks = new SecretKeySpec(digest.digest(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, sks, iv);

            //Encrypt data with key and vector
            byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
            String encodedVector = Base64.encodeToString(initVector, Base64.DEFAULT);
            return encodedVector + ":" + Base64.encodeToString(encryptedData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String key, String data){
        try{
            //Data is in format initVector:Data
            String[] dataParts = data.split(":");

            //Convert key to MD5 hash since MD5 is 128-bit
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(Base64.decode(key, Base64.DEFAULT));

            //Decode the vector
            byte[] decodedVector = Base64.decode(dataParts[0], Base64.DEFAULT);

            //Set up cipher
            IvParameterSpec iv = new IvParameterSpec(decodedVector);
            SecretKeySpec sks = new SecretKeySpec(digest.digest(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sks, iv);

            //Decrypt data
            byte[] decrypted = cipher.doFinal(Base64.decode(dataParts[1], Base64.DEFAULT));
            return new String(decrypted, "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    public static String getHash(String data) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = generateSalt(16);
        int iterations = 1234;

        //Generate hash using PBKDF2 algorithm with Hmac SHA512
        PBEKeySpec kSpec = new PBEKeySpec(data.toCharArray(), salt, iterations, 512);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = skf.generateSecret(kSpec).getEncoded();
        return Base64.encodeToString(hash, Base64.DEFAULT);
    }

    private static byte[] generateSalt(int blockSize) throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[blockSize];
        sr.nextBytes(salt);
        return salt;
    }
}
