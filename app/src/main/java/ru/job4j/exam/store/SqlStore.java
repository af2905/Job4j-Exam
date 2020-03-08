package ru.job4j.exam.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.job4j.exam.model.Exam;
import ru.job4j.exam.model.Option;
import ru.job4j.exam.model.Question;

public class SqlStore {
    private static SqlStore sqlStore;
    private ExamBaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;
    private Cursor cursor;


    private SqlStore(Context context) {
        this.context = context.getApplicationContext();
        dbHelper = new ExamBaseHelper(this.context);
    }

    public static SqlStore getInstance(Context context) {
        if (sqlStore == null) {
            sqlStore = new SqlStore(context);
        }
        return sqlStore;
    }

    public void addExam(Exam exam) {
        db = dbHelper.getWritableDatabase();
        ContentValues examValues = new ContentValues();
        examValues.put(ExamDbSchema.ExamTable.Cols.NAME, exam.getName());
        examValues.put(ExamDbSchema.ExamTable.Cols.DESC, exam.getDesc());
        examValues.put(ExamDbSchema.ExamTable.Cols.RESULT, exam.getResult());
        examValues.put(ExamDbSchema.ExamTable.Cols.DATE, exam.getDate());
        long examId = db.insert(ExamDbSchema.ExamTable.NAME, null, examValues);
        exam.setId((int) examId);

        for (Question question : exam.getQuestions()) {
            ContentValues questionValues = new ContentValues();
            questionValues.put(ExamDbSchema.QuestionTable.Cols.NAME, question.getName());
            questionValues.put(ExamDbSchema.QuestionTable.Cols.DESC, question.getDesc());
            questionValues.put(ExamDbSchema.QuestionTable.Cols.EXAM_ID, examId);
            questionValues.put(ExamDbSchema.QuestionTable.Cols.ANSWER_ID, question.getAnswer());
            questionValues.put(ExamDbSchema.QuestionTable.Cols.POSITION, question.getPosition());
            questionValues.put(ExamDbSchema.QuestionTable.Cols.CORRECT, question.getCorrect());
            long questionId = db.insert(
                    ExamDbSchema.QuestionTable.NAME, null, questionValues);
            question.setId((int) questionId);

            for (Option option : question.getOptions()) {
                ContentValues optionValues = new ContentValues();
                optionValues.put(ExamDbSchema.OptionTable.Cols.TEXT, option.getText());
                optionValues.put(ExamDbSchema.OptionTable.Cols.QUESTION_ID, questionId);
                long optionId = db.insert(ExamDbSchema.OptionTable.NAME, null, optionValues);
                option.setId((int) optionId);
            }
        }
        db.close();
    }

    public void updateExam(Exam exam) {
        db = dbHelper.getWritableDatabase();
        ContentValues examValues = new ContentValues();
        examValues.put(ExamDbSchema.ExamTable.Cols.NAME, exam.getName());
        examValues.put(ExamDbSchema.ExamTable.Cols.DESC, exam.getDesc());
        examValues.put(ExamDbSchema.ExamTable.Cols.RESULT, exam.getResult());
        examValues.put(ExamDbSchema.ExamTable.Cols.DATE, exam.getDate());
        db.update(ExamDbSchema.ExamTable.NAME, examValues,
                ExamDbSchema.ExamTable.Cols.ID + " = ?",
                new String[]{String.valueOf(exam.getId())});
    }

    public void updateQuestion(int examId, Question question) {
        db = dbHelper.getWritableDatabase();
        ContentValues questionValues = new ContentValues();
        questionValues.put(ExamDbSchema.QuestionTable.Cols.NAME, question.getName());
        questionValues.put(ExamDbSchema.QuestionTable.Cols.DESC, question.getDesc());
        questionValues.put(ExamDbSchema.QuestionTable.Cols.EXAM_ID, examId);
        questionValues.put(ExamDbSchema.QuestionTable.Cols.ANSWER_ID, question.getAnswer());
        questionValues.put(ExamDbSchema.QuestionTable.Cols.POSITION, question.getPosition());
        questionValues.put(ExamDbSchema.QuestionTable.Cols.CORRECT, question.getCorrect());
        db.update(ExamDbSchema.QuestionTable.NAME, questionValues,
                ExamDbSchema.QuestionTable.Cols.ID + " = ?",
                new String[]{String.valueOf(question.getId())});
    }

    public List<Exam> getAllExams() {
        db = dbHelper.getReadableDatabase();
        List<Exam> allExams = new ArrayList<>();
        cursor = db.query(ExamDbSchema.ExamTable.NAME,
                null, null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            try {
                do {
                    Exam exam = new Exam(cursor.getInt(
                            cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.ID)),
                            cursor.getString(
                                    cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.NAME)),
                            cursor.getString(
                                    cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DESC)),
                            cursor.getString(
                                    cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.RESULT)),
                            cursor.getString(
                                    cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DATE)),
                            new ArrayList<>());
                    allExams.add(exam);
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return allExams;
    }

    public Exam getOnlyOneExam(int id) {
        return getExamMethod(id);
    }

    public Exam getExam(int id) {
        List<Question> questions = new ArrayList<>();
        Exam exam = getExamMethod(id);
        cursor = db.query(ExamDbSchema.QuestionTable.NAME,
                new String[]{ExamDbSchema.QuestionTable.Cols.ID,
                        ExamDbSchema.QuestionTable.Cols.NAME,
                        ExamDbSchema.QuestionTable.Cols.DESC,
                        ExamDbSchema.QuestionTable.Cols.EXAM_ID,
                        ExamDbSchema.QuestionTable.Cols.ANSWER_ID,
                        ExamDbSchema.QuestionTable.Cols.POSITION,
                        ExamDbSchema.QuestionTable.Cols.CORRECT},
                ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        try {
            do {
                List<Option> options = new ArrayList<>();
                Question question = new Question(
                        cursor.getInt(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.ID)),
                        cursor.getString(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.NAME)),
                        cursor.getString(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.DESC)),
                        cursor.getInt(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.POSITION)),
                        cursor.getInt(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.ANSWER_ID)),
                        new ArrayList<>(),
                        cursor.getInt(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.CORRECT))
                );

                Cursor optionCursor = db.query(ExamDbSchema.OptionTable.NAME,
                        new String[]{ExamDbSchema.OptionTable.Cols.ID,
                                ExamDbSchema.OptionTable.Cols.TEXT,
                                ExamDbSchema.OptionTable.Cols.QUESTION_ID},
                        ExamDbSchema.OptionTable.Cols.QUESTION_ID + " = ?",
                        new String[]{String.valueOf(question.getId())},
                        null, null, null, null
                );
                if (optionCursor != null) {
                    optionCursor.moveToFirst();
                }
                try {
                    do {
                        Option option = new Option(
                                optionCursor.getInt(
                                        optionCursor.getColumnIndex(ExamDbSchema.OptionTable.Cols.ID)),
                                optionCursor.getString(
                                        optionCursor.getColumnIndex(ExamDbSchema.OptionTable.Cols.TEXT))
                        );
                        options.add(option);
                    } while (optionCursor.moveToNext());
                } finally {
                    optionCursor.close();
                }
                question.setOptions(options);
                questions.add(question);
            } while (cursor.moveToNext());
        } finally {
            cursor.close();
        }
        exam.setQuestions(questions);
        return exam;
    }

    private Exam getExamMethod(int id) {
        db = dbHelper.getReadableDatabase();
        cursor = db.query(ExamDbSchema.ExamTable.NAME,
                new String[]{ExamDbSchema.ExamTable.Cols.ID,
                        ExamDbSchema.ExamTable.Cols.NAME,
                        ExamDbSchema.ExamTable.Cols.DESC,
                        ExamDbSchema.ExamTable.Cols.RESULT,
                        ExamDbSchema.ExamTable.Cols.DATE},
                ExamDbSchema.ExamTable.Cols.ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return new Exam(
                cursor.getInt(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.ID)),
                cursor.getString(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.NAME)),
                cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DESC)),
                cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.RESULT)),
                cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DATE)),
                new ArrayList<>());
    }

    public List<Exam> getFinishedExams() {
        String request = "SELECT * FROM " + ExamDbSchema.ExamTable.NAME
                + " WHERE " + ExamDbSchema.ExamTable.Cols.RESULT + " != \"\"";
        return getExamsMethod(request);

    }

    public List<Exam> getNotFinishedExams() {
        String request = "SELECT * FROM " + ExamDbSchema.ExamTable.NAME
                + " WHERE " + ExamDbSchema.ExamTable.Cols.RESULT + " = \"\"";
        return getExamsMethod(request);
    }

    private List<Exam> getExamsMethod(String request) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = db.rawQuery(request, null);
        if (cursor.moveToFirst()) {
            try {
                do {
                    Exam exam = new Exam(cursor.getInt(
                            cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.ID)),
                            cursor.getString(
                                    cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.NAME)),
                            cursor.getString(
                                    cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DESC)),
                            cursor.getString(
                                    cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.RESULT)),
                            cursor.getString(
                                    cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DATE)),
                            new ArrayList<>());
                    exams.add(exam);
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return exams;
    }

    public void deleteExam(Exam exam) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ExamDbSchema.ExamTable.NAME,
                ExamDbSchema.ExamTable.Cols.ID + " = ?",
                new String[]{String.valueOf(exam.getId())});
        db.close();
    }
}






