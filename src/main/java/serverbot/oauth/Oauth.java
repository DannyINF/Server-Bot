package serverbot.oauth;

import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import serverbot.util.SECRETS;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Oauth {
    String clientId = "441962292297596928";

    String clientSecret = SECRETS.SECRET;

    String scope = "identify";

    String redirectUri = "https://localhost:8080/login";

    String discordLoginUrl = String.format("https://discord.com/api/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=%s", clientId, redirectUri, scope);

    String discordTokenUrl = "https://discordapp.com/api/oauth2/token";

    String discordApiUrl = "https://discordapp.com/api";

    public static String getAccessToken(String code) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("client_id", new Oauth().clientId);
        payload.put("client_secret", new Oauth().clientSecret);
        payload.put("grant_type", "authorization_code");
        payload.put("code", code);
        payload.put("redirect_uri", new Oauth().redirectUri);
        payload.put("scope", new Oauth().scope);

        URL url = new URL(new Oauth().discordTokenUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(payload));

        out.flush();
        out.close();

        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();

        //TODO: do this the correct way
        return content.toString().split(": \"")[1].split("\", ")[0];
    }

    public static String getUserId(String accessToken) throws IOException {
        URL url = new URL(new Oauth().discordApiUrl + "/users/@me");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Authorization", String.format("Bearer %s", accessToken));

        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();

        //TODO: do this the correct way
        return content.toString().split(": \"")[1].split("\", ")[0];
    }
}
