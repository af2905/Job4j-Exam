package ru.job4j.exam.fragments;

import androidx.fragment.app.Fragment;

public class ExamUpdateActivity extends BaseActivity {
    @Override
    public Fragment loadFrg() {
        return ExamUpdateFragment.of(
                getIntent().getIntExtra("id", 0),
                getIntent().getStringExtra("name"));
    }
}
