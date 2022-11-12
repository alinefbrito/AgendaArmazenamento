package com.example.agendasqlite;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper mydb;
    private TextView text_empty;
    private ListView obj;
    public static final String EXTRA_VALUE ="com.example.agendasqlite.EXTRA_VALUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_empty = findViewById(R.id.textViewNoData);
        obj = findViewById(R.id.listView1);
        mydb = new DatabaseHelper(this);
        atualizaLista();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.item1:
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(), ExibeContatosActivity.class);
                intent.putExtras(dataBundle);

                startActivity(intent);
                return true;
            case R.id.item2:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteAll)
                        .setPositiveButton(R.string.yes, (dialog, id) -> {
                            mydb.deleteAll();
                            Toast.makeText(getApplicationContext(), R.string.delete_ok,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent1);
                        })
                        .setNegativeButton(R.string.no, (dialog, id) -> {
                            // Não faz nada
                        });

                AlertDialog d = builder.create();
                d.setTitle(R.string.deleteContact);
                d.show();
                atualizaLista();
                return true;
            case R.id.item3:
                Intent it = new Intent(getApplicationContext(), BackupActivity.class);
                startActivity(it);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    public void atualizaLista() {

      if ( mydb.numberOfRows() >0){
        ArrayList<Contato> array_list = mydb.getContactsList();

        if (array_list.isEmpty()) {
            text_empty.setVisibility(View.VISIBLE);
            obj.setVisibility(View.GONE);

        } else {
            text_empty.setVisibility(View.GONE);

            ContatoArrayAdapter arrayAdapter = new ContatoArrayAdapter(this, array_list);

            obj.setAdapter(arrayAdapter);
            obj.setOnItemClickListener((parent, view, position, id) -> {
                Contato c = (Contato) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ExibeContatosActivity.class);
                intent.putExtra(EXTRA_VALUE,c);
                startActivity(intent);
            });
        }


            obj.setVisibility(View.VISIBLE);
        }}
    }
