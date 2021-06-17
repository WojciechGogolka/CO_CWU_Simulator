package com.example.panelpieca;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.text.DecimalFormat;

public class FrontPanel extends AppCompatActivity implements View.OnClickListener, HolidayScreen.HolidayListener {


    double temp_co_act = 20.5;
    double temp_cwu_act = 20.5;

    public static Button menu, close;
    public static Button arrow1, arrow2;
    ViewFlipper front;

    int co_dzien, co_noc;

    boolean co_praca = false;

    int cwu ;

    boolean cwu_praca = false;

    int hour=0, min=0, sec=0;
    String h,m,s;
    int start_work_hour = 0;
    int start_work_min = 0;
    int end_work_hour = 0;
    int end_work_min = 0;

    //CZUJNIKI
    boolean piec_day=false;
     boolean temp_ukladu=false, temp_wody=false;

    TextView co_act, co_zad,co_display;
    TextView cwu_act, cwu_zad, cwu_display;
    TextView time;
    ImageView cohouse, cwuhouse;
    ImageView mode;
    Button wateruse;
    boolean water = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpanel);

        SharedPreferences sp = getSharedPreferences("PANEL_PARAM",MODE_PRIVATE);
        co_dzien = sp.getInt("CODAY",50);
        co_noc = sp.getInt("CONIGHT", 50);
        cwu = sp.getInt("CWU", 50);
        start_work_hour = sp.getInt("WORK_HOUR", 0);
        start_work_min = sp.getInt("WORK_MIN", 0);
        end_work_hour = sp.getInt("END_HOUR", 0);
        end_work_min = sp.getInt("END_MIN", 0);

        // FINDVIEWBYID
        cwuhouse = (ImageView)findViewById(R.id.cwu_house);
        cohouse = (ImageView)findViewById(R.id.co_house);
        front = (ViewFlipper)findViewById(R.id.MainFlipper);
        close = (Button)findViewById(R.id.close);
        menu = (Button)findViewById(R.id.option);
        co_act = (TextView) findViewById(R.id.temp_act);
        co_zad = (TextView)findViewById(R.id.temp_zad);
        co_display =  (TextView)findViewById(R.id.cowork);
        cwu_zad = (TextView)findViewById(R.id.cwu_zad);
        cwu_act = (TextView)findViewById(R.id.cwu_act);
        cwu_display = (TextView)findViewById(R.id.cwu_display);
        time = (TextView)findViewById(R.id.godzina);
        mode = (ImageView)findViewById(R.id.mode);
        arrow1 = (Button)findViewById(R.id.arrow1);
        arrow2 = (Button)findViewById(R.id.arrow2);
        wateruse = (Button)findViewById(R.id.wateruse);

        // ONCLICKLISTENERS
        menu.setOnClickListener(this);
        close.setOnClickListener(this);


        cwu_zad.setText(""+cwu+"°C");
        co_zad.setText(""+co_dzien+"°C");
        piec_work.start();
        front_thread.start();
    }

    public void previousView(View v) {
        front.setInAnimation(this, android.R.anim.slide_in_left);
        front.setOutAnimation(this, android.R.anim.slide_out_right);
        front.showPrevious();
    }

    public void nextView(View v) {
        front.setInAnimation(this, R.anim.slide_in_right);
        front.setOutAnimation(this, R.anim.slide_out_left);
        front.showNext();

        wateruse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(water == false){
                    wateruse.setBackgroundColor(Color.parseColor("#FFFF33"));
                    water = true;
                }else{
                    water = false;
                    wateruse.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.option:
                Intent intent = new Intent(FrontPanel.this, MainActivity.class);
                intent.putExtra("hour",hour);
                intent.putExtra("minute",min);
                intent.putExtra("sec",sec);
                intent.putExtra("codaytemp",co_dzien);
                intent.putExtra("conighttemp",co_noc);
                intent.putExtra("cwu",cwu);
                intent.putExtra("swh",start_work_hour);
                intent.putExtra("swm",start_work_min);
                intent.putExtra("ewh",end_work_hour);
                intent.putExtra("ewm",end_work_min);
                intent.putExtra("coss",co_praca);
                intent.putExtra("cwuss",cwu_praca);
                startActivityForResult(intent,1);
                break;
            case R.id.close:
                ExitDialog dialog = new ExitDialog();
                dialog.show(getSupportFragmentManager(), "EXIT DIALOG");
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(1, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == -1) {
                //CO
                co_dzien = data.getIntExtra("coday", 50);
                co_noc = data.getIntExtra("conight", 50);
                co_praca = data.getBooleanExtra("coss", false);
                //CWU
                cwu = data.getIntExtra("cwu", 50);
                cwu_praca = data.getBooleanExtra("cwuss", false);
                //WORK
                start_work_hour = data.getIntExtra("work_hour", 0);
                start_work_min = data.getIntExtra("work_min", 0);
                end_work_hour = data.getIntExtra("end_hour", 0);
                end_work_min = data.getIntExtra("end_min", 0);
                //TIME
                hour = data.getIntExtra("hour", 0);
                min = data.getIntExtra("minute", 0);
                sec = data.getIntExtra("sec", 0);

                SharedPreferences sp = getSharedPreferences("PANEL_PARAM",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("CODAY", co_dzien);
                editor.putInt("CONIGHT", co_noc);
                editor.putInt("CWU", cwu);
                editor.putInt("WORK_HOUR", start_work_hour);
                editor.putInt("WORK_MIN", start_work_min);
                editor.putInt("END_HOUR", end_work_hour);
                editor.putInt("END_MIN", end_work_min);
                editor.apply();

                if(start_work_hour < end_work_hour){
                    if((start_work_hour < hour || (start_work_hour == hour && start_work_min <= min)) && (end_work_hour > hour || (end_work_hour == hour && end_work_min > min))) {
                        piec_day = true;
                        mode.setImageResource(R.drawable.sun);
                        co_zad.setText(""+co_dzien+"°C");
                    }
                    else {
                        piec_day = false;
                        mode.setImageResource(R.drawable.moon);
                        co_zad.setText(""+co_noc+"°C");
                    }
                }
                else{
                    if((start_work_hour < hour || (start_work_hour == hour && start_work_min <= min)) || (end_work_hour > hour || (end_work_hour == hour && end_work_min > min))) {
                        piec_day = true;
                        mode.setImageResource(R.drawable.sun);
                        co_zad.setText(""+co_dzien+"°C");
                    }
                    else {
                        piec_day = false;
                        mode.setImageResource(R.drawable.moon);
                        co_zad.setText(""+co_noc+"°C");
                    }
                }
            }
            if(resultCode == 5){
                menu.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                arrow1.setVisibility(View.GONE);
                arrow2.setVisibility(View.GONE);
                co_dzien = 24;
                co_noc = 24;
                co_praca = true;
                cwu_praca = false;
                int days = data.getIntExtra("days",0);
                openHolidayFragment(days);
            }
        }

        cwu_zad.setText(""+cwu+"°C");
    }

    Thread front_thread = new Thread() {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(water == true && temp_cwu_act > 19.5) {
                                temp_cwu_act -= 0.2;
                                cwu_act.setText(""+format.format(temp_cwu_act));
                            }
                            else{}

                            sec++;
                            if (sec == 60) {
                                min++;
                                sec = 0;
                            }
                            if (min == 60) {
                                hour++;
                                min = 0;
                            }
                            if (hour == 24) {
                                hour = 0;
                            }
                            h = (hour < 10 ? "0" : "") + hour;
                            m = (min < 10 ? "0" : "") + min;
                            s = (sec < 10 ? "0" : "") + sec;
                            time.setText(h + ":" + m + ":" + s);

                            if((hour == start_work_hour && min == start_work_min) || (start_work_hour == end_work_hour && start_work_min == end_work_min)){
                                // Uruchomienie dziennej symulacji ogrzewania
                                piec_day = true;
                                co_zad.setText(""+co_dzien+"°C");
                                mode.setImageResource(R.drawable.sun);
                            }
                            else if(hour == end_work_hour && min == end_work_min ){
                                // Uruchomienie nocnej symulacji ogrzewania
                                piec_day = false;
                                co_zad.setText(""+co_noc+"°C");
                                mode.setImageResource(R.drawable.moon);
                            }

                        }
                    });
                }
            } catch (Exception e) {
                time.setText(R.string.app_name);
            }
        }
    };

    //WATEK PRACY CO I CWU
    DecimalFormat format = new DecimalFormat("##.0");

    Thread piec_work = new Thread(){
        @Override
        public void run(){
            try{
                while(!isInterrupted()){
                    Thread.sleep(10000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //PROCES STEROWANIA CO
                            if(piec_day == true  && co_praca == true){
                                if(temp_co_act < co_dzien && temp_ukladu == false){
                                    // temp_ukladu = false -- sygnał do sterownika w palniku;
                                    //SYMULACJA
                                    temp_co_act += 0.5;
                                    co_display.setText("CO MODE: DAY");
                                    co_act.setText(""+format.format(temp_co_act));
                                    if(temp_co_act >= co_dzien) temp_ukladu = true;
                                }
                                else if(temp_ukladu == true){
                                    // temp_ukladu = true -- co ogrzana, koniec pracy palnika, zaczyna sie powolny spadek temp;
                                    //SYMULACJA
                                    temp_co_act -= 0.1;
                                    co_display.setText("CO MODE: DAY SUPERVISION");
                                    co_act.setText(""+format.format(temp_co_act));
                                    if(temp_co_act <= co_dzien-3) temp_ukladu = false;
                                    }
                            }
                            if(piec_day == false && co_praca == true){
                                if(temp_co_act < co_noc && temp_ukladu == false){
                                    // temp_ukladu = true -- sygnał do sterownika w palniku;
                                    //SYMULACJA
                                    temp_co_act += 0.5;
                                    co_display.setText("CO MODE: NIGHT");
                                    co_act.setText(""+format.format(temp_co_act));
                                    if(temp_co_act >= co_noc) temp_ukladu = true;
                                }
                                else if(temp_ukladu == true){
                                    // temp_ukladu = false -- co ogrzana, koniec pracy palnika, zaczyna sie powolny spadek temp;
                                    //SYMULACJA
                                    temp_co_act -= 0.05;
                                    co_display.setText("CO MODE: NIGHT SUPERVISION");
                                    co_act.setText(""+format.format(temp_co_act));
                                    if(temp_co_act <= co_noc-3) temp_ukladu = false;
                                }
                            }
                            //PROCES STEROWANIA CWU
                            if(cwu_praca == true){
                                if(temp_cwu_act < cwu && temp_wody == false){
                                    temp_cwu_act += 0.5;
                                    cwu_act.setText(""+format.format(temp_cwu_act));
                                    cwu_display.setText("CWU MODE: HEATING");
                                    if(temp_cwu_act >= cwu) temp_wody = true;
                                }
                                else if(temp_wody == true){
                                    temp_cwu_act -= 0.05;
                                    cwu_act.setText(""+format.format(temp_cwu_act));
                                    cwu_display.setText("CWU MODE: SUPERVISION");
                                    if(temp_cwu_act < cwu - 5) temp_wody = false;
                                }
                            }
                            if((temp_co_act > co_dzien - 7 && piec_day == true) || (temp_co_act > co_noc - 7 && piec_day == false)) cohouse.setImageResource(R.drawable.hot);
                            else cohouse.setImageResource(R.drawable.cold);

                            if(temp_cwu_act > cwu - 7) cwuhouse.setImageResource(R.drawable.hot);
                            else cwuhouse.setImageResource(R.drawable.cold);

                        }
                    });
                }
            }catch (Exception d){
                co_act.setText("STOPPED");
            }
        }
    };

    public void openHolidayFragment(int days){
        HolidayScreen holiday = HolidayScreen.newInstance(days);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.holiday_screen, holiday,"HOLIDAY SCREEN").commit();
    }

    @Override
    public void onBackButtonPressed() {
        SharedPreferences sp = getSharedPreferences("PANEL_PARAM",MODE_PRIVATE);
        menu.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        arrow1.setVisibility(View.VISIBLE);
        arrow2.setVisibility(View.VISIBLE);
        co_dzien = sp.getInt("CODAY",50);
        co_noc = sp.getInt("CONIGHT",50);
        cwu_praca = true;
        if(piec_day == true) co_zad.setText(""+co_dzien+"°C");
        else co_zad.setText(""+co_noc+"°C");
        cwu_zad.setText(""+cwu+"°C");
    }
}