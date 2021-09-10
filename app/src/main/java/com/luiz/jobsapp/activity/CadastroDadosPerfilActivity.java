package com.luiz.jobsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.helper.FirebaseConfig;
import com.luiz.jobsapp.helper.Permissoes;
import com.luiz.jobsapp.model.Usuario;

import java.util.HashMap;
import java.util.Map;

public class CadastroDadosPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView edtProfissao, edtFormacao, edtExperiencias, edtTempo;
    private Button btnSalvarDados;
    private ImageView imgPerfil;
    private String caminhoImagem;
    private Usuario usuario;


    private DatabaseReference perfilRef = FirebaseConfig.getFirebaseDatabase().getRef();
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private StorageReference storage = FirebaseConfig.getReferenciaStorage();

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_dados_perfil);

        this.inicializarComponentes();

        Permissoes.validarPermissoes(permissoes, this,1);

        btnSalvarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (caminhoImagem != null) {
                    salvarFoto(caminhoImagem);
                    finish();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Necessário uma imagem",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }


    private void salvarFoto(String caminhoImagem){

        final StorageReference imagemPerfil = storage
                .child("imagens")
                .child("fotosPerfil")
                .child(autenticacao.getUid())
                .child("imagem");


        UploadTask uploadTask = imagemPerfil.putFile(Uri.parse(this.caminhoImagem));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemPerfil.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri firebaseUrl = task.getResult();
                        String urlConvertida = firebaseUrl.toString();
                        if(urlConvertida != null){
                            String profissao = edtProfissao.getText().toString();
                            String formacao = edtFormacao.getText().toString();
                            String experiencia = edtExperiencias.getText().toString();
                            String tempo = edtTempo.getText().toString();

                            if (validarComponentes(profissao, formacao, experiencia, tempo)) {
                                salvarDados(profissao, formacao, experiencia, tempo, urlConvertida);
                                finish();
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Preencha todos os campos !!!",
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CadastroDadosPerfilActivity.this,
                        "Falha ao fazer upload",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void inicializarComponentes(){

        imgPerfil = findViewById(R.id.imgPerfilCadastro);
        edtProfissao = findViewById(R.id.edt_profissao);
        edtFormacao = findViewById(R.id.edt_formacao);
        edtExperiencias = findViewById(R.id.edt_experiencias);
        edtTempo = findViewById(R.id.edt_tempo);
        btnSalvarDados = findViewById(R.id.btn_salvar_dados);
        imgPerfil = findViewById(R.id.imgPerfilCadastro);
        imgPerfil.setOnClickListener(this);

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

    private void salvarDados(String profissao, String formacao, String experiencia, String tempo, String caminhoImagem){
        Map<String, Object> dadosParaSalvar = new HashMap<>();


        dadosParaSalvar.put("foto", caminhoImagem);
        dadosParaSalvar.put("profissao", profissao);
        dadosParaSalvar.put("formacao", formacao);
        dadosParaSalvar.put("experiencia", experiencia);
        dadosParaSalvar.put("tempo", tempo);

        perfilRef.child("usuarios").child(autenticacao.getUid())
                .child("dados")
                .setValue(dadosParaSalvar);
    }



    private void escolherImage(int requestCode) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            Uri imagemSelecionada = data.getData();
            caminhoImagem = imagemSelecionada.toString();

            if (requestCode == 2){
                imgPerfil.setImageURI(imagemSelecionada);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado: grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessario aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onClick(View v) {
        escolherImage(2);
    }
}