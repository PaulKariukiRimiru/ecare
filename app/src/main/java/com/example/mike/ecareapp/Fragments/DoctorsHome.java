package com.example.mike.ecareapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mike.ecareapp.Adapter.DoctorsHomeAdapter;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.PatientItem;
import com.example.mike.ecareapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoctorsHome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoctorsHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorsHome extends Fragment implements NavigationInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<PatientItem> mainObjectList = new ArrayList<>();
    private DoctorsHomeAdapter mainAdapter;

    public DoctorsHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorsHome.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorsHome newInstance(String param1, String param2) {
        DoctorsHome fragment = new DoctorsHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView recyclerView;
    Spinner hospital, specialties;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_doctors_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.viewHomeItems);
        hospital = (Spinner) view.findViewById(R.id.spinnerHospital);
        specialties = (Spinner) view.findViewById(R.id.spinnerSpecialty);

        prepareObjects();

        return view;
    }

    private void prepareObjects() {
        String REGISTER_URL = "https://footballticketing.000webhostapp.com/doctor_patients.php?shed_docId="+mParam2;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, REGISTER_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Doctor shedule",response.toString());
                parsePatientData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }


    //This method will parse json data doctors patients only
    private void parsePatientData(final JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            PatientItem patientItem = new PatientItem();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                patientItem.setLocation(json.getString("location"));
                patientItem.setName(json.getString("name"));
                patientItem.setEmail(json.getString("email"));
                patientItem.setPat_id(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            mainObjectList.add(patientItem);
        }

        mainAdapter = new DoctorsHomeAdapter(getContext(),mainObjectList);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

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
    public void fragmentNavigation(Fragment fragment) {

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
