package com.example.kseniya57.applicationcall;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextInputEditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (TextInputEditText)findViewById(R.id.text);
    }

    public void performAppCall(View v) {
        String text = input.getText().toString();
        if (text.startsWith("http")) {
            openBrowser(text);
        } else if (text.contains(",")) {
            openMap(text);
        } else {
            performPhoneCall(text);
        }
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
