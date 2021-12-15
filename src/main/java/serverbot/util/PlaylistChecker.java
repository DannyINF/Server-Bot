package serverbot.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class PlaylistChecker {

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Server-Bot";

    /**
     * Directory to store data.user credentials for this application.
     */

    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("data.user.home"), "/credentials/screambot");
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final Collection<String> SCOPES = Collections.singletonList(YouTubeScopes.YOUTUBE_READONLY);
    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY = null;
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT = null;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     */
    private static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream("Properties/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger data.user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("serverbot/user");
    }

    /**
     * Build and return an authorized API client service, such as a YouTube
     * Data API client service.
     *
     * @return an authorized API client service
     */
    private static YouTube getYouTubeService() throws IOException {
        Credential credential = authorize();
        return new YouTube.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static ArrayList check(String pl_link) throws IOException {

        YouTube youtube = getYouTubeService();
        String lenght = null;

        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet,contentDetails");
            parameters.put("playlistId", pl_link);

            YouTube.PlaylistItems.List playlistItemsListByPlaylistIdRequest = youtube.playlistItems().list(parameters.get("part"))
                    .setMaxResults(50L);

            if (parameters.containsKey("playlistId") && !Objects.equals(parameters.get("playlistId"), "")) {
                playlistItemsListByPlaylistIdRequest.setPlaylistId(parameters.get("playlistId"));
            }

            lenght = String.valueOf(playlistItemsListByPlaylistIdRequest.execute().getPageInfo().getTotalResults());

            playlistItemsListByPlaylistIdRequest.execute();


        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        ArrayList<String> information = new ArrayList<>();
        information.add(0, lenght);
        return information;
    }
}