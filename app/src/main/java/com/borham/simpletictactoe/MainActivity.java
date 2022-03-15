package com.borham.simpletictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup=null;
    RadioButton com_radio=null,player_radio=null;
    Intent intentToGamingActivity=null;
    private static final String keyOfIntent="GAME_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Variables Initialization
        radioGroup=findViewById(R.id.tic_types);
        com_radio=findViewById(R.id.com);
        player_radio=findViewById(R.id.player);
        intentToGamingActivity=new Intent(this,GamingActivity.class);
    }

    public void startGame(View view) {
        if (radioGroup.getCheckedRadioButtonId()==R.id.com){
           intentToGamingActivity.putExtra(keyOfIntent,"comType");
           startActivity(intentToGamingActivity);
        }
        else {
            intentToGamingActivity.putExtra(keyOfIntent,"playerType");
            startActivity(intentToGamingActivity);
        }
    }
}

//toast test ==> Toast.makeText(this,"com is on",Toast.LENGTH_SHORT).show();