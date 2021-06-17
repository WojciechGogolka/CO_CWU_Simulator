package com.example.panelpieca;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


public class HolidayScreen extends Fragment {

    private  HolidayListener listener;
    private String time;
    private Button close1;
    private TextView timer;
    private CountDownTimer CountDownTimer;
    long timeLeft;

    public interface HolidayListener {
        void onBackButtonPressed();
    }

    public HolidayScreen() {
        // Required empty public constructor
    }

    public static HolidayScreen newInstance(int day) {
        HolidayScreen fragment = new HolidayScreen();
        Bundle args = new Bundle();
        args.putInt("day",day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_holiday_screen, container, false);

        int DaysSet = getArguments().getInt("day");
        timer = (TextView)view.findViewById(R.id.countdown);
        close1 = (Button)view.findViewById(R.id.close1);
        timer = (TextView)view.findViewById(R.id.countdown);

        startTimer(DaysSet);
        updateCountDownText();
        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBackButtonPressed();
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    public void startTimer(int days){
        timeLeft = days * 86400000L;
        CountDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
            updateCountDownText();
            }

            @Override
            public void onFinish() {
                listener.onBackButtonPressed();
                getActivity().onBackPressed();
            }
        }.start();
    }

    private void updateCountDownText(){
        int days = (int) (timeLeft / 1000) / 86400;
        int hour = (int) (timeLeft / 1000) / 3600 % 24;
        int min  = (int) (timeLeft / 1000) / 60 %  60;
        int sec  = (int) (timeLeft / 1000) % 60;

        time = String.format(Locale.getDefault(),"%03d:%02d:%02d:%02d", days, hour, min, sec);
        timer.setText(time);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HolidayListener) {
            listener = (HolidayListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HolidayListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
