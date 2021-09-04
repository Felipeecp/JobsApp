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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.helper.FirebaseConfig;
import com.luiz.jobsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button btnEntrar;
    private TextView cadastro;

    private FirebaseAuth autenticacao;
    
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startActivity(new Intent(LoginActivity.this, HomeActivity.class));

        inicializarComponentes();

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(validarCamposLogin(email, senha)){
                    usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    validarLogin();
                }
            }
        });
    }

    private void validarLogin() {
        autenticacao = FirebaseConfig.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                abrirHome();
                            }
                        }else{
                            String excecao = "";
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Email e/ou senha não correspondem a um usuário cadastrado";
                            }catch (FirebaseAuthInvalidUserException e){
                                excecao = "Usuario não cadastrado";
                            }catch (Exception e){
                                excecao = "Erro ao fazer login: "+ e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(
                                    LoginActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                }
        );
    }

    private void abrirHome() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    private boolean validarCamposLogin(String email, String senha){
        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                return true;
            }else{
                Toast.makeText(
                        LoginActivity.this,
                        "Preencha o senha",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }else{
            Toast.makeText(
                    LoginActivity.this,
                    "Preencha o email",
                    Toast.LENGTH_SHORT
            ).show();
        }
        return false;
    }
    

    private void inicializarComponentes(){

        cadastro = findViewById(R.id.txtv_cadastrar_login);
        campoEmail = findViewById(R.id.edt_email_login);
        campoSenha = findViewById(R.id.edt_senha_login);
        btnEntrar = findViewById(R.id.btn_entrar_login);

    }
}