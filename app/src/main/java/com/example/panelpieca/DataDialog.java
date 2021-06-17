package com.example.panelpieca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DataDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle mArgs = getArguments();
        int start_hour = mArgs.getInt("start_hour");
        String start_work_hour = (start_hour < 10 ? "0" : "") + String.format("%d",start_hour);
        int start_min = mArgs.getInt("start_min");
        String start_work_min = (start_min < 10 ? "0" : "") + String.format("%d",start_min);
        int end_hour = mArgs.getInt("end_hour");
        String end_work_hour = (end_hour < 10 ? "0" : "") + String.format("%d",end_hour);
        int end_min = mArgs.getInt("end_min");
        String end_work_min = (end_min < 10 ? "0" : "") + String.format("%d",end_min);

        builder.setTitle("Clock settings:")
                .setMessage("START DAY WORK TIME:  "+ start_work_hour + ":"+ start_work_min + "\n"+ "END DAY WORK TIME:  "+ end_work_hour + ":" + end_work_min)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();
    }


}