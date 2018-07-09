package ru.use.marathon.fragments.navigation.student;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.use.marathon.R;
import ru.use.marathon.activities.LoginActivity;
import ru.use.marathon.models.Student;

/**
 * Created by ilyas on 10-Jun-18.
 */

public class SNavProfileFragment extends Fragment {

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

    public SNavProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_nav_home, parent, false);
        unbinder = ButterKnife.bind(this, view);

        final Student s = new Student(getActivity().getApplicationContext());
        HashMap<String, String> data = s.getData();
        user_id_tv.setText(data.get("id"));
        user_email_tv.setText(data.get("email"));
        user_name_tv.setText(data.get("name"));
        Picasso.get().load(R.drawable.user_default).into(user_image);

        user_name_tv.setTextSize(28);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gilroy.otf");

        user_email_tv.setTypeface(font);
        user_name_tv.setTypeface(font);

        int right = Integer.valueOf(data.get(s.KEY_WRONG_ANSWERS_COUNTER));
        stats.setText("Статистика: " + "\n");

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(Integer.valueOf(data.get(s.KEY_ANSWERS_COUNTER)),"Right answers"));
        entries.add(new PieEntry(Integer.valueOf(data.get(s.KEY_WRONG_ANSWERS_COUNTER)),"Wrong answers"));

        PieDataSet pieDataSet = new PieDataSet(entries,"");
        PieData data1 = new PieData();
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        data1.setDataSet(pieDataSet);
        Description d = new Description();
        d.setText("");
        pieChart.setDescription(d);
        pieChart.setData(data1);

        if (logout != null) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    s.login(false);
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            });
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}