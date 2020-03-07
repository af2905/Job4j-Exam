package ru.job4j.exam.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import ru.job4j.exam.store.ExamBaseHelper;
import ru.job4j.exam.store.ExamDbSchema;

public class ExamUpdateFragment extends Fragment implements View.OnClickListener {
    private SQLiteDatabase store;
    private Button save;
    private int id;
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_add_update, container, false);
        this.store = new ExamBaseHelper(this.getContext()).getWritableDatabase();
        id = getArguments().getInt("id");
        String name = getArguments().getString("name");
        title = view.findViewById(R.id.name);
        title.setText(name);
        save = view.findViewById(R.id.save);
        save.setOnClickListener(this);
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
            ContentValues value = new ContentValues();
            value.put(ExamDbSchema.ExamTable.Cols.NAME, title.getText().toString());
            store.update(ExamDbSchema.ExamTable.NAME, value, "id = ?",
                    new String[]{String.valueOf(id)});
            Intent intent = new Intent(getActivity(), ExamListActivity.class);
            startActivity(intent);
        } else {
            throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}