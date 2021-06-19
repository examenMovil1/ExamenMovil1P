package com.example.activityexamen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.example.activityexamen.contactos.Contactos;
import com.example.activityexamen.transacciones.Transacciones;

import java.util.ArrayList;

public class ActivityListView extends AppCompatActivity {

    //Variables globales de la actividad
    SQLiteConexion conexion;
    ListView listacontactos;
    EditText id;

    ArrayList<Contactos> lista;
    ArrayList<String> ArregloContactos;

    private String telefono;
    private static final int REQUEST_CALL = 1;
    private Boolean SelectedRow=false;


    private Button btnllamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        conexion= new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
        listacontactos= (ListView) findViewById(R.id.listaContactos);
        id = (EditText) findViewById(R.id.txtcid);

        Button btnregresar = (Button)findViewById(R.id.btnregresar);
        Button btneliminar = (Button)findViewById(R.id.btneliminar);
        Button btnactualizar = (Button)findViewById(R.id.btnactualizar);

        btnllamar = (Button)findViewById(R.id.btnllamar);


        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //actualizar();

            }
        });



        ObtenerListaContactos();

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, ArregloContactos);
        listacontactos.setAdapter(adp);


        listacontactos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listacontactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick (AdapterView<?> sele1, View selec2, int posicion, long select3){

                telefono = "+"+lista.get(posicion).getTelefono();
                SelectedRow = true;


               //BTNELIMINAR

                //btnllamarAqui


            }
        });



        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adp.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }







    private void ObtenerListaContactos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos listContactos= null;
        lista = new ArrayList<Contactos>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablacontactos, null);

        while (cursor.moveToNext()){
            listContactos = new Contactos();
            listContactos.setId(cursor.getInt(0));
            listContactos.setNombre(cursor.getString(2));
            listContactos.setTelefono(cursor.getString(3));

            lista.add(listContactos);

        }
        cursor.close();
        fillList();

    }
    private void fillList() {

        ArregloContactos = new ArrayList<String>();

        for (int i = 0;  i < lista.size(); i++){

            ArregloContactos.add(lista.get(i).getId() + " | "
                    +lista.get(i).getNombre() + " | "
                    +lista.get(i).getTelefono());

        }
    }


    private void mostrarnumero() {
        String numero = telefono;
        if (SelectedRow==true){
            if(ContextCompat.checkSelfPermission(ActivityListView.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ActivityListView.this,
                        new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String n = "tel:" + numero;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(n)));
            }
        }

        else{
            Toast.makeText(ActivityListView.this, "Seleccione Un Contacto", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mostrarnumero();
            }else{
                Toast.makeText(this, "NO TIENE ACCESO", Toast.LENGTH_SHORT).show();
            }
        }
    }
}