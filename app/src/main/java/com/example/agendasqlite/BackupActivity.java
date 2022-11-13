package com.example.agendasqlite;

import android.Manifest;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.LocaleData;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
        editor.commit();
    }
    private void recuperar()
    {

        SharedPreferences settings = getSharedPreferences(PREFERENCIAS_NAME , 0);
        usesExternalStorage = settings.getBoolean(PREFERENCIAS_VALOR, false);
        switcher.setChecked(usesExternalStorage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.backup_menu, menu);
        return true;
    }
public boolean BackupInterno()
{
    try {
        ArrayList<Contato> c = mydb.getContactsList();
        File file =getFileStreamPath(BACKUP_CONTATOS);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(fos);
        oos.writeObject(c);
        oos.close();
        fos.close();
        return  true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
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
            File subDir = new File (dir, "appSqlite");
            if (!subDir.exists()) {
                subDir.mkdirs();
            }
            String name= "backup_"+ LocalDateTime.now() + ".json";
            File arquivo = new File(subDir, name);

            FileWriter fileWriter = new FileWriter(arquivo);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jLista.toString());
            bufferedWriter.close();
            Toast.makeText(getApplicationContext(), arquivo.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    else {

        Toast.makeText(getApplicationContext(), "Sem acesso de Gravação",
                Toast.LENGTH_SHORT).show();

    }
}}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.do_backup:

              if(usesExternalStorage)
              {
BackupExterno();
              }
              else{
BackupInterno();

              }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                // Permissão garantida
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    BackupExterno();
                } else {
                    Toast.makeText(this,
                            "Permissão negada",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}