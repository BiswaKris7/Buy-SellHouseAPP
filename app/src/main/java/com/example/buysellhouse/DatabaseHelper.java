package com.example.buysellhouse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HouseHunt.db";
    private static final int DATABASE_VERSION = 7;  // Incremented version to apply changes
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PROPERTIES = "Properties";
    private static final String TABLE_BUYER_PREFERENCES = "BuyerPreferences";

    // User table columns
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";


    private static final String COL_PROPERTY_ADDRESS = "PropertyAddress";
    private static final String COL_NUMBER_OF_BEDS = "NumberOfBeds";
    private static final String COL_NUMBER_OF_BATH = "NumberOfBath";
    private static final String COL_PROPERTY_TYPE = "PropertyType";
    private static final String COL_SQUARE_FEET = "SquareFeet";
    private static final String COL_ASKING_PRICE = "AskingPrice";

    private static final String COL_IMAGE_URI = "image_uri";

    // New columns for properties
    private static final String COL_FIRST_NAME = "FirstName";
    private static final String COL_LAST_NAME = "LastName";
    private static final String COL_PHONE_NUMBER = "PhoneNumber";
    private static final String COL_AREA_LOCATION = "AreaLocation";
    private static final String COL_SQUARE_FOOTAGE = "SquareFootage";
    private static final String COL_NUM_OF_BEDROOMS = "NumOfBedrooms";
    private static final String COL_NUM_OF_BATHROOMS = "NumOfBath";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        // Create properties table with additional columns
        String createPropertiesTable = "CREATE TABLE " + TABLE_PROPERTIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROPERTY_ADDRESS + " TEXT, " +
                COL_NUMBER_OF_BEDS + " TEXT, " +
                COL_NUMBER_OF_BATH + " TEXT, " +
                COL_PROPERTY_TYPE + " TEXT, " +
                COL_SQUARE_FEET + " TEXT, " +
                COL_ASKING_PRICE + " TEXT) ";
        db.execSQL(createPropertiesTable);

        String buyerPreferencesTable = "CREATE TABLE " + TABLE_BUYER_PREFERENCES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FIRST_NAME + " TEXT, " +
                COL_LAST_NAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PHONE_NUMBER + " TEXT, " +
                COL_NUM_OF_BEDROOMS + " TEXT, " +
                COL_NUM_OF_BATHROOMS + " TEXT, " +
                COL_SQUARE_FOOTAGE + " TEXT, " +
                COL_AREA_LOCATION + " TEXT, " +
                "PropertyTypes TEXT)";
        db.execSQL(buyerPreferencesTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROPERTIES);
            db.execSQL("CREATE TABLE " + TABLE_PROPERTIES + " (" +
                    COL_PROPERTY_ADDRESS + " TEXT, " +
                    COL_NUMBER_OF_BEDS + " TEXT, " +
                    COL_NUMBER_OF_BATH + " TEXT, " +
                    COL_PROPERTY_TYPE + " TEXT, " +
                    COL_SQUARE_FEET + " TEXT, " +
                    COL_ASKING_PRICE + " TEXT, " +
                    COL_IMAGE_URI + " TEXT)");
        }
    }




    // Add listing with new fields

    public String addListing(Property listing) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PROPERTY_ADDRESS, listing.getPropertyAddress());
        values.put(COL_NUMBER_OF_BEDS, listing.getBeds());
        values.put(COL_NUMBER_OF_BATH, listing.getBaths());
        values.put(COL_PROPERTY_TYPE, listing.getType());
        values.put(COL_SQUARE_FEET, listing.getArea());
        values.put(COL_ASKING_PRICE, listing.getPrice());
        values.put(COL_IMAGE_URI, listing.getUri());

        String result = db.insert(TABLE_PROPERTIES, null, values) == -1 ? "Error!!" : "Record Added Successfully!!";
        db.close();

        return result;
    }



    public String addBuyerPreferences(String firstName, String lastName, String email, String phoneNumber, String areaLocation, String numberOfBedrooms, String numberOfBathrooms, String squareFootage, String propertyTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FIRST_NAME, firstName);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE_NUMBER, phoneNumber);
        values.put(COL_AREA_LOCATION, areaLocation);
        values.put(COL_NUM_OF_BEDROOMS, numberOfBedrooms);
        values.put(COL_NUM_OF_BATHROOMS, numberOfBathrooms);
        values.put(COL_SQUARE_FOOTAGE, squareFootage);
        values.put("PropertyTypes", propertyTypes);  // Add PropertyTypes


        long result = db.insert(TABLE_BUYER_PREFERENCES, null, values);
        db.close();

        if (result == -1) {
            return "Error!!";
        }

        return "Record Added Successfully!!";

    }

    public boolean addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check for duplicate email
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_EMAIL}, COL_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount() > 0) {  // Email already exists
            cursor.close();
            db.close();
            return false;  // Return false to indicate failure
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_ID},
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password},
                null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, newPassword);

        int rowsAffected = db.update(TABLE_USERS, values, COL_EMAIL + "=?", new String[]{email});
        db.close();
        return rowsAffected > 0;
    }

    public List<Property> getAllProperties() {
        List<Property> propertiesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PROPERTIES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Property property = new Property(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PROPERTY_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NUMBER_OF_BEDS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NUMBER_OF_BATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PROPERTY_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SQUARE_FEET)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ASKING_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_URI)) // Retrieve the image URI (or handle if not available)
                );
                propertiesList.add(property);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return propertiesList;
    }

    public void deleteAllProperties() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_PROPERTIES, null, null); // Deletes all rows in the table
            db.setTransactionSuccessful(); // Mark the transaction as successful

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // End the transaction
            db.close(); // Close the database
        }
    }


}
