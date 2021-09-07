package com.luiz.jobsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.helper.FirebaseConfig;

import java.util.HashMap;
import java.util.Map;

public class CadastroDadosPerfilActivity extends AppCompatActivity {

    TextView edtProfissao, edtFormacao, edtExperiencias, edtTempo;
    Button btnSalvarDados;
    private DatabaseReference perfilRef = FirebaseConfig.getFirebaseDatabase().getRef();
    private FirebaseAuth usuario = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_dados_perfil);

        this.inicializarComponentes();

        btnSalvarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profissao = edtProfissao.getText().toString();
                String formacao = edtFormacao.getText().toString();
                String experiencia = edtExperiencias.getText().toString();
                String tempo = edtTempo.getText().toString();

                if (validarComponentes(profissao, formacao, experiencia, tempo)) {
                    salvarDados(profissao, formacao, experiencia, tempo);
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Preencha todos os campos !!!",
                            Toast.LENGTH_LONG
                    ).show();;
                }
            }
        });
    }



    private void inicializarComponentes(){

        edtProfissao = findViewById(R.id.edt_profissao);
        edtFormacao = findViewById(R.id.edt_formacao);
        edtExperiencias = findViewById(R.id.edt_experiencias);
        edtTempo = findViewById(R.id.edt_tempo);
        btnSalvarDados = findViewById(R.id.btn_salvar_dados);

    }

    private boolean validarComponentes(String profissao, String formacao, String experiencia, String tempo){
        boolean valido = true;
        if (    profissao == null || profissao.equals("")
                && formacao == null || formacao.equals("")
                && experiencia == null || experiencia.equals("")
                && tempo == null || tempo.equals("")
        ) {
            valido = false;
        }
        return valido;
    }

    private void salvarDados(String profissao, String formacao, String experiencia, String tempo){
        Map<String, Object> dadosParaSalvar = new HashMap<>();

        dadosParaSalvar.put("profissao", profissao);
        dadosParaSalvar.put("formacao", formacao);
        dadosParaSalvar.put("experiencia", experiencia);
        dadosParaSalvar.put("tempo", tempo);

        perfilRef.child("usuarios").child(usuario.getUid())
                .child("dados")
                .setValue(dadosParaSalvar);
    }

}