package ru.job4j.exam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.job4j.exam.R;
import ru.job4j.exam.model.Exam;
import ru.job4j.exam.store.SqlStore;

public class ExamUpdateFragment extends Fragment implements View.OnClickListener {
    private TextView title;
    private int examId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_add_update, container, false);
        String name = getArguments().getString("name");
        title = view.findViewById(R.id.name);
        title.setText(name);
        Button save = view.findViewById(R.id.save);
        save.setOnClickListener(this);
        examId = getArguments().getInt("id");
        Log.d("log", "examId = " + examId);
        return view;
    }

    static ExamUpdateFragment of(int id, String name) {
        ExamUpdateFragment fragment = new ExamUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save) {
            Exam exam = SqlStore.getInstance(getContext()).getOnlyOneExam(examId);
            exam.setName(title.getText().toString());
            SqlStore.getInstance(getContext()).updateExam(exam);
            Intent intent = new Intent(getActivity(), ExamListActivity.class);
            startActivity(intent);
        }
    }
}