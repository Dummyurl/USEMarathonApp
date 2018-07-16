package ru.use.marathon.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
import ru.use.marathon.models.chat.ChatIdResponse;
import ru.use.marathon.models.chat.Rooms;
import ru.use.marathon.utils.ItemClickSupport;

public class AllUsersActivity extends AppCompatActivity {

    public static final String TAG = AllUsersActivity.class.getSimpleName();

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
    int local_user_type,layout_type,chat_id;

    String pos = "", id = "";
    Set<Integer> positions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        teacher = new Teacher(this);
        student = new Student(this);
        positions = new HashSet<>();
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
        layout_type = getIntent().getIntExtra("layout_type",-1);
        chat_id = getIntent().getIntExtra("chat_id",-1);
        usersArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initUsersList();

        mMessageReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                pos = intent.getStringExtra("pos");
                id = intent.getStringExtra("id");
                Toast.makeText(AllUsersActivity.this, "pos and user_id: " + pos + "_"+ id, Toast.LENGTH_SHORT).show();
                positions.add(Integer.valueOf(id));
                Log.d(TAG, "onReceive(): positions array: " + positions.toString());
            }
        };

        if(layout_type == -1 ){
            create_chat_btn.setText("Создать чат");
            getSupportActionBar().setTitle("Создание чата");
        }else{
            create_chat_btn.setText("Добавить в чат");
            getSupportActionBar().setTitle("Добавление в чат");
        }
        create_chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!id.isEmpty() && !pos.isEmpty()){
                            if(layout_type == -1) initButton();
                            else initAddMemberButton();
                        }else{
                            Toast.makeText(AllUsersActivity.this, "Нельзя создать чат, не выбрав участника", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("selected_user"));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        positions.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        positions.clear();
    }



    private void initAddMemberButton() {
        int[] p = new int[positions.size()];
        int counter = 0;
        for(Integer i : positions){
            p[counter] = i;
            counter++;
        }
        for (int i = 0; i < p.length; i++) {
            if(chat_id != -1){
                AppController.getApi().addChatMember(1,"addChatMember",String.valueOf(chat_id),String.valueOf(p[i]),0).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        //todo notify user about response from server
                        Toast.makeText(AllUsersActivity.this, "Пользователь добавлен!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(AllUsersActivity.this, "Что-то пошло не так, а именно:" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }else{
                create_chat_btn.setError("Error! Try again later");
                Toast.makeText(this, "Error. Please try again later..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initButton() {

        final String[] title = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(getApplicationContext());
        input.setTextColor(Color.BLACK);
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
                            ChatIdResponse response1 = new ChatIdResponse(response);
                            final String chat_id = response1.getChatId();
                            AppController.getApi().addChatMember(1, "addChatMember", chat_id, id, user_type).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                   Intent i = new Intent(AllUsersActivity.this,ChatRoomActivity.class);
                                   i.putExtra("chat_id",Integer.valueOf(chat_id));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_menu,menu);
        return true;
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
