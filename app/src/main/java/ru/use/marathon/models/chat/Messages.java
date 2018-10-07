package ru.use.marathon.models.chat;

import ru.use.marathon.fragments.ChatRoomFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Messages implements Serializable {

    ArrayList<ChatRoomFragment.Message> messages;
    public Messages() {
        messages = new ArrayList<>();
    }
    public void clear(){
        messages.clear();
    }
    public int size(){
        return messages.size();
    }

    public void addMessage(ChatRoomFragment.Message message){
        messages.add(message);
    }


    public void setup(){
        Collections.reverse(messages);
    }

    public ChatRoomFragment.Message getMessage(int position){
        return messages.get(position);
    }
}
