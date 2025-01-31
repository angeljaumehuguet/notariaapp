package com.example.notariaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ListCitasActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewCitas;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> citasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_citas);

        listViewCitas = findViewById(R.id.listViewCitas);
        dbHelper = new DatabaseHelper(this);

        citasList = dbHelper.getAllCitas();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, citasList);
        listViewCitas.setAdapter(adapter);

        listViewCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCita = citasList.get(position);
                String descripcion = selectedCita.split("\n")[5].split(": ")[1]; // Extrae la descripción
                showDeleteDialog(descripcion, position);
            }
        });
    }

    private void showDeleteDialog(final String descripcion, final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Cita")
                .setMessage("¿Estás seguro de que quieres eliminar esta cita?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteCita(descripcion);
                        citasList.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ListCitasActivity.this, "Cita eliminada", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}