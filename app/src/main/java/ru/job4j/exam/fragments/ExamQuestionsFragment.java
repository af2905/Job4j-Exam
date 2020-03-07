package ru.job4j.exam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

import ru.job4j.exam.CalculateCorrectAnswer;
import ru.job4j.exam.CalendarFormat;
import ru.job4j.exam.R;
import ru.job4j.exam.model.Exam;
import ru.job4j.exam.model.Option;
import ru.job4j.exam.model.Question;
import ru.job4j.exam.store.SqlStore;

public class ExamQuestionsFragment extends Fragment
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private View view;
    private RadioGroup variants;
    private RadioButton radioButton;
    private Button previous, next;
    private static Exam exam;
    private static Question question;
    private int examId, rightAnswerCount;
    private static int position = 0;
    private static final String TAG = "log";
    static final String RESULT = "examResult";

    static int getRightAnswer() {
        question = exam.getQuestions().get(position);
        return question.getCorrect();
    }

    public static String getQuestion() {
        return question.getDesc();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exam_questions, container, false);
        next = view.findViewById(R.id.next);
        next.setOnClickListener(this);
        previous = view.findViewById(R.id.previous);
        previous.setEnabled(false);
        previous.setOnClickListener(this);
        Button hint = view.findViewById(R.id.hint);
        hint.setOnClickListener(this);
        examId = getArguments().getInt("id");
        exam = SqlStore.getInstance(getContext()).getExam(examId);
        fillForm(view);
        variants.setOnCheckedChangeListener(this);
        if (!(savedInstanceState == null)) {
            rightAnswerCount = savedInstanceState.getInt("rightAnswerCount");
        }
        return view;
    }

    private void fillForm(View view) {
        TextView text = view.findViewById(R.id.question);
        question = exam.getQuestions().get(position);
        text.setText(String.format("%s\n%s", question.getName(), question.getDesc()));
        variants = view.findViewById(R.id.variants);
        variants.clearCheck();
        for (int index = 0; index != variants.getChildCount(); index++) {
            radioButton = (RadioButton) variants.getChildAt(index);
            Option option = question.getOptions().get(index);
            radioButton.setId(option.getId());
            radioButton.setText(option.getText());
        }
        for (int i = 0; i < variants.getChildCount(); i++) {
            variants.getChildAt(i).setEnabled(true);
        }
    }

    static ExamQuestionsFragment of(int id) {
        ExamQuestionsFragment fragment = new ExamQuestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("rightAnswerCount", rightAnswerCount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                addSelectedVariants();
                position++;
                fillForm(view);
                break;
            case R.id.previous:
                position--;
                fillForm(view);
                break;
            case R.id.hint:
                DialogFragment dialog = new ConfirmHintDialogFragment();
                dialog.showNow(getActivity()
                        .getSupportFragmentManager(), ExamQuestionsActivity.HINT_FOR);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        previous.setEnabled(position != 0);
        for (int i = 0; i < group.getChildCount(); i++) {
            group.getChildAt(i).setEnabled(false);
        }
        transferIntentAfterClickToResultActivity();
    }

    private void addSelectedVariants() {
        int checkedId = variants.getCheckedRadioButtonId();
        if (checkedId == -1) {
            return;
        }
        radioButton = variants.findViewById(checkedId);
        int checkedIndex = variants.indexOfChild(radioButton);
        RadioButton rb = (RadioButton) variants.getChildAt(checkedIndex);
        int id = Integer.parseInt(rb.getTag().toString());
        question.setAnswer(id);
        SqlStore.getInstance(getContext()).updateQuestion(examId, exam.getQuestions().get(position));
        if (question.getAnswer() == question.getCorrect()) {
            ++rightAnswerCount;
        }
        Log.d(TAG, "id = " + id
                + " - question.getCorrect() = " + question.getCorrect()
                + " - rightAnswerCount = " + rightAnswerCount);
    }

    private void transferIntentAfterClickToResultActivity() {
        if (position == exam.getQuestions().size() - 1) {
            next.setOnClickListener(v -> {
                        addSelectedVariants();
                        int percent = CalculateCorrectAnswer.percent(position, rightAnswerCount);
                        String result = percent + "%";
                        SqlStore.getInstance(getContext()).addExam(
                                new Exam(exam.getName(),
                                        exam.getDesc(),
                                        result,
                                        CalendarFormat.dateFormatMethod(),
                                        new ArrayList<>()));
                        Intent intent = new Intent(
                                getActivity(), ResultActivity.class);
                        intent.putExtra(RESULT, examResultText());
                        startActivity(intent);
                    }
            );
        }
    }

    private String examResultText() {
        String text = String.format(Locale.getDefault(),
                "You correctly answered %d of %d exam questions. "
                        + "\nThe percentage of correct answers is %d.",
                rightAnswerCount, position + 1,
                CalculateCorrectAnswer.percent(position, rightAnswerCount));
        if (CalculateCorrectAnswer.percent(position, rightAnswerCount) < 70) {
            text += " \n\nExam failed.";
        } else {
            text += " \n\nCONGRATULATIONS!\nEXAM PASSED!";
        }
        return text;
    }
}

