package ru.job4j.job4jexam;

import android.content.Intent;
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
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private static final String TAG = "ExamActivity";
    public static final String HINT_FOR = "hint_for";
    private int count = 0;
    private int position = 0;
    private RadioGroup variants;
    private Button next;
    private Button previous;
    private List<String> selectedVariants = new ArrayList<>();
    private int rightAnswerCount = 0;
    Store store = Store.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        this.fillForm();

        next = findViewById(R.id.next);
        next.setOnClickListener(this::nextBtn);

        previous = findViewById(R.id.previous);
        previous.setEnabled(false);
        previous.setOnClickListener(this::previousBtn);

        variants = findViewById(R.id.variants);
        variants.setOnCheckedChangeListener((group, checkedId) -> {
                    previous.setEnabled(position != 0);
                    for (int i = 0; i < variants.getChildCount(); i++) {
                        variants.getChildAt(i).setEnabled(false);
                    }
                    transferIntentAfterClickToResultActivity();
                }
        );
        showHint();

        if (!(savedInstanceState == null)) {
            count = savedInstanceState.getInt("count");
            position = savedInstanceState.getInt("position");
            selectedVariants = savedInstanceState.getStringArrayList("selectedVariants");
            rightAnswerCount = savedInstanceState.getInt("rightAnswerCount");
        }
        Log.d(TAG, "onCreate");
        Log.d(TAG, "count = " + count);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ++count;
        outState.putInt("count", count);
        outState.putInt("position", position);
        outState.putStringArrayList("selectedVariants", (ArrayList<String>) selectedVariants);
        outState.putInt("rightAnswerCount", rightAnswerCount);
        Log.d(TAG, "OnSaveInstanceState");
    }

    private void fillForm() {
        final TextView text = findViewById(R.id.question);
        Question question = store.getQuestions().get(this.position);
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
        Question question = store.getQuestions().get(this.position);
        Toast.makeText(
                this, "Your answer is " + id + ", correct is " + question.getAnswer(),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void showHint() {
        Button hint = findViewById(R.id.hint);
        hint.setOnClickListener(v -> {
                    Intent intent = new Intent(ExamActivity.this, HintActivity.class);
                    intent.putExtra(HINT_FOR, position);
                    intent.putExtra("question", store.getQuestions().get(position).getText());
                    startActivity(intent);
                }
        );
    }

    private void addSelectedVariants() {
        if (variants.getCheckedRadioButtonId() == -1) {
            return;
        }
        RadioButton button = findViewById(variants.getCheckedRadioButtonId());
        String asText = button.getText().toString();
        selectedVariants.add(asText);
        Log.d(TAG, "selectedVariants " + selectedVariants);
        Question question = store.getQuestions().get(this.position);
        if (button.getId() == question.getAnswer()) {
            rightAnswerCount++;
        }
    }

    private void transferIntentAfterClickToResultActivity() {
        if (position == store.getQuestions().size() - 1) {
            next.setOnClickListener(v -> {
                Intent intent = new Intent(
                        ExamActivity.this, ResultActivity.class);
                intent.putExtra("testResult", testResult());
                startActivity(intent);
            });
        }
        addSelectedVariants();
    }

    private void nextBtn(View view) {
        for (int i = 0; i < variants.getChildCount(); i++) {
            variants.getChildAt(i).setEnabled(true);
        }
        showAnswer();
        position++;
        fillForm();
    }

    private void previousBtn(View view) {
        position--;
        fillForm();
    }

    private String testResult() {
        String text = "Вы выбрали " + selectedVariants.toString() + "\n" + "" +
                "Колличество правильных ответов: " + rightAnswerCount;
        if (rightAnswerCount < position + 1) {
            text += " \n\nВы проиграли";
        } else {
            text += " \n\nВы выиграли";
        }
        return text;
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
}