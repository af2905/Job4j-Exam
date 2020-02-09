package ru.job4j.exam.fragments;

import androidx.fragment.app.Fragment;

public class ExamAddActivity extends BaseActivity {
    @Override
    public Fragment loadFrg() {
        return new ExamAddFragment();
    }
}
