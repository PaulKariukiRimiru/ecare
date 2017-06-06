package com.example.mike.ecareapp.Fragments.MainPages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.ecareapp.Custom.ProcessUser;
import com.example.mike.ecareapp.Custom.WorkerInterface;
import com.example.mike.ecareapp.Pojo.MainObject;
import com.example.mike.ecareapp.Pojo.PatientItem;
import com.example.mike.ecareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements WorkerInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    //myRef.setValue("Hello, World!");

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(int param1, int param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        mAuth = FirebaseAuth.getInstance();
    }

    TextInputEditText name, email, password;
    AppCompatSpinner locationSpinner;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_register, container, false);

        name = (TextInputEditText) view.findViewById(R.id.edNames);
        email = (TextInputEditText) view.findViewById(R.id.edEmail);
        password = (TextInputEditText) view.findViewById(R.id.edPassword);
        locationSpinner = (AppCompatSpinner) view.findViewById(R.id.location_spinner);

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

                    PatientItem patientItem = new PatientItem();
                    patientItem.setName(name.getText().toString());
                    patientItem.setEmail(email.getText().toString());
                    patientItem.setPassword(password.getText().toString());
                    patientItem.setLocation(locationSpinner.getSelectedItem().toString());

                    Log.d("item",name.getText().toString()+email.getText().toString()+password.getText().toString()+locationSpinner.getSelectedItem().toString());

                    registerUser(patientItem);
                    if (status){
                        Fragment login = LoginFragment.newInstance(mParam1,mParam2);
                        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                        transaction1.replace(R.id.fragment,login);
                        transaction1.addToBackStack(null);
                        transaction1.commit();
                    }
                }else
                    Snackbar.make(v,"Fill the form and try again",Snackbar.LENGTH_SHORT).show();

                try {
                    super.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
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



    private void registerUser(MainObject object){
        ProcessUser processUser = new ProcessUser(getContext(), getActivity(), this);
        processUser.createFirebaseUser(object);
    }
    Boolean status = false;
    private void setProgStatus(Boolean status){
        this.status = status;
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

    @Override
    public void isSuccessful(boolean success) {
        if (success){
            Fragment login = LoginFragment.newInstance(mParam1, mParam2);
            FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
            transaction1.replace(R.id.fragment, login);
            transaction1.addToBackStack(null);
            transaction1.commit();
        }else {
            Snackbar.make(view,"Try again please",Snackbar.LENGTH_SHORT).show();
        }
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
