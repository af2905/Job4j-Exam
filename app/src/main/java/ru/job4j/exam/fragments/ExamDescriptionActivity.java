package ru.job4j.exam.fragments;

import androidx.fragment.app.Fragment;

public class ExamDescriptionActivity extends BaseActivity {
    @Override
    public Fragment loadFrg() {
        return ExamDescriptionFragment.of(getIntent().getIntExtra("id", 0));
    }
}
