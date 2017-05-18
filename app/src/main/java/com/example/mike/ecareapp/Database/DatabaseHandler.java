package com.example.mike.ecareapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.Pojo.DoctorAppointmentItem;
import com.example.mike.ecareapp.Pojo.DoctorItem;
import com.example.mike.ecareapp.Pojo.PatientItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 4/28/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

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
    private static final String KEY_DAY = "day";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";

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
                + KEY_PAT_SHEDULE_ID + " TEXT," + KEY_SCHEDULE_HOSPITAL + " TEXT,"
                +KEY_SHEDULE_STATUS + " TEXT,"+KEY_DAY + " TEXT,"+KEY_MONTH + " TEXT,"
                +KEY_YEAR + " TEXT,"+KEY_HOUR + " TEXT,"+KEY_MINUTE + " TEXT,"
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

    public void addPatient(PatientItem patientItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, patientItem.getName()); // Name
        values.put(KEY_EMAIL, patientItem.getEmail()); // Email
        values.put(KEY_PASSWORD, patientItem.getPassword());
        values.put(KEY_LOCATION, patientItem.getLocation());

        // Inserting Row
        long id = db.insert(TABLE_PATIENT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New patientitem inserted into sqlite: " + id);
    }

    public void addDoctor(DoctorItem doctorItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DOC_NAME, doctorItem.getName()); // Name
        values.put(KEY_DOC_EMAIL, doctorItem.getEmail()); // Email
        values.put(KEY_DOC_PASSWORD, doctorItem.getPassword());
        values.put(KEY_DOC_HOSPITAL, doctorItem.getHospital());
        values.put(KEY_DOC_SPECIALTY, doctorItem.getSpecialty());

        // Inserting Row
        long id = db.insert(TABLE_DOCTOR, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New doctorItem inserted into sqlite: " + id);
    }

    public long addAppointment(AppiontmentItem appiontment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DOC_SHEDULE_ID, appiontment.getDoc_id()); // Name
        values.put(KEY_PAT_SHEDULE_ID, appiontment.getPat_id()); // Email
        values.put(KEY_SCHEDULE_HOSPITAL, appiontment.getHospital());
        values.put(KEY_DAY, appiontment.getDay());
        values.put(KEY_MONTH, appiontment.getMonth());
        values.put(KEY_YEAR, appiontment.getYear());
        values.put(KEY_HOUR, appiontment.getHour());
        values.put(KEY_MINUTE, appiontment.getMinute());
        values.put(KEY_SCHEDULE_TREATMENT, appiontment.getTreatment());
        values.put(KEY_SHEDULE_STATUS, appiontment.getStatus());

        // Inserting Row
        long id = db.insert(TABLE_SHCEDULE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New appointment inserted into sqlite: " + id);
        return id;
    }


    /**
     * Getting user data from database
     * */

    public List<PatientItem> getAllPatients() {
        List<PatientItem> patientItemList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PATIENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.getCount() != 0){
        cursor.moveToFirst();
        do{
            PatientItem patientItem = new PatientItem();
            patientItem.setPat_id(String.valueOf(cursor.getInt(0)));
            patientItem.setName(cursor.getString(1));
            patientItem.setEmail(cursor.getString(2));
            patientItem.setPassword(cursor.getString(3));
            patientItem.setLocation(cursor.getString(4));
            patientItemList.add(patientItem);
        }while (cursor.moveToNext());

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching patients from Sqlite: " + patientItemList.size());

        return patientItemList;
        }
        else {
            Log.d(TAG, "No Patient items");
            return patientItemList;
        }
    }

    public List<DoctorItem> getAllDoctors() {
        List<DoctorItem> doctorsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DOCTOR;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            do{
            DoctorItem doctorItem = new DoctorItem();
            doctorItem.setDoc_id(String.valueOf(cursor.getInt(0)));
            doctorItem.setName(cursor.getString(1));
            doctorItem.setEmail(cursor.getString(2));
            doctorItem.setPassword(cursor.getString(3));
            doctorItem.setHospital(cursor.getString(4));
            doctorItem.setSpecialty(cursor.getString(5));
            doctorsList.add(doctorItem);
            }while (cursor.moveToNext());

            cursor.close();
            db.close();
            // return user
            Log.d(TAG, "Fetching doctors from Sqlite: " + doctorsList.size());

            return doctorsList;
        }
        else {
            Log.d(TAG, "No doctors items");
            return doctorsList;
        }
    }

    public List<AppiontmentItem> getAllAppointment() {
        List<AppiontmentItem> appiontmentsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SHCEDULE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            do{
                prepareList(appiontmentsList, cursor);
            }while (cursor.moveToNext());

            cursor.close();
            db.close();
            // return user
            Log.d(TAG, "Fetching appointments from Sqlite: " + appiontmentsList.size());

            return appiontmentsList;
        }
        else {
            Log.d(TAG, "No appointment items");
            return appiontmentsList;
        }
    }

    private void prepareList(List<AppiontmentItem> appiontmentsList, Cursor cursor) {
        AppiontmentItem appiontment = new AppiontmentItem();
        appiontment.setAppoint_id(String.valueOf(cursor.getInt(0)));
        appiontment.setDoc_id(cursor.getString(1));
        appiontment.setPat_id(cursor.getString(2));
        appiontment.setHospital(cursor.getString(3));
        appiontment.setStatus(cursor.getString(4));
        appiontment.setDay(cursor.getString(5));
        appiontment.setMonth(cursor.getString(6));
        appiontment.setYear(cursor.getString(7));
        appiontment.setHour(cursor.getString(8));
        appiontment.setMinute(cursor.getString(9));
        appiontment.setTreatment(cursor.getString(10));
        appiontmentsList.add(appiontment);
    }


    /**
     * Deleting data from database
     */

    public Integer deletePatient (PatientItem patientItem){
        SQLiteDatabase database = this.getWritableDatabase();
            return database.delete(TABLE_PATIENT,KEY_ID+" = ?", new String[]{patientItem.getPat_id()});
    }

    public Integer deleteDoctor (DoctorItem patient){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_DOCTOR,KEY_DOC_ID+" = ?", new String[]{patient.getDoc_id()});
    }

    public Integer deleteAppointment (AppiontmentItem patient){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_SHCEDULE,KEY_SCHEDULE_ID+" = ?", new String[]{patient.getAppoint_id()});
    }

    /**
     * Modify a single object
     */

    public void modifyPatient(PatientItem patientItem){
        deletePatient(patientItem);
        addPatient(patientItem);
    }

    public void modifyDoctor(DoctorItem doctorItem){
        deleteDoctor(doctorItem);
        addDoctor(doctorItem);
    }

    public void modifyAppointment(AppiontmentItem appiontment){
        deleteAppointment(appiontment);
        addAppointment(appiontment);
    }

    /**
     * get single object
     */

    public DoctorItem getDoctorItem(String id){
        String selectQuery = "SELECT  * FROM " + TABLE_DOCTOR + " WHERE "+ KEY_DOC_ID + " = " + Integer.parseInt(id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        DoctorItem doctorItem = new DoctorItem();
        doctorItem.setDoc_id(String.valueOf(cursor.getInt(0)));
        doctorItem.setName(cursor.getString(1));
        doctorItem.setEmail(cursor.getString(2));
        doctorItem.setPassword(cursor.getString(3));
        doctorItem.setHospital(cursor.getString(4));
        doctorItem.setSpecialty(cursor.getString(5));
        Log.d(TAG, "Fetching doctor from Sqlite: " + doctorItem.getName());
        return doctorItem;
    }

    public PatientItem getPatient(String id){
        String selectQuery = "SELECT  * FROM " + TABLE_PATIENT + " WHERE "+ KEY_ID + " = " + Integer.parseInt(id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            PatientItem patientItem = new PatientItem();
            patientItem.setPat_id(String.valueOf(cursor.getInt(0)));
            patientItem.setName(cursor.getString(1));
            patientItem.setEmail(cursor.getString(2));
            patientItem.setPassword(cursor.getString(3));
            patientItem.setLocation(cursor.getString(4));

            Log.d(TAG, "Fetching patient from Sqlite: " + patientItem.getName());
            return patientItem;
        }
        Log.d(TAG, "No patients");
        return new PatientItem();
    }

   /* public List<AppiontmentItem> getPatientAppointmets(String doc_id, String pat_id){
        String selectQuery = "SELECT  * FROM " + TABLE_SHCEDULE + " WHERE "+ KEY_DOC_SHEDULE_ID + " = " + doc_id;
        List<AppiontmentItem> appiontmentItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                if (cursor.getString(2).contentEquals(pat_id)){
                    AppiontmentItem appiontment = new AppiontmentItem();
                    appiontment.setAppoint_id(String.valueOf(cursor.getInt(0)));
                    appiontment.setDoc_id(cursor.getString(1));
                    appiontment.setPat_id(cursor.getString(2));
                    appiontment.setHospital(cursor.getString(3));
                    appiontment.setStatus(cursor.getString(4));
                    appiontment.setDay(cursor.getString(5));
                    appiontment.setMonth(cursor.getString(6));
                    appiontment.setYear(cursor.getString(7));
                    appiontment.setHour(cursor.getString(8));
                    appiontment.setMinute(cursor.getString(9));
                    appiontment.setTreatment(cursor.getString(10));
                    appiontmentItemList.add(appiontment);
                }
            }while (cursor.moveToNext());
            return appiontmentItemList;
        }
        return appiontmentItemList;
    }*/

    public List<AppiontmentItem> getPatientAppointmets(String doc_id){
        String selectQuery = "SELECT  * FROM " + TABLE_SHCEDULE + " WHERE "+ KEY_DOC_SHEDULE_ID + " = " + doc_id;
        List<AppiontmentItem> appiontmentItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                AppiontmentItem appiontment = new AppiontmentItem();
                appiontment.setAppoint_id(String.valueOf(cursor.getInt(0)));
                appiontment.setDoc_id(cursor.getString(1));
                appiontment.setPat_id(cursor.getString(2));
                appiontment.setHospital(cursor.getString(3));
                appiontment.setStatus(cursor.getString(4));
                appiontment.setDay(cursor.getString(5));
                appiontment.setMonth(cursor.getString(6));
                appiontment.setYear(cursor.getString(7));
                appiontment.setHour(cursor.getString(8));
                appiontment.setMinute(cursor.getString(9));
                appiontment.setTreatment(cursor.getString(10));
                appiontmentItemList.add(appiontment);
            }while (cursor.moveToNext());
            return appiontmentItemList;
        }
        return appiontmentItemList;
    }

    public List<DoctorAppointmentItem> getDoctorAppointment(String doc_id){
        String selectQuery = "SELECT  * FROM " + TABLE_SHCEDULE + " WHERE "+ KEY_DOC_SHEDULE_ID + " = " + doc_id;
        List<DoctorAppointmentItem> appiontmentItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                DoctorAppointmentItem appiontment = new DoctorAppointmentItem();
                appiontment.setAppoint_id(String.valueOf(cursor.getInt(0)));
                appiontment.setDoc_id(cursor.getString(1));
                appiontment.setPat_id(cursor.getString(2));
                appiontment.setHospital(cursor.getString(3));
                appiontment.setStatus(cursor.getString(4));
                appiontment.setDay(cursor.getString(5));
                appiontment.setMonth(cursor.getString(6));
                appiontment.setYear(cursor.getString(7));
                appiontment.setHour(cursor.getString(8));
                appiontment.setMinute(cursor.getString(9));
                appiontment.setTreatment(cursor.getString(10));
                appiontmentItemList.add(appiontment);
            }while (cursor.moveToNext());
            return appiontmentItemList;
        }
        return appiontmentItemList;
    }

    public AppiontmentItem getAppointment(String appoint_id){
        String selectQuery = "SELECT  * FROM " + TABLE_SHCEDULE + " WHERE "+ KEY_SCHEDULE_ID + " = " + Integer.parseInt(appoint_id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        AppiontmentItem appiontment = new AppiontmentItem();
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
                appiontment.setAppoint_id(String.valueOf(cursor.getInt(0)));
                appiontment.setDoc_id(cursor.getString(1));
                appiontment.setPat_id(cursor.getString(2));
                appiontment.setHospital(cursor.getString(3));
                appiontment.setStatus(cursor.getString(4));
                appiontment.setDay(cursor.getString(5));
                appiontment.setMonth(cursor.getString(6));
                appiontment.setYear(cursor.getString(7));
                appiontment.setHour(cursor.getString(8));
                appiontment.setMinute(cursor.getString(9));
                appiontment.setTreatment(cursor.getString(10));
            return appiontment;
        }
        return appiontment;
    }

    public List<AppiontmentItem> getPatientAppointment(String pat_id){
        String selectQuery = "SELECT  * FROM " + TABLE_SHCEDULE + " WHERE "+ KEY_PAT_SHEDULE_ID + " = " + pat_id;
        List<AppiontmentItem> appiontmentItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                AppiontmentItem appiontment = new AppiontmentItem();
                appiontment.setAppoint_id(String.valueOf(cursor.getInt(0)));
                appiontment.setDoc_id(cursor.getString(1));
                appiontment.setPat_id(cursor.getString(2));
                appiontment.setHospital(cursor.getString(3));
                appiontment.setStatus(cursor.getString(4));
                appiontment.setDay(cursor.getString(5));
                appiontment.setMonth(cursor.getString(6));
                appiontment.setYear(cursor.getString(7));
                appiontment.setHour(cursor.getString(8));
                appiontment.setMinute(cursor.getString(9));
                appiontment.setTreatment(cursor.getString(10));
                appiontmentItemList.add(appiontment);
            }while (cursor.moveToNext());
            Log.d(TAG, "Fetching patient appointments from Sqlite: " + appiontmentItemList.size());
            return appiontmentItemList;
        }
        Log.d(TAG, "No Patient appointments");
        return appiontmentItemList;
    }
}