package com.sumeyra.javaworkmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.impl.WorkManagerImplExtKt;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //verimize key value değeri atıyor
        Data data = new Data.Builder().putInt("intkey",1).build();

        //uygulama kullanılırken söylenen ksııtlamalar
        Constraints constraints= new Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(true)
                .build();

/*
        //SADECE BİR KERE WORK REQUEST OLUŞTURDUK
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setConstraints(constraints)
                //.setInitialDelay(5, TimeUnit.MINUTES)
                .setInputData(data)
               // .addTag("MyTag")
                .build();
        WorkManager.getInstance(this).enqueue(workRequest)*/


        //BİRDEN ÇOK WORK REQUEST OLUŞTURMAK
            WorkRequest workRequest = new PeriodicWorkRequest.Builder(RefreshDatabase.class,15,TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .setInputData(data)
                    .build();
            WorkManager.getInstance(this).enqueue(workRequest);

            WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo) {
                    if (workInfo.getState()== WorkInfo.State.RUNNING){
                        System.out.println("running");
                    } else if (workInfo.getState()== WorkInfo.State.SUCCEEDED) {
                        System.out.println("succeced");
                    } else if (workInfo.getState()== WorkInfo.State.FAILED) {
                        System.out.println("failed");
                    }
                }

            });
        //iptal etmek için
       // WorkManager.getInstance(this).cancelAllWork();

        //chaning bunu sadece onetime olanlarda art arda ekleyerek yapabiliyoruz
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        //birden fazla onetimeworkrequest yapıyorsak eğer
        //WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
          //      .then(oneTimeWorkRequest)
            //    .then(oneTimeWorkRequest)
              //  .enqueue();
        //yukarıdaki gibi birbirine bağlıyoruz

    }
}