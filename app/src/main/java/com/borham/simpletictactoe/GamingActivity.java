package com.borham.simpletictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GamingActivity extends AppCompatActivity implements View.OnClickListener {
    TextView turnTxt = null;
    LinearLayout root=null;
    Button box00, box01, box02, box10, box11, box12, box20, box21, box22, score1Btn, score2Btn,comPlayBtn,randomBox = null;
    TextView score1Txt,score2Txt=null;
    String gameType,defaultPlayer = null;
    private static final String keyOfIntent = "GAME_TYPE";
    private boolean is_XorO;
    private int[][] arrayOfBoard = null;
    private int count=0;
    private SharedPreferences sharedPreferences=null;
    private SharedPreferences.Editor editor=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setFinishOnTouchOutside(false);
        //Variables Initialization
        gameType = getIntent().getStringExtra(keyOfIntent);
        root=findViewById(R.id.root);
        turnTxt = findViewById(R.id.turnTxt);
        score1Btn=findViewById(R.id.score1);
        score2Btn=findViewById(R.id.score2);
        comPlayBtn=findViewById(R.id.com);
        score1Txt=findViewById(R.id.score1Txt);
        score2Txt=findViewById(R.id.score2Txt);
        arrayOfBoard = new int[3][3];
        sharedPreferences=getSharedPreferences("MySavedGame",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        //////////////////////////////
        box00 = findViewById(R.id.box00);
        box01 = findViewById(R.id.box01);
        box02 = findViewById(R.id.box02);
        box10 = findViewById(R.id.box10);
        box11 = findViewById(R.id.box11);
        box12 = findViewById(R.id.box12);
        box20 = findViewById(R.id.box20);
        box21 = findViewById(R.id.box21);
        box22 = findViewById(R.id.box22);
        ///////////////////////////////
        box00.setOnClickListener(this);
        box01.setOnClickListener(this);
        box02.setOnClickListener(this);
        box10.setOnClickListener(this);
        box11.setOnClickListener(this);
        box12.setOnClickListener(this);
        box20.setOnClickListener(this);
        box21.setOnClickListener(this);
        box22.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (gameType.equals("playerType")){
            turnTxt.setText(R.string.turn_own_1);
            defaultPlayer=getString(R.string.turn_own_1);
            comPlayBtn.setVisibility(View.INVISIBLE);
            comPlayBtn.setEnabled(false);
        }
        else {
            defaultPlayer=getString(R.string.turn_own);
            score1Txt.setText(R.string.turn_own);
            score2Txt.setText(R.string.turn_com);
        }
    }


    @Override
    public void onClick(View v) {
        Button box = (Button) v;
        if (gameType.equals("playerType")) {
            checkWinner(box);
            if (is_XorO) {
                turnTxt.setText(R.string.turn_own_1);
                box.setText("O");
                box.setTextColor(Color.RED);
            } else {
                turnTxt.setText(R.string.turn_own_2);
                box.setText("X");
                box.setTextColor(Color.BLACK);
            }
            box.setEnabled(false);
            is_XorO = !is_XorO;
        }
        else {
            if (!is_XorO){
                box.setText("X");
                box.setTextColor(Color.BLACK);
                box.setEnabled(false);
                checkWinner(box);
                turnTxt.setText(R.string.turn_com);
                is_XorO = !is_XorO;
            }
            else {
                createToast(getString(R.string.toast_to_com));
            }

        }
    }

    private void checkWinner(Button button) {
        count++;
        String boxId = getResources().getResourceEntryName(button.getId());
        int x = Integer.parseInt(boxId.charAt(3) + "");
        int y = Integer.parseInt(boxId.charAt(4) + "");

        if (is_XorO) arrayOfBoard[x][y] = 2;
        else arrayOfBoard[x][y] = 1;

        if (
                        (arrayOfBoard[x][0] == arrayOfBoard[x][1] && arrayOfBoard[x][1] == arrayOfBoard[x][2]) ||
                        (arrayOfBoard[0][y] == arrayOfBoard[1][y] && arrayOfBoard[1][y] == arrayOfBoard[2][y]) ||
                        (x == y && arrayOfBoard[0][0] == arrayOfBoard[1][1] && arrayOfBoard[1][1] == arrayOfBoard[2][2]) ||
                        (x + y == 2 && arrayOfBoard[0][2] == arrayOfBoard[1][1] && arrayOfBoard[1][1] == arrayOfBoard[2][0])
        ) {
            createGameDialog(this, android.R.drawable.btn_star_big_on,turnTxt.getText().toString() + getString(R.string.winTxt));
            if (is_XorO){
                score2Btn.setText(""+(Integer.parseInt(score2Btn.getText().toString())+1));
            }
            else {
                score1Btn.setText(""+(Integer.parseInt(score1Btn.getText().toString())+1));
            }
        }
        else if(count>8){
            createGameDialog(this, android.R.drawable.alert_dark_frame,getString(R.string.drawTxt));
        }
    }

    public void saveGame(View view) {
        if (gameType.equals("playerType")){
            editor.putString("player1",score1Btn.getText().toString());
            editor.putString("player2",score2Btn.getText().toString());
        }
        else{
            editor.putString("player",score1Btn.getText().toString());
            editor.putString("computer",score2Btn.getText().toString());
        }
        editor.commit();
        createPeriodicAlertDialog();
    }

    public void clearScore(View view) {
        score1Btn.setText("0");
        score2Btn.setText("0");
    }

    public void reloadGame(View view) {
        String s1tmp;
        String s2tmp;
        if (gameType.equals("playerType")){
            s1tmp=sharedPreferences.getString("player1",score1Btn.getText().toString());
            s2tmp=sharedPreferences.getString("player2",score2Btn.getText().toString());
        }
        else {
            s1tmp=sharedPreferences.getString("player",score1Btn.getText().toString());
            s2tmp=sharedPreferences.getString("computer",score2Btn.getText().toString());
        }

        score1Btn.setText(s1tmp);
        score2Btn.setText(s2tmp);
    }

    private void createPeriodicAlertDialog() {
        AlertDialog test_ok;
        android.os.Handler messagHandler;
        Message msg = new Message();
        test_ok = new AlertDialog.Builder(this).setMessage(R.string.saved_msg).create();
        test_ok.show();
        AlertDialog finalTest_ok = test_ok;
        messagHandler = new android.os.Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 1) {
                    finalTest_ok.dismiss();
                }
            }
        };

        msg.what = 1;
        messagHandler.sendMessageDelayed(msg, 2000);
    }
    private void createGameDialog(Context context, int iconId, String title){
        new AlertDialog.Builder(context)
                .setIcon(iconId)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(R.string.confirm_msg)
                .setPositiveButton(R.string.yes_btn_dialog, (dialogInterface, i) -> {
                    arrayOfBoard=null;
                    arrayOfBoard=new int[3][3];
                    turnTxt.setText(defaultPlayer);
                    is_XorO=false;
                    count=0;
                    box00.setText("");
                    box00.setEnabled(true);
                    box01.setText("");
                    box01.setEnabled(true);
                    box02.setText("");
                    box02.setEnabled(true);
                    box10.setText("");
                    box10.setEnabled(true);
                    box11.setText("");
                    box11.setEnabled(true);
                    box12.setText("");
                    box12.setEnabled(true);
                    box20.setText("");
                    box20.setEnabled(true);
                    box21.setText("");
                    box21.setEnabled(true);
                    box22.setText("");
                    box22.setEnabled(true);
                })

                .setNegativeButton(R.string.no_btn_dialog, (dialogInterface, i) -> finish())
                .show();
    }

    public void playComputer(View view) {
        if (is_XorO){
            do {
                int xrand = (int) ((Math.random() * (3)) + 1);
                int yrand = (int) ((Math.random() * (3)) + 0);
                randomBox= (Button) ((LinearLayout)root.getChildAt(xrand)).getChildAt(yrand);
            }while (!randomBox.isEnabled());
            randomBox.setText("O");
            randomBox.setEnabled(false);
            randomBox.setTextColor(Color.RED);
            checkWinner(randomBox);
            turnTxt.setText(R.string.turn_own);
            is_XorO = !is_XorO;
        }
        else {
            createToast(getString(R.string.toast_to_player));
            }
    }

    private void createToast(String errorMsg){
        Toast toast = Toast.makeText(this,errorMsg, Toast.LENGTH_LONG);
        toast.getView().setBackgroundColor(Color.RED);
        TextView toastMessage =toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        toast.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
//toast test ==> Toast.makeText(this,"com is on",Toast.LENGTH_SHORT).show();