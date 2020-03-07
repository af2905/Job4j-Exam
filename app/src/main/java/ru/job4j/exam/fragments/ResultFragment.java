package ru.job4j.exam.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.job4j.exam.R;

public class ResultFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_result, container, false);
        TextView result = view.findViewById(R.id.result);
        String resultText = getArguments().getString(ExamQuestionsFragment.RESULT);
        result.setText(resultText);
        return view;
    }

    static ResultFragment of(String result) {
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ExamQuestionsFragment.RESULT, result);
        fragment.setArguments(bundle);
        return fragment;
    }
}

