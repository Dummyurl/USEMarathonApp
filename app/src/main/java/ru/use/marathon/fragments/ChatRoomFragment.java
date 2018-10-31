package ru.use.marathon.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiMessage;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.adapters.chat.VkChatAdapter;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.chat.Messages;

import static ru.use.marathon.models.Success.success;
import static ru.use.marathon.models.Success.teacherId;
import static ru.use.marathon.models.Success.teacherName;

/**
 * Created by Daria on 07-Oct-18.
 */
public class ChatRoomFragment extends AbstractFragment {
    public static final String TAG = ChatRoomFragment.class.getSimpleName();
    private Messages messages;
    VkChatAdapter adapter;

    @BindView(R.id.chat_layout)
    RelativeLayout chat_layout;
    @BindView(R.id.status_layout)
    RelativeLayout status_layout;
    @BindView(R.id.infomation_txt)
    TextView info;

    @BindView(R.id.chat_messages_rv)
    RecyclerView chat_messages_rv;
    @BindView(R.id.chat_message_write_et)
    EditText chat_message_write_et;
    @BindView(R.id.chat_message_send_btn)
    ImageButton chat_message_send_btn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    boolean isSubscribed = false;
    String teacher_id,teacher_name;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_chat_room, container, false);
        ButterKnife.bind(this, view);

        status_layout.setVisibility(View.GONE);
        chat_layout.setVisibility(View.GONE);


        AppController.getApi().checkSubscription(1,"check", FirebaseInstanceId.getInstance().getToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                new Success(response);
                if(success()){
                    status_layout.setVisibility(View.GONE);
                    chat_layout.setVisibility(View.VISIBLE);
                    isSubscribed = true;

                    teacher_id = teacherId();
                    teacher_name = teacherName();

                }else{
                    chat_layout.setVisibility(View.GONE);
                    status_layout.setVisibility(View.VISIBLE);
                    info.setText("Купи подписку!11");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        if(isSubscribed){
            toolbar.setTitle("Чат с учителем");
            messages = new Messages();
            refreshMessages(0);
            Log.d(TAG, "onCreateView: " + messages.size());


            chat_message_send_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VKRequest request = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, Integer.valueOf(teacher_id),
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
        }else{
            toolbar.setTitle("Чат с учителем");
        }

        return view;
    }

    private void refreshMessages(final int flag) {
        if (flag == 1) {
            messages.clear();
        }
        final VKRequest request = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, Integer.valueOf(teacher_id)));
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
                    for (int i = 0; i < array.length(); i++) {
                        VKApiMessage mes = new VKApiMessage(array.getJSONObject(i));
                        msg[i] = mes;
                    }
                    for (VKApiMessage mess : msg) {
                        Message message = new Message();
                        message.setMsg(mess.body);
                        message.setUser_id(mess.user_id);
                        message.setUser_name(teacher_name);
                        message.setAt(mess.date);
                        message.setOut(mess.out);
                        messages.addMessage(message);
                    }
                    messages.setup();
                    if (flag == 1) {
                        adapter.notifyDataSetChanged();
                    } else {
                        chat_messages_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new VkChatAdapter(getActivity(), messages);
                        chat_messages_rv.setAdapter(adapter);
                    }
                    chat_messages_rv.scrollToPosition(adapter.getItemCount() - 1);
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

