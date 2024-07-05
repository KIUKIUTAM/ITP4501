package com.example.itp4915assignment_69;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
    String sql;
    TextView GG;
    int GameMode;
    ImageView myImageView;


    private static final String TAG = "DatabaseHelper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        GG = findViewById(R.id.GG);

        sharedPreferences = getSharedPreferences("GameMode",MODE_PRIVATE);
        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                GameMode = sharedPreferences.getInt("GameModeSetting", -1);
                GG.setText("GG "+GameMode);
            }
        };
        myImageView  = findViewById(R.id.myImageView);
        Glide.with(this).load("http://example.com/my_image.jpg").into(myImageView);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4915assignment_69/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

            String sql = "DROP TABLE IF EXISTS GamesLog;";
            db.execSQL(sql);

            sql = "CREATE TABLE GamesLog (playDate DATE, playTime TIME, correctQuestion INT,playingTime INT);";
            db.execSQL(sql);

            sql = "INSERT INTO GamesLog (playDate, playTime, correctQuestion,playingTime) VALUES (date('now'), time('now'),3, 9);";
            db.execSQL(sql);

            sql = "INSERT INTO GamesLog (playDate, playTime, correctQuestion,playingTime) VALUES (date('now'), time('now'),4, 2);";
            db.execSQL(sql);

        } catch (SQLiteException e) {
            GG.setText("GG");
            Log.e(TAG, "Database error: ", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        Timer();
    }
    public void closeGame(View view){
        finish();
    }
    public void goToGameRanking(View view){
        Intent intent = new Intent(this,GameRanking.class);
        startActivity(intent);
    }
    public void playGame(View view){
        Intent intent = new Intent(this,PlayGame.class);
        startActivity(intent);
    }
    public void goToYourRecord(View view){
        Intent intent = new Intent(this,YourRecord.class);

        startActivity(intent);
    }
    public void goToSetting(View view){
        Intent intent = new Intent(this,Setting.class);
        startActivity(intent);
        myImageView2.setRotationX(23);
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
                        if(toggle){
                            Thread.sleep(1);
                            myImageView.setRotationX(ro);
                            toggle=false;
                        }else {
                            myImageView.setScaleY(1f);
                            myImageView2.setRotationX(ro);

                            toggle=true;
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }




                }
            }
        };
        t.start();



    }
}