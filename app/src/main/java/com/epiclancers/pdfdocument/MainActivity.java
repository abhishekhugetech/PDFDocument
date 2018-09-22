package com.epiclancers.pdfdocument;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreatePdf createPdf = new CreatePdf();
        createPdf.generete(this,this,"abhishek");
        finish();


    }
}
