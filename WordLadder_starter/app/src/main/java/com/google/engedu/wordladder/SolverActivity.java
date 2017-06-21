package com.google.engedu.wordladder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.engedu.worldladder.R;

public class SolverActivity extends AppCompatActivity {
    String [] words;
    EditText []editTexts;
    LinearLayout linearLayout;
    TextView startText,endText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_solver);
        Intent intent=getIntent();
        Bundle data=intent.getExtras();
        words=data.getStringArray("words");

        linearLayout=(LinearLayout)findViewById(R.id.ll_editTexts);
        startText=(TextView)findViewById(R.id.startTextView);
        endText=(TextView)findViewById(R.id.endTextView);
        editTexts=new EditText[words.length-2];

        startText.setText(words[0].toString());
        endText.setText(words[words.length-1].toString());
        //1 to length-1 ... First and last words are already there
        for(int i=1;i<words.length-1;i++)
        {
            editTexts[i-1]=new EditText(SolverActivity.this);
            linearLayout.addView(editTexts[i-1]);
        }
    }

    public void onSolve(View view)
    {
        for(int i=1;i<words.length-1;i++)
        {
            editTexts[i-1].setText(words[i]);
        }
        Toast.makeText(this,"Solved",Toast.LENGTH_SHORT).show();
    }
}
