package com.e.faiagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddContact extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year;
    private int month;
    private int dayOfMonth;

    private EditText et_name,et_telephone, et_birth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final EditText editText = (EditText) findViewById(R.id.userdate);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(AddContact.this, AlertDialog.THEME_HOLO_DARK    , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editText.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        et_birth = (EditText) findViewById(R.id.userdate);
        et_name = (EditText) findViewById(R.id.username);
        et_telephone = (EditText) findViewById(R.id.userphone);

    }

    public void addContacts(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        SQLiteDatabase dbContacts = admin.getWritableDatabase();

        String name = et_name.getText().toString();
        String telephone = et_telephone.getText().toString();
        String birth = et_birth.getText().toString();

        if(!name.isEmpty() && !telephone.isEmpty()){
            ContentValues registro = new ContentValues();
            if(birth != null && !birth.equals("")){
                //Cambia formato de la fecha a formato de sqlite
                birth = dateFormat(birth);
            }else{
                birth = null;
            }
            registro.put("name",name);
            registro.put("telephone",telephone);
            registro.put("birth",birth);
            Cursor tableContacts = dbContacts.rawQuery("select * from contact where telephone="+telephone, null);

            if(!tableContacts.moveToFirst()){
                dbContacts.insert("contact",null, registro);
                dbContacts.close();
            }else{
                Toast.makeText(this, getString(R.string.existing_contact), Toast.LENGTH_LONG).show();
            }

            et_birth.setText("");
            et_name.setText("");
            et_telephone.setText("");
            Toast.makeText(this, getString(R.string.toast_add_contact), Toast.LENGTH_LONG).show();
            openContacts(view);
        } else {
            Toast.makeText(this,getString(R.string.toast_fields),Toast.LENGTH_SHORT).show();
        }
        dbContacts.close();


    }

    public void openContacts(View view){
        Intent intent = new Intent(this,ContactsActivity.class);
        startActivity(intent);
    }

    private String dateFormat(String date){
        String[] parts = date.split("-");
        String day, month, year;

        day = parts[0];
        month = parts[1];
        year = parts[2];

        return year+"-"+month+"-"+day;
    }
}
