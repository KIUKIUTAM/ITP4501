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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;

public class YourRecord extends AppCompatActivity {
    private static final String TAG = "YourRecord";
    String [] listitems;
    ListView lvRanking;
    SQLiteDatabase db;
    Cursor cursor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_record);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Record");
        setSupportActionBar(toolbar);
        lvRanking = findViewById(R.id.lvRanking);

        setupDatabase();
    }
    

    private void setupDatabase() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4915assignment_69/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
            cursor = db.rawQuery("SELECT * FROM GamesLog ORDER BY correctQuestion DESC", null);

            int rank = 1;
            StringBuilder msg = new StringBuilder();
            int i = 0;

            // Initialize the listitems array with the size of the cursor count
            listitems = new String[cursor.getCount()];

            while (cursor.moveToNext()) {
                String playDate = cursor.getString(cursor.getColumnIndex("playDate"));
                String playTime = cursor.getString(cursor.getColumnIndex("playTime"));
                String correctQuestion = cursor.getString(cursor.getColumnIndex("correctQuestion"));
                String playingTime = cursor.getString(cursor.getColumnIndex("playingTime"));

                msg.setLength(0); // Clear the StringBuilder
                msg.append("Rank: ").append(rank).append(" ")
                        .append("playDate: ").append(playDate).append(" ")
                        .append("Time: ").append(playTime).append(" ")
                        .append("CorrectCount: ").append(correctQuestion).append(" ")
                        .append("PlayingTime: ").append(playingTime).append(" Sec");

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


}