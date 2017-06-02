package amrith.com.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static amrith.com.scarnesdice.R.id.imageView;

public class MainActivity extends AppCompatActivity {

    public int userTurnScore=0,userOverallScore=0,computerTurnScore=0,computerOverallScore=0;

    public int [] drawable={
            R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6
    };

    String userScoreLabel = "Your Score : ";
    String compScoreLabel = " Computer Score : ";
    String userTurnScoreLabel = "\nYour Turn Score : ";
    String compTurnScoreLabel = "\nComputer Turn Score : ";

    String labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore;

    Button reset,hold,roll;
    TextView status;
    ImageView diceImage;

    Timer computerTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reset=(Button)findViewById(R.id.btn_reset);
        hold=(Button)findViewById(R.id.btn_hold);
        roll=(Button)findViewById(R.id.btn_roll);

        status=(TextView)findViewById(R.id.statusView);

        diceImage=(ImageView)findViewById(imageView);
    }

    public void rollClicked(View view){

        Log.d("Click","Roll");
        int rolledNumber=rollDice();
        diceImage.setImageResource(drawable[rolledNumber]);

        rolledNumber++;

        if(rolledNumber==1){
            userTurnScore = 0;
            //No need to add 0 to overall score

            labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore + "\n you lost your chance";
            //Since there's a delay here... disable buttons..otherwise u can roll within the 3 seconds
            enableButtons(false);
            //Start computer turn after one second delay
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            },3000);
        }
        else
        {
            userTurnScore+=rolledNumber;
            labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore;
        }

        status.setText(labelText);

    }

    public void holdClicked(View view){

        Log.d("Click","Hold");

        userOverallScore += userTurnScore;  //Calculate Overall score only when user loses or presses hold button
        userTurnScore = 0;

        labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore;
        status.setText(labelText);
        if(userOverallScore>=100)
        {
            Toast.makeText(getApplicationContext(),"You Won!!",Toast.LENGTH_SHORT).show();
            status.setText("You Are the Winner!!");
            enableButtons(false);
        }
        else
        {
            computerTurn();
        }
    }

    public void resetClicked(View view){

        Log.d("Click","Reset");

        userTurnScore=0;
        userOverallScore=0;
        computerTurnScore=0;
        computerOverallScore=0;

        labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore;
        status.setText(labelText);
        enableButtons(true);

    }

    public void computerTurn(){

        computerTimer=new Timer();

        computerTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                //First change - Disable buttons
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enableButtons(false);
                    }
                });

                int computerRolledNumber=rollDice();

                final int finalRolled=computerRolledNumber;
                //Change Image
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        diceImage.setImageResource(drawable[finalRolled]);
                    }
                });

                computerRolledNumber++;

                if(computerRolledNumber==1)
                {
                    computerTurnScore=0;
                    labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore
                            + "\nComputer rolled a one and lost it's chance";

                    //Enable Buttons and Set Label Again
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            enableButtons(true);
                            status.setText(labelText);
                        }
                    });

                    //cancel the timer, this is exiting out of function
                    computerTimer.cancel();
                }

                else {

                    computerTurnScore += computerRolledNumber;

                    labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore
                            + "\nComputer rolled a " + computerRolledNumber
                            + compTurnScoreLabel + computerTurnScore;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            status.setText(labelText);
                        }
                    });

                    if(computerTurnScore>20)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                enableButtons(true);
                            }
                        });

                        //Calculate score till now
                        computerOverallScore += computerTurnScore;
                        computerTurnScore = 0;
                        labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + "\nComputer holds";

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                status.setText(labelText);
                            }
                        });
                        if(computerOverallScore>100)
                        {
                            Toast.makeText(getApplicationContext(),"Computer Wins!!",Toast.LENGTH_LONG).show();
                            status.setText("Computer : I won the Game !!!");
                            enableButtons(false);
                        }
                        computerTimer.cancel();
                    }
                }

            }//end of run
        },0,3000);//end of timer

    }

    private int rollDice() {

        Random random = new Random();
        int randomNumber = random.nextInt(6);
        return randomNumber;

    }

    private void enableButtons(boolean isEnabled) {
        roll.setEnabled(isEnabled);
        hold.setEnabled(isEnabled);
    }

    public void computerTurnOld() {

        Log.d("Call", "computerTurnOld called ");
        enableButtons(false);

        while (true) {

            int computerRolledNumber = rollDice();
            diceImage.setImageResource(drawable[computerRolledNumber]);
            computerRolledNumber++;

            Log.d("Turn", "computerTurn: " + computerRolledNumber);

            if (computerRolledNumber == 1) {
                computerTurnScore = 0;
                labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore
                        + "\nComputer rolled a one and lost it's chance";
                enableButtons(true);
                status.setText(labelText);
                return;
            }

            else {

                computerTurnScore += computerRolledNumber;
                labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore
                        + "\nComputer rolled a " + computerRolledNumber;
                status.setText(labelText);
            }
            //hold strategy for comp...if turnScore is > 20 then hold and save the turnScore and exit from this function, also enable the buttons
            if (computerTurnScore > 20) {
                computerOverallScore += computerTurnScore;
                computerTurnScore = 0;
                labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + "\n" +
                        "Computer holds";
                status.setText(labelText);
                enableButtons(true);
                return;
            }
        }
    }

}
