package utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Anuran on 5/19/2017.
 */

public class SharedPrefManager {
    private final String FILE_NAME="event_shared_pref";
    private final String ID_KEY="USER_ID";
    private final String NAME_KEY="USER_NAME";
    private final String EMAIL_KEY="EMAIL_KEY";
    private final String LOGIN_KEY="LOGIN_KEY";
    private final String DOWNLOAD_EVENT="DOWNLOAD_EVENT";
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPrefManager(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void setID(String id){
        editor.putString(ID_KEY,id);
        editor.commit();
    }

    public String getID(){
        return sharedPreferences.getString(ID_KEY,"-1");
    }

    public void setName(String id){
        editor.putString(NAME_KEY,id);
        editor.commit();
    }

    public String getName(){
        return sharedPreferences.getString(NAME_KEY,"-1");
    }

    public void setEmail(String id){
        editor.putString(EMAIL_KEY,id);
        editor.commit();
    }

    public String getDownloadEvent(){
        return sharedPreferences.getString(DOWNLOAD_EVENT,"-1");
    }

    public void setDownloadEvent(String id){
        editor.putString(DOWNLOAD_EVENT,id);
        editor.commit();
    }

    public String getEmail(){
        return sharedPreferences.getString(EMAIL_KEY,"-1");
    }

    public void setLogin(boolean login){
        editor.putBoolean(LOGIN_KEY,login);
        editor.commit();
    }

    public boolean getLogin(){
        return sharedPreferences.getBoolean(LOGIN_KEY,false);
    }


}
