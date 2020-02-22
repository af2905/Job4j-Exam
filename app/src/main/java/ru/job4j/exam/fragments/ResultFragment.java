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
        String testResult = getActivity().getIntent().getStringExtra("testResult");
        result.setText(testResult);
        return view;
    }

    static ResultFragment of(String index) {
        ResultFragment result = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("testResult", index);
        result.setArguments(bundle);
        return result;
    }
}

