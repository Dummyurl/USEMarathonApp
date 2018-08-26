package ru.use.marathon.models.Tests;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ilyas on 24-Aug-18.
 */

@Entity(tableName = "tests")
public class TestCollection {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    int id;

    @ColumnInfo(name = "title")
    private
    String title;

    @ColumnInfo(name = "answer_type")
    private
    int answer_type;


    @ColumnInfo(name = "subject")
    private
    int subject;

    @ColumnInfo(name = "task_number")
    int task_number;

    @ColumnInfo(name = "topic_id")
    private
    int topic_id;

    @ColumnInfo(name = "content")
    private
    String content;

    @ColumnInfo(name = "content_image")
    private
    String content_image;

    @ColumnInfo(name = "content_html")
    private
    String content_html;

    @ColumnInfo(name = "answers")
    private
    String answers;

    @ColumnInfo(name = "right_answers")
    private
    String right_answers;

    @ColumnInfo(name = "user_answers")
    private
    String user_answers;


    public TestCollection( String title, int answer_type, int subject, int task_number, int topic_id,@NonNull String content, @NonNull String content_image,@NonNull String content_html,@NonNull String answers,@NonNull String right_answers, @NonNull String user_answers) {
        this.title = title;
        this.answer_type = answer_type;
        this.subject = subject;
        this.task_number = task_number;
        this.topic_id = topic_id;
        this.content = content;
        this.content_image = content_image;
        this.content_html = content_html;
        this.answers = answers;
        this.right_answers = right_answers;
        this.user_answers = user_answers;
    }

    @Ignore
    public TestCollection(TestCollectionBuilder builder) {
        this.title = builder.title;
        this.answer_type = builder.answer_type;
        this.task_number = builder.task_number;
        this.topic_id = builder.topic_id;
        this.answers = builder.answers;
        this.right_answers = builder.right_answers;
        this.user_answers = builder.user_answers;
        this.content = builder.content;
        this.content_html = builder.content_html;
        this.subject = builder.subject;
        this.content_image = builder.content_image;
    }

    public String getTitle() {
        return title;
    }

    public int getAnswer_type() {
        return answer_type;
    }

    public int getSubject() {
        return subject;
    }

    public int getTask_number() {
        return task_number;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public String getContent() {
        return content;
    }

    public String getContent_image() {
        return content_image;
    }

    public String getContent_html() {
        return content_html;
    }

    public String getAnswers() {
        return answers;
    }

    public String getRight_answers() {
        return right_answers;
    }

    public String getUser_answers() {
        return user_answers;
    }

    public static class TestCollectionBuilder {

        String title;
        int answer_type;
        int task_number;
        int topic_id;
        int subject;
        String content;
        String content_image;
        String content_html;
        String answers;
        String right_answers;
        String user_answers;

        public TestCollectionBuilder() {
        }

        public TestCollectionBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TestCollectionBuilder answerType(int answer_type) {
            this.answer_type = answer_type;
            return this;
        }

        public TestCollectionBuilder taskNumber(int taskNumber) {
            this.task_number = taskNumber;
            return this;
        }

        public TestCollectionBuilder subject(int sbj) {
            this.subject = sbj;
            return this;
        }

        public TestCollectionBuilder topicId(int topic_id) {
            this.topic_id = topic_id;
            return this;
        }

        public TestCollectionBuilder content(String content) {
            this.content = content;
            return this;
        }

        public TestCollectionBuilder contentImage(String content_image) {
            this.content_image = content_image;
            return this;
        }

        public TestCollectionBuilder contentHtml(String content_html) {
            this.content_html = content_html;
            return this;
        }

        public TestCollectionBuilder answers(List<String> answers) {
            this.answers = answers.toString();
            return this;
        }

        public TestCollectionBuilder rightAnswers(List<String> ranswers) {
            this.right_answers = ranswers.toString();
            return this;
        }

        public TestCollectionBuilder userAnswers(List<String> uanswers) {
            this.user_answers = uanswers.toString();
            return this;
        }

        public TestCollection build() {
            return new TestCollection(this);
        }
    }


}
