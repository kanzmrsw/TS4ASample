package com.kanzmrsw.example.ts4asample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class GetApiKeyTask extends AsyncTask<String, Void, String> {
    TextView tv;
    String apikey;

    public GetApiKeyTask(TextView tv) {
        super();
        this.tv = tv;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            ModHttpClient client = createConnectionClient(URI
                    .create("https://api.typesquare.com/api/auth"));

            HttpPost post = new HttpPost("https://api.typesquare.com/api/auth");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("mail_address",
                    "ma8test@test.com"));
            nameValuePairs.add(new BasicNameValuePair("password",
                    "ma8testmorisawa"));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                InputStream is = null;
                BufferedReader br = null;
                String ss = "";
                Pattern pat = null;
                Matcher mat;

                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                br = new BufferedReader(new InputStreamReader(is));

                pat = Pattern.compile("<api_key>(.+)?</api_key>");
                while ((ss = br.readLine()) != null) {
                    mat = pat.matcher(ss);
                    if (mat.find()) {
                        apikey = mat.group(1);
                        break;
                    }
                }
                entity.consumeContent();
            }
            Log.v("TS4ASample", "statusCode: " + statusCode);
        } catch (Exception e) {
            Log.v("TS4ASample", "Request Error");
        }
        return apikey;
    }

    public void onPreExecute() {
    }

    protected void onProgressUpdate(Integer... values) {
    }

    public void onPostExecute(String apikey) {
        tv.setText(apikey);
    }

    protected ModHttpClient createConnectionClient(URI uri) {
        ModHttpClient conn = null;
        try {
            if (uri.getScheme().equals("https")) {
                if (uri.getPort() == -1) {
                    conn = new ModHttpClient(443);
                } else {
                    conn = new ModHttpClient(uri.getPort());
                }
            } else {
                conn = new ModHttpClient();
            }
        } catch (KeyManagementException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyStoreException e) {
        } catch (UnrecoverableKeyException e) {
        }
        return conn;
    }
}
