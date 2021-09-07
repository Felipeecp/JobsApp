package com.luiz.jobsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.helper.FirebaseConfig;
import com.luiz.jobsapp.model.Usuario;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button btnEntrar;
    private TextView cadastro;
    private LoginButton btnLoginFacebook;
    private static final String TAG = "FacebookAuthentication";
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;

    private CallbackManager mCallbackManager;

    private FirebaseAuth autenticacao;
    
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        inicializarComponentes();
        FacebookSdk.sdkInitialize(getApplicationContext());
        btnLoginFacebook.setReadPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        btnLoginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"onError" + error);

            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!= null){
                    updateUI(user);
                }else{updateUI(null);}

            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    autenticacao.signOut();
                }

            }
        };




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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        autenticacao.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            autenticacao.removeAuthStateListener(authStateListener);
        }

    }

    private void handleFacebookToken(AccessToken token) {

        Log.d(TAG, "handleFacebookToken" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        autenticacao.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG,"sign in with credential: successful");
                    FirebaseUser user = autenticacao.getCurrentUser();
                    updateUI(user);
                }else {
                    Log.d(TAG, "sign in with credential: failure", task.getException());
                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }

    private void updateUI( FirebaseUser user) {
        if(user != null){
            abrirHome();
        }
        else {}
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
        btnLoginFacebook = findViewById(R.id.login_button);


    }
}