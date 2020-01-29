package com.axway.client.pubsub;

import com.axway.api.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

/**
 * Extremely basic client for publishing events to PubSub.
 *
 * This is "enough" for this sample project, but we should probably have
 * something better created for projects that ship to production :).
 */
public class PubSubClient {

    /**
     * Static logging instance for all log messages emitted by this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    /**
     * Media type used for all JSON requests to the server.
     */
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * Singleton JSON mapper to use when translating JSON between containers.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * The remote PubSub hostname to connect to (domain only).
     */
    private final String host;

    /**
     * The API key configured on the PubSub instance to accept publishes.
     */
    private final String key;

    /**
     * The secret key associated with the API key of this client.
     */
    private final String secret;

    /**
     * An internal HTTP client to use for all requests.
     */
    private final OkHttpClient client;

    /**
     * Creates a new client instance for a provided host and authentication pair.
     *
     * @param host
     *      the remote hostname to connect to.
     * @param key
     *      the API key provided by the PubSub admins.
     * @param secret
     *      the secret key associated with the API key.
     */
    public PubSubClient(String host, String key, String secret) {
        this.host = Objects.requireNonNull(host);
        this.key = Objects.requireNonNull(key);
        this.secret = Objects.requireNonNull(secret);
        this.client = new OkHttpClient();
    }

    /**
     * Sends an event to the PubSub service.
     *
     * @param event
     *      the event to publish to PubSub.
     */
    public void send(Event event) throws IOException {
        byte[] content = MAPPER.writeValueAsBytes(event);
        RequestBody body = RequestBody.create(JSON, content);

        Request request = new Request.Builder()
                .url("https://" + this.host + "/api/event")
                .header("User-Agent", "Sample Java PubSub Client")
                .header("APIKey", this.key)
                .header("APISig", new String(sign(content)))
                .post(body)
                .build();

        Response response = this.client.newCall(request).execute();

        try (ResponseBody responseBody = response.body()) {
            LOGGER.info("Received response from PubSub: {}", responseBody.string());
        }

        if (response.code() != 200) {
            throw new IOException("Unable to publish event to PubSub");
        }
    }

    /**
     * Signs a content string using the client secret key.
     *
     * @param content
     *      the content to sign using the PubSub signing method.
     * @return
     *      a new base64 encoded {@link String} instance.
     */
    private byte[] sign(byte[] content) throws IOException {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(this.secret.getBytes(), "HmacSHA256");
            mac.init(secret_key);
            return Base64.getEncoder().encode(mac.doFinal(content));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IOException(e);
        }
    }
}
