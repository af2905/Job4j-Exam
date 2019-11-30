package ru.job4j.job4jexam;

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
        int question = getActivity().getIntent()
                .getExtras().getInt(ExamActivity.HINT_FOR, 0);
        String answer = getActivity().getIntent().getExtras().getString("question");
        text.setText(answer + " " + "\n" + this.answers.get(question));

        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> getActivity().onBackPressed());
        return view;
    }
    public static HintFragment of (int index){
        HintFragment hint = new HintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExamActivity.HINT_FOR, index);
        hint.setArguments(bundle);
        return hint;
    }
}
