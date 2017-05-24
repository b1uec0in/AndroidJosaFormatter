package com.github.b1uec0in.androidjosaformatter;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.github.b1uec0in.josaformatter.KoreanUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        EditText nickNameEditText = (EditText)findViewById(R.id.nickNameEditText);

        String nickName = nickNameEditText.getText().toString();

        String message;
        if (nickName.length() > 0) {
            message = KoreanUtils.format(getString(R.string.nick_name_confirm_format), nickName);
        } else {
            message = getString(R.string.nick_name_hint);
        }
        new AlertDialog.Builder(this).setMessage(message).setPositiveButton(android.R.string.ok, null).show();
    }
}
