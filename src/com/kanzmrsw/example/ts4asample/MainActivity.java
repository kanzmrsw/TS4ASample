package com.kanzmrsw.example.ts4asample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    EditText etEmailAddress;
    EditText etPassword;
    Button btnGetApiKey;
    TextView tvResultApiKey;
    EditText etRequestText;
    Button btnRequestFont;
    TextView tvResultText;
    Spinner spSelectFont;
    String email = "";
    String password = "";
    String apikey = "";
    String fontname = "";
    String requestText = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnGetApiKey = (Button) findViewById(R.id.btnGetApiKey);
        tvResultApiKey = (TextView) findViewById(R.id.tvResultApiKey);
        etRequestText = (EditText) findViewById(R.id.etRequestText);
        btnRequestFont = (Button) findViewById(R.id.btnRequestFont);
        tvResultText = (TextView) findViewById(R.id.tvResultText);
        spSelectFont = (Spinner) findViewById(R.id.spSelectFont);

        btnGetApiKey.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                email = etEmailAddress.getText().toString();
                password = etPassword.getText().toString();

                GetApiKeyTask task = new GetApiKeyTask(tvResultApiKey);
                task.execute(email, password);
            }
        });

        // http request
        btnRequestFont.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmailAddress.getText().toString();
                apikey = tvResultApiKey.getText().toString();
                requestText = etRequestText.getText().toString();
                fontname = spSelectFont.getSelectedItem().toString();

                CallCloudFontTask task = new CallCloudFontTask(tvResultText,
                        getCacheDir(), requestText);
                task.execute(email, apikey, requestText, fontname);
            }
        });
    }
}
