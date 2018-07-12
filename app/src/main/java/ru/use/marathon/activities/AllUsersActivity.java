package ru.use.marathon.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import ru.use.marathon.R;
import ru.use.marathon.adapters.AllUsersAdapter;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;
import ru.use.marathon.models.Users;
import ru.use.marathon.models.UsersResponse;
import ru.use.marathon.models.chat.Rooms;
import ru.use.marathon.utils.ItemClickSupport;

public class AllUsersActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.all_users_rv)
    RecyclerView recyclerView;
    int user_type = -1;
    AllUsersAdapter allUsersAdapter;
    ArrayList<Users> usersArrayList;
    @BindView(R.id.create_chat_btn)
    Button create_chat_btn;
    private BroadcastReceiver mMessageReceiver;
    private Teacher teacher;
    private Student student;
    String user_id = "";
    int local_user_type;

    String pos = "", id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        teacher = new Teacher(this);
        student = new Student(this);
        HashMap<String,String> user_data;
        if(student.isLoggedIn() && !teacher.isLoggedIn()){
            user_data = student.getData();
            user_id = user_data.get(student.KEY_ID);
            local_user_type = 0;
        }else if(!student.isLoggedIn() && teacher.isLoggedIn()){
            user_data = teacher.getData();
            user_id = user_data.get(teacher.KEY_ID);
            local_user_type = 1;
        }

        user_type = getIntent().getIntExtra("type", -1);
        usersArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initUsersList();

        mMessageReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                pos = intent.getStringExtra("pos");
                id = intent.getStringExtra("id");
            }
        };

        create_chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initButton();
                    }
                });

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("selected_user"));
    }

    private void initButton() {

        final String[] title = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setTitle("Придумайте имя вашему чату");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                title[0] = input.getText().toString();
                if (!pos.isEmpty() && !id.isEmpty() && !title[0].isEmpty()) {
                    AppController.getApi().createChat(1, "createChatRoom", title[0], user_id, local_user_type).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Rooms rooms = new Rooms(response);
                            final String chat_id = String.valueOf(rooms.getChatId());
                            AppController.getApi().addChatMember(1, "addChatMember", chat_id, id, user_type).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                   Intent i = new Intent(AllUsersActivity.this,ChatRoomActivity.class);
                                   i.putExtra("chat_id",chat_id);
                                   i.putExtra("title",title[0]);
                                   startActivity(i);
                                   finish();
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                }
            }


        });
        builder.create().show();
    }


    private void initUsersList() {

        AppController.getApi().getAllUsers(1, "getAllUsers", user_type).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                UsersResponse users = new UsersResponse(response);
                for (int i = 0; i < users.size(); i++) {
                    usersArrayList.add(new Users(Integer.valueOf(users.getId(i)), users.getName(i), users.getImage(i), false));
                }
                allUsersAdapter = new AllUsersAdapter(getApplicationContext(), usersArrayList, user_type);
                recyclerView.setAdapter(allUsersAdapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }


}
