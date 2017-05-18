package com.example.mike.ecareapp.Fragments;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mike.ecareapp.Database.DatabaseHandler;
import com.example.mike.ecareapp.MainActivity;
import com.example.mike.ecareapp.Pojo.DoctorItem;
import com.example.mike.ecareapp.Pojo.PatientItem;
import com.example.mike.ecareapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    switch (mParam2) {
                        case 0:
                            if (email.getText().toString().contentEquals("") || passs.getText().toString().contentEquals("")) {
                                Snackbar.make(view, "Check your email or password", Snackbar.LENGTH_SHORT).show();
                            }else {
                                if (registerUser(view,email.getText().toString(),"https://footballticketing.000webhostapp.com/getPatient")){
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    intent.putExtra("name",gname);
                                    intent.putExtra("Id",gid);
                                    intent.putExtra("type", 1);
                                    startActivity(intent);

                                    getActivity().finish();
                                }else {
                                    Snackbar.make(view, "Something went completely wrong", Snackbar.LENGTH_SHORT).show();
                                }
                            }


                            break;
                        case 1:
                            if (email.getText().toString().contentEquals("") || passs.getText().toString().contentEquals("")) {
                                Snackbar.make(view, "Check your email or password", Snackbar.LENGTH_SHORT).show();
                            }else {
                                if (registerUser(view,email.getText().toString(),"https://footballticketing.000webhostapp.com/getDoctor")){
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    intent.putExtra("name",gname);
                                    intent.putExtra("Id",gid);
                                    intent.putExtra("type", 1);
                                    startActivity(intent);

                                    getActivity().finish();
                                }else {
                                    Snackbar.make(view, "Something went completely wrong", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                            break;
                    }
                }else {
                    Snackbar.make(view, "Please ensure that you have written both your email and password", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    Boolean status;
    private boolean setStatus(Boolean val){
        status = val;
        return status;
    }

    private boolean registerUser(final View view, final String useremail, String REGISTER_URL){


        StringRequest stringRequest = new StringRequest(REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                    String password = jsonObject1.getString("pat_password");
                    gname = jsonObject1.getString("pat_name");
                    gid = jsonObject1.getInt("pat_id");

                    if (passs.getText().toString().contentEquals(password)){
                        setStatus(true);
                    }else {
                        setStatus(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setStatus(false);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("doc_email",useremail);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        return status;
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
