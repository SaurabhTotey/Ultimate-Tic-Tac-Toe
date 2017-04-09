package com.saurabhtotey.ultimatetictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Started 10/12/16
 * Saurabh Totey
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void initGame(View view) throws InterruptedException{
        int ID = view.getId();
        Intent intent = new Intent(this, GameActivity.class);
        switch(ID){
            case R.id.localNormal:
                intent.putExtra("isUltimate", false);
                intent.putExtra("opponentType", 0);
                break;
            case R.id.onlineNormal:
                intent.putExtra("isUltimate", false);
                intent.putExtra("opponentType", 1);
                break;
            case R.id.computerNormal:
                intent.putExtra("isUltimate", false);
                intent.putExtra("opponentType", 2);
                break;
            case R.id.localUltimate:
                intent.putExtra("isUltimate", true);
                intent.putExtra("opponentType", 0);
                break;
            case R.id.onlineUltimate:
                intent.putExtra("isUltimate", true);
                intent.putExtra("opponentType", 1);
                break;
            case R.id.computerUltimate:
                intent.putExtra("isUltimate", true);
                intent.putExtra("opponentType", 2);
                break;
        }
        startActivity(intent);
    }

}
