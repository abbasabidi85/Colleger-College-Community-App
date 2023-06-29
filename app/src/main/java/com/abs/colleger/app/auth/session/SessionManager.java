package com.abs.colleger.app.auth.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "CollegerLoginSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static final String ACCOUNT_ROLE_ADMIN = "isAccountAdmin";

    private static final String ACCOUNT_ROLE_USER = "isAccountUser";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public void setAccountRoleAdmin(boolean isAccountAdmin) {
        editor.putBoolean(ACCOUNT_ROLE_ADMIN, isAccountAdmin);
        editor.apply();
    }
    public boolean isAccountAdmin() {
        return pref.getBoolean(ACCOUNT_ROLE_ADMIN, false);
    }

    public void setAccountRoleUser(boolean isAccountUser) {
        editor.putBoolean(ACCOUNT_ROLE_USER, isAccountUser);
        editor.apply();
    }
    public boolean isAccountUser() {
        return pref.getBoolean(ACCOUNT_ROLE_USER, false);
    }
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
