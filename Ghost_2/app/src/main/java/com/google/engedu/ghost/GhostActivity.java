/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {

    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    public String wordFragment="";
    Button challenge,reset;
    TextView ghostText,ghostStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        challenge=(Button)findViewById(R.id.btn_challenge);
        reset=(Button)findViewById(R.id.btn_reset);
        ghostText=(TextView)findViewById(R.id.ghostText);
        ghostStatus=(TextView)findViewById(R.id.gameStatus);
        AssetManager assetManager = getAssets();
        try{
            dictionary=new SimpleDictionary(getAssets().open("words.txt"));
        }catch (IOException e)
        {
            Log.d("ERROR",e.toString());
        }
        onStart(null);
    }

    public void challenge(View view){
        String string = ghostText.getText().toString();
        if (string.length()>=4&&dictionary.isWord(string)) {
            ghostText.setText("You Win");
            return;
        }
        if (string.length()>=4 && !dictionary.isWord(dictionary.getAnyWordStartingWith(string))) {
            ghostStatus.setText("you win");
            return;
        }
        String display=dictionary.getAnyWordStartingWith(string);
        ghostText.setText(display);
        ghostStatus.setText("You lose");
        return;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
//        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        String prefix,result;
        prefix=ghostText.getText().toString();
        Log.d("prefix",prefix);
        if(prefix==null)
        {
            prefix="";
        }
        if(prefix.length()>=1 && dictionary.isWord(prefix))
        {
            ghostStatus.setText("You Lose!!");
            return;
        }
//        Log.d("startingWord",dictionary.getAnyWordStartingWith(prefix));
        result=dictionary.getAnyWordStartingWith(prefix);
        if(result.isEmpty())
        {
            ghostStatus.setText("You Lose!!");
            return;
        }
        ghostText.append(String.valueOf(result.charAt(prefix.length())));
        userTurn = true;
        ghostStatus.setText(USER_TURN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text",ghostText.getText().toString());
        outState.putString("label",ghostStatus.getText().toString());
        outState.putBoolean("ut",userTurn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.get("text");
        savedInstanceState.get("label");
        savedInstanceState.get("ut");
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        if(keyCode<=KeyEvent.KEYCODE_Z &&keyCode>=KeyEvent.KEYCODE_A){
            Log.d("keypressed",String.valueOf(keyCode));
            char pressKey=(char)event.getUnicodeChar();
            ghostText.append(String.valueOf(pressKey));
            userTurn=false;
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }
}
