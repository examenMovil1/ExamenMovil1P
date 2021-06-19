package com.example.activityexamen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.activityexamen.transacciones.Transacciones;


public class MainActivity extends AppCompatActivity {
//HOLA QUE TAL
    EditText pais,nombre,telefono,nota;
    Spinner comboPais;

    public String paises;
    public String cptxt;
    public String cpaisesn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nombre = (EditText)findViewById(R.id.txtnombre);
        telefono = (EditText)findViewById(R.id.txttelefono);
        nota= (EditText)findViewById(R.id.txtnota);
        comboPais =(Spinner)findViewById(R.id.comboPais);

        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this,R.array.combo_pais, android.R.layout.simple_spinner_item);
        comboPais.setAdapter(adp);


        Button btn = (Button)findViewById(R.id.btnguardar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validar()){
                    AgregarContacto();
                }

            }
        });

        Button btnlista = (Button)findViewById(R.id.btnlista);
        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityListView.class);
                startActivity(intent);

            }
        });

    }
    public boolean validar(){
        boolean retorno= true;

        String nom= nombre.getText().toString();
        String tel= telefono.getText().toString();
        String nt= nota.getText().toString();

        if(nom.isEmpty()){
            nombre.setError("Campo nombre no puede quedar vacio");
            retorno = false;
        }
        if(tel.isEmpty()){
            telefono.setError("Campo telefono no puede quedar vacio");
            retorno = false;
        }
        if(nt.isEmpty()){
            nota.setError("Campo nota no puede quedar vacio");
            retorno = false;
        }

        return retorno;

    }

    public void recortar() {
        paises = comboPais.getSelectedItem().toString();
        cptxt = paises.substring(0, paises.length() - 5);
        cpaisesn = paises.substring(paises.length() - 4, paises.length() - 1);
    }

    private void AgregarContacto() {
        recortar();

        SQLiteConexion conexion = new SQLiteConexion(   this,Transacciones.NameDataBase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put(Transacciones.pais, cptxt );
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.telefono,cpaisesn + telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());

        Long resultado = db.insert(Transacciones.tablacontactos, Transacciones.id, valores);
        Toast.makeText(getApplicationContext(), "Registro Ingresado:" + resultado.toString(),Toast.LENGTH_LONG).show();

        db.close();
        ClearScreen();
    }
    private void ClearScreen()
    {
        comboPais.setSelection(0);
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
    }

}