package com.google.engedu.wordladder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        linearLayout=(LinearLayout)findViewById(R.id.ll_editTexts);
        words=data.getStringArray("words");
        editTexts=new EditText[words.length];
        for(int i=0;i<words.length;i++)
        {
            editTexts[i]=new EditText(SolverActivity.this);
            linearLayout.addView(editTexts[i]);
        }
    }
}
