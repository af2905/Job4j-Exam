package ru.job4j.job4jexam;

import androidx.fragment.app.Fragment;

public class HintActivity extends BaseActivity {
    @Override
    public Fragment loadFrg() {
        return HintFragment.of(getIntent().getIntExtra(ExamActivity.HINT_FOR, 0));
    }
}