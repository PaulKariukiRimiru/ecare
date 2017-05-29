package com.example.mike.ecareapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mike.ecareapp.Custom.ProcessUser;
import com.example.mike.ecareapp.Database.DatabaseHandler;
import com.example.mike.ecareapp.MainActivity;
import com.example.mike.ecareapp.Pojo.DoctorItem;
import com.example.mike.ecareapp.Pojo.MainObject;
import com.example.mike.ecareapp.Pojo.PatientItem;
import com.example.mike.ecareapp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoctorsRegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoctorsRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorsRegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String REGISTER_URL = "https://footballticketing.000webhostapp.com/doctor_insert.php";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;

    private OnFragmentInteractionListener mListener;

    public DoctorsRegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorsRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorsRegisterFragment newInstance(int param1, int param2) {
        DoctorsRegisterFragment fragment = new DoctorsRegisterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    TextInputEditText name, email, password;

    android.support.v7.widget.AppCompatSpinner hospital_spinner, specialty_spinner;;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_doctors_register, container, false);

        name  = (TextInputEditText) view.findViewById(R.id.edNames);
        email = (TextInputEditText) view.findViewById(R.id.edEmail);
        password = (TextInputEditText) view.findViewById(R.id.edPassword);

        hospital_spinner = (android.support.v7.widget.AppCompatSpinner) view.findViewById(R.id.spinnerHospital);
        specialty_spinner = (android.support.v7.widget.AppCompatSpinner) view.findViewById(R.id.spinnerSpecialty);

        android.support.v7.widget.AppCompatTextView signin = (android.support.v7.widget.AppCompatTextView) view.findViewById(R.id.tvSignIn);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment login = LoginFragment.newInstance(mParam1,mParam2);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment,login);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        AppCompatButton signup = (AppCompatButton) view.findViewById(R.id.btnSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateForm()){
                    DoctorItem doctorItem = new DoctorItem();
                    doctorItem.setName(name.getText().toString());
                    doctorItem.setEmail(email.getText().toString());
                    doctorItem.setPassword(password.getText().toString());
                    doctorItem.setHospital(hospital_spinner.getSelectedItem().toString());
                    doctorItem.setSpecialty(specialty_spinner.getSelectedItem().toString());

                    if (registerUser(doctorItem)){
                    Fragment login = LoginFragment.newInstance(mParam1,mParam2);
                    FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                    transaction1.replace(R.id.fragment,login);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    }else {
                        Snackbar.make(v,"Try again please",Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private boolean validateForm() {
        boolean valid = true;

        String emailT = email.getText().toString();
        if (TextUtils.isEmpty(emailT)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String passwordT = password.getText().toString();
        if (TextUtils.isEmpty(passwordT)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }



    private boolean registerUser(MainObject object){
        ProcessUser processUser = ProcessUser.getNewInstance(getContext(), getActivity());
        return processUser.createFirebaseUser(object);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
