package com.example.mike.ecareapp.Fragments.MainPages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.mike.ecareapp.Adapter.PatientsHomeAdapter;
import com.example.mike.ecareapp.Fragments.SecondaryPages.AppointmentBookingFragment;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.DoctorItem;
import com.example.mike.ecareapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientsHome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientsHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientsHome extends Fragment implements NavigationInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PatientsHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientsHome.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientsHome newInstance(String param1, String param2) {
        PatientsHome fragment = new PatientsHome();
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
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patients_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.viewHomeItems);
        hospital = (Spinner) view.findViewById(R.id.spinnerHospital);
        specialties = (Spinner) view.findViewById(R.id.spinnerSpecialty);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshView);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainObjectList.clear();
                prepareObjects();
            }
        });
        prepareObjects();

        return view;
    }

    private void prepareObjects() {
        String REGISTER_URL = "https://footballticketing.000webhostapp.com/doctor.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, REGISTER_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("prepareHome: ", response.toString());
                parseDoctorData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("prepareHome: ", error.toString());
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    List<DoctorItem> mainObjectList = new ArrayList<>();

    PatientsHomeAdapter mainAdapter;

    //This method will parse json data
    private void parseDoctorData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            DoctorItem doctorItem = new DoctorItem();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                doctorItem.setDoc_id(json.getString("id"));
                doctorItem.setName(json.getString("name"));
                doctorItem.setEmail(json.getString("email"));
                doctorItem.setPassword(json.getString("password"));
                doctorItem.setHospital(json.getString("hospital"));
                doctorItem.setSpecialty(json.getString("specialty"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            mainObjectList.add(doctorItem);
        }

        mainAdapter = new PatientsHomeAdapter(getContext(),mainObjectList,this);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        swipeRefreshLayout.setRefreshing(false);
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
        FragmentManager manager = getFragmentManager();
        AppointmentBookingFragment appointmentBookingFragment = (AppointmentBookingFragment) fragment;
        appointmentBookingFragment.show(manager,"Appointments");
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
