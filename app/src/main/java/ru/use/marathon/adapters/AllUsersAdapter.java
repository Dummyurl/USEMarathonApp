package ru.use.marathon.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.use.marathon.R;
import ru.use.marathon.models.Users;
import ru.use.marathon.models.UsersResponse;

/**
 * Created by ilyas on 10-Jul-18.
 */

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.UsersViewHolder> {


    private Context context;
    private ArrayList<Users> usersArrayList;
    private ArrayList<Integer> selected_users;
    private RadioButton rbtn = null;
    private int last_pos = 0;
    int ut;

    public AllUsersAdapter(Context context,ArrayList<Users> users,int user_type) {
        this.context = context;
        this.usersArrayList = users;
        this.ut = user_type;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_users_item,parent,false);

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {
        usersArrayList.get(position);

        if(!usersArrayList.get(position).getImage().isEmpty())
            Picasso.get().load(usersArrayList.get(position).getImage()).resize(100,100).into(holder.image);

        holder.name.setText(usersArrayList.get(position).getName());
        holder.id.setText(String.valueOf(usersArrayList.get(position).getId()));

        if(ut == 0){
            holder.select.setVisibility(View.VISIBLE);
            holder.select_teacher.setVisibility(View.GONE);
            holder.select.setChecked(usersArrayList.get(position).isSelected());

        }else{
            holder.select.setVisibility(View.GONE);
            holder.select_teacher.setVisibility(View.VISIBLE);

            if(position == 0 && usersArrayList.get(position).isSelected() && holder.select_teacher.isChecked()){
                rbtn = holder.select_teacher;
                last_pos = 0;

            }

            holder.select_teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton radioButton = (RadioButton) view;
                    int clicked = radioButton.getId();
                    if(radioButton.isChecked()){
                        if(rbtn != null){
                            rbtn.setChecked(false);
                            usersArrayList.get(position).setSelected(false);
                        }
                        rbtn = radioButton;
                        last_pos = clicked;

                    }else{
                        rbtn = null;
                    }
                    usersArrayList.get(position).setSelected(radioButton.isSelected());
                        Intent i = new Intent("selected_user");
                    i.putExtra("id",String.valueOf(usersArrayList.get(position).getId()));
                    i.putExtra("pos",String.valueOf(position));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.users_image)  CircleImageView image;
        @BindView(R.id.users_name)   TextView name;
        @BindView(R.id.users_id)     TextView id;
        @BindView(R.id.users_select) CheckBox select;
        @BindView(R.id.users_select_teacher)
        RadioButton select_teacher;

        public UsersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
