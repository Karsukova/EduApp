package space.karsukova.educateapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import space.karsukova.educateapp.adapters.GroupPagerAdapterAdmin;

public class ViewGroupAdminActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private GroupPagerAdapterAdmin groupPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_admin);
        viewPager = (ViewPager) findViewById(R.id.tabPagerGroup);
        groupPagerAdapter = new GroupPagerAdapterAdmin(getSupportFragmentManager());

        viewPager.setAdapter(groupPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.groupTabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}