package com.example.kseniya57.applicationcall;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    TextInputEditText input;
    int callType = 0;
    final static int BROWSER_ID = 0;
    final static int GEO_ID = 2;
    final static int PHONE_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        input = (TextInputEditText)findViewById(R.id.text);
        setRadioListener(R.id.browser, BROWSER_ID);
        setRadioListener(R.id.geo, GEO_ID);
        setRadioListener(R.id.phone, PHONE_ID);
    }

    public void performAppCall(View v) {
        String text = input.getText().toString();
        switch (callType) {
            case BROWSER_ID: openBrowser(text);
                break;
            case GEO_ID: openMap(text);
                break;
            case PHONE_ID: performPhoneCall(text);
                break;
        }
    }

    private void setRadioListener(int id, final int index) {
        RadioButton radioButton = (RadioButton)findViewById(id);
        if (index == callType) {
            radioButton.setChecked(true);
        }
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callType = index;
            }
        });
    }

    private void openBrowser(String text) {
        try {
            Uri address = Uri.parse(text);
            Intent intent = new Intent(Intent.ACTION_VIEW, address);
            startActivity(intent);
        } catch (Exception e) {
            input.setText("");
            showError("Введите корректный URL");
        }
    }

    private void openMap(String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:" + text));
        startActivity(intent);
    }

    private void performPhoneCall(String text) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + text));
        startActivity(intent);
    }

    private void showError(String text) {
        Toast toast = Toast.makeText(getApplicationContext(),
                text,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
