package com.example.mike.ecareapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mike.ecareapp.Adapter.MainAdapter;
import com.example.mike.ecareapp.Database.DatabaseHandler;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.Pojo.DoctorAppointmentItem;
import com.example.mike.ecareapp.Pojo.MainObject;
import com.example.mike.ecareapp.R;

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

    List<MainObject> appointmentList;
    MainAdapter adapter;
    private int range;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.viewAppointments);
        if (appointmentList == null){
            appointmentList = new ArrayList<>();
            if (prepareAppointments() != null)
                appointmentList = prepareAppointments();
        }
        adapter = new MainAdapter(getContext(),appointmentList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        Button refresh = (Button) view.findViewById(R.id.btnRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                range = Integer.parseInt(String.valueOf(mListener.getRange())) - appointmentList.size();
                appointmentList = prepareAppointments();
                for (int i =appointmentList.size()-range; i <= range ; i++ )
                    adapter.notifyItemInserted(i);
                adapter.notifyItemRangeChanged(appointmentList.size()-range, range);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        appointmentList = new ArrayList<>();
        if (prepareAppointments() != null)
            appointmentList = prepareAppointments();
    }

    public List<MainObject> prepareAppointments(){
        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        List<MainObject> mainObjectList = new ArrayList<>();

        switch (mParam1){
            case 0:
                for (AppiontmentItem appiontmentItem : databaseHandler.getPatientAppointment(mParam2))
                    mainObjectList.add(appiontmentItem);
                break;
            case 1:
                for (DoctorAppointmentItem appiontmentItem : databaseHandler.getDoctorAppointment(mParam2))
                    mainObjectList.add(appiontmentItem);
                break;
        }
        return mainObjectList;
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
