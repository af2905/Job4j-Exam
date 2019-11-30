package ru.job4j.job4jexam;

import androidx.fragment.app.Fragment;

public class ResultActivity extends BaseActivity {
    @Override
    public Fragment loadFrg() {
        return new ResultFragment();
    }
}
