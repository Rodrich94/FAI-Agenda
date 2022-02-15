package com.e.faiagenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                queryBirthday(dayOfMonth,month);
            }

        });
    }

    private void queryBirthday (int dayOfMonth,int month){

        String date = "'%-"+(month+1)+"-"+dayOfMonth+"'";

        TextView cumpleañeros = (TextView) findViewById(R.id.cumpleañeros);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        final SQLiteDatabase dbContacts = admin.getWritableDatabase();

        ArrayList<Contact> contacts = new ArrayList<>();

        Cursor tableContacts = dbContacts.rawQuery("select * from contact where birth like "+date, null);

        if(tableContacts.moveToFirst()){
            do {
                contacts.add(new Contact(tableContacts.getString(2), tableContacts.getString(1), tableContacts.getString(0)));
            } while (tableContacts.moveToNext());
            String pibes = getString(R.string.calendar_text);
            for (int i=0; i<contacts.size(); i++){
                pibes = pibes +"\n\n"+getString(R.string.names)+" "+contacts.get(i).getName()+" - "+getString(R.string.telephones)+" "+
                        contacts.get(i).getTelephone()+
                "\n"+getString(R.string.birthday)+" "+contacts.get(i).getBirth();
            }
            cumpleañeros.setText(pibes);
        }else{
            cumpleañeros.setText("");
            Toast.makeText(getApplicationContext(), getString(R.string.toast_birth)+" "+dayOfMonth+"/"+month, Toast.LENGTH_SHORT).show();
        }
        dbContacts.close();
    }
}
