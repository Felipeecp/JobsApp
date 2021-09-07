package com.luiz.jobsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.luiz.jobsapp.R;

public class PerfilActivity extends AppCompatActivity {

    private Button btnAdicionarDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        inicializarComponentes();

        btnAdicionarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilActivity.this, CadastroDadosPerfilActivity.class));
            }
        });
    }

    private void inicializarComponentes(){
        btnAdicionarDados = findViewById(R.id.btn_addDados);
    }
}