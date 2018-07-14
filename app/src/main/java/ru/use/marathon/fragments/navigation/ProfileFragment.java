package ru.use.marathon.fragments.navigation;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.use.marathon.R;
import ru.use.marathon.fragments.AbstractFragment;

/**
 * Created by ilyas on 10-Jun-18.
 */

public class ProfileFragment extends AbstractFragment {

    Unbinder unbinder;

    @BindView(R.id.user_image)
    CircleImageView user_image;
    @BindView(R.id.user_id)
    TextView user_id_tv;
    @BindView(R.id.user_email)
    TextView user_email_tv;
    @BindView(R.id.user_name)
    TextView user_name_tv;
    @Nullable
    @BindView(R.id.logout)
    Button logout;
    @BindView(R.id.statistics) TextView stats;
    @BindView(R.id.piechart)
    PieChart pieChart;
    @BindView(R.id.student_relative)
    RelativeLayout student_layout;
    @BindView(R.id.teacher_relative)
    RelativeLayout teacher_relative;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, parent, false);
        unbinder = ButterKnife.bind(this, view);



        user_id_tv.setText(String.valueOf(id()));
        user_email_tv.setText(String.valueOf(email()));
        user_name_tv.setText(String.valueOf(name()));

        Picasso.get().load(R.drawable.user_default).into(user_image);

        user_name_tv.setTextSize(28);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gilroy.otf");

        user_email_tv.setTypeface(font);
        user_name_tv.setTypeface(font);

        if(userType() == STUDENT) {
            student_layout.setVisibility(View.VISIBLE);
            initStudentStats();
        }else if(userType() == TEACHER){
            teacher_relative.setVisibility(View.VISIBLE);

        }

        if (logout != null) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   logout();
                }
            });
        }
        return view;
    }

    private void initStudentStats() {
        int right = Integer.valueOf(user_data.get(student.KEY_WRONG_ANSWERS_COUNTER));
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(Integer.valueOf(user_data.get(student.KEY_ANSWERS_COUNTER)),"Right answers"));
        entries.add(new PieEntry(Integer.valueOf(user_data.get(student.KEY_WRONG_ANSWERS_COUNTER)),"Wrong answers"));

        PieDataSet pieDataSet = new PieDataSet(entries,"");
        PieData data1 = new PieData();
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        data1.setDataSet(pieDataSet);
        Description d = new Description();
        d.setText("");

        if(userType() == STUDENT){
            stats.setText("Статистика: " + "\n");
            pieChart.setDescription(d);
            pieChart.setData(data1);
        }else{
            stats.setVisibility(View.GONE);
            pieChart.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}