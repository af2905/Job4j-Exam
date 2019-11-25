package ru.job4j.job4jexam;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private static final String TAG = "ExamActivity";
    private int count = 0;
    private int position = 0;
    RadioGroup variants;
    List<String> selectedVariants = new ArrayList<>();

    private final List<Question> questions = Arrays.asList(
            new Question(1, "How many primitive variables does Java have?",
                    Arrays.asList(
                            new Option(1, "1.1"), new Option(2, "1.2"),
                            new Option(3, "1.3"), new Option(4, "1.4")
                    ), 4
            ),
            new Question(
                    2, "What is Java Virtual Machine?",
                    Arrays.asList(
                            new Option(1, "2.1"), new Option(2, "2.2"),
                            new Option(3, "2.3"), new Option(4, "2.4")
                    ), 4
            ),
            new Question(
                    3, "What is happen if we try unboxing null?",
                    Arrays.asList(
                            new Option(1, "3.1"), new Option(2, "3.2"),
                            new Option(3, "3.3"), new Option(4, "3.4")
                    ), 4
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        this.fillForm();

        final Button next = findViewById(R.id.next);
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
                position++;
                fillForm();
            }
        });
        final Button previous = findViewById(R.id.previous);
        previous.setEnabled(false);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                fillForm();
            }
        });

        variants = findViewById(R.id.variants);
        variants.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                previous.setEnabled(position != 0);
                next.setEnabled(position != questions.size() - 1);

                if (variants.getCheckedRadioButtonId() != -1) {
                    RadioButton button = findViewById(variants.getCheckedRadioButtonId());

                    String asText = button.getText().toString();
                    selectedVariants.add(asText);


                    Log.d(TAG, "selectedVariants " + selectedVariants);
                }
            }
        });

        if (!(savedInstanceState == null)) {
            count = savedInstanceState.getInt("count");
        }
        Log.d(TAG, "onCreate");
        Log.d(TAG, "count = " + count);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ++count;
        outState.putInt("count", count);
        Log.d(TAG, "OnSaveInstanceState");
    }

    private void fillForm() {
        Button previous = findViewById(R.id.previous);
        previous.setEnabled(position != 0);
        Button next = findViewById(R.id.next);
        next.setEnabled(position != questions.size() - 1);

        final TextView text = findViewById(R.id.question);
        Question question = this.questions.get(this.position);
        text.setText(question.getText());
        RadioGroup variants = findViewById(R.id.variants);

        for (int index = 0; index != variants.getChildCount(); index++) {
            final RadioButton button = (RadioButton) variants.getChildAt(index);

            Option option = question.getOptions().get(index);
            button.setId(option.getId());
            button.setText(option.getText());
        }
    }

    private void showAnswer() {
        RadioGroup variants = findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        Question question = this.questions.get(this.position);
        Toast.makeText(
                this, "Your answer is " + id + ", correct is " + question.getAnswer(),
                Toast.LENGTH_SHORT
        ).show();
    }
}