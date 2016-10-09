package com.cs_a_scarne.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    TextView UserScore, ComputerScore;
    ImageView dice;
    Button Roll, Hold, Reset;
    private Random random = new Random();
    private Random otherRandom = new Random();
    private int currentUserScore, currentComputerScore, sureComputerScore;
    private final int MAX_SCORE = 100;
    private int diceIcons [] = {
            R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6
    };
    private Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserScore = (TextView) findViewById(R.id.uscore);
        ComputerScore = (TextView) findViewById(R.id.cscore);
        dice = (ImageView) findViewById(R.id.dice);
        Roll = (Button) findViewById(R.id.roll);
        Hold = (Button) findViewById(R.id.hold);
        Reset = (Button) findViewById(R.id.reset);
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
        Roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });
        Hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startComputerTurn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUserScore = 0;
        currentComputerScore = 0;
        sureComputerScore = 0;
        UserScore.setText("0");
        ComputerScore.setText("0");
        timerHandler.removeCallbacks(computerTurnRunnable);
    }

    Runnable computerTurnRunnable = new Runnable() {
        @Override
        public void run() {
            if (otherRandom.nextInt(10) < 7 || currentComputerScore == 0){
                boolean b = computerTurn();
                if (b){
                    timerHandler.postDelayed(this, 1000);
                } else {
                    endComputerTurn();
                }
            } else 
                endComputerTurn();
            
        }
    };

    private void endComputerTurn(){
        updateComputerScoreBoard();
        Hold.setEnabled(true);
        Roll.setEnabled(true);
    }

    private void startComputerTurn(){
        Hold.setEnabled(false);
        Roll.setEnabled(false);
        currentUserScore = 0;
        currentComputerScore = 0;
        sureComputerScore = Integer.parseInt(ComputerScore.getText().toString());
        timerHandler.postDelayed(computerTurnRunnable, 500);
    }

    private void updateComputerScoreBoard(){
        ComputerScore.setText("" + (sureComputerScore + currentComputerScore));
    }

    private boolean computerTurn(){
        int num;
        num = random.nextInt(6) + 1;
        dice.setImageResource(diceIcons[num-1]);
        if (num == 1){
            currentComputerScore = 0;
            return false;
        } else {
            currentComputerScore += num;
            updateComputerScoreBoard();
            if ((sureComputerScore + currentComputerScore) >= MAX_SCORE){
                endGame("Computer");
                return false;
            }
        }
        return true;
    }

    private void rollDice(){
        int num = random.nextInt(6) + 1;
        int currentScore = Integer.parseInt(UserScore.getText().toString());
        dice.setImageResource(diceIcons[num-1]);
        if (num == 1){
            currentScore -= currentUserScore;
            UserScore.setText(currentScore + "");
            startComputerTurn();
        } else {
            currentScore += num;
            if (currentScore >= MAX_SCORE){
                endGame("User");
                return;
            }
            currentUserScore += num;
            UserScore.setText(currentScore + "");
        }
    }

    private void endGame(String winner){
        (Toast.makeText(this, "Game over. " + winner + " won", Toast.LENGTH_LONG)).show();
        onStart();
    }

}
