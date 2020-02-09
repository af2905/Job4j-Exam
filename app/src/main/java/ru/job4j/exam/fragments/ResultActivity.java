package ru.job4j.exam.fragments;

import androidx.fragment.app.Fragment;

public class ResultActivity extends BaseActivity {
    @Override
    public Fragment loadFrg() {
        return ResultFragment.of(getIntent().getStringExtra("testResult"));
    }
}
