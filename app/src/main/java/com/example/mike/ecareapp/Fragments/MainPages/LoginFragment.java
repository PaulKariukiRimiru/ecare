package com.example.mike.ecareapp.Fragments.MainPages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mike.ecareapp.Custom.ProcessUser;
import com.example.mike.ecareapp.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;
    private String gname;
    private int gid;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(int param1, int param2) {
        LoginFragment fragment = new LoginFragment();
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

    TextInputEditText email, passs;
     ProcessUser processUser;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_login, container, false);

        email = (TextInputEditText) view.findViewById(R.id.edEmail);
        passs = (TextInputEditText) view.findViewById(R.id.edPassword);

        ImageView imageView = (ImageView) view.findViewById(R.id.imgLogin);
        imageView.setImageResource(mParam1);

        AppCompatButton signin = (AppCompatButton) view.findViewById(R.id.btnSignIn);

        AppCompatTextView signup = (AppCompatTextView) view.findViewById(R.id.tvSignUp);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mParam2){
                    case 0:
                        Fragment registerPatient = RegisterFragment.newInstance(mParam1,mParam2);
                        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                        transaction1.replace(R.id.fragment,registerPatient);
                        transaction1.addToBackStack(null);
                        transaction1.commit();
                        break;
                    case 1:
                        Fragment registerDoctor = DoctorsRegisterFragment.newInstance(mParam1,mParam2);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment,registerDoctor);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                }

            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().isEmpty() && !passs.getText().toString().isEmpty()) {
                    ProcessUser processUser = new ProcessUser(getContext(), getActivity(), mParam2);
                    boolean stat = processUser.confirmDetails(email.getText().toString(),passs.getText().toString());

                }else {
                    Snackbar.make(view, "Please ensure that you have written both your email and password", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (processUser != null)
            processUser.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (processUser != null)
            processUser.onStop();
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
