package com.luiz.jobsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.luiz.jobsapp.R;

public class TermosECondicoesActivity extends AppCompatActivity {
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos_econdicoes);

        inicializarCampos();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(TermosECondicoesActivity.this, CadastroActivity.class));
            }
        });
    }

    private void inicializarCampos(){
        btnOk = findViewById(R.id.btn_ok);
    }
}