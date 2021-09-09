package com.luiz.jobsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.helper.FirebaseConfig;
import com.luiz.jobsapp.model.Servico;
import com.squareup.picasso.Picasso;

public class DetalheOfertaActivity extends AppCompatActivity {

    private TextView tituloOferta, tempoOferta, localOferta, descricaoOferta, valorSalario, qtdVagas, telefone;
    private ImageView fotoOferta;
    private Button btnContato;
    private Servico servicoSelecionado;
    private DatabaseReference meusServicosRef;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_oferta);

        Toolbar myToolbar = findViewById(R.id.toolbar_detalhes);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

        ativarBotao();
        btnContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualizarTelefone(v);
            }
        });
    }

    private void visualizarTelefone(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", servicoSelecionado.getTelefone(), null));
        startActivity(i);
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

    private void ativarBotao(){
        boolean eDoUsuario = false;
        autenticacao = FirebaseAuth.getInstance();
        Log.i("Id usuario", autenticacao.getUid());
        btnContato = findViewById(R.id.btn_contato);

        meusServicosRef= FirebaseConfig.getFirebaseDatabase()
                .child("minhas_ofertas")
                .child(autenticacao.getUid());


        meusServicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!= null) {
                    for(DataSnapshot meusAnuncios: snapshot.getChildren()){
                        Servico servico = meusAnuncios.getValue(Servico.class);

                        if (servico.getIdServico().equalsIgnoreCase(servicoSelecionado.getIdServico())){
                            btnContato.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}