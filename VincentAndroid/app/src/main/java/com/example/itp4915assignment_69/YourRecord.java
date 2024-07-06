package com.example.itp4915assignment_69;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;

import java.util.Objects;

public class YourRecord extends AppCompatActivity {
    private static final String TAG = "YourRecord";
    String [] listitems;
    ListView lvRanking;
    Spinner spRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_record);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Record");
        setSupportActionBar(toolbar);
        lvRanking = findViewById(R.id.lvRanking);
        spRecord =findViewById(R.id.spRecord);
        if(spRecord.getSelectedItem().toString().equals("Normal Mode")){
            setupDatabaseNormal();
        }else if(spRecord.getSelectedItem().toString().equals("Speed Time Mode")){
            setupDatabaseSpeed();
        }

    }

    //database for Normal Mode
    private void setupDatabaseNormal() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4915assignment_69/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
            cursor = db.rawQuery("SELECT * FROM GameLogNormal ORDER BY correctQuestion DESC", null);

            int rank = 1;
            StringBuilder msg = new StringBuilder();
            int i = 0;

            listitems = new String[cursor.getCount()];

            while (cursor.moveToNext()) {
                String playDate = cursor.getString(cursor.getColumnIndex("playDate"));
                String playTime = cursor.getString(cursor.getColumnIndex("playTime"));
                String correctQuestion = cursor.getString(cursor.getColumnIndex("correctQuestion"));
                String playingTime = cursor.getString(cursor.getColumnIndex("playingTime"));
                String GameMode = cursor.getString(cursor.getColumnIndex("GameMode"));
                msg.setLength(0); // Clear the StringBuilder
                msg.append("Rank: ").append(rank).append("\n")
                        .append("play Date: ").append(playDate).append("\n")
                        .append("Time: ").append(playTime).append("\n")
                        .append("Game Mode: ").append(GameMode).append("\n")
                        .append("Correct Question: ").append(correctQuestion).append("\n")
                        .append("Playing Time: ").append(playingTime).append(" Sec");
                listitems[i++] = msg.toString();
                rank++;
            }

            lvRanking.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listitems));

        } catch (SQLiteException e) {
            Log.e(TAG, "Database error: ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    //database for Speed Time Mode
    private void setupDatabaseSpeed() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4915assignment_69/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
            cursor = db.rawQuery("SELECT * FROM GameLogSpeed ORDER BY playingTime DESC", null);

            int rank = 1;
            StringBuilder msg = new StringBuilder();
            int i = 0;

            listitems = new String[cursor.getCount()];

            while (cursor.moveToNext()) {
                String playDate = cursor.getString(cursor.getColumnIndex("playDate"));
                String playTime = cursor.getString(cursor.getColumnIndex("playTime"));
                String correctQuestion = cursor.getString(cursor.getColumnIndex("correctQuestion"));
                String playingTime = cursor.getString(cursor.getColumnIndex("playingTime"));
                String GameMode = cursor.getString(cursor.getColumnIndex("GameMode"));
                msg.setLength(0); // Clear the StringBuilder
                msg.append("Rank: ").append(rank).append("\n")
                        .append("play Date: ").append(playDate).append("\n")
                        .append("Time: ").append(playTime).append("\n")
                        .append("Game Mode: ").append(GameMode).append("\n")
                        .append("Correct Question: ").append(correctQuestion).append("\n")
                        .append("Effective time: ").append(playingTime).append(" Sec");



                listitems[i++] = msg.toString();
                rank++;
            }

            lvRanking.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listitems));

        } catch (SQLiteException e) {
            Log.e(TAG, "Database error: ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
    public void goToMenu(View view) {finish();
    }


    public void SetDataBase(View view) {
        if(spRecord.getSelectedItem().toString().equals("Normal Mode")){
            setupDatabaseNormal();
        }else if(spRecord.getSelectedItem().toString().equals("Speed Time Mode")){
            setupDatabaseSpeed();
        }
    }
}