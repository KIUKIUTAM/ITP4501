package com.example.itp4915assignment_69;

import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameRanking extends AppCompatActivity {
    ListView lvRanking;
    DownloadTask task = null;
    String [] listitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_ranking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Game Ranking");
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lvRanking = findViewById(R.id.lvRanking);

        if(task == null || task.getStatus().equals(AsyncTask.Status.FINISHED)){
            task =new DownloadTask();
            task.execute("https://ranking-mobileasignment-wlicpnigvf.cn-hongkong.fcapp.run");
        }
    }



    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... values) {

            InputStream inputStream = null;
            String result = "";
            URL url = null;

            try {
                url = new URL(values[0]);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

// Make GET request
                con.setRequestMethod("GET");  // May omit this line since "GET" is the default.
                con.connect();


// Get response string from inputstream of the connection

                inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                    result += line;

                Log.d("doInBackground", "get data complete");
                inputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("onPostExecute",result);
            receiveData(result);

        }
    }
    public void receiveData(String result){
        Log.d("ReceiveData",result);
        try{
            JSONArray rankAaray = new JSONArray(result);
            listitems = new String[rankAaray.length()];
            for(int i = 0;i<rankAaray.length();i++){
                String msg = "";
                msg +="Rank " + (i+1)+ ",";
                String name = rankAaray.getJSONObject(i).getString("Name");
                msg += name + ",";
                String correcttime =  rankAaray.getJSONObject(i).getString("Correct");
                msg += "Correct Answer " + correcttime + ",";
                String  time = rankAaray.getJSONObject(i).getString("Time");
                msg += "Time: " +time + "," + "sec";

                listitems[i] = msg;
            }
            lvRanking.setAdapter(new ArrayAdapter<String>(GameRanking.this, android.R.layout.simple_list_item_1,listitems));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void goToMenu(View view){
        finish();
    }
}