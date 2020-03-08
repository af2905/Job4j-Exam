package ru.job4j.exam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.job4j.exam.R;
import ru.job4j.exam.model.Exam;
import ru.job4j.exam.store.InitSql;

public class ChooseExamFragment extends Fragment {
    private RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_list, container, false);
        this.recycler = view.findViewById(R.id.list);
        this.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        List<Exam> exams = InitSql.getInstance(getContext()).loadingExams();
        recycler.setAdapter(new ChooseExamAdapter(exams));
    }

    public class ChooseExamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<Exam> exams;

        ChooseExamAdapter(List<Exam> exams) {
            this.exams = exams;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.name_of_exam, parent, false)) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Exam exam = this.exams.get(position);
            TextView nameOfExam = holder.itemView.findViewById(R.id.name_of_exam);
            nameOfExam.setText(exam.getName());
            nameOfExam.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ExamDescriptionActivity.class);
                intent.putExtra("id", exam.getId());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return this.exams.size();
        }
    }
}
