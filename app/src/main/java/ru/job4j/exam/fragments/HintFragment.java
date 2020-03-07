package ru.job4j.exam.fragments;

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

public class HintFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_hint, container, false);
        TextView text = view.findViewById(R.id.hint);
        int answer = getArguments().getInt(ExamQuestionsActivity.HINT_FOR, 0);
        String question = getArguments().getString(ExamQuestionsActivity.QUESTION);
        text.setText(String.format("%s \nThe correct answer is %d", question, answer));
        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> getActivity().onBackPressed());
        return view;
    }

    public static HintFragment of(int index, String question) {
        HintFragment hint = new HintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExamQuestionsActivity.HINT_FOR, index);
        bundle.putString(ExamQuestionsActivity.QUESTION, question);
        hint.setArguments(bundle);
        return hint;
    }
}
