package osu.passwordmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for managing user data, which is stored in SharedPreferences.
 * Created by Jacob on 11/21/2015.
 */
public class DataHandler {
    final String USER_KEYS = "USER_KEYS";
    final String USER_PW = "USER_PW";
    final String USER_FACE_PROPERTIES = "USER_FACE_PROPERTIES";
    final int MIN_PASSWORD_LENGTH = 6;

    SharedPreferences sP;
    String masterKey;


    public DataHandler(SharedPreferences sP, String key){
        this.sP = sP;
        this.masterKey = key;
    }

    /**
     * Removes the password from the password manager list if it exists.
     * @param key the associated key with the password
     */
    public void deleteManagedPassword(String key){
        String json = sP.getString(USER_KEYS, null);
        Gson gson = new Gson();
        List<String> userKeys = gson.fromJson(json, ArrayList.class);
        SharedPreferences.Editor editor = sP.edit();
        //Remove password from password list, along with any data associated with it
        if (userKeys.contains(key)){
            userKeys.remove(key);
            String newJson = gson.toJson(userKeys);
            editor.remove(USER_KEYS);
            editor.putString(USER_KEYS, newJson);
            if (sP.contains(key)){
                editor.remove(key);
            }
            editor.apply();
        }
    }

    /**
     * Returns all saved managed passwords in key value pairs.
     * @return Arraylist of key value pair of passwords
     */
    public List<Pair<String, String>> getAllManagedPasswords(){
        List<Pair<String, String>> passwords = new ArrayList<Pair<String, String>>();
        String json = sP.getString(USER_KEYS, null);
        Gson gson = new Gson();
        List<String> userKeys;
        if (json != null){
            userKeys = gson.fromJson(json, ArrayList.class);
        } else {
            userKeys = new ArrayList<>();
        }

        if (userKeys != null && userKeys.size() > 0) {
            for (String userKey : userKeys){
                String value = CryptoHelper.decrypt(this.masterKey, sP.getString(userKey, null));
                passwords.add(new Pair<String, String>(userKey, value));
            }
        }
        return passwords;
    }

    /**
     * Retrieves the hash of the user's master password
     * @return hash of master password
     */
    public String getUserPassword(){
        if (sP.contains(USER_PW)){
            return sP.getString(USER_PW, null);
        }
        return "";
    }

    /**
     * Sets up the initial user password for the app
     * @param pw the password to set
     * @return true if successful. If there already exists a password,
     * nothing is changed and false is returned
     * @throws Exception
     */
    public boolean setInitialUserPassword(String pw) throws Exception {
        if (pw.length() <= MIN_PASSWORD_LENGTH) return false;
        if (!sP.contains(USER_PW)){
            SharedPreferences.Editor editor = sP.edit();
            String hash = CryptoHelper.getHash(pw);
            editor.putString(USER_PW, hash);
            editor.apply();
            return true;
        }

        return false;
    }

    /**
     * Saves the password and associated data to the list of managed passwords.
     * @param key the "name" of the password (i.e if we want to store password
     *            for Facebook, Google, etc.)
     * @param data the password itself we want to store
     * @return true if operation is successful, false otherwise. Password must meet
     * minimum length requirements.
     * @throws Exception
     */
    public boolean saveManagedPassword(String key, String data) throws Exception{
        if (key.equals(USER_PW)) return false;
        //In order to be able to retrieve data given either a voice or text password we must save
        //the data twice, once for each key
        SharedPreferences.Editor editor = sP.edit();
        String encryptedData = CryptoHelper.encrypt(this.masterKey, data);
        if (sP.contains(key)){
            editor.remove(key);
        }
        editor.putString(key, encryptedData);

        //Update the list of saved passwords
        String json = sP.getString(USER_KEYS, null);
        Gson gson = new Gson();
        List<String> userKeys = gson.fromJson(json, ArrayList.class);
        if (userKeys == null) userKeys = new ArrayList<String>();
        if (!userKeys.contains(key)){
            userKeys.add(key);
            String newJson = gson.toJson(userKeys);
            editor.remove(USER_KEYS);
            editor.putString(USER_KEYS, newJson);
        }

        editor.apply();
        return true;
    }

    /**
     * Sets a new password for the user, re-encrypting all data with the new password
     * @param newPw new password to change to
     * @param oldPw old password used to verify authentication and decrypt data
     * @return true if operation successful, false otherwise. Password must meet minimum
     * password requirements.
     * @throws Exception
     */
    public boolean setUserPassword(String newPw, String oldPw) throws Exception {
        if (newPw.length() < MIN_PASSWORD_LENGTH) return false;
        SharedPreferences.Editor editor = sP.edit();
        String oldPwHash = CryptoHelper.getHash(oldPw);
        if (getUserPassword().equals(oldPwHash)){
            String newHash = CryptoHelper.getHash(newPw);
            //Update the new password
            editor.remove(USER_PW);
            editor.putString(USER_PW, newHash);

            //Re-encrypt all the data with the new password
            String json = sP.getString(USER_KEYS, null);
            Gson gson = new Gson();
            List<String> userKeys = gson.fromJson(json, ArrayList.class);
            for (String userKey : userKeys){
                if (sP.contains(userKey)){
                    String data = CryptoHelper.decrypt(oldPw, sP.getString(userKey, null));
                    editor.remove(userKey);
                    editor.putString(userKey, CryptoHelper.encrypt(newPw, data));
                }
            }

            editor.apply();
            return true;
        }

        return false;
    }

}
