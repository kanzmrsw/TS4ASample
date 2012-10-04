package com.kanzmrsw.example.ts4asample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    Button btnGetApiKey;
    TextView tvResultApiKey;
    EditText etRequestText;
    Button btnRequestFont;
    TextView tvResultText;
    String apikey = "";
    String requestText = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetApiKey = (Button) findViewById(R.id.btnGetApiKey);
        tvResultApiKey = (TextView) findViewById(R.id.tvResultApiKey);
        etRequestText = (EditText) findViewById(R.id.etRequestText);
        btnRequestFont = (Button) findViewById(R.id.btnRequestFont);
        tvResultText = (TextView) findViewById(R.id.tvResultText);

        btnGetApiKey.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                GetApiKeyTask task = new GetApiKeyTask(tvResultApiKey);
                task.execute(apikey);
            }
        });

        // http request
        btnRequestFont.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                apikey = tvResultApiKey.getText().toString();
                requestText = etRequestText.getText().toString();

                CallCloudFontTask task = new CallCloudFontTask(
                        tvResultText, getCacheDir(), requestText);
                task.execute(apikey, requestText);
            }
        });
    }
}
