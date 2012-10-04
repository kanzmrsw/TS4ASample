package com.kanzmrsw.example.ts4asample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

public class CallCloudFontTask extends AsyncTask<String, Void, String> {
    String rawFontBinary;
    TextView tv;
    File path;
    String text;

    public CallCloudFontTask(TextView tv, File path, String text) {
        super();
        this.tv = tv;
        this.path = path;
        this.text = text;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            ModHttpClient client = createConnectionClient(URI
                    .create("https://api.typesquare.com/api/service"));

            HttpPost post = new HttpPost(
                    "https://api.typesquare.com/api/service");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            nameValuePairs.add(new BasicNameValuePair("method",
                    "typesquare.dynamic.css"));
            nameValuePairs.add(new BasicNameValuePair("mail_address",
                    "ma8test@test.com"));
            nameValuePairs.add(new BasicNameValuePair("api_key", params[0]));
            nameValuePairs.add(new BasicNameValuePair("text", params[1]));
            nameValuePairs.add(new BasicNameValuePair("fontname",
                    "Maru Folk Bold"));
            nameValuePairs.add(new BasicNameValuePair("type", "TTF"));
            nameValuePairs.add(new BasicNameValuePair("binary", "1"));

            Log.v("TS4ASample", "api_key: " + params[0]);
            Log.v("TS4ASample", "text: " + params[1]);

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                InputStream is = null;
                BufferedReader br = null;
                String ss = "";

                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                br = new BufferedReader(new InputStreamReader(is));

                while ((ss = br.readLine()) != null) {
                    rawFontBinary = ss;
                }
                entity.consumeContent();
            }
            Log.v("TS4ASample", "statusCode: " + statusCode);
        } catch (Exception e) {
            Log.v("TS4ASample", "Request Error");
        }
        return rawFontBinary;
    }

    public void onPreExecute() {
    }

    public void onPostExecute(String rawFontBinary) {
        File tempfile = null;
        byte[] data = Base64.decode(rawFontBinary, Base64.DEFAULT);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        try {
            tempfile = File.createTempFile(sdf.format(new Date()), "", path);
            tempfile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempfile);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tempfilepath = tempfile.getPath();
        Typeface tfCloudFont = Typeface.createFromFile(tempfilepath);
        tv.setTypeface(tfCloudFont);
        tv.setText(text);
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
