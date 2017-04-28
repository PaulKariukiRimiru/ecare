package com.example.mike.ecareapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mike.ecareapp.Pojo.Appiontment;
import com.example.mike.ecareapp.Pojo.Doctor;
import com.example.mike.ecareapp.Pojo.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mike on 4/28/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ecare";

    // table names
    private static final String TABLE_PATIENT = "patients";
    private static final String TABLE_DOCTOR = "doctors";
    private static final String TABLE_SHCEDULE = "schedules";

    // Table patient columns
    private static final String KEY_ID = "pat_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private  static final String KEY_PASSWORD = "password";
    private static final String KEY_LOCATION = "location";

    //Table doctor columns
    private static final String KEY_DOC_ID = "doc_id";
    private static final String KEY_DOC_NAME = "name";
    private static final String KEY_DOC_EMAIL = "email";
    private  static final String KEY_DOC_PASSWORD = "password";
    private  static final String KEY_DOC_HOSPITAL = "hospital";
    private static final String KEY_DOC_SPECIALTY = "specialty";

    //Table schedule columns
    private static final String KEY_SCHEDULE_ID = "shed_id";
    private static final String KEY_DOC_SHEDULE_ID ="shed_doc_id";
    private static final String KEY_PAT_SHEDULE_ID = "shed_pat_id";
    private static final String KEY_SCHEDULE_HOSPITAL = "shed_hospital";
    private static final String KEY_SCHEDULE_DATE = "date";
    private static final String KEY_SCHEDULE_TIME = "time";
    private static final String KEY_SCHEDULE_TREATMENT = "treatment";
    private static final String KEY_SHEDULE_STATUS = "status";

    private static final String KEY_CREATED_AT = "created_at";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENT_TABLE = "CREATE TABLE " + TABLE_PATIENT+ "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_PASSWORD + " TEXT,"
                +KEY_LOCATION + " TEXT" + ")";

        String CREATE_DOCTOR_TABLE = "CREATE TABLE " + TABLE_DOCTOR+ "("
                + KEY_DOC_ID + " INTEGER PRIMARY KEY," + KEY_DOC_NAME + " TEXT,"
                + KEY_DOC_EMAIL + " TEXT UNIQUE," + KEY_DOC_PASSWORD + " TEXT,"
                +KEY_DOC_HOSPITAL + " TEXT,"+KEY_DOC_SPECIALTY + " TEXT" + ")";
        String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_SHCEDULE+ "("
                + KEY_SCHEDULE_ID + " INTEGER PRIMARY KEY," + KEY_DOC_SHEDULE_ID + " TEXT,"
                + KEY_PAT_SHEDULE_ID + " TEXT UNIQUE," + KEY_SCHEDULE_HOSPITAL + " TEXT,"
                +KEY_SCHEDULE_DATE+ " TEXT,"+KEY_SCHEDULE_TIME + " TEXT,"+KEY_SHEDULE_STATUS + " TEXT,"
                +KEY_SCHEDULE_TREATMENT+" TEXT" + ")";


        db.execSQL(CREATE_PATIENT_TABLE);
        db.execSQL(CREATE_DOCTOR_TABLE);
        db.execSQL(CREATE_APPOINTMENTS_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHCEDULE);

        // Create tables again
        onCreate(db);
    }

    /*** Storing user details in database
     * */

    public void addPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, patient.getName()); // Name
        values.put(KEY_EMAIL, patient.getEmail()); // Email
        values.put(KEY_PASSWORD, patient.getPassword());
        values.put(KEY_LOCATION, patient.getLocation());

        // Inserting Row
        long id = db.insert(TABLE_PATIENT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New patient inserted into sqlite: " + id);
    }

    public void addDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DOC_NAME, doctor.getName()); // Name
        values.put(KEY_DOC_EMAIL, doctor.getEmail()); // Email
        values.put(KEY_DOC_PASSWORD, doctor.getPassword());
        values.put(KEY_DOC_HOSPITAL, doctor.getHospital());
        values.put(KEY_DOC_SPECIALTY, doctor.getSpecialty());

        // Inserting Row
        long id = db.insert(TABLE_DOCTOR, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New doctor inserted into sqlite: " + id);
    }

    public void addAppointment(Appiontment appiontment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DOC_SHEDULE_ID, appiontment.getDoc_id()); // Name
        values.put(KEY_PAT_SHEDULE_ID, appiontment.getPat_id()); // Email
        values.put(KEY_SCHEDULE_HOSPITAL, appiontment.getHospital());
        values.put(KEY_SCHEDULE_DATE, appiontment.getDate());
        values.put(KEY_SCHEDULE_TIME, appiontment.getTime());
        values.put(KEY_SCHEDULE_TREATMENT, appiontment.getTreatment());
        values.put(KEY_SHEDULE_STATUS, appiontment.getStatus());

        // Inserting Row
        long id = db.insert(TABLE_SHCEDULE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New appointment inserted into sqlite: " + id);
    }


    /**
     * Getting user data from database
     * */

    public List<Patient> getAllPatients() {
        List<Patient> patientList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PATIENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            Patient patient = new Patient();
            patient.setPat_id(String.valueOf(cursor.getInt(0)));
            patient.setName(cursor.getString(1));
            patient.setEmail(cursor.getString(2));
            patient.setPassword(cursor.getString(3));
            patient.setLocation(cursor.getString(4));
            patientList.add(patient);
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching patient from Sqlite: " + patientList.get(1).getName());

        return patientList;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctorsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DOCTOR;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            Doctor doctor = new Doctor();
            doctor.setDoc_id(String.valueOf(cursor.getInt(0)));
            doctor.setName(cursor.getString(1));
            doctor.setEmail(cursor.getString(2));
            doctor.setPassword(cursor.getString(3));
            doctor.setHospital(cursor.getString(4));
            doctor.setSpecialty(cursor.getString(5));
            doctorsList.add(doctor);
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching patient from Sqlite: " + doctorsList.get(1).getName());

        return doctorsList;
    }

    public List<Appiontment> getAllAppointment() {
        List<Appiontment> appiontmentsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SHCEDULE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            Appiontment appiontment = new Appiontment();
            appiontment.setAppoint_id(String.valueOf(cursor.getInt(0)));
            appiontment.setDoc_id(cursor.getString(1));
            appiontment.setPat_id(cursor.getString(2));
            appiontment.setHospital(cursor.getString(3));
            appiontment.setDate(cursor.getString(4));
            appiontment.setTime(cursor.getString(5));
            appiontment.setStatus(cursor.getString(6));
            appiontment.setTreatment(cursor.getString(7));
            appiontmentsList.add(appiontment);
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching patient from Sqlite: " + appiontmentsList.get(1).getTreatment());

        return appiontmentsList;
    }


    /**
     * Deleting data from database
     */

    public Integer deletePatient (Patient patient){
        SQLiteDatabase database = this.getWritableDatabase();
            return database.delete(TABLE_PATIENT,KEY_ID+" = ?", new String[]{patient.getPat_id()});
    }

    public Integer deleteDoctor (Doctor patient){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_DOCTOR,KEY_DOC_ID+" = ?", new String[]{patient.getDoc_id()});
    }

    public Integer deleteAppointment (Appiontment patient){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_SHCEDULE,KEY_SCHEDULE_ID+" = ?", new String[]{patient.getAppoint_id()});
    }

    /**
     * Modify a single object
     */

    public void modifyPatient(Patient patient){
        deletePatient(patient);
        addPatient(patient);
    }

    public void modifyDoctor(Doctor doctor){
        deleteDoctor(doctor);
        addDoctor(doctor);
    }

    public void modifyAppointment(Appiontment appiontment){
        deleteAppointment(appiontment);
        addAppointment(appiontment);
    }

}