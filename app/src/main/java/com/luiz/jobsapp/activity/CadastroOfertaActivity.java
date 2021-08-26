package com.luiz.jobsapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.luiz.jobsapp.R;

public class CadastroOfertaActivity extends AppCompatActivity {
    private EditText editNome, editDescricao, editLocal, editRequisitos, editValorPagoPorHora;
    private Button btnCadastrar;
    private RadioGroup radioGroupVagas;
    private RadioGroup radioGroupPeriodo;
    boolean camposValidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_oferta);

        inicializarComponentes();
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camposValidos = true;
                validarCampos();
                AlertDialog alertDialog = new AlertDialog.Builder(CadastroOfertaActivity.this).create();
                if (camposValidos){

                } else {
                    mostrarErro("Isso aqui é JAVA", "Campos obrigatório não preenchidos." );

                }

            }
        });
    }



    private void inicializarComponentes(){
        editNome = findViewById(R.id.edi_nome_oferta);
        editDescricao = findViewById(R.id.edi_descricao_oferta);
        editRequisitos = findViewById(R.id.edi_requisitos);
        editLocal = findViewById(R.id.edi_local);
        editValorPagoPorHora = findViewById(R.id.valorSalario);

        btnCadastrar = findViewById(R.id.btn_cadastrar_oferta);
        radioGroupVagas = findViewById(R.id.rdgVagas);
        radioGroupPeriodo = findViewById(R.id.rdgPeriodo);
    }

    private void validarCampos(){
        if(TextUtils.isEmpty(editNome.getText().toString())){
            editNome.setError("Digite o nome da serviço");
            editNome.requestFocus();
            camposValidos = false;
        }
        if(TextUtils.isEmpty(editDescricao.getText().toString())){
            editDescricao.setError("Digite a descrição do serviço");
            editDescricao.requestFocus();
            camposValidos = false;
        }
        if(TextUtils.isEmpty(editLocal.getText().toString())){
            editLocal.setError("Digite a localização do serviço");
            editLocal.requestFocus();
            camposValidos = false;
        }

        if(TextUtils.isEmpty(editValorPagoPorHora.getText().toString())){
            editValorPagoPorHora.setError("Se deixar em aberto digite: combinar");
            editValorPagoPorHora.requestFocus();
            camposValidos = false;
        }

        int itemRadioGroupVagasSelecionado = radioGroupVagas.getCheckedRadioButtonId();
        if (itemRadioGroupVagasSelecionado == -1) {
            camposValidos = false;
            TextView textHorario = findViewById(R.id.txt_qtd_vagas);
            textHorario.requestFocus();
            camposValidos = false;

      }

        int itemRGPeriodosSelecionado = radioGroupVagas.getCheckedRadioButtonId();
        if(itemRGPeriodosSelecionado == -1) {
            TextView textHorario = findViewById(R.id.horario);
            textHorario.requestFocus();
            camposValidos = false;
        }

    }

    private void mostrarErro(String title, String Message){
        AlertDialog alertDialog = new AlertDialog.Builder(CadastroOfertaActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(Message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}