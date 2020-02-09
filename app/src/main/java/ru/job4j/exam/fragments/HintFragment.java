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

import java.util.HashMap;
import java.util.Map;

import ru.job4j.exam.R;

public class HintFragment extends Fragment {
    private final Map<Integer, String> answers = new HashMap<>();

    public HintFragment() {
        answers.put(0, "Hint 1");
        answers.put(1, "Hint 2");
        answers.put(2, "Hint 3");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_hint, container, false);
        TextView text = view.findViewById(R.id.hint);
        int answer = getArguments().getInt(ExamQuestionsActivity.HINT_FOR, 0);
        String question = getArguments().getString(ExamQuestionsActivity.QUESTION);
        text.setText(question + " " + "\nThe correct answer is " + answer);
        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> getActivity().onBackPressed());
        return view;
    }

    public static HintFragment of(int index, String text) {
        HintFragment hint = new HintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExamQuestionsActivity.HINT_FOR, index);
        bundle.putString(ExamQuestionsActivity.QUESTION, text);
        hint.setArguments(bundle);
        return hint;
    }
}
