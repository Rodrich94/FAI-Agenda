package com.e.faiagenda;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactList extends RecyclerView.Adapter implements Filterable {

    private Context context;
    private ArrayList<Contact> listContacts;
    private ArrayList<Contact> listFilter;

    public ContactList(Context context, ArrayList<Contact> listContacts) {
        this.context = context;
        this.listFilter = listContacts;
        this.listContacts = new ArrayList<>(listContacts);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View contentView = LayoutInflater.from(context).inflate(R.layout.listview_layout,null);

        return new Holder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        Contact contact = listFilter.get(position);
        final Holder holder1 = (Holder) holder;

        holder1.tv_name.setText(contact.getName());
        holder1.tv_tel.setText(contact.getTelephone());
        holder1.iv_img.setImageResource(R.drawable.ic_contact);

        String data = contact.getName()+"#"+contact.getTelephone();

        holder1.ib_delete.setTag(data);

        holder1.ib_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContactFromList(v,position,v.getTag().toString());
            }

        });

        holder1.ib_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callContact(holder1.tv_tel.getText().toString());
            }
        });

    }

    private void callContact(String telephone){


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED)
        {

            context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+telephone)));
        } else {
            Toast.makeText(context, context.getString(R.string.toast_call), Toast.LENGTH_SHORT).show();
        }

    }



    private void deleteContactFromList(View v, final int position,String data) {

        String[] parts = data.split("#");
        final String name, tel;

        name = "'"+parts[0]+"'                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              ";
        tel = parts[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        //builder.setTitle("Dlete ");
        builder.setMessage(context.getString(R.string.toast_delete))
                .setNegativeButton(context.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .setPositiveButton(context.getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        listFilter.remove(position);

                        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context,"administracion",null,1);
                        SQLiteDatabase dbContacts = admin.getWritableDatabase();


                        dbContacts.delete("contact","name="+name+"and telephone = "+tel,null);
                        dbContacts.close();

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, listFilter.size());

                        Toast.makeText(context,context.getString(R.string.toast_delete_confirmed),Toast.LENGTH_SHORT).show();
                    }
                });

        builder.show();

    }

    @Override
    public int getItemCount() {
        return listFilter.size();
    }

    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    private Filter contactFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Contact> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listContacts);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Contact contact: listContacts) {
                    if (contact.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(contact);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listFilter.clear();
            listFilter.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public static class Holder extends RecyclerView.ViewHolder{

        TextView tv_name;
        TextView tv_tel;
        ImageView iv_img;
        ImageButton ib_delete;
        ImageButton ib_call;


        public Holder(@NonNull final View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.img_contact);
            tv_name = (TextView) itemView.findViewById(R.id.name);
            tv_tel = (TextView) itemView.findViewById(R.id.tel);
            ib_delete = (ImageButton) itemView.findViewById(R.id.delete);
            ib_call = (ImageButton) itemView.findViewById(R.id.call_contact);
        }
    }


}

