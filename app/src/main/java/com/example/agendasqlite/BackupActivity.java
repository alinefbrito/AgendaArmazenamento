package com.example.agendasqlite;

import android.Manifest;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BackupActivity extends AppCompatActivity {
    private DatabaseHelper mydb ;
    private boolean usesExternalStorage;
    SwitchCompat switcher;
    public static final String PREFERENCIAS_NAME = "com.example.agendasqlite";
    public static final String PREFERENCIAS_VALOR = "com.example.agendasqlite";
    public static final String BACKUP_CONTATOS = "backup_agenda_contatos";
    private static final int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        switcher = findViewById(R.id.switch1);
        mydb = new DatabaseHelper(this);
        recuperar();
    }
    public void switchClick(View v)
    {
        usesExternalStorage = switcher.isChecked();
        SharedPreferences settings = getSharedPreferences(PREFERENCIAS_NAME , 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREFERENCIAS_VALOR, usesExternalStorage);
        editor.apply();
    }
    private void recuperar()
    {

        SharedPreferences settings = getSharedPreferences(PREFERENCIAS_NAME ,MODE_PRIVATE);
        usesExternalStorage = settings.getBoolean(PREFERENCIAS_VALOR, false);
        switcher.setChecked(usesExternalStorage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.backup_menu, menu);
        return true;
    }
public void BackupInterno()
{
    try {
        ArrayList<Contato> c = mydb.getContactsList();
        File file =getFileStreamPath(BACKUP_CONTATOS);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(c);
        oos.close();
        fos.close();
        Toast.makeText(this, file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
public void BackupExterno()
{

    String state = Environment.getExternalStorageState();
    if (ActivityCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION);
    } else {

    if (Environment.MEDIA_MOUNTED.equals(state)) {
        // Podemos ler e escrever os meios de comunicação
        //mExternalStorageAvailable = mExternalStorageWriteable = true;
        try {
            ArrayList<Contato> lista = mydb.getContactsList();
           JSONArray jLista = new JSONArray();

            for (Contato c: lista
                 ) {
jLista.put( c.getJSONObject());
            }

File dir = Environment.getExternalStorageDirectory();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File subDir = new File (dir, String.valueOf(R.string.app_name));
            if (!subDir.exists()) {
                subDir.mkdirs();
            }
            String name= "backup_"+ LocalDateTime.now() + ".json";
            File f = new File(subDir, name);

            FileWriter fileWriter = new FileWriter(f);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jLista.toString());
            bufferedWriter.close();
            Toast.makeText(getApplicationContext(), f.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    else {

        Toast.makeText(getApplicationContext(), R.string.no_record_acces,
                Toast.LENGTH_SHORT).show();

    }
}}
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.do_backup) {
            if (usesExternalStorage) {
                BackupExterno();
            } else {
                BackupInterno();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {// Permissão garantida
            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                BackupExterno();
            } else {
                Toast.makeText(this,
                        R.string.perm_denied,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}