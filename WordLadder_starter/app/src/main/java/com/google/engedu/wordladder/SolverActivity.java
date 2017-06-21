package com.google.engedu.wordladder;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.engedu.worldladder.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SolverActivity extends AppCompatActivity {
    String [] words;
    EditText []editTexts;
    LinearLayout linearLayout;
    TextView startText,endText;
    private PathDictionary dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_solver);
        Intent intent=getIntent();
        Bundle data=intent.getExtras();
        words=data.getStringArray("words");

        linearLayout=(LinearLayout)findViewById(R.id.ll_editTexts);//Created by us
        startText=(TextView)findViewById(R.id.startTextView);
        endText=(TextView)findViewById(R.id.endTextView);
        editTexts=new EditText[words.length-2];//No need of first and last word

        startText.setText(words[0].toString()); //Set first word
        endText.setText(words[words.length-1].toString());//set last word
        //Loading dictionary
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new PathDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        //Initialize editTexts along with adding listeners for color
        for(int i=0;i<editTexts.length;i++)
        {
            editTexts[i]=new EditText(SolverActivity.this);
            //Coloring
            editTexts[i].setRawInputType(InputType.TYPE_CLASS_TEXT);
            editTexts[i].setImeOptions(EditorInfo.IME_ACTION_GO);
            //Adding to layout
            linearLayout.addView(editTexts[i]);
            //Again to coloring
            final EditText et=editTexts[i];
            final int index=i;
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        processWord(et,index);
                        handled = true;
                    }
                    return handled;
                }
            });

        }
    }

    //Handler for solve button
    public void onSolve(View view)
    {
        for(int i=1;i<words.length-1;i++)
        {
            editTexts[i-1].setText(words[i]);
            //Coloring the word
            editTexts[i-1].setTextColor(getResources().getColor(R.color.green));
        }
        Toast.makeText(this,"Solved",Toast.LENGTH_SHORT).show();
    }

    //Coloring the word

    private void processWord(EditText editText,int index) {

        String word = editText.getText().toString().trim().toLowerCase();
        if (word.length() == 0) {
            return;
        }

        int red = getResources().getColor(R.color.red);
        int green = getResources().getColor(R.color.green);

        if (dictionary.isWord(word)) {
            ArrayList<String> neighbours=dictionary.neighbours(word);
            Boolean valid=true;
            if(index>0)
            {
                String wordAbove=editTexts[index-1].getText().toString().toLowerCase();
                if(index<editTexts.length-1)    //Index from 1 to editTexts.lenth -1 Check both top and bottom words
                {
                    String wordBelow=editTexts[index+1].getText().toString().toLowerCase();
                    Log.d("Wordabove",wordAbove);
                    if(!wordBelow.isEmpty())    //Sometimes bottom word will be empty
                    {
                        Log.d("Wordbelow",wordBelow);
                        if(!neighbours.contains(wordBelow))
                            valid=false;
                    }
                }
                else //Index=editTexts.length - 1   Check only the last word
                {
                    if(!dictionary.neighbours(words[words.length-1]).contains(word))
                    {
                        valid=false;
                    }
                }
                if(!neighbours.contains(wordAbove))
                    valid=false;
            }
            else if(index==0)   //If index==0 check only the first word
            {
                if(!dictionary.neighbours(words[0]).contains(word))
                {
                    valid=false;
                }
            }

            //Check status of flag
            if(valid)
                editText.setTextColor(green);
            else
                editText.setTextColor(red);
        } else {
            editText.setTextColor(red);
        }
//        editText.setText("");
    }
}
