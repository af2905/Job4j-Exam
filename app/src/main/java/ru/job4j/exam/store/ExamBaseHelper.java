package ru.job4j.exam.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ExamBaseHelper extends SQLiteOpenHelper {
    private static final String DB = "exam_list.db";
    private static final int VERSION = 1;

    public ExamBaseHelper(@Nullable Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ExamDbSchema.ExamTable.NAME + " ("
                + ExamDbSchema.ExamTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExamDbSchema.ExamTable.Cols.NAME + " TEXT, "
                + ExamDbSchema.ExamTable.Cols.DESC + " TEXT, "
                + ExamDbSchema.ExamTable.Cols.RESULT + " TEXT, "
                + ExamDbSchema.ExamTable.Cols.DATE + " TEXT"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ExamDbSchema.QuestionTable.NAME + " ("
                + ExamDbSchema.QuestionTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExamDbSchema.QuestionTable.Cols.NAME + " TEXT, "
                + ExamDbSchema.QuestionTable.Cols.DESC + " TEXT, "
                + ExamDbSchema.QuestionTable.Cols.EXAM_ID + " INTEGER, "
                + ExamDbSchema.QuestionTable.Cols.ANSWER_ID + " INTEGER, "
                + ExamDbSchema.QuestionTable.Cols.POSITION + " INTEGER, "
                + ExamDbSchema.QuestionTable.Cols.CORRECT + " INTEGER "
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ExamDbSchema.OptionTable.NAME + " ("
                + ExamDbSchema.OptionTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExamDbSchema.OptionTable.Cols.TEXT + " TEXT, "
                + ExamDbSchema.OptionTable.Cols.QUESTION_ID + " INTEGER "
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
