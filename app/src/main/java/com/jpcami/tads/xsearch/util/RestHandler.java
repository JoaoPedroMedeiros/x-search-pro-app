package com.jpcami.tads.xsearch.util;

import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestHandler {

    public JSONObject call(URL url, String method, JSONObject body) throws IOException, JSONException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.getOutputStream().write(body.toString().getBytes());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            if (sb.toString() != null) {
                return new JSONObject(sb.toString());
            }
            return null;
        }
    }
}
