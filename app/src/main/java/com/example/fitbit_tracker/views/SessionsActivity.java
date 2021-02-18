package com.example.fitbit_tracker.views;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.fitbit_tracker.R;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SessionsActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        textView = findViewById(R.id.textView3);

        String data = readFileOnInternalStorage(this,"testing.txt");

        textView.setText(data);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

    public String readFileOnInternalStorage(Context mcoContext, String sFileName){
        File dir = new File(mcoContext.getFilesDir(), "mydir");
        String content = "";

        try {
            File gpxfile = new File(dir, sFileName);

            FileReader reader = new FileReader(gpxfile);

            int i;
            while((i=reader.read())!=-1)
                content+=(char)i;

            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return content;
    }

    public void onClearButtonClick(View view) {
        writeFileOnInternalStorage(this,"testing.txt","");
        String data = readFileOnInternalStorage(this,"testing.txt");
        textView.setText(data);
    }

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = new File(mcoContext.getFilesDir(), "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}