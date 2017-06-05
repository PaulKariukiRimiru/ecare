package com.example.mike.ecareapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.mike.ecareapp.Activities.AccountActivity;
import com.example.mike.ecareapp.Custom.ProcessUser;
import com.example.mike.ecareapp.Fragments.AppointmentBookingFragment;
import com.example.mike.ecareapp.Fragments.AppointmentFragment;
import com.example.mike.ecareapp.Fragments.DoctorAppoitmentSchedule;
import com.example.mike.ecareapp.Fragments.HomeFragment;
import com.example.mike.ecareapp.Fragments.RescheduleFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, AppointmentFragment.OnFragmentInteractionListener, AppointmentBookingFragment.OnFragmentInteractionListener, RescheduleFragment.OnFragmentInteractionListener, DoctorAppoitmentSchedule.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    String name, id;
    private long new_range;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type",0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        List<Fragment> fragments = new ArrayList<>();
        switch (type) {
            case 0:
                fragments.add(HomeFragment.newInstance(type, id));
                fragments.add(AppointmentFragment.newInstance(type, id));

                String [] titles = {"Home", "Appointments"};
                int [] images = {R.drawable.ic_home, R.drawable.ic_appointments};

                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments,titles, images,this);

                break;
            case 1:
                fragments.add(HomeFragment.newInstance(type, id));
                fragments.add(AppointmentFragment.newInstance(type, id));

                String [] titles2 = {"Patients","Appointments"};
                int [] images2 = {R.drawable.ic_home,R.drawable.ic_appointments};

                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments,titles2, images2,this);
                break;
        }


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_logout){
            ProcessUser processUser = new ProcessUser(this, this, null);
            processUser.signOutUser();
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public long getRange() {
        return new_range;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setRange(long id) {
        new_range = id;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private final Context context;
        List<Fragment> fragmentList;
        int [] images;
        String [] titles;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles, int[] images, Context context) {
            super(fm);
            this.fragmentList = fragmentList;
            this.context = context;

            this.titles = titles;
            this.images = images;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragmentList.size();
        }



        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            // Generate title based on item position
            Drawable image = ContextCompat.getDrawable(context, images[position]);
            //Drawable image = VectorDrawableCompat.create(context.getResources(),images[position],null);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            // Replace blank spaces with image icon
            SpannableString sb = new SpannableString("   "+"\n"+titles[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
    }
}
