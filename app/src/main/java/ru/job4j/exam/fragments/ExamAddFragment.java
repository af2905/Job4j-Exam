package ru.job4j.exam.fragments;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.job4j.exam.R;
import ru.job4j.exam.store.ExamBaseHelper;
import ru.job4j.exam.store.ExamDbSchema;

public class ExamAddFragment extends Fragment implements View.OnClickListener {
    private SQLiteDatabase store;
    private Button save;
    private EditText edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_add_update, container, false);
        this.store = new ExamBaseHelper(this.getContext()).getWritableDatabase();
        edit = view.findViewById(R.id.name);
        save = view.findViewById(R.id.save);
        save.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save) {
            ContentValues value = new ContentValues();
            value.put(ExamDbSchema.ExamTable.Cols.NAME, edit.getText().toString());
            store.insert(ExamDbSchema.ExamTable.NAME, null, value);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content, new ExamListFragment())
                    .addToBackStack(null)
                    .commit();
        } else {
            throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}
