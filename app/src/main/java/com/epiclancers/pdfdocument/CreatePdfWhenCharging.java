package com.epiclancers.pdfdocument;

import android.app.Activity;
import android.support.annotation.NonNull;

import androidx.work.Constraints;
import androidx.work.Worker;

public class CreatePdfWhenCharging extends Worker {


    @NonNull
    @Override
    public Result doWork() {


        System.out.println("Starting the Work" + " PDF WORK");

        CreatePdf createPdf = new CreatePdf();
        Boolean res = createPdf.generete(getApplicationContext() ,"abhishek");

        if(res){
            System.out.println("Work DOne PDF Genereted" + " PDF WORK");
            return Result.SUCCESS;
        }else {
            System.out.println("Work DOne PDF Genereted" + " PDF WORK");
            return Result.RETRY;
        }




    }
}
