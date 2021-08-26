package com.luiz.jobsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.adapter.AdapterOfertas;
import com.luiz.jobsapp.databinding.ActivityMinhasOfertasBinding;
import com.luiz.jobsapp.model.Servico;

import java.util.ArrayList;
import java.util.List;

public class MinhasOfertasActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMinhasOfertasBinding binding;

    private List<Servico> servicoList = new ArrayList<>();
    private RecyclerView recyclerOfertas;
    private AdapterOfertas adapterOfertas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMinhasOfertasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MinhasOfertasActivity.this, CadastroOfertaActivity.class));
            }
        });

        this.criarServico();
        adapterOfertas = new AdapterOfertas(servicoList, this);

        // Configurando RecyclerView
        recyclerOfertas = findViewById(R.id.rv_minhas_ofertas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerOfertas.setLayoutManager(layoutManager);
        recyclerOfertas.setHasFixedSize(true);
        recyclerOfertas.setAdapter(adapterOfertas);


    }

    public void criarServico(){
        Servico servico = new Servico("Programador", "R$ 1000.00", "3");
        this.servicoList.add( servico );

        Servico servico2 = new Servico("Pintor", "R$ 100.00", "1");
        this.servicoList.add( servico2 );

        Servico servico3 = new Servico("Design", "R$ 500.00", "1");
        this.servicoList.add( servico3 );


    }
}