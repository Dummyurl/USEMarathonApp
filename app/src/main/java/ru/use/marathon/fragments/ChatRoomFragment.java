package ru.use.marathon.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.JsonObject;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.VKApiMessage;
import org.json.JSONArray;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.activities.AbstractActivity;
import ru.use.marathon.activities.ChatDetailsActivity;
import ru.use.marathon.adapters.chat.ChatMessagesAdapter;
import ru.use.marathon.adapters.chat.VkChatAdapter;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;
import ru.use.marathon.models.chat.Message;
import ru.use.marathon.models.chat.Messages;
import ru.use.marathon.models.chat.MessagesResponse;
import ru.use.marathon.utils.NotificationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daria on 07-Oct-18.
 */
public class ChatRoomFragment extends AbstractFragment {
public static final String TAG = ChatRoomFragment.class.getSimpleName();
    private Messages messages;
    VkChatAdapter adapter;
    int id = 182013774;

    @BindView(R.id.chat_messages_rv) RecyclerView chat_messages_rv;
    @BindView(R.id.chat_message_write_et) EditText chat_message_write_et;
    @BindView(R.id.chat_message_send_btn) Button chat_message_send_btn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_chat_room,container,false);
        ButterKnife.bind(this, view);
        messages =  new Messages();
        refreshMessages(0);
        Log.d(TAG, "onCreateView: " + messages.size());



        chat_message_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest request = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, id,
                        VKApiConst.MESSAGE, chat_message_write_et.getText().toString()));

                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        chat_message_write_et.setText("");
                        refreshMessages(1);
                    }
                });
            }
        });
        return view;
    }

    private void refreshMessages(final int flag) {
        if(flag==1) {
            messages.clear();
        }
        final VKRequest request = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id ));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d(TAG, "onCreateView: " + error.toString());
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                    VKApiMessage[] msg = new VKApiMessage[array.length()];
                    for (int i = 0; i < array.length(); i++ ){
                        VKApiMessage mes = new VKApiMessage(array.getJSONObject(i));
                        msg[i] = mes;
                    }
                    for (VKApiMessage mess : msg){
                        Message message = new Message();
                        message.setMsg(mess.body);
                        message.setUser_id(mess.user_id);
                        message.setUser_name("Имя учителя");
                        message.setAt(mess.date);
                        message.setOut(mess.out);
                        messages.addMessage(message);
                    }
                    messages.setup();
                    if(flag==1) {
                        adapter.notifyDataSetChanged();
                        chat_messages_rv.scrollToPosition(adapter.getItemCount() - 1);
                    }
                    else {
                        chat_messages_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new VkChatAdapter(getActivity(), messages);
                        chat_messages_rv.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class Message implements Serializable {

        private String msg;
        private int user_id;
        private String user_name;
        private long at;
        private boolean out;

        public Message() {
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public long getAt() {
            return at;
        }

        public boolean isOut() {
            return out;
        }

        public void setOut(boolean out) {
            this.out = out;
        }

        public void setAt(long at) {
            this.at = at;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }
}

