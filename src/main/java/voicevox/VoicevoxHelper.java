package voicevox;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class VoicevoxHelper {
    private static final OkHttpClient httpClient = new OkHttpClient();

    private static final int SPEAKER = 3;

    public static @NotNull String getQuery(String text) throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:50021/audio_query?speaker=" + SPEAKER + "&text=" + text)
                .addHeader("User-Agent", "OkHttp Bot")
                .post(new FormBody.Builder().build())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            return response.body().string();
        }
    }

    public static @NotNull byte[] getWav(String json) throws IOException{
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("http://localhost:50021/synthesis?speaker=" + SPEAKER)
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
