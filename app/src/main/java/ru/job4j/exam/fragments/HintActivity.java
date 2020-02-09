package ru.job4j.exam.fragments;

import androidx.fragment.app.Fragment;

public class HintActivity extends BaseActivity {
    @Override
    public Fragment loadFrg() {
        return HintFragment.of(getIntent().getIntExtra(ExamQuestionsActivity.HINT_FOR, 0),
                getIntent().getStringExtra(ExamQuestionsActivity.QUESTION));
    }
}