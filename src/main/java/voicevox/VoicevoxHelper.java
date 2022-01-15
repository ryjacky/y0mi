package voicevox;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.BotPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class VoicevoxHelper {
    private static final OkHttpClient httpClient = new OkHttpClient();

    private static String voicevoxServer = BotPreferences.getVoicevoxServer();

    public static @NotNull String getQuery(Long guildId, String text) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + voicevoxServer + ":50021/audio_query?speaker=" + BotPreferences.getVoice(guildId) + "&text=" + text)
                .addHeader("User-Agent", "OkHttp Bot")
                .post(new FormBody.Builder().build())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            return response.body().string();
        }
    }

    public static @NotNull byte[] getWav(Long guildId, String json) throws IOException{
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("http://" + voicevoxServer + ":50021/synthesis?speaker=" + BotPreferences.getVoice(guildId))
                .addHeader("User-Agent", "OkHttp Bot")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            return response.body().bytes();
        }
    }
}
