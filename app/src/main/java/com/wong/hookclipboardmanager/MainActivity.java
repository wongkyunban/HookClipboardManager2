package com.wong.hookclipboardmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private EditText output;
    private Button copy;
    private Button paste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText)findViewById(R.id.input);
        output = (EditText) findViewById(R.id.output);
        copy = (Button)findViewById(R.id.copy);
        paste = (Button)findViewById(R.id.paste);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(input.getText().toString());
            }
        });

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paste();
            }
        });

        HookHelper.hook(this);
    }

    private void copy(String data){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText(null,data);
        clipboardManager.setPrimaryClip(clipData);
    }

    private void paste(){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData = clipboardManager.getPrimaryClip();

        assert clipData != null;
        String text = (String)clipData.getItemAt(0).getText();

        output.setText(text);
    }

}
