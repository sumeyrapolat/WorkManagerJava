package com.sumeyra.javaworkmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.UserDictionary;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RefreshDatabase extends Worker {
    Context myContext;
    public RefreshDatabase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.myContext= context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Data data = getInputData();
        int myNumber= data.getInt("intkey",1);
        refreshDatabase(myNumber);

        return Result.success();
    }

    private void refreshDatabase(int myNumber){
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("com.sumeyra.javaworkmanager", Context.MODE_PRIVATE);
        int mySavedNumber = sharedPreferences.getInt("myNumber",0);
        mySavedNumber = mySavedNumber+ myNumber;
        System.out.println(mySavedNumber);
        sharedPreferences.edit().putInt("myNumber",mySavedNumber).apply();
    }
}
