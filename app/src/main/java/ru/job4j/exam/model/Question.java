package ru.job4j.exam.model;

import java.util.List;
import java.util.Objects;

public class Question {
    private int id;
    private String name;
    private String desc;
    private int position;
    private int answer;
    private List<Option> options;
    private int correct;

    public Question(int id, String name, String desc, int position, int answer,
                    List<Option> options, int correct) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.position = position;
        this.answer = answer;
        this.options = options;
        this.correct = correct;
    }

    public Question(String name, String desc, int position, int answer,
                    List<Option> options, int correct) {
        this.name = name;
        this.desc = desc;
        this.position = position;
        this.answer = answer;
        this.options = options;
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return id == question.id
                && position == question.position
                && correct == question.correct
                && Objects.equals(name, question.name)
                && Objects.equals(desc, question.desc)
                && Objects.equals(answer, question.answer)
                && Objects.equals(options, question.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, position, answer, options, correct);
    }
}
