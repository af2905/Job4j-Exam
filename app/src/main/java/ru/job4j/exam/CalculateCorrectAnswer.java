package ru.job4j.exam;

public class CalculateCorrectAnswer {
    public static int percent(int position, int rightAnswerCount) {
        return rightAnswerCount * 100 / (position + 1);
    }
}
