package com.luiz.jobsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.luiz.jobsapp.R;
import com.luiz.jobsapp.adapter.AdapterOfertas;
import com.luiz.jobsapp.model.Servico;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<Servico> servicoList = new ArrayList<>();
    private RecyclerView recyclerOfertas;
    private AdapterOfertas adapterOfertas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.criarServico();
        adapterOfertas = new AdapterOfertas(servicoList, this);

        adapterOfertas = new AdapterOfertas(servicoList, this);

        // Configurando RecyclerView
        recyclerOfertas = findViewById(R.id.rv_servicos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerOfertas.setLayoutManager(layoutManager);
        recyclerOfertas.setHasFixedSize(true);
        recyclerOfertas.setAdapter(adapterOfertas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home_perfil_top:
                startActivity(new Intent(HomeActivity.this, MinhasOfertasActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
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