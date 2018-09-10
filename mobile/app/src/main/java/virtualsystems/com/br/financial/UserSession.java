package virtualsystems.com.br.financial;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static UserSession userSession;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor= null;

    public UserSession(Context context){
        sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static UserSession getInstance(Context context) {
        if (userSession != null){
            return userSession;
        }

        userSession = new UserSession(context);

        return userSession;
    }

    public void setUserToken(String token){
        editor.putString("token", token);
        editor.commit();
    }

    public String getUserToken(){
        return sharedPreferences.getString("token", "");
    }

    public void setUserName(String username){
        editor.putString("username", username);
        editor.commit();
    }

    public String getUserName(){
        return sharedPreferences.getString("username", "Android Studio");
    }

    public void setUserEmail(String email){
        editor.putString("email", email);
        editor.commit();
    }

    public String getUserEmail(){
        return sharedPreferences.getString("email", "android.studio@android.com");
    }

    public void setUserAvatar(String email){
        String userEmail = (String) email;
        String imageWidth = "192";
        String imgURL = Gravatar.gravatarUrl(userEmail, imageWidth);

        editor.putString("avatar", imgURL);
        editor.commit();
    }

    public String getUserAvatar(){
        return sharedPreferences.getString("avatar", "http://www.gravatar.com/avatar/00000000000000000000000000000000.jpeg?s=192&d=mp");
    }

    public Boolean isUserLoggedIn(){
        return (sharedPreferences.getString("token", "").length() > 0);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}
