package com.example.activityexamen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        conexion= new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
        listacontactos= (ListView) findViewById(R.id.listaContactos);
        id = (EditText) findViewById(R.id.txtcid);
        Button btnregresar = (Button)findViewById(R.id.btnregresar);



        Button btneliminar = (Button)findViewById(R.id.btneliminar);

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });



        ObtenerListaContactos();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloContactos);
        listacontactos.setAdapter(adp);

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
}