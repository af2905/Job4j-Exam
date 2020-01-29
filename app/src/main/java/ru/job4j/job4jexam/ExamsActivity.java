package ru.job4j.job4jexam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExamsActivity extends AppCompatActivity
        implements ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener {
    private RecyclerView recycler;
    List<Exam> exams = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams);
        this.recycler = findViewById(R.id.exams);
        this.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exams_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(ExamsActivity.this, "ADD", Toast.LENGTH_SHORT).show();
                exams.add(new Exam(
                        exams.size(), String.format("Exam %s", exams.size()),
                        System.currentTimeMillis(), exams.size()));
                this.recycler.setAdapter(new ExamAdapter(exams));
                return true;
            case R.id.delete_item:
                DialogFragment deleteDialog = new ConfirmDeleteDialogFragment();
                deleteDialog.show(getSupportFragmentManager(), "deleteDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        Toast.makeText(ExamsActivity.this, "DELETE", Toast.LENGTH_SHORT).show();
        exams.clear();
        this.recycler.setAdapter(new ExamAdapter(exams));
    }

    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {

    }

    public class ExamAdapter extends RecyclerView.Adapter<ExamHolder> {
        private final List<Exam> exams;

        public ExamAdapter(List<Exam> exams) {
            this.exams = exams;
        }

        @NonNull
        @Override
        public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.info_exam, parent, false);
            return new ExamHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExamHolder holder, int position) {
            final Exam exam = this.exams.get(position);
            TextView text = holder.view.findViewById(R.id.info);
            text.setText(exam.getName());

            TextView result = holder.view.findViewById(R.id.result);
            result.setText(String.valueOf(exam.getResult()));

            TextView date = holder.view.findViewById(R.id.date);
            date.setText(String.valueOf(exam.getTime()));

            text.setOnClickListener(v -> {
                Intent intent = new Intent(ExamsActivity.this, ExamActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        "You select " + exam, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return this.exams.size();
        }
    }

    public class ExamHolder extends RecyclerView.ViewHolder {
        private View view;

        public ExamHolder(@NonNull View view) {
            super(view);
            this.view = view;
        }
    }

    private void updateUI() {

        for (int index = 0; index != 100; index++) {
            exams.add(new Exam(index, String.format("Exam %s", index), System.currentTimeMillis(), index));
        }
        this.recycler.setAdapter(new ExamAdapter(exams));
    }
}
