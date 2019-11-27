package ru.job4j.job4jexam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class HintActivity extends AppCompatActivity {
    private final Map<Integer, String> answers = new HashMap<>();

    public HintActivity() {
        answers.put(0, "Hint 1");
        answers.put(1, "Hint 2");
        answers.put(2, "Hint 3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        TextView text = findViewById(R.id.hint);
        int question = getIntent().getExtras().getInt(ExamActivity.HINT_FOR, 0);
        String answer = getIntent().getExtras().getString("question");
        text.setText(answer + " " + "\n" + this.answers.get(question));

        Button back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
    }
}
