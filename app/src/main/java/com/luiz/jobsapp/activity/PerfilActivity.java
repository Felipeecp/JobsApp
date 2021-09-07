package com.luiz.jobsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.helper.FirebaseConfig;
import com.luiz.jobsapp.model.Dados;
import com.luiz.jobsapp.model.Servico;
import com.luiz.jobsapp.model.Usuario;

public class PerfilActivity extends AppCompatActivity {
    TextView txtNome, txtProfissao, txtFormacao, txtExperiencias, txtTempo;
    private Button btnAdicionarDados;
    private DatabaseReference perfilRef = FirebaseConfig.getFirebaseDatabase()
                                                            .getRef().child("usuarios");
    private FirebaseAuth usuario = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        inicializarComponentes();

        PegarDados();


        btnAdicionarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilActivity.this, CadastroDadosPerfilActivity.class));
            }
        });
    }

    private void inicializarComponentes(){

        btnAdicionarDados = findViewById(R.id.btn_addDados);
        txtNome = findViewById(R.id.txtNomeDoUsuario);
        txtProfissao = findViewById(R.id.txtv_profissao_usuario_valor);
        txtFormacao = findViewById(R.id.txtValorFormacaoDoUsuario);
        txtExperiencias = findViewById(R.id.txtValorExperienciasDoUsuario);
        txtTempo = findViewById(R.id.txtValorTempoUsuario);

    }



    private void PegarDados(){
        perfilRef.child(usuario.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario user = snapshot.getValue(Usuario.class);
                txtNome.setText(user.getNome());

                DatabaseReference refDados = perfilRef.child(usuario.getUid()).child("dados");
                adicionarDados(refDados);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void adicionarDados(DatabaseReference refDados){
        /* Tratar a imagem aqui */
        refDados.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    /* Tratar a imagem aqui */
                    btnAdicionarDados.setText("Atualizar dados");
                    Dados dadosUser = snapshot.getValue(Dados.class);
                    txtProfissao.setText(dadosUser.getProfissao());
                    txtFormacao.setText(dadosUser.getFormacao());
                    txtExperiencias.setText(dadosUser.getExperiencia());
                    txtTempo.setText(dadosUser.getTempo());
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Atenção, adicione seus dados",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

}