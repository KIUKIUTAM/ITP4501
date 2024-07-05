package com.example.itp4915assignment_69;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Setting extends AppCompatActivity {
    int ModeSetting;
    RadioButton NormalMode;
    SharedPreferences sharedPreferences;
    RadioButton TimeUpMode;
    RadioButton UniversityMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Setting");
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("GameMode", MODE_PRIVATE);
        NormalMode=  findViewById(R.id.NormalMode);
        TimeUpMode=  findViewById(R.id.TimeUpMode);
    }

    public void goToMenu(View view) {
        finish();
    }

    public void setMode(View view) {
        if(NormalMode.isChecked()){
            ModeSetting =0;
            Toast.makeText(this, "Normal Mode Start", Toast.LENGTH_LONG).show();
        }else if(TimeUpMode.isChecked()){
            ModeSetting =1;
            Toast.makeText(this, "Speed Time Mode Start", Toast.LENGTH_LONG).show();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("GameModeSetting", ModeSetting);
        editor.apply();

    }
}