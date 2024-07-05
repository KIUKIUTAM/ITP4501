package com.example.itp4915assignment_69;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    long startTime = 0;
    boolean isHardMode =false;
    CheckBox cbModeSetting;
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    TextView GG;
    int GameMode;
    ImageView myImageView;
    LinearLayout titlePicture;
    TextView TitleText;


    private static final String TAG = "DatabaseHelper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        GG = findViewById(R.id.GG);
        TitleText = findViewById(R.id.TitleText);
        titlePicture = findViewById(R.id.titlePicture);

        sharedPreferences = getSharedPreferences("GameMode",MODE_PRIVATE);
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4915assignment_69/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

            String sql = "DROP TABLE IF EXISTS GameLogNormal;";
            db.execSQL(sql);
            sql = "DROP TABLE IF EXISTS GameLogSpeed;";
            db.execSQL(sql);
            sql = "CREATE TABLE GameLogNormal (playDate DATE, playTime TIME,GameMode TEXT,correctQuestion INT,playingTime INT);";
            db.execSQL(sql);
            sql = "CREATE TABLE GameLogSpeed (playDate DATE, playTime TIME,GameMode TEXT,correctQuestion INT,playingTime INT);";
            db.execSQL(sql);
            sql = "INSERT INTO GameLogNormal (playDate, playTime,GameMode, correctQuestion,playingTime) VALUES (date('now'), time('now'),'Normal Mode',3, 50);";
            db.execSQL(sql);
            sql = "INSERT INTO GameLogSpeed (playDate, playTime,GameMode, correctQuestion,playingTime) VALUES (date('now'), time('now'),'Speed Time Mode',8, 9);";
            db.execSQL(sql);
        } catch (SQLiteException e) {
            GG.setText("GG");
            Log.e(TAG, "Database error: ", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        //Timer();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();

        GameMode = sharedPreferences.getInt("GameModeSetting", 0);
        if(GameMode==0){
            titlePicture.setBackgroundResource(R.drawable.normal);
            TitleText.setTextColor(Color.parseColor("#000000"));
        }else if(GameMode==1){
            titlePicture.setBackgroundResource(R.drawable.fire);
            TitleText.setTextColor(Color.parseColor("#FCF8F3"));
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_LONG).show();
    }
    public void closeGame(View view){
        finish();
    }
    public void goToGameRanking(View view){
        Intent intent = new Intent(this,GameRanking.class);
        startActivity(intent);
    }
    public void playGame(View view){
        Intent intent;
        if(GameMode==0){
            intent = new Intent(this,PlayGame.class);
        }else if(GameMode==1){
            intent = new Intent(this,PlayGameForTimeUp.class);
        }else {
            intent = new Intent(this,PlayGame.class);
        }

        startActivity(intent);
    }
    public void goToYourRecord(View view){
        Intent intent = new Intent(this,YourRecord.class);

        startActivity(intent);
    }
    public void goToSetting(View view){
        Intent intent = new Intent(this,Setting.class);
        startActivity(intent);

    }
    private void Timer() {
        Thread t = new Thread() {
            @Override
            public void run() {
                boolean toggle=true;
                int ro=0;
                while (true){
                    ro+=10;
                    try {
                            Thread.sleep(100);
                            myImageView.setRotationX(ro);
                            toggle=false;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        t.start();



    }
}