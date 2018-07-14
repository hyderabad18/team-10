package com.cfg.iandeye.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.cfg.iandeye.R;
import com.cfg.iandeye.ViewPagerAdapter;

public class Admin_Dashboard extends AppCompatActivity{
    private ViewPager viewPager;
    private MenuItem prevMenuItem;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer__dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Saved Files");
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.uploadfiles:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.statistics:
                                viewPager.setCurrentItem(1);
                                break;

                        }

                        return  true;
                    }
                });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.i("page", "onPageSelected: "+position);
                if(position==0){
                    toolbar.setTitle("Saved Files");
                }
                else {
                    toolbar.setTitle("Online files");
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);



    }
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        AcceptBooksFragment acceptBooks = new AcceptBooksFragment();
        adapter.addFragment(acceptBooks);
       // adapter.addFragment(volunteer_statistics);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

    }



}
