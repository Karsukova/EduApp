package space.karsukova.educateapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import space.karsukova.educateapp.adapters.GroupPagerAdapterAdmin;
import space.karsukova.educateapp.adapters.GroupPagerAdapterUser;

public class ViewGroupUserActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private GroupPagerAdapterUser groupPagerAdapter;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_user);
        viewPager = (ViewPager) findViewById(R.id.tabPagerGroup);
        groupPagerAdapter = new GroupPagerAdapterUser(getSupportFragmentManager());

        viewPager.setAdapter(groupPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.groupTabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}