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
    final String USER_TEXT_PW = "USER_TEXT_PW";
    final String USER_VOICE_PW = "USER_VOICE_PW";
    final String USER_FACE_PROPERTIES = "USER_FACE_PROPERTIES";
    final String TEXT_EXT = "_TEXT";
    final String VOICE_EXT = "_VOICE";

    SharedPreferences sP;
    String textKey;
    String voiceKey;


    public DataHandler(SharedPreferences sP, String tKey, String vKey){
        this.sP = sP;
        this.textKey = tKey;
        this.voiceKey = vKey;
    }

    public void savePassword(String key, String data) throws Exception{
        //In order to be able to retrieve data given either a voice or text password we must save
        //the data twice, once for each key
        String encryptedDataText = CryptoHelper.encrypt(this.textKey, data);
        String encryptedDataVoice = CryptoHelper.encrypt(this.voiceKey, data);
        if (sP.contains(key + TEXT_EXT)){
            sP.edit().remove(key + TEXT_EXT);
        }
        if (sP.contains(key + VOICE_EXT)){
            sP.edit().remove(key + VOICE_EXT);
        }
        sP.edit().putString(key + TEXT_EXT, encryptedDataText);
        sP.edit().putString(key + VOICE_EXT, encryptedDataVoice);

        //Update the list of saved passwords
        String json = sP.getString(USER_KEYS, null);
        Gson gson = new Gson();
        List<String> userKeys = gson.fromJson(json, ArrayList.class);
        if (!userKeys.contains(key)){
            userKeys.add(key);
            String newJson = gson.toJson(userKeys);
            sP.edit().putString(USER_KEYS, newJson);
        }

        sP.edit().apply();
    }

    public void deletePassword(String key){
        String json = sP.getString(USER_KEYS, null);
        Gson gson = new Gson();
        List<String> userKeys = gson.fromJson(json, ArrayList.class);
        //Remove password from password list, along with any data associated with it
        if (userKeys.contains(key)){
            userKeys.remove(key);
            if (sP.contains(key + TEXT_EXT)){
                sP.edit().remove(key + TEXT_EXT);
            }
            if (sP.contains(key + VOICE_EXT)){
                sP.edit().remove(key + VOICE_EXT);
            }
            sP.edit().apply();
        }
    }

}
