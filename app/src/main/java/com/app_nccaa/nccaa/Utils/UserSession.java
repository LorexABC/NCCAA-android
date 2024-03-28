package com.app_nccaa.nccaa.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSession {

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    public String BASEURL = "https://nccaatest1.globaltechkyllc.com/api/";
//    public String BASEURL = "https://www.nccaatest.org/api/";
//    public String BASEURL = "https://www.nccaatest.org/api/";

    public String ANDROID_APP_VERSION = "1.0";


    private static final String PREF_NAME = "UserSessionPref";


    private static final String IS_LOGIN = "IsLoggedIn";
    private final String USER_ID = "User_id";
    private final String DEVICE_TOKEN = "device_token";
    private final String APITOKEN = "api_token";

    private final String TOKEN_TYPE = "token_type";
    private final String EMAIL = "email";
    private final String USER_TYPE = "user_type";
    private final String GRADUATION_YEAR = "graduation_year";
    private final String PSI_FILLED = "psi_filled";
    private final String UNIVERSITY_ID = "university_id";

    private final String FIRST_NAME = "first_name";
    private final String MIDDLE_NAME = "middle_name";
    private final String LAST_NAME = "last_name";



    public UserSession(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }


    public void setIsLogin(boolean b) {
        editor.putBoolean(IS_LOGIN, b);
        editor.commit();
    }


    public boolean logout() {
        return sharedPreferences.edit().clear().commit();
    }



    public void setAPITOKEN(String apitoken) {
        editor.putString(APITOKEN, apitoken);
        editor.commit();
    }

    public String getAPITOKEN() {
        return sharedPreferences.getString(APITOKEN, "");
    }

    public void setPSI_FILLED(String psi_filled) {
        editor.putString(PSI_FILLED, psi_filled);
        editor.commit();
    }

    public String getPSI_FILLED() {
        return sharedPreferences.getString(PSI_FILLED, "false");
    }



    public void setTOKEN_TYPE(String token_type) {
        editor.putString(TOKEN_TYPE, token_type);
        editor.commit();
    }

    public String getTOKEN_TYPE() {
        return sharedPreferences.getString(TOKEN_TYPE, "");
    }




    public void setUSER_ID(String user_id) {
        editor.putString(USER_ID, user_id);
        editor.commit();
    }

    public String getUSER_ID() {
        return sharedPreferences.getString(USER_ID, "");
    }





    public String trimMessage(String json, String key){
        String trimmedString = null;
        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }


    public void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public void setFIRST_NAME(String first_name) {
        editor.putString(FIRST_NAME, first_name);
        editor.commit();
    }

    public String getFIRST_NAME() {
        return sharedPreferences.getString(FIRST_NAME, "");
    }

    public void setMIDDLE_NAME(String middle_name) {
        editor.putString(MIDDLE_NAME, middle_name);
        editor.commit();
    }

    public String getMIDDLE_NAME() {
        return sharedPreferences.getString(MIDDLE_NAME, "");
    }

    public void setLAST_NAME(String last_name) {
        editor.putString(LAST_NAME, last_name);
        editor.commit();
    }

    public String getLAST_NAME() {
        return sharedPreferences.getString(LAST_NAME, "");
    }


    public void setUSER_TYPE(String user_type) {

        editor.putString(USER_TYPE, user_type);
        editor.commit();
    }

    public String getUSER_TYPE() {
        return sharedPreferences.getString(USER_TYPE, "");
    }

    public void setGRADUATION_YEAR(String graduation_year) {

        editor.putString(GRADUATION_YEAR, graduation_year);
        editor.commit();
    }

    public String getGRADUATION_YEAR() {
        return sharedPreferences.getString(GRADUATION_YEAR, "");
    }

    public void setUNIVERSITY_ID(String university_id) {

        editor.putString(UNIVERSITY_ID, university_id);
        editor.commit();
    }

    public String getUNIVERSITY_ID() {
        return sharedPreferences.getString(UNIVERSITY_ID, "");
    }


}
