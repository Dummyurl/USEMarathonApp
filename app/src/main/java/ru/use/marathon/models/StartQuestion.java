package ru.use.marathon.models;

import java.io.Serializable;

/**
 * Created by ilyas on 05-Aug-18.
 */

public class StartQuestion implements Serializable {

    String question;
    String[] answers;

    public StartQuestion(String question, String[] answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }
}
