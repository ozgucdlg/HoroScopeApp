package com.example.horoscopeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Button btn;
    Spinner sp1;
    Spinner sp2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        sp1 = findViewById(R.id.zodiac_signs);
        sp2 = findViewById(R.id.days);

        //With this button we can go to the next page and see our horoscope on the day selected
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("Zodiac Sign", sp1.getSelectedItem().toString());
                intent.putExtra("Day", sp2.getSelectedItem().toString());
                startActivity(intent);
            }
        });


    }

    // this is final call you receive before you destroy your activity
    protected void onDestroy(){

        super.onDestroy();
        Log.d("Main activity", "onDestroy: 1");

    }

    protected void onPause(){
        super.onPause();
        Log.d("Main activity", "onPause: 1");

    }

    protected void onStart(){
        super.onStart();
        Log.d("Main activity", "onStart: 1");

    }

    protected void onStop(){

        super.onStop();
        Log.d("Main Activity", "onStop: 1");
    }

    protected void onRestart(){
        super.onRestart();

        Log.d("Main Activity", "onRestart: 1");
    }

    protected void onResume(){
        super.onResume();

        Log.d("Main Activity", "onResume:1 ");
    }
}