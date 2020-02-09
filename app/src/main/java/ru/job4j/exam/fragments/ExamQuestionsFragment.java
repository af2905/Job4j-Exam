package ru.job4j.exam.fragments;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Locale;

import ru.job4j.exam.R;
import ru.job4j.exam.model.CalculateCorrectAnswer;
import ru.job4j.exam.model.Option;
import ru.job4j.exam.model.Question;
import ru.job4j.exam.store.Store;

public class ExamQuestionsFragment extends Fragment
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private View view;
    private static int position = 0;
    private RadioGroup variants;
    private Button next;
    private Button previous;
    private Button examList;
    private int rightAnswerCount = 0;
    private Button hint;
    Store store = Store.getInstance();

    public static int getPosition() {
        return position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exam_questions, container, false);
        fillForm(view);
        next = view.findViewById(R.id.next);
        next.setOnClickListener(this);
        previous = view.findViewById(R.id.previous);
        previous.setEnabled(false);
        previous.setOnClickListener(this);
        variants.setOnCheckedChangeListener(this);
        hint = view.findViewById(R.id.hint);
        hint.setOnClickListener(this);
        examList = view.findViewById(R.id.exam_list);
        examList.setOnClickListener(this);

        if (!(savedInstanceState == null)) {
            position = savedInstanceState.getInt("position");
            rightAnswerCount = savedInstanceState.getInt("rightAnswerCount");
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
        outState.putInt("rightAnswerCount", rightAnswerCount);
    }

    private void fillForm(View view) {
        TextView text = view.findViewById(R.id.question);
        Question question = store.getQuestions().get(position);
        text.setText(question.getText());
        variants = view.findViewById(R.id.variants);
        for (int index = 0; index != variants.getChildCount(); index++) {
            final RadioButton button = (RadioButton) variants.getChildAt(index);
            Option option = question.getOptions().get(index);
            button.setId(option.getId());
            button.setText(option.getText());
        }
        variants.clearCheck();
        for (int i = 0; i < variants.getChildCount(); i++) {
            variants.getChildAt(i).setEnabled(true);
        }
    }

    private void addSelectedVariants() {
        if (variants.getCheckedRadioButtonId() == -1) {
            return;
        }
        RadioButton button = getActivity().findViewById(variants.getCheckedRadioButtonId());
        Question question = store.getQuestions().get(this.position);
        if (button.getId() == question.getAnswer()) {
            rightAnswerCount++;
        }
    }

    private void transferIntentAfterClickToResultActivity() {
        if (position == store.getQuestions().size() - 1) {
            next.setOnClickListener(v -> {
                Intent intent = new Intent(
                        getActivity(), ResultActivity.class);
                intent.putExtra("testResult", examResultText());
                startActivity(intent);
            });
        }
        addSelectedVariants();
    }

    private String examResultText() {
        String text = String.format(Locale.getDefault(),
                "You correctly answered %d of %d exam questions. "
                        + "\n\nThe percentage of correct answers is %d.",
                rightAnswerCount, position + 1,
                CalculateCorrectAnswer.percent(position, rightAnswerCount));

        if (CalculateCorrectAnswer.percent(position, rightAnswerCount) < 70) {
            text += " \n\nExam failed.";
        } else {
            text += " \n\nExam passed!";
        }
        return text;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                position++;
                fillForm(view);
                break;
            case R.id.previous:
                position--;
                fillForm(view);
                break;
            case R.id.exam_list:
                Intent intent = new Intent(getActivity(), ExamListActivity.class);
                startActivity(intent);
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
        for (int i = 0; i < variants.getChildCount(); i++) {
            variants.getChildAt(i).setEnabled(false);
        }
        transferIntentAfterClickToResultActivity();
    }
}

