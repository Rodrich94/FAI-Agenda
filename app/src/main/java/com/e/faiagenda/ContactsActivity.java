package com.e.faiagenda;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactList recyclerviewAdapter;
    private RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        recyclerView = findViewById(R.id.contact_list);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        recyclerviewAdapter = new ContactList(this,getListContact());

        recyclerView.setAdapter(recyclerviewAdapter);

        final FloatingActionButton addContact = (FloatingActionButton) findViewById(R.id.add_contact);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact(v);
            }
        });

    }

    private ArrayList<Contact> getListContact(){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        SQLiteDatabase dbContacts = admin.getWritableDatabase();

        ArrayList<Contact> contacts = new ArrayList<>();

        Cursor tableContacts = dbContacts.rawQuery("select * from contact", null);

        if (tableContacts.moveToFirst()) {
            do {
                contacts.add(new Contact(tableContacts.getString(2), tableContacts.getString(1), tableContacts.getString(0)));
            } while (tableContacts.moveToNext());
        }

        return contacts;

    }

    public void addContact(View view){

        Intent intent = new Intent(this,AddContact.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerviewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


}
