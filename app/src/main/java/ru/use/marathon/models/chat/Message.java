package ru.use.marathon.models.chat;

/**
 * Created by ilyas on 09-Jul-18.
 */

public class Message {

    private String text, name, timestamp, uid,chat_id;
    private int user_type;

    public Message(String text, String name, String timestamp, String uid, int user_type,String chat_id) {
        this.text = text;
        this.name = name;
        this.timestamp = timestamp;
        this.uid = uid;
        this.user_type = user_type;
        this.chat_id = chat_id;
    }

    public Message(String text, String name, String timestamp) {
        this.text = text;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
}
