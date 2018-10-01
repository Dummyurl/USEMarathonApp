package ru.use.marathon.activities.auth;
/**
 * Created by Marat on 24-July-18.
 */
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.vk.sdk.util.VKUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.activities.AbstractActivity;
import ru.use.marathon.fragments.LoginStudentFragment;
import ru.use.marathon.fragments.LoginTeacherFragment;

public class LoginActivity extends AbstractActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.toolbar) Toolbar toolbar;
//    @BindView(R.id.tabs)
//    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.logo_text)
    TextView logo_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/gilroy.otf");

        logo_text.setTypeface(font);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginStudentFragment(), "");
//        adapter.addFragment(new LoginStudentFragment(), "Для ученика");
       // adapter.addFragment(new LoginTeacherFragment(), "Для учителя");
        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);


//        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
//        Log.d(TAG, "onCreate: " + Arrays.toString(fingerprints));

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
