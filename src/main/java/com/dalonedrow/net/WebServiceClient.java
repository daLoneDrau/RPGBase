package com.dalonedrow.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.rpg.base.flyweights.ErrorMessage;
import com.dalonedrow.rpg.base.flyweights.RPGException;

/**
 * @author drau
 */
public abstract class WebServiceClient {
    /** the api properties. */
    private final Properties apiProperties;
    /**
     * Constructor for inheritance.
     * @param is the {@link InputStream} containing the properties file
     * @throws IOException if an error occurs
     */
    protected WebServiceClient(final InputStream is) throws IOException {
        apiProperties = new Properties();
        apiProperties.load(is);
    }
    /**
     * Gets the API properties.
     * @return {@link Properties}
     */
    protected Properties getApiProperties() {
        return apiProperties;
    }
    /**
     * Gets the response from the web service.
     * @param uri the web service URI
     * @return {@link String}
     * @throws RPGException if an error occurs
     */
    protected String getResponse(final String uri) throws RPGException {
        String s = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);
            BufferedReader rd =
                    new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));
            PooledStringBuilder sb =
                    StringBuilderPool.getInstance().getStringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            s = sb.toString();
            sb.returnToPool();
            sb = null;
        } catch (IOException | PooledException e) {
            throw new RPGException(ErrorMessage.INTERNAL_ERROR, e);
        }
        return s;
    }
}
