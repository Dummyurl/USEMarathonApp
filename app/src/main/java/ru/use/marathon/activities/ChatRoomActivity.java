package ru.use.marathon.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.adapters.chat.ChatMessagesAdapter;
import ru.use.marathon.fragments.navigation.student.SNavFeedFragment;
import ru.use.marathon.fragments.navigation.teacher.TNavHomeFragment;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;
import ru.use.marathon.models.chat.Message;
import ru.use.marathon.models.chat.MessagesResponse;
import ru.use.marathon.utils.NotificationUtils;

public class ChatRoomActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chat_messages_rv)
    RecyclerView chat_messages_rv;
    @BindView(R.id.chat_message_write_et)
    EditText write_msg;
    @BindView(R.id.chat_message_send_btn)
    Button send_btn;
    @BindView(R.id.chat_messages_progress)
    ProgressBar progressBar;
    @BindView(R.id.chat_messages_status)
    TextView status;

    String title;
    int chat_id;
    Student student;
    Teacher teacher;
    ArrayList<Message> messageArrayList;
    ChatMessagesAdapter adapter;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    String user_id, user_name;
    int user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = getIntent().getStringExtra("title");
        chat_id = getIntent().getIntExtra("chat_id", 0);

        getSupportActionBar().setTitle(title);

        chat_messages_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        messageArrayList = new ArrayList<>();
        student = new Student(this);
        teacher = new Teacher(this);

        if (student.isLoggedIn() && !teacher.isLoggedIn()) {
            HashMap<String, String> user_data = student.getData();
            user_id = user_data.get(student.KEY_ID);
            user_name = user_data.get(student.KEY_NAME);
            user_type = 0;
        } else if (!student.isLoggedIn() && teacher.isLoggedIn()) {
            HashMap<String, String> user_data = teacher.getData();
            user_id = user_data.get(teacher.KEY_ID);
            user_name = user_data.get(teacher.KEY_NAME);
            user_type = 1;
        }


        progressBar.setVisibility(View.VISIBLE);
        status.setVisibility(View.VISIBLE);
        status.setText("Загрузка сообщений..");
        fetchMessages();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
                    if(messageArrayList.isEmpty()){
                        adapter = new ChatMessagesAdapter(messageArrayList,user_id,user_type);
                        chat_messages_rv.setAdapter(adapter);
                    }

                    String name = intent.getStringExtra("name");
                    String chat_id = intent.getStringExtra("chat_id");
                    String message = intent.getStringExtra("message");
                    String timestamp = intent.getStringExtra("timestamp");
                    String uid = intent.getStringExtra("uid");
                    int ut = intent.getIntExtra("ut", -1);

                    messageArrayList.add(new Message(message, name, timestamp, uid, ut, chat_id));
                    adapter.notifyDataSetChanged();
                    chat_messages_rv.scrollToPosition(adapter.getItemCount() - 1);
                    status.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        };


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = write_msg.getText().toString();
                sendMessage(message);
                write_msg.setText("");
            }
        });

    }

    private void sendMessage(String message) {
        AppController.getApi().sendMsg(1, "sendMsg", message, chat_id, user_id, user_type, user_name).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent();
        setResult(100,i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home){
            Intent i = new Intent();
            setResult(100,i);
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    private void fetchMessages() {
        AppController.getApi().getMessages(1, "getMessages", String.valueOf(chat_id), user_id, user_type).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                MessagesResponse messagesResponse = new MessagesResponse(response);
                for (int i = 0; i < messagesResponse.getData().size(); i++) {
                    Message message = new Message(
                            messagesResponse.getText(i),
                            messagesResponse.getUserName(i),
                            messagesResponse.getTimestamp(i),
                            messagesResponse.getUserId(i),
                            messagesResponse.getUserType(i),
                            messagesResponse.getChatId(i)
                    );

                    messageArrayList.add(message);
                }
                if (messageArrayList.isEmpty()) {
                    status.setText("Нет сообщений");
                } else {
                    adapter = new ChatMessagesAdapter(messageArrayList, user_id, user_type);
                    chat_messages_rv.setAdapter(adapter);
                    chat_messages_rv.scrollToPosition(adapter.getItemCount() - 1);
                    progressBar.setVisibility(View.INVISIBLE);
                    status.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }


}
