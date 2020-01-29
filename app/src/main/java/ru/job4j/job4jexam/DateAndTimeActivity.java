package ru.job4j.job4jexam;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DateAndTimeActivity extends AppCompatActivity
        implements DatePickerFragment.DatePickerListener, TimePickerFragment.TimePickerListener {

    private int year, month, day, hour, minute;
    private TextView tvDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_and_time);
        tvDateTime = findViewById(R.id.tvDateTime);

        if (year != 0 && month != 0 && day != 0 && hour != 0 && minute != 0) {
            updateUI();
        }
    }

    @Override
    public void onDatePicked(Calendar date) {
        year = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH) + 1;
        day = date.get(Calendar.DAY_OF_MONTH);
        updateUI();
    }

    @Override
    public void onTimePicked(Calendar time) {
        hour = time.get(Calendar.HOUR_OF_DAY);
        minute = time.get(Calendar.MINUTE);
        updateUI();
    }

    public void onButtonClick(View view) {
        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), "timePicker");

        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    void updateUI() {
        String text = String.format("%s %s %s\n\n%s:%s",
                addZeroIfNecessary(day), addZeroIfNecessary(month), year,
                addZeroIfNecessary(hour), addZeroIfNecessary(minute));
        tvDateTime.setText(text);
    }

    @SuppressWarnings("checkstyle:RightCurly")
    public String addZeroIfNecessary(int value) {
        String right = "";
        if (value >= 10) {
            right = String.valueOf(value);
        } else {
            right = "0" + value;
        }
        return right;
    }

}
