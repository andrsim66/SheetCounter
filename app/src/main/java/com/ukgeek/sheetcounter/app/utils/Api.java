package com.ukgeek.sheetcounter.app.utils;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by andrii on 04.10.15.
 */
public class Api {
    public static int send(String phrase, String text) throws IOException, NoSuchAlgorithmException,
            KeyManagementException, JSONException {
        URL url = new URL("https://speech-json.azure-mobile.net/tables/speech");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // Create the SSL connection
        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        sc.init(null, null, new java.security.SecureRandom());
        conn.setSSLSocketFactory(sc.getSocketFactory());

        conn.setRequestProperty("Content-Type", "application/json");

        // set Timeout and method
        conn.setReadTimeout(7000);
        conn.setConnectTimeout(7000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);

        conn.connect();

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("word", phrase);
        jsonParams.put("text", text);

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("message", "\"" + jsonParams + "\"");
        OutputStream os = new BufferedOutputStream(conn.getOutputStream());
        os.write(jsonMessage.toString().getBytes());
        os.flush();
        os.close();
        return conn.getResponseCode();
    }

    public static void sendToServer(String phrase, String text) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                try {
                    Logger.d(Api.send(params[0], params[1]) + "");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(phrase, text);
    }

    public static String receive() {
        HttpResponse response;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("https://speech-json.azure-mobile.net/tables/speech"));
            response = client.execute(request);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }

            Logger.d("s=" + s);
            return s.toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
