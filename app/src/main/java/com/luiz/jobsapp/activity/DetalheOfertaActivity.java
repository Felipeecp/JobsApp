package com.luiz.jobsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.luiz.jobsapp.R;
import com.luiz.jobsapp.model.Servico;
import com.squareup.picasso.Picasso;

public class DetalheOfertaActivity extends AppCompatActivity {

    private TextView tituloOferta, tempoOferta, localOferta, descricaoOferta, valorSalario, qtdVagas, telefone;
    private ImageView fotoOferta;
    private Button btnContato;
    private Servico servicoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_oferta);

        inicializarComponentes();

        servicoSelecionado = (Servico) getIntent().getSerializableExtra("servicoSelecionado");

        if( servicoSelecionado !=  null){
            tituloOferta.setText(servicoSelecionado.getTitulo());
            tempoOferta.setText(servicoSelecionado.getTempo());
            localOferta.setText(servicoSelecionado.getLocal());
            descricaoOferta.setText(servicoSelecionado.getDescricao());
            valorSalario.setText(servicoSelecionado.getSalario());
            qtdVagas.setText(servicoSelecionado.getQtdVagas());
            telefone.setText(servicoSelecionado.getTelefone());

            String urlString  = servicoSelecionado.getFoto();

            Picasso.get().load(urlString).into(fotoOferta);

        }
    }

    private void inicializarComponentes(){
        tituloOferta = findViewById(R.id.titulo_detalhe_oferta);
        tempoOferta = findViewById(R.id.txtv_tempo_detalhes);
        localOferta = findViewById(R.id.txtv_local_detalhe);
        descricaoOferta = findViewById(R.id.descricao_oferta);
        valorSalario = findViewById(R.id.salario_oferta_valor);
        qtdVagas = findViewById(R.id.qtdVagas_detalhes_valor);
        telefone = findViewById(R.id.telefone_detalhes_valor);
        fotoOferta = findViewById(R.id.detalhes_oferta_IMG);
    }
}