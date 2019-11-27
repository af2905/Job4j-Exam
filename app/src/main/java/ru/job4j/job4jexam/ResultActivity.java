package ru.job4j.job4jexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView result = findViewById(R.id.result);
        String testResult = getIntent().getStringExtra("testResult");
        result.setText(testResult);
    }
}
