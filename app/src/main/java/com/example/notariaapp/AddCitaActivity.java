package com.example.notariaapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddCitaActivity extends AppCompatActivity {

    private EditText etNotario, etSala, etFecha, etHora, etDescripcion;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cita);

        etNotario = findViewById(R.id.et_notario);
        etSala = findViewById(R.id.et_sala);
        etFecha = findViewById(R.id.et_fecha);
        etHora = findViewById(R.id.et_hora);
        etDescripcion = findViewById(R.id.et_descripcion);
        Button btnGuardar = findViewById(R.id.btn_guardar);

        dbHelper = new DatabaseHelper(this);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notario = etNotario.getText().toString();
                String sala = etSala.getText().toString();
                String fecha = etFecha.getText().toString();
                String hora = etHora.getText().toString();
                String descripcion = etDescripcion.getText().toString();

                if (notario.isEmpty() || sala.isEmpty() || fecha.isEmpty() || hora.isEmpty() || descripcion.isEmpty()) {
                    Toast.makeText(AddCitaActivity.this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean insert = dbHelper.insertCita(notario, sala, fecha, hora, descripcion);
                if (insert) {
                    showNotification("Cita Creada", "Tu cita ha sido registrada correctamente.");
                    Toast.makeText(AddCitaActivity.this, "Cita guardada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddCitaActivity.this, "Error al guardar la cita", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "cita_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Citas", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new Notification.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification)
                .build();

        notificationManager.notify(1, notification);
    }
}

