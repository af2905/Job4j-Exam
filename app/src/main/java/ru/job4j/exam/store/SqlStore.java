package ru.job4j.exam.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.job4j.exam.model.Exam;
import ru.job4j.exam.model.Option;
import ru.job4j.exam.model.Question;

public class SqlStore {
    private static SqlStore sqlStore;
    private ExamBaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;
    Cursor cursor;
    private final List<Exam> exams = new ArrayList<>(Arrays.asList(
            new Exam("Android Developer Exam",
                    "The exam is designed to test the basic knowledge of Android developers",
                    "", "",
                    Arrays.asList(new Question("1 question",
                                    "What database is used in Android OS?",
                                    0, -1,
                                    Arrays.asList(
                                            new Option(1, "T-SQL"),
                                            new Option(2, "MySQL"),
                                            new Option(3, "PostgreSQL"),
                                            new Option(4, "SQLite")),
                                    4),
                            new Question("2 question",
                                    "Which method launches a new activity?",
                                    1, -1,
                                    Arrays.asList(
                                            new Option(1, "startActivity()"),
                                            new Option(2, "beginActivity()"),
                                            new Option(3, "intentActivity()"),
                                            new Option(4, "newActivity()")),
                                    1),
                            new Question("3 question",
                                    "Can a mobile application access a database created"
                                            + " in another application?",
                                    2, -1,
                                    Arrays.asList(
                                            new Option(1, "no, cannot"),
                                            new Option(2, "can, but only with the help "
                                                    + "of content providers"),
                                            new Option(3, "the right to access opens "
                                                    + "the database host application"),
                                            new Option(4, "can contact directly")),
                                    2),
                            new Question("4 question",
                                    "Switching between activities is carried out",
                                    3, -1,
                                    Arrays.asList(
                                            new Option(1, "only using buttons"),
                                            new Option(2, "only using the touch screen "
                                                    + "of a smartphone"),
                                            new Option(3, "only with buttons "
                                                    + "and other controls"),
                                            new Option(4, "all three options are possible")),
                                    4)
                    )
            ),
            new Exam("Java Programmer Exam",
                    "The exam is designed to test the basic knowledge of Java programmers",
                    "", "",
                    Arrays.asList(new Question("1 question",
                                    "One of the keywords in Java:",
                                    0, -1,
                                    Arrays.asList(
                                            new Option(1, "false"),
                                            new Option(2, "null"),
                                            new Option(3, "true"),
                                            new Option(4, "default")),
                                    4),
                            new Question("2 question",
                                    " How many objects are generated "
                                            + "when the array is initialized new int[3][]:",
                                    1, -1,
                                    Arrays.asList(
                                            new Option(1, "1"),
                                            new Option(2, "3"),
                                            new Option(3, "2"),
                                            new Option(4, "0")),
                                    1),
                            new Question("3 question",
                                    "Which statement about the String class is true?",
                                    2, -1,
                                    Arrays.asList(
                                            new Option(1, "is abstract"),
                                            new Option(2, "contains only static methods"),
                                            new Option(3, "has the property of immutability"),
                                            new Option(4, "all answers are correct")),
                                    3),
                            new Question("4 question",
                                    "One of the keywords in Java:",
                                    3, -1,
                                    Arrays.asList(
                                            new Option(1, "null"),
                                            new Option(2, "protected"),
                                            new Option(3, "false"),
                                            new Option(4, "true")),
                                    2)
                    )
            )
    ));

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

    public List<Exam> loadingExams() {
        List<Exam> allExams = new ArrayList<>();
        allExams.addAll(getNotFinishedExams());
        if (allExams.size() == 0) {
            for (Exam exam : exams) {
                addExam(exam);
            }
            allExams = getAllExams();
        }
        return allExams;
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
        long examId = db.update(ExamDbSchema.ExamTable.NAME, examValues,
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
        }
        cursor.close();
        return allExams;
    }

    public Exam getOnlyOneExam(int id) {
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

    public Exam getExam(int id) {
        Exam exam;
        List<Question> questions = new ArrayList<>();
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
        exam = new Exam(
                cursor.getInt(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.ID)),
                cursor.getString(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.NAME)),
                cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DESC)),
                cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.RESULT)),
                cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DATE)),
                new ArrayList<>());

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
            do {
                Option option = new Option(
                        optionCursor.getInt(
                                optionCursor.getColumnIndex(ExamDbSchema.OptionTable.Cols.ID)),
                        optionCursor.getString(
                                optionCursor.getColumnIndex(ExamDbSchema.OptionTable.Cols.TEXT))
                );
                options.add(option);
            } while (optionCursor.moveToNext());
            optionCursor.close();
            question.setOptions(options);
            questions.add(question);
        } while (cursor.moveToNext());
        cursor.close();
        exam.setQuestions(questions);
        return exam;
    }

    public List<Exam> getFinishedExams() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Exam> exams = new ArrayList<>();
        String selectExams = "SELECT * FROM " + ExamDbSchema.ExamTable.NAME
                + " WHERE " + ExamDbSchema.ExamTable.Cols.RESULT + " != \"\"";
        Cursor cursor = db.rawQuery(selectExams, null);
        if (cursor.moveToFirst()) {
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
        }
        cursor.close();
        return exams;
    }

    public List<Exam> getNotFinishedExams() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Exam> exams = new ArrayList<>();
        String selectExams = "SELECT * FROM " + ExamDbSchema.ExamTable.NAME
                + " WHERE " + ExamDbSchema.ExamTable.Cols.RESULT + " = \"\"";
        Cursor cursor = db.rawQuery(selectExams, null);
        if (cursor.moveToFirst()) {
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
        }
        cursor.close();
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






