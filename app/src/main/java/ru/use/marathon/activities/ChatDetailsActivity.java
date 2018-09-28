package ru.use.marathon.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.adapters.chat.ChatMembersAdapter;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.Teacher;
import ru.use.marathon.models.chat.ChatMembers;

import static ru.use.marathon.models.Success.success;
/**
 * Created by Ilyas on 24-July-18.
 */
public class ChatDetailsActivity extends AbstractActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.chat_details_title)
    TextView title_txt;

    @BindView(R.id.chat_details_paticipants)
    RecyclerView recyclerView;

    @BindView(R.id.chat_details_add_member)
    Button add_member_btn;

    @BindView(R.id.chat_details_leave_chat)
    Button leaveChat;

    String title;
    int chat_id;

    Teacher teacher;
    Student student;
    int user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        student = new Student(this);
        teacher = new Teacher(this);

        if(student.isLoggedIn() && !teacher.isLoggedIn()){
            user_type = 0;
        }else if(!student.isLoggedIn() && teacher.isLoggedIn()){
            user_type = 1;
        }

        title = getIntent().getStringExtra("title");
        chat_id = getIntent().getIntExtra("chat_id",-1);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //todo set rv animation

        initParticipants();
        initFunctions();

        title_txt.setText(title);


    }

    private void initParticipants() {
        AppController.getApi().getChatMembers(1,"getChatMembers",String.valueOf(chat_id)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ChatMembers members = new ChatMembers(response);
                ChatMembersAdapter adapter = new ChatMembersAdapter(members,ChatDetailsActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void initFunctions() {
        add_member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo complete following class
                Intent i = new Intent(ChatDetailsActivity.this,AllUsersActivity.class);
                i.putExtra("type",0);
                i.putExtra("layout_type",0);
                i.putExtra("chat_id",chat_id);

                startActivity(i);
            }
        });

        leaveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder b = new AlertDialog.Builder(ChatDetailsActivity.this);
                b.setTitle("Покинуть беседу?");
                if (user_type == 0) {
                    b.setMessage("После ухода из беседы вы не сможете вернуться в нее обратно.");
                } else {
                    b.setMessage("После ухода из беседы все участники беседы будут удалены из беседы.");
                }
                b.setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(user_type == 0){
                            initLeave();
                        }else{
                            initDestruction();
                        }
                    }
                });
                b.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = b.create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home){
//            Intent i = new Intent(ChatDetailsActivity.this,ChatRoomActivity.class);
//            i.putExtra("chat_id",chat_id);
//            i.putExtra("title",title);
//            startActivity(i);
//            finish();
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initDestruction() {
        //todo write server function and complete it here
    }
    private void initLeave() {

        AppController.getApi().leaveChat(1,"leaveChat",String.valueOf(chat_id),String.valueOf(student.UID()),0).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(success(response)){
                    Intent i = new Intent(ChatDetailsActivity.this,MainActivity.class);
                    i.putExtra("bnv_tag","chat");
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
