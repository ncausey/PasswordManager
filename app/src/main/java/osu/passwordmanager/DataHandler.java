package osu.passwordmanager;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void deleteManagedPassword(String key){
        String json = sP.getString(USER_KEYS, null);
        Gson gson = new Gson();
        List<String> userKeys = gson.fromJson(json, ArrayList.class);
        //Remove password from password list, along with any data associated with it
        if (userKeys.contains(key)){
            userKeys.remove(key);
            if (sP.contains(key)){
                sP.edit().remove(key);
            }
            sP.edit().apply();
        }
    }

    private String getUserPassword(){
        if (sP.contains(USER_PW)){
            return sP.getString(USER_PW, null);
        }
        return "";
    }

    public boolean setInitialUserPassword(String pw) throws Exception {
        if (pw.length() <= MIN_PASSWORD_LENGTH) return false;
        if (!sP.contains(USER_PW)){
            String hash = CryptoHelper.getHash(pw);
            sP.edit().putString(USER_PW, hash);
            sP.edit().apply();
            return true;
        }

        return false;
    }

    public boolean saveManagedPassword(String key, String data) throws Exception{
        if (key.equals(USER_PW)) return false;
        //In order to be able to retrieve data given either a voice or text password we must save
        //the data twice, once for each key
        String encryptedData = CryptoHelper.encrypt(this.masterKey, data);
        if (sP.contains(key)){
            sP.edit().remove(key);
        }
        sP.edit().putString(key, encryptedData);

        //Update the list of saved passwords
        String json = sP.getString(USER_KEYS, null);
        Gson gson = new Gson();
        List<String> userKeys = gson.fromJson(json, ArrayList.class);
        if (!userKeys.contains(key)){
            userKeys.add(key);
            String newJson = gson.toJson(userKeys);
            sP.edit().remove(USER_KEYS);
            sP.edit().putString(USER_KEYS, newJson);
        }

        sP.edit().apply();
        return true;
    }

    public boolean setUserPassword(String newPw, String oldPw, String medium) throws Exception {
        if (newPw.length() < MIN_PASSWORD_LENGTH) return false;
        String oldPwHash = CryptoHelper.getHash(oldPw);
        if (getUserPassword().equals(oldPwHash)){
            String newHash = CryptoHelper.getHash(newPw);

            sP.edit().apply();
            return true;
        }

        return false;
    }

}
