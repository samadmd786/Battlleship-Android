package edu.msu.leemyou1.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView winnerText = findViewById(R.id.winner);
        TextView loserText = findViewById(R.id.loser);
        Bundle extras = getIntent().getExtras();
        String winner = extras.getString("winner");
        String loser = extras.getString("loser");

        winnerText.setText(winner);
        loserText.setText(loser);
    }

    public void onMainMenu(View view){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}

