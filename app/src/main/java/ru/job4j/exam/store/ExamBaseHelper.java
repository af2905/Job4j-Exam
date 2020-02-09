package ru.job4j.exam.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ExamBaseHelper extends SQLiteOpenHelper {
    public static final String DB = "exam_list.db";
    public static final int VERSION = 1;

    public ExamBaseHelper(@Nullable Context context) {
        super(context, DB, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ExamDbSchema.ExamTable.NAME + " ("
                + "id integer primary key autoincrement, "
                + ExamDbSchema.ExamTable.Cols.TITLE + " " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
