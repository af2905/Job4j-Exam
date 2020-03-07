package ru.job4j.exam.fragments;

import android.content.Intent;
import android.os.Bundle;
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

public class ExamDescriptionFragment extends Fragment implements View.OnClickListener {
    private Exam exam;
    private int examId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.description_of_exam, container, false);
        TextView desc = view.findViewById(R.id.desc_of_exam);
        examId = getArguments().getInt("id");
        exam = SqlStore.getInstance(getContext()).getExam(examId);
        desc.setText(exam.getDesc());
        Button start = view.findViewById(R.id.start_exam);
        start.setOnClickListener(this);
        return view;
    }

    static ExamDescriptionFragment of(int id) {
        ExamDescriptionFragment fragment = new ExamDescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ExamQuestionsActivity.class);
        intent.putExtra("id", exam.getId());
        startActivity(intent);
    }
}
