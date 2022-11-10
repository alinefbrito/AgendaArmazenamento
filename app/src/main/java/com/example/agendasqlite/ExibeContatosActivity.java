package com.example.agendasqlite;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExibeContatosActivity extends AppCompatActivity {
    private DatabaseHelper mydb ;

    TextView name ;
    TextView phone;
    TextView email;
    TextView street;
    TextView place;
    Contato contato;
    int id_To_Update = 0;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_contatos);
        name = findViewById(R.id.editTextName);
        phone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextStreet);
        street = findViewById(R.id.editTextEmail);
        place = findViewById(R.id.editTextCity);

        mydb = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();
contato = new Contato();
                contato.set_nome(rs.getString(rs.getColumnIndex(DatabaseHelper.CONTACTS_COLUMN_NAME)));
                contato.set_num_tel(rs.getString(rs.getColumnIndex(DatabaseHelper.CONTACTS_COLUMN_PHONE)));
                contato.set_email(rs.getString(rs.getColumnIndex(DatabaseHelper.CONTACTS_COLUMN_EMAIL)));
                contato.set_logradouro(rs.getString(rs.getColumnIndex(DatabaseHelper.CONTACTS_COLUMN_STREET)));
               contato.set_cidade(rs.getString(rs.getColumnIndex(DatabaseHelper.CONTACTS_COLUMN_CITY)));

                if (!rs.isClosed())  {
                    rs.close();
                }
                Button b = findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                name.setText(contato.get_nome());
                name.setFocusable(true);
                name.setClickable(false);

                phone.setText(contato.get_num_tel());
                phone.setFocusable(false);
                phone.setClickable(false);

                email.setText(contato.get_email());
                email.setFocusable(false);
                email.setClickable(false);

                street.setText(contato.get_logradouro());
                street.setFocusable(false);
                street.setClickable(false);

                place.setText(contato.get_cidade());
                place.setFocusable(false);
                place.setClickable(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.exibe_contato, menu);
            } else{
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Contact:
                Button b = findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);

                email.setEnabled(true);
                email.setFocusableInTouchMode(true);
                email.setClickable(true);

                street.setEnabled(true);
                street.setFocusableInTouchMode(true);
                street.setClickable(true);

                place.setEnabled(true);
                place.setFocusableInTouchMode(true);
                place.setClickable(true);

                return true;
            case R.id.Delete_Contact:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact)
                        .setPositiveButton(R.string.yes, (dialog, id) -> {
                            mydb.deleteContact(id_To_Update);
                            Toast.makeText(getApplicationContext(), R.string.delete_ok,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        })
                        .setNegativeButton(R.string.no, (dialog, id) -> {
                            // User cancelled the dialog
                        });

                AlertDialog d = builder.create();
                d.setTitle(R.string.deleteContact);
                d.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){

                if(mydb.updateContact(new Contato( id_To_Update,name.getText().toString(),
                        phone.getText().toString(), email.getText().toString(),
                        street.getText().toString(), place.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "Atualizado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "Não Atualizado", Toast.LENGTH_SHORT).show();
                }
            } else{
                if(mydb.insertContact(new Contato(name.getText().toString(), phone.getText().toString(),
                        email.getText().toString(), street.getText().toString(),
                        place.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }
}