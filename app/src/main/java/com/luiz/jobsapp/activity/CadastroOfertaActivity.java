package com.luiz.jobsapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.helper.FirebaseConfig;
import com.luiz.jobsapp.helper.Permissoes;
import com.luiz.jobsapp.model.Servico;
import com.santalu.maskara.widget.MaskEditText;

import java.util.Locale;

public class CadastroOfertaActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editNome, editDescricao, editLocal, campoQtdVagas;
    private CurrencyEditText campoValor;
    private MaskEditText campoTelefone;
    private Button btnCadastrar;
    private Spinner campoTempo, campoArea;
    private ImageView imagemServico;
    private Servico servico;
    private String caminhoImagem;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_oferta);

        storage = FirebaseConfig.getReferenciaStorage();

        Permissoes.validarPermissoes(permissoes, this, 1);

        inicializarComponentes();

        carregarDadosSpinner();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();
            }
        });
    }

    private Servico configurarServico(){
        String telefone = "";
        String local = editLocal.getText().toString();
        String area = campoArea.getSelectedItem().toString();
        String titulo = editNome.getText().toString();
        String valor = campoValor.getText().toString();
        String qtdVagas = campoQtdVagas.getText().toString();
        String tempo = campoTempo.getSelectedItem().toString();
        String descricao = editDescricao.getText().toString();

        if(campoTelefone.getUnMasked() != null){
            telefone = campoTelefone.getText().toString();
        }

        servico = new Servico();
        servico.setLocal(local);
        servico.setArea(area);
        servico.setTitulo(titulo);
        servico.setSalario(valor);
        servico.setQtdVagas(qtdVagas);
        servico.setTempo(tempo);
        servico.setDescricao(descricao);
        servico.setTelefone(telefone);

        return servico;

    }

    private void salvarOferta() {
        salvarFotoStorage(caminhoImagem);
    }


    private void salvarFotoStorage(String caminhoImagem){

        final StorageReference imagemServico = storage
                .child("imagens")
                .child("serviços")
                .child(servico.getIdServico())
                .child("imagem");

        UploadTask uploadTask = imagemServico.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagemServico.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri firebaseUrl = task.getResult();
                        String urlConvertida = firebaseUrl.toString();
                        if(urlConvertida != null){
                            servico.setFoto(urlConvertida);
                            servico.salvar();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CadastroOfertaActivity.this,
                        "Falha ao fazer upload",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void inicializarComponentes() {
        editNome = findViewById(R.id.edi_nome_oferta);
        editDescricao = findViewById(R.id.edi_descricao_oferta);
        editLocal = findViewById(R.id.edi_local_oferta);
        campoValor = findViewById(R.id.cdt_valor_cadastrar);
        campoTempo = findViewById(R.id.spinner_tempo_trabalho);
        campoArea = findViewById(R.id.spinner_area_servico);
        btnCadastrar = findViewById(R.id.btn_cadastrar_oferta);
        campoQtdVagas = findViewById(R.id.edi_qtdVagas_cadastro);
        campoTelefone = findViewById(R.id.edi_telefone_oferta);
        imagemServico = findViewById(R.id.img_cadastro_oferta);
        imagemServico.setOnClickListener(this);

        Locale locale = new Locale("pt","BR");
        campoValor.setLocale(locale);
    }

    private void carregarDadosSpinner() {

        // Spinner Tempo
        String[] tempoOfertaList = getResources().getStringArray(R.array.tempo_oferta);
        ArrayAdapter<String> adapterTempo = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, tempoOfertaList
        );
        adapterTempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoTempo.setAdapter(adapterTempo);

        // Spinner Area
        String[] areaServicoList = getResources().getStringArray(R.array.areas);
        ArrayAdapter<String> adapterArea = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, areaServicoList
        );
        adapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoArea.setAdapter(adapterArea);
    }

    private void validarCampos(){
       servico = configurarServico();
       String valor = String.valueOf(campoValor.getRawValue());

       if(caminhoImagem != null){
           if(!servico.getTitulo().isEmpty()){
               if(!servico.getQtdVagas().isEmpty()){
                   if(!servico.getSalario().isEmpty() && !valor.equals("0")){
                       if(!servico.getTempo().isEmpty()){
                           if(!servico.getArea().isEmpty()){
                               if(!servico.getLocal().isEmpty()){
                                   if(!servico.getTelefone().isEmpty()){
                                       if(!servico.getDescricao().isEmpty()){
                                            salvarOferta();
                                            finish();
                                       }else{
                                           exibirMensagemErro("Preencha o campo Descrição!");
                                       }
                                   }else{
                                       exibirMensagemErro("Preencha o campo Telefone!");
                                   }
                               }else{
                                    exibirMensagemErro("Preencha o campo local!");
                               }
                           }else{
                               exibirMensagemErro("Preencha o campo de área!");
                           }
                       }else{
                           exibirMensagemErro("Preencha o campo de Tempo de serviço!");
                       }
                   }else{
                       exibirMensagemErro("Preencha o campo de valor da oferta!");
                   }
               }else{
                   exibirMensagemErro("Preencha o campo de Quantidade de vagas!");
               }
           }else{
               exibirMensagemErro("Preencha o campo titulo!");
           }
       }else{
            exibirMensagemErro("Selecione uma foto");
       }

    }

    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        escolherImage(1);
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

            if (requestCode == 1){
                imagemServico.setImageURI(imagemSelecionada);
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
}