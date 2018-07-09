package ru.use.marathon.models.chat;

/**
 * Created by ilyas on 08-Jul-18.
 */

public class ChatRoom {

   private int id,unreadCount;
   private String title, name, timestamp,image,lastMessage;

    public int getId() {
        return id;
    }


    public ChatRoom(int id, int unreadCount, String title, String name, String timestamp, String image, String lastMessage) {
        this.id = id;
        this.unreadCount = unreadCount;
        this.title = title;
        this.name = name;
        this.timestamp = timestamp;
        this.image = image;
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
