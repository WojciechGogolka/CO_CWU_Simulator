package com.example.panelpieca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewFlipper settings_flipper;

    //MAIN SENDING VALUES
    Intent resultIntent = new Intent();
    private int start_hour, start_min, end_hour, end_min;

    //MENU VARIABLE
    private TextView co, cwu, holiday, clock, work, godzina;
    private int menu_stan = 3;
    private  Button menu;

    //CO
    private ProgressBar co_pb, co_pb2;
    private boolean co_ss = false;
    private TextView co_day_temp, co_night_temp, co_start_stop;
    private Button co_plus, co_minus;
    private Button co_plus2, co_minus2;

    //CWU VARIABLE
    private ProgressBar cwu_pb, cwu_pb2;
    private boolean cwu_ss = false;
    private TextView cwutemp, cwu_start_stop;
    private Button cwu_plus, cwu_minus;

    //CLOCK VARIABLES
    private String h, m, s;
    private TimePicker tp;
    private Button btnGet_time;
    private int minute = 0, hour = 0, sec = 0;

    //WORK HOURS VARIABLES
    private TimePicker tp_work;
    private Button work_start, work_end;
    private Button work_apply;

    //HOLIDAY VARIABLES
    private Button oneplus, tenplus, hundredplus;
    private Button oneminus, tenminus, hundredminus;
    private TextView holidaytime;
    private Button holiday_apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //DOWNLOADING ACTUAL TIME
            Intent intent = getIntent();
        hour = intent.getIntExtra("hour",0);
        minute = intent.getIntExtra("minute",0);
        sec = intent.getIntExtra("sec",0);

        settings_flipper = findViewById(R.id.Setting_flipper);
        godzina = (TextView) findViewById(R.id.textview_godzina);
        menu = (Button) findViewById(R.id.frontpanel);

        menu.setOnClickListener(this);

        // CO
        co_pb = (ProgressBar) findViewById(R.id.co_pb);
        co_pb2 = (ProgressBar) findViewById(R.id.co_pb2);
        co_day_temp = (TextView) findViewById(R.id.textview_tempsett);
        co_night_temp = (TextView) findViewById(R.id.textview_tempsett2);
        co_plus = (Button) findViewById(R.id.button_tempplus);
        co_minus = (Button) findViewById(R.id.button_tempminus);
        co_plus2 = (Button) findViewById(R.id.button_tempplus2);
        co_minus2 = (Button) findViewById(R.id.button_tempminus2);
        co_start_stop = (TextView) findViewById(R.id.button_costartstop);

        //Setting downloaded CO value
        co_day_temp.setText(""+intent.getIntExtra("codaytemp",50));
        co_night_temp.setText(""+intent.getIntExtra("conighttemp",50));
        co_ss = intent.getBooleanExtra("coss",false);
        coStart();

        co_plus.setOnClickListener(this);
        co_minus.setOnClickListener(this);
        co_plus2.setOnClickListener(this);
        co_minus2.setOnClickListener(this);
        co_start_stop.setOnClickListener(this);

        // CLOCK
        tp = (TimePicker) findViewById(R.id.zegar);
        tp.setIs24HourView(true);
        btnGet_time = (Button) findViewById(R.id.button_time);
        btnGet_time.setOnClickListener(this);

        thread.start();

        // CWU findViewByID
        cwu_pb = (ProgressBar) findViewById(R.id.cwu_pb);
        cwu_pb2 = (ProgressBar) findViewById(R.id.cwu_pb2);
        cwutemp = (TextView) findViewById(R.id.textview_wodasett);
        cwu_plus = (Button) findViewById(R.id.button_wodaplus);
        cwu_minus = (Button) findViewById(R.id.button_wodaminus);
        cwu_start_stop = (TextView) findViewById(R.id.button_cwustartstop);
        cwu_plus.setOnClickListener(this);
        cwu_minus.setOnClickListener(this);
        cwu_start_stop.setOnClickListener(this);

        //Setting downloaded CWU value
        cwutemp.setText(""+intent.getIntExtra("cwu",50));
        cwu_ss = intent.getBooleanExtra("cwuss",false);
        cwuStart();

        //MENU COLORS
        co = (TextView) findViewById(R.id.co);
        cwu = (TextView) findViewById(R.id.cwu);
        holiday = (TextView) findViewById(R.id.holiday);
        clock = (TextView) findViewById(R.id.clock);
        work = (TextView) findViewById(R.id.work);

        //WORK HOURS
        work_apply = (Button) findViewById(R.id.button_work_apply);
        tp_work = (TimePicker) findViewById(R.id.tp_work);
        tp_work.setIs24HourView(true);
        work_start = (Button) findViewById(R.id.work_start);
        work_end = (Button) findViewById(R.id.work_end);

        work_start.setOnClickListener(this);
        work_end.setOnClickListener(this);
        work_apply.setOnClickListener(this);

        //Setting downloaded WORK value
        start_hour = intent.getIntExtra("swh",0);
        start_min = intent.getIntExtra("swm",0);
        end_hour = intent.getIntExtra("ewh",0);
        end_min = intent.getIntExtra("ewm",0);
        work_start.setText("START AT\n"+((start_hour < 10 ? "0" : "") + String.format("%d",start_hour))+":"+((start_min < 10 ? "0" : "") + String.format("%d",start_min)));
        work_end.setText("END AT\n"+((end_hour < 10 ? "0" : "") + String.format("%d",end_hour))+":"+((end_min < 10 ? "0" : "") + String.format("%d",end_min)));

        //HOLIDAY
        holidaytime = (TextView) findViewById(R.id.textview_holiday);
        oneplus = (Button) findViewById(R.id.plusone);
        tenplus = (Button) findViewById(R.id.plusten);
        hundredplus = (Button) findViewById(R.id.plushundred);
        oneminus = (Button) findViewById(R.id.minusone);
        tenminus = (Button) findViewById(R.id.minusten);
        hundredminus = (Button) findViewById(R.id.minushundred);
        holiday_apply = (Button) findViewById(R.id.holiday_apply);

        oneplus.setOnClickListener(this);
        tenplus.setOnClickListener(this);
        hundredplus.setOnClickListener(this);
        oneminus.setOnClickListener(this);
        tenminus.setOnClickListener(this);
        hundredminus.setOnClickListener(this);
        holiday_apply.setOnClickListener(this);

    }

    //  ZMIANA WIDOKÓW

    public void previousView(View v) {
        settings_flipper.setInAnimation(this, android.R.anim.slide_in_left);
        settings_flipper.setOutAnimation(this, android.R.anim.slide_out_right);
        settings_flipper.showPrevious();
        menu_stan--;
    }

    public void nextView(View v) {
        menu_stan++;
        settings_flipper.setInAnimation(this, R.anim.slide_in_right);
        settings_flipper.setOutAnimation(this, R.anim.slide_out_left);
        settings_flipper.showNext();
    }


    // ON CLICK EVENTY

    String temp;
    int temp1;

    String holiday_set;
    int holiday_temp;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // FRONT PANEL BUTTON
            case R.id.frontpanel:
                resultIntent.putExtra("coday", Integer.parseInt(co_day_temp.getText().toString()));
                resultIntent.putExtra("conight", Integer.parseInt(co_night_temp.getText().toString()));
                resultIntent.putExtra("cwu", Integer.parseInt(cwutemp.getText().toString()));
                resultIntent.putExtra("hour", hour);
                resultIntent.putExtra("minute", minute);
                resultIntent.putExtra("sec", sec);
                resultIntent.putExtra("work_hour", start_hour);
                resultIntent.putExtra("work_min", start_min);
                resultIntent.putExtra("end_hour", end_hour);
                resultIntent.putExtra("end_min", end_min);
                setResult(RESULT_OK,resultIntent);
                finish();
                break;

            // CO BUTTONS
            case R.id.button_tempplus:
                co_day_temp.setText(temperatura(co_day_temp.getText().toString(), 1, true));
                break;
            case R.id.button_tempminus:
                co_day_temp.setText(temperatura(co_day_temp.getText().toString(), -1, true));
                break;
            case R.id.button_tempplus2:
                co_night_temp.setText(temperatura(co_night_temp.getText().toString(), 1, false));
                break;
            case R.id.button_tempminus2:
                co_night_temp.setText(temperatura(co_night_temp.getText().toString(), -1, false));
                break;

            case R.id.button_costartstop:
                coStart();
                break;

            // CALENDAR BUTTONS
            case R.id.button_time:
                hour = tp.getHour();
                minute = tp.getMinute();
                break;

            // CWU BUTTONS
            case R.id.button_wodaplus:
                cwutemp.setText(temperatura(cwutemp.getText().toString(), 1, true));
                break;
            case R.id.button_wodaminus:
                cwutemp.setText(temperatura(cwutemp.getText().toString(), -1, true));
                break;
            case R.id.button_cwustartstop:
                cwuStart();
                break;

            // WORK BUTTONS
            case R.id.work_start:
                start_hour = tp_work.getHour();
                start_min = tp_work.getMinute();
                work_start.setText("START AT\n"+((start_hour < 10 ? "0" : "") + String.format("%d",start_hour))+":"+((start_min < 10 ? "0" : "") + String.format("%d",start_min)));

                break;
            case R.id.work_end:
                end_hour = tp_work.getHour();
                end_min = tp_work.getMinute();
                work_end.setText("END AT\n"+((end_hour < 10 ? "0" : "") + String.format("%d",end_hour))+":"+((end_min < 10 ? "0" : "") + String.format("%d",end_min)));

                break;
            case R.id.button_work_apply:
                Bundle work = new Bundle();
                work.putInt("start_hour", start_hour);
                work.putInt("start_min", start_min);
                work.putInt("end_hour", end_hour);
                work.putInt("end_min", end_min);
                DataDialog data = new DataDialog();
                data.setArguments(work);
                data.show(getSupportFragmentManager(), "WORK HOURS dialog");
                break;

            // HOLIDAY BUTTONS
            case R.id.plusone:
                holidaytime.setText(HolidayTime(holidaytime.getText().toString(), 1));
                break;
            case R.id.plusten:
                holidaytime.setText(HolidayTime(holidaytime.getText().toString(), 10));
                break;
            case R.id.plushundred:
                holidaytime.setText(HolidayTime(holidaytime.getText().toString(), 100));
                break;
            case R.id.minusone:
                holidaytime.setText(HolidayTime(holidaytime.getText().toString(), -1));
                break;
            case R.id.minusten:
                holidaytime.setText(HolidayTime(holidaytime.getText().toString(), -10));
                break;
            case R.id.minushundred:
                holidaytime.setText(HolidayTime(holidaytime.getText().toString(), -100));
                break;
            case R.id.holiday_apply:
                if(Integer.parseInt(holidaytime.getText().toString()) != 0)
                {
                resultIntent.putExtra("days",Integer.parseInt(holidaytime.getText().toString()));
                setResult(5,resultIntent);
                finish();
                }
                else Toast.makeText(MainActivity.this, "Are you already back? Chose more days", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // MAIN RUNNABLE
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sec++;
                            if (sec == 60) {
                                minute++;
                                sec = 0;
                            }
                            if (minute == 60) {
                                hour++;
                                minute = 0;
                            }
                            if (hour == 24) {
                                hour = 0;
                            }
                            h = (hour < 10 ? "0" : "") + hour;
                            m = (minute < 10 ? "0" : "") + minute;
                            s = (sec < 10 ? "0" : "") + sec;

                            godzina.setText(h + ":" + m + ":" + s);

                            // MENU SWAPPING COLORS
                            if (menu_stan == 6) menu_stan = 1;
                            if (menu_stan == 0) menu_stan = 5;
                            if (menu_stan == 3) co.setBackgroundColor(Color.parseColor("#FFFF33"));
                            else co.setBackgroundColor(Color.parseColor("#A0A0A0"));

                            if (menu_stan == 2) cwu.setBackgroundColor(Color.parseColor("#FFFF33"));
                            else cwu.setBackgroundColor(Color.parseColor("#A0A0A0"));

                            if (menu_stan == 1)
                                holiday.setBackgroundColor(Color.parseColor("#FFFF33"));
                            else holiday.setBackgroundColor(Color.parseColor("#A0A0A0"));

                            if (menu_stan == 4)
                                work.setBackgroundColor(Color.parseColor("#FFFF33"));
                            else work.setBackgroundColor(Color.parseColor("#A0A0A0"));

                            if (menu_stan == 5)
                                clock.setBackgroundColor(Color.parseColor("#FFFF33"));
                            else clock.setBackgroundColor(Color.parseColor("#A0A0A0"));
                        }
                    });
                }
            } catch (Exception e) {
                godzina.setText(R.string.app_name);
            }

        }
    };

    // HOLIDAY TIME DISPLAY
    private String HolidayTime(String text, int wart) {
        holiday_set = text;
        holiday_temp = Integer.parseInt(holiday_set);
        if ((holiday_temp + wart) >= 0) holiday_temp = holiday_temp + wart;
        else
            Toast.makeText(MainActivity.this, "You can't chose value smaller than 0", Toast.LENGTH_SHORT).show();
        holiday_set = Integer.toString(holiday_temp);
        return holiday_set;
    }

    // USTAWIENIE TEMP CO NA WYSWIETLACZU
    private String temperatura(String text, int wart, boolean time) {
        temp = text;
        temp1 = Integer.parseInt(text);
        if (time == true) {
            if (temp1 >= 45 && wart > 0 && temp1 < 80) temp1 = temp1 + wart;
            if (temp1 > 45 && wart < 0) temp1 = temp1 + wart;
        } else {
            if (temp1 >= 30 && wart > 0 && temp1 < 80) temp1 = temp1 + wart;
            if (temp1 > 30 && wart < 0) temp1 = temp1 + wart;
        }
        temp = Integer.toString(temp1);
        return temp;
    }


    void coStart() {
        if (co_ss == false) {
            co_pb.setVisibility(View.GONE);
            co_pb2.setVisibility(View.VISIBLE);
            co_start_stop.setTextColor(Color.parseColor("#66ff66"));
            co_start_stop.setText("WORK");
            resultIntent.putExtra("coss", co_ss);
            co_ss = true;
        } else {
            co_pb.setVisibility(View.VISIBLE);
            co_pb2.setVisibility(View.GONE);
            co_start_stop.setTextColor(Color.parseColor("#ff0000"));
            co_start_stop.setText("STOP");
            resultIntent.putExtra("coss", co_ss);
            co_ss = false;
        }
    }

    void cwuStart(){
        if (cwu_ss == false) {
            cwu_pb.setVisibility(View.GONE);
            cwu_pb2.setVisibility(View.VISIBLE);
            cwu_start_stop.setTextColor(Color.parseColor("#66ff66"));
            cwu_start_stop.setText("WORK");
            resultIntent.putExtra("cwuss", cwu_ss);
            cwu_ss = true;
        } else {
            cwu_pb.setVisibility(View.VISIBLE);
            cwu_pb2.setVisibility(View.GONE);
            cwu_start_stop.setTextColor(Color.parseColor("#ff0000"));
            cwu_start_stop.setText("STOP");
            resultIntent.putExtra("cwuss", cwu_ss);
            cwu_ss = false;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, "Please press 'BACK TO MAIN PANEL' BUTTON", Toast.LENGTH_SHORT).show();
    }
}
