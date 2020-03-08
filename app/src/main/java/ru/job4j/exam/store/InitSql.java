package ru.job4j.exam.store;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.job4j.exam.model.Exam;
import ru.job4j.exam.model.Option;
import ru.job4j.exam.model.Question;

public class InitSql {
    private static InitSql initSql;
    private Context context;
    private SqlStore sqlStore;

    private InitSql(Context context) {
        this.context = context.getApplicationContext();
        this.sqlStore = SqlStore.getInstance(this.context);
    }

    public static InitSql getInstance(Context context) {
        if (initSql == null) {
            initSql = new InitSql(context);
        }
        return initSql;
    }

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

    public List<Exam> loadingExams() {
        List<Exam> allExams = new ArrayList<>(sqlStore.getNotFinishedExams());
        if (allExams.size() == 0) {
            for (Exam exam : exams) {
                sqlStore.addExam(exam);
            }
            allExams = sqlStore.getAllExams();
        }
        return allExams;
    }
}
