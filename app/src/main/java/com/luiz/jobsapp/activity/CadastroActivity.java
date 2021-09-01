package com.luiz.jobsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.helper.FirebaseConfig;
import com.luiz.jobsapp.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button btnCadastro;
    private TextView btnTermosCondicoes;

    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarCampos();

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = campoNome.getText().toString();
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (validarCampos(nome,email, senha)){
                    usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    cadastrarUsuario();
                }

            }
        });
        btnTermosCondicoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(CadastroActivity.this,TermosECondicoesActivity.class));

            }
        });
    }

    private void cadastrarUsuario() {
        FirebaseAuth autenticacao = FirebaseConfig.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    String excecao = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um email valido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Conta já cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(
                            CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private boolean validarCampos(String nome, String email, String senha) {
        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!senha.isEmpty()){
                    return true;
                }else{
                    Toast.makeText(
                            CadastroActivity.this,
                            "Preencha o campo senha",
                            Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(
                        CadastroActivity.this,
                        "Preencha o campo email",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(
                    CadastroActivity.this,
                    "Preencha o campo nome",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void inicializarCampos(){
        campoNome = findViewById(R.id.edt_cadastro_nome);
        campoEmail = findViewById(R.id.edt_cadastro_email);
        campoSenha = findViewById(R.id.edt_cadastro_senha);
        btnCadastro = findViewById(R.id.btn_cadastrar);
        btnTermosCondicoes = findViewById(R.id.txtv_termos_condicoes);
    }
}