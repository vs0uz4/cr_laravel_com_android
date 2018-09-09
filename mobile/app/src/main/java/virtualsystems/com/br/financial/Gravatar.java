package virtualsystems.com.br.financial;

public class Gravatar {
    public static String gravatarUrl(String email, String size) {
        return "http://www.gravatar.com/avatar/" + MD5.md5(email) + ".jpeg?s=" + size + "&d=mp";
    }
}
