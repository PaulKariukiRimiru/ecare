package com.example.mike.ecareapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mike.ecareapp.Custom.Constants;
import com.example.mike.ecareapp.Adapter.AppointmentAdapter;
import com.example.mike.ecareapp.Adapter.DoctorsAppointmentAdapter;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.Pojo.DoctorAppointmentItem;
import com.example.mike.ecareapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentFragment extends Fragment implements NavigationInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    AppointmentAdapter adapter;
    private DoctorsAppointmentAdapter adapter2;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(int param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
         recyclerView = (RecyclerView) view.findViewById(R.id.viewAppointments);

        getAppointments(mParam2);



        return view;
    }

    private void setMainList(List<AppiontmentItem> mainObjectList) {
        this.mainObjectList = mainObjectList;
    }

    List<AppiontmentItem> mainObjectList = new ArrayList<>();
    public void getAppointments(final String id){

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        switch (mParam1){
            case 0:
                final List<AppiontmentItem> mainObjectList1 = new ArrayList<>();
                String url = Constants.SHEDULE_PATIENT_GET_URL+id;
                JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response appointment: ", response.toString());
                        for (int i = 0; i <response.length(); i++){
                            final AppiontmentItem appiontmentItem = new AppiontmentItem();

                            try {
                                JSONObject object = response.getJSONObject(i);
                                appiontmentItem.setAppoint_id("id");
                                appiontmentItem.setDate(object.getString("date"));
                                appiontmentItem.setTime(object.getString("time"));
                                appiontmentItem.setHospital(object.getString("hospital"));
                                appiontmentItem.setTreatment(object.getString("treatment"));
                                appiontmentItem.setPat_id(object.getString("pat_id"));
                                appiontmentItem.setDoc_id(object.getString("doc_id"));
                                appiontmentItem.setStatus(object.getString("status"));
                                mainObjectList1.add(appiontmentItem);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        adapter = new AppointmentAdapter(getContext(),mainObjectList1);

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(arrayRequest);
                setMainList(mainObjectList1);
                break;
            case 1:
                final List<DoctorAppointmentItem> mainObjectList2 = new ArrayList<>();

                String url1 = Constants.SHEDULE_DOCTOR_GET_URL+id;
                JsonArrayRequest arrayRequest2 = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DoctorAppointment", response.toString());
                        for (int i = 0; i <response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                DoctorAppointmentItem appiontmentItem = new DoctorAppointmentItem();
                                appiontmentItem.setAppoint_id("id");
                                appiontmentItem.setDate(object.getString("date"));
                                appiontmentItem.setTime(object.getString("time"));
                                appiontmentItem.setHospital(object.getString("hospital"));
                                appiontmentItem.setTreatment(object.getString("treatment"));
                                appiontmentItem.setPat_id(object.getString("pat_id"));
                                appiontmentItem.setDoc_id(object.getString("doc_id"));
                                appiontmentItem.setStatus(object.getString("status"));
                                mainObjectList2.add(appiontmentItem);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter2 = new DoctorsAppointmentAdapter(getContext(),mainObjectList2, AppointmentFragment.this);

                        recyclerView.setAdapter(adapter2);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(arrayRequest2);
                setMainList2(mainObjectList2);
                break;
            default:
                Log.d("Unknown id", id);
        }
    }

    private void setMainList2(List<DoctorAppointmentItem> mainObjectList2) {
        this.mainObjectList = mainObjectList;
    }

    @Override
    public void onStart() {
        super.onStart();

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
        DoctorAppoitmentSchedule appointmentBookingFragment = (DoctorAppoitmentSchedule) fragment;
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
        long getRange();
    }
}
