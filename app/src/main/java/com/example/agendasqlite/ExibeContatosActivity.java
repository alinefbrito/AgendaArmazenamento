package com.example.agendasqlite;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.xml.validation.Validator;

public class ExibeContatosActivity extends AppCompatActivity {
    private DatabaseHelper mydb;

    TextView name;
    TextView phone;
    TextView email;
    TextView street;
    TextView place;
    TextView id;
    Button btn;
    Contato contato;


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_contatos);
        id = findViewById(R.id.editTextID);
        id.setEnabled(false);
        name = findViewById(R.id.editTextName);
        phone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextStreet);
        street = findViewById(R.id.editTextEmail);
        place = findViewById(R.id.editTextCity);
        btn = findViewById(R.id.btnSalvar);
        mydb = new DatabaseHelper(this);
        btn.setOnClickListener(view -> salvar());
        contato = (Contato) getIntent().getSerializableExtra(MainActivity.EXTRA_VALUE);
        if (contato != null) {


            btn.setVisibility(View.INVISIBLE);
            id.setText(String.valueOf(contato.get_id()));
            id.setFocusable(false);
            id.setClickable(false);

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
        } else
            contato = new Contato();
    }
    //}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            getMenuInflater().inflate(R.menu.exibe_contato, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Edit_Contact:
                Button b = findViewById(R.id.btnSalvar);
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
                            mydb.deleteContact(contato.get_id());
                            Toast.makeText(getApplicationContext(), R.string.delete_ok,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    public void salvar() {
        contato.set_nome(name.getText().toString());
        contato.set_num_tel(phone.getText().toString());
        contato.set_email(email.getText().toString());
        contato.set_logradouro(street.getText().toString());
        contato.set_cidade(place.getText().toString());
        if (contato.get_id() > 0) {

            if (mydb.updateContact(contato)) {
                Toast.makeText(getApplicationContext(), "Atualizado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Não Atualizado", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (mydb.insertContact(contato)) {
                Toast.makeText(getApplicationContext(), "done",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "not done",
                        Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
