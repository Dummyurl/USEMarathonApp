package ru.use.marathon.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.fragments.StartQuestionFragment;
import ru.use.marathon.models.StartQuestion;

public class StartQuestionsActivity extends AppCompatActivity implements StartQuestionFragment.AnswerListener {

    public static final String TAG = StartQuestionsActivity.class.getSimpleName();
    private static int NUM_ITEMS = 6;
    MyPagerAdapter pagerAdapter;

    private TextView[] dots;
    public static int[] colors;
    public  static ArrayList<StartQuestion> dataList;

    @BindView(R.id.questions_vp) ViewPager viewPager;
    @BindView(R.id.layoutDots) LinearLayout dots_layout;
    @BindView(R.id.btn_next) Button next_btn;
    @BindView(R.id.btn_prev) Button prev_btn;
    StartQuestionFragment mContent;

    static ArrayList<Integer> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_questions);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mContent = (StartQuestionFragment) getSupportFragmentManager().getFragment(savedInstanceState, "start_question");
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        colors = new int[]{R.color.bg_screen1,R.color.bg_screen2,R.color.bg_screen3,R.color.bg_screen4,R.color.bg_screen5};

        changeStatusBarColor();

        answers = new ArrayList<>();

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        addBottomDots(0);
        prepareData();

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current < NUM_ITEMS) {
                    viewPager.setCurrentItem(current);
                } else {
                    startActivity(new Intent(StartQuestionsActivity.this,MainActivity.class));
                }
            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = viewPager.getCurrentItem();
                viewPager.setCurrentItem(current - 1);
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);


                if (position == NUM_ITEMS - 1) {
                    next_btn.setText(getString(R.string.question_end));
                }else if(position == 0){
                    next_btn.setText(getString(R.string.question_next));
                    next_btn.setVisibility(View.VISIBLE);
                    prev_btn.setVisibility(View.GONE);
                }else{
                    prev_btn.setVisibility(View.VISIBLE);
                    next_btn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "start_question", mContent);
    }

    private void prepareData(){

        dataList = new ArrayList<>();
        String[] data1 = new String[]{"от 40 до 50","от 50 до 70","больше 70"};
        String[] data2 = new String[]{"менее 10 минут","25 минут","40+ минут"};
        String[] data3 = new String[]{"Отлично!","Хорошо","Пойдет","Плохо"};
        String[] data4 = new String[]{"Юриспруденция","IT-специальность","Медицина","Техническая","Экономическая","Творческая"};

        dataList.add(new StartQuestion("Тут название вопроса1",data1)); //not important, but should be
        dataList.add(new StartQuestion("На какой балл ЕГЭ ты ориентирован?",data1));
        dataList.add(new StartQuestion("Сколько времени ты готов тратить на подготовку в день",data2));
        dataList.add(new StartQuestion("Насколько ты оцениваешь свои знания?",data3));
        dataList.add(new StartQuestion("На какую специальность ты планируешь поступать?",data4));


    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[NUM_ITEMS];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dots_layout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dots_layout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    @Override
    public void onAnswerFetched(int index, int position) {

        if(answers.get(index) != null){
            answers.remove(index);
        }
        answers.add(index,position);
        Log.d(TAG, "onAnswerFetched: answers:" + answers.toString());
    }


    public static class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return StartQuestionFragment.newInstance(position,colors[position],dataList.get(position));
        }

    }


}
