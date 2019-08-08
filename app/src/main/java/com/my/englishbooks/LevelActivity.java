package com.my.englishbooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LevelActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        intent = new Intent(getApplicationContext(),ListBookActivity.class);
    }

    public void listBookGo(View view){
        intent.putExtra("level","elementary");
        startActivity(intent);
    }
    public void listBookGoPreInter(View view){
        intent.putExtra("level","preinter");
        startActivity(intent);
    }
    public void listBookGoInter(View view){
        intent.putExtra("level","inter");
        startActivity(intent);
    }
    public void listBookGoUpInter(View view){
        intent.putExtra("level","upinter");
        startActivity(intent);
    }
    public void listBookGoAdvance(View view){
        intent.putExtra("level","advance");
        startActivity(intent);
    }
}
