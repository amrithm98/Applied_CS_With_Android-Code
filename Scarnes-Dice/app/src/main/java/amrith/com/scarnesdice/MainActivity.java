package amrith.com.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public int userTurnScore=0,userOverallScore=0,computerTurnScore=0,computerOverallScore=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private int rollDice() {

        Random random = new Random();
        int randomNumber = random.nextInt(6);
        return randomNumber;

    }

    private void enableButtons(boolean isEnabled) {
//        rollButton.setEnabled(isEnabled);
//        holdButton.setEnabled(isEnabled);
    }
}
