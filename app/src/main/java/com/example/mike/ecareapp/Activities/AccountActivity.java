package com.example.mike.ecareapp.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.mike.ecareapp.Fragments.MainPages.DoctorsRegisterFragment;
import com.example.mike.ecareapp.Fragments.MainPages.LoginFragment;
import com.example.mike.ecareapp.Fragments.MainPages.RegisterFragment;
import com.example.mike.ecareapp.Fragments.SecondaryPages.WelcomeFragment;
import com.example.mike.ecareapp.R;

public class AccountActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, WelcomeFragment.OnFragmentInteractionListener, DoctorsRegisterFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        Fragment register = WelcomeFragment.newInstance("","");
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.add(R.id.fragment,register);
        transaction1.addToBackStack(null);
        transaction1.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
