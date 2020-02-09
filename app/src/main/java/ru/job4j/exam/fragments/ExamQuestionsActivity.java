package ru.job4j.exam.fragments;

import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import ru.job4j.exam.store.Store;

public class ExamQuestionsActivity extends BaseActivity
        implements ConfirmHintDialogFragment.ConfirmHintDialogListener {
    public static final String HINT_FOR = "hint_for";
    public static final String QUESTION = "question";
    Store store = Store.getInstance();

    @Override
    public Fragment loadFrg() {
        return new ExamQuestionsFragment();
    }


    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        Intent intent = new Intent(ExamQuestionsActivity.this, HintActivity.class);
        intent.putExtra(HINT_FOR, store.getQuestions().get(ExamQuestionsFragment.getPosition()).getAnswer());
        intent.putExtra(QUESTION, store.getQuestions().get(ExamQuestionsFragment.getPosition()).getText());
        startActivity(intent);
    }

    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {
        Toast.makeText(this, "Well done!", Toast.LENGTH_SHORT).show();
    }


}