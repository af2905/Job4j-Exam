package ru.job4j.exam.fragments;

import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class ExamQuestionsActivity extends BaseActivity
        implements ConfirmHintDialogFragment.ConfirmHintDialogListener {
    public static final String HINT_FOR = "hint_for";
    public static final String QUESTION = "question";

    @Override
    public Fragment loadFrg() {
        return ExamQuestionsFragment.of(getIntent().getIntExtra("id", 0));
    }

    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        Intent intent = new Intent(ExamQuestionsActivity.this, HintActivity.class);
        intent.putExtra(HINT_FOR, ExamQuestionsFragment.getRightAnswer());
        intent.putExtra(QUESTION, ExamQuestionsFragment.getQuestion());
        startActivity(intent);
    }

    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {
        Toast.makeText(this, "Well done!", Toast.LENGTH_SHORT).show();
    }
}