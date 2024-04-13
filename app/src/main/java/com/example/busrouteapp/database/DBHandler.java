package com.example.busrouteapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper
{
    public static final String TB_NAME_BRT = "tb_BusRouteTransactions";

    public static final String COL_TRANSID = "transID";
    public static final String COL_DATE = "transDate";
    public static final String COL_TIME = "transTime";
    public static final String COL_ROUTE = "route";
    public static final String COL_CLIENT = "client";
    public static final String COL_STARTINGLOCATION = "startingLocation";
    public static final String COL_ENDINGLOCATION = "endingLocation";
    public static final String COL_TRAVELSCHEDDATE = "travelSchedDate";
    public static final String COL_TRAVELSCHEDTIME = "travelSchedTime";
    public static final String COL_BOOKINGSTATUS = "bookingStatus";
    public static final String COL_BOARDINGSTATUS = "boardingStatus";
    public static final String COL_SEATRESERVED = "seatReserved";
    public static final String COL_RESERVATIONBALANCE = "reservationBalance";

    public static final String TB_NAME_LOG = "tb_Logs";

    public static final String LOG_DATE = "col_date";
    public static final String LOG_TIME = "col_time";
    public static final String LOG_ACTION = "col_action";
    public static final String LOG_STATUS = "col_status";
    public static final String LOG_READER = "col_reader";
    public static final String LOG_QR_OWNER = "col_qrOwner";

    public static final String TB_NAME_ROUTES = "tb_Routes";

    public static final String RCOL_ROUTE_ID = "routeID";
    public static final String RCOL_STARTINGLOCATION = "startingLocation";
    public static final String RCOL_ENDINGLOCATION = "endingLocation";
    public static final String RCOL_TRAVELDATESCHED = "travelDateSched";
    public static final String RCOL_TRAVELTIMESCHED = "travelTimeSched";
    public static final String RCOL_PRICE = "price";
    public static final String RCOL_ROUTE_STATUS = "routeStatus";
    public static final String RCOL_MAXSLOT = "maxSlots";
    public static final String RCOL_REMAININGSLOT = "remainingSlots";
    public static final String RCOL_PASSENGERS = "passengers";

    public DBHandler(Context context) {super(context, "busRoute.db", null, 1);}

    @Override public void onCreate(SQLiteDatabase db)
    {
        String busRouteTransactions = "CREATE TABLE IF NOT EXISTS " + TB_NAME_BRT;
        String logs = "CREATE TABLE IF NOT EXISTS " + TB_NAME_BRT;
        String routes = "CREATE TABLE IF NOT EXISTS " + TB_NAME_ROUTES;
        db.execSQL(busRouteTransactions);
        db.execSQL(logs);
        db.execSQL(routes);
    }

    /////////////// ADD TO DATABASE ///////////////
    public boolean addTransaction(String transID, String transDate, String transTime, String route, String client, String startingLocation, String endingLocation, String travelSchedDate, String travelSchedTime, String bookingStatus, String boardingStatus, String seatReserved, String reservationBalance)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put();

        long data = database.insert(TB_NAME_BRT, null, values);
        if (data > 0) {return true;} else {return false;}
    }

    /////////////// GET FROM DATABASE ///////////////
    public List<DBSetterGetter> getTransaction()
    {
        List<DBSetterGetter> returnData = new ArrayList<>();
        String get_data = "SELECT * FROM " + TB_NAME_BRT + " ORDER BY " + COL_DATE + " ASC";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(get_data, null);

        if(cursor.moveToFirst())
        {
            do {
                String transID = cursor.getString(0);
                String transDate = cursor.getString(1);
                String transTime = cursor.getString(2);
                String route = cursor.getString(3);
                String client = cursor.getString(4);
                String startingLocation = cursor.getString(5);
                String endingLocation = cursor.getString(6);
                String travelSchedDate = cursor.getString(7);
                String travelSchedTime = cursor.getString(8);
                String bookingStatus = cursor.getString(9);
                String boardingStatus = cursor.getString(10);
                String seatReserved = cursor.getString(11);
                String reservationBalance = cursor.getString(12);

                DBSetterGetter input = new DBSetterGetter(transID, transDate, transTime, route, client, startingLocation, endingLocation, travelSchedDate, travelSchedTime, bookingStatus, boardingStatus, seatReserved, reservationBalance);
                returnData.add(input);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return returnData;
    }

    /////////////// DELETE A USER FROM DATABASE ///////////////
    /*
    public boolean deleteUserData(DBSetterGetter dbSetterGetter)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String delete = "DELETE FROM scores WHERE username = '" + dbSetterGetter.getUsername() + "'";
        Cursor cursor = database.rawQuery(delete, null);
        if(cursor.moveToFirst()) { return true; } else  { return false; }
    }
    */

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}