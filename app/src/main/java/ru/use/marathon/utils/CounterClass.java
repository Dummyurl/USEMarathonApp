package ru.use.marathon.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;

import java.util.concurrent.TimeUnit;

public class CounterClass extends CountDownTimer {

    private static String hms;
    private static CounterClass instance;

    private CounterClass(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public static CounterClass initInstance(long millisInFuture, long countDownInterval) {
        if (instance == null) {
            instance = new CounterClass(millisInFuture, countDownInterval);
        }
        return instance;
    }

    public static CounterClass getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("Parameters not initialized. Initiate with initInstance");
        } else {
            return instance;
        }
    }

    public static String getFormatedTime() {
        return hms;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTick(long l) {

        hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(l),
                TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
        );
    }

    @Override
    public void onFinish() {

    }
}