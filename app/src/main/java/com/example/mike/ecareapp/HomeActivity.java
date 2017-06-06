package com.example.mike.ecareapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mike.ecareapp.Fragments.SecondaryPages.AppointmentBookingFragment;
import com.example.mike.ecareapp.Fragments.MainPages.AppointmentFragment;
import com.example.mike.ecareapp.Fragments.SecondaryPages.DoctorAppoitmentSchedule;
import com.example.mike.ecareapp.Fragments.MainPages.HomeFragment;
import com.example.mike.ecareapp.Fragments.SecondaryPages.RescheduleFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,HomeFragment.OnFragmentInteractionListener, AppointmentFragment.OnFragmentInteractionListener, AppointmentBookingFragment.OnFragmentInteractionListener, RescheduleFragment.OnFragmentInteractionListener, DoctorAppoitmentSchedule.OnFragmentInteractionListener{


    private String name, id;
    private int type;
    private long new_range;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type",0);

        Fragment register = HomeFragment.newInstance(type,id);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.add(R.id.fragment,register);
        transaction1.addToBackStack(null);
        transaction1.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id1 = item.getItemId();

        if (id1 == R.id.nav_home) {
            // Handle the camera action
            Fragment register = HomeFragment.newInstance(type,id);
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            transaction1.replace(R.id.fragment,register);
            transaction1.addToBackStack(null);
            transaction1.commit();
        } else if (id1 == R.id.nav_appointments) {
            Fragment register = AppointmentFragment.newInstance(type,id);
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            transaction1.replace(R.id.fragment,register);
            transaction1.addToBackStack(null);
            transaction1.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
