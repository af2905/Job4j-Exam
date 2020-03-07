package ru.job4j.exam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import ru.job4j.exam.store.SqlStore;

public class ExamListFragment extends Fragment {
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
        List<Exam> exams = SqlStore.getInstance(getContext()).getFinishedExams();
        recycler.setAdapter(new ExamAdapter(exams));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.exams, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_exam) {
            Intent intent = new Intent(getActivity(), ExamAddActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ExamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<Exam> exams;

        ExamAdapter(List<Exam> exams) {
            this.exams = exams;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.exam, parent, false)) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Exam exam = this.exams.get(position);
            TextView name = holder.itemView.findViewById(R.id.q_text);
            name.setText(exam.getName());
            TextView result = holder.itemView.findViewById(R.id.result);
            result.setText(exam.getResult());
            TextView date = holder.itemView.findViewById(R.id.date);
            date.setText(exam.getDate());
            holder.itemView.findViewById(R.id.edit).setOnClickListener(
                    btn -> {
                        Intent intent = new Intent(getActivity(), ExamUpdateActivity.class);
                        intent.putExtra("id", exam.getId());
                        intent.putExtra("name", exam.getName());
                        Log.d("log", "exam.getId() = " + exam.getId());
                        startActivity(intent);
                    });
            holder.itemView.findViewById(R.id.delete)
                    .setOnClickListener(
                            btn -> {
                                SqlStore.getInstance(getContext()).deleteExam(exam);
                                updateUI();
                            }
                    );
        }

        @Override
        public int getItemCount() {
            return this.exams.size();
        }
    }
}
