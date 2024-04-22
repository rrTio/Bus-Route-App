package com.example.busrouteapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.busrouteapp.database.DBHandler;
import com.example.busrouteapp.database.logSetterGetter;
public class logs extends AppCompatActivity {
    ListView list_Logs;
    ArrayAdapter arrayAdapter;
    DBHandler dbHandler;
    logSetterGetter logSetGet;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.log_details);

        dbHandler = new DBHandler(this);
        arrayAdapter = new ArrayAdapter<logSetterGetter>(logs.this, R.layout.custom_list_layout, dbHandler.getLogs());

        list_Logs = findViewById(R.id.listLogItems);
        list_Logs.setOnItemClickListener(new AdapterView.OnItemClickListener(){
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             }
         });
        list_Logs.setAdapter(arrayAdapter);
    }
}
