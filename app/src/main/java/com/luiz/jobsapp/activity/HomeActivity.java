package com.luiz.jobsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.adapter.AdapterOfertas;
import com.luiz.jobsapp.helper.FirebaseConfig;
import com.luiz.jobsapp.helper.RecyclerItemClickListener;
import com.luiz.jobsapp.model.Servico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<Servico> servicoList = new ArrayList<>();
    private RecyclerView recyclerOfertas;
    private AdapterOfertas adapterOfertas;
    private FirebaseAuth autenticacao;
    private String filtroArea = "";
    private boolean filtrandoPorArea = false;
    private DatabaseReference servicosPublicosRef;
    private Button btnArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar myToolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(myToolbar);

        inicializarCampos();

        autenticacao = FirebaseConfig.getFirebaseAuth();
        servicosPublicosRef = FirebaseConfig.getFirebaseDatabase().child("serviços");

        btnArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrarPorArea(v);
            }
        });

        adapterOfertas = new AdapterOfertas(servicoList, this);

        // Configurando RecyclerView
        recyclerOfertas = findViewById(R.id.rv_servicos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerOfertas.setLayoutManager(layoutManager);
        recyclerOfertas.setHasFixedSize(true);
        recyclerOfertas.setAdapter(adapterOfertas);

        recuperarAnunciosPublicos();

        recyclerOfertas.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerOfertas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Servico servicoSelecionado = servicoList.get(position);
                        Intent i = new Intent(HomeActivity.this, DetalheOfertaActivity.class);
                        i.putExtra("servicoSelecionado", servicoSelecionado);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));


    }

    private void inicializarCampos(){
        btnArea = findViewById(R.id.btn_area);
    }

    // Recupera anuncios do firebase para exibir utilizando recyclerView
    public void recuperarAnunciosPublicos(){
        servicoList.clear();
        servicosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot area: snapshot.getChildren()){
                    for(DataSnapshot servicos: area.getChildren()){
                        Servico servico = servicos.getValue(Servico.class);
                        servicoList.add(servico);
                    }
                }
                Collections.reverse(servicoList);
                adapterOfertas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Filtrar Anuncios por área
    public void filtrarPorArea(View view){

        AlertDialog.Builder dialogArea = new AlertDialog.Builder(this);
        dialogArea.setTitle("Selecione uma área");

        //Configurar Spinner
        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

        Spinner spinnerArea = viewSpinner.findViewById(R.id.spinner_filtro_area);

        String[] areaServicoList = getResources().getStringArray(R.array.areas);
        ArrayAdapter<String> adapterArea = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, areaServicoList
        );
        adapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapterArea);

        dialogArea.setView(viewSpinner);

        dialogArea.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtroArea = spinnerArea.getSelectedItem().toString();
                recuperarAnunciosPorArea();
                filtrandoPorArea = true;
            }
        });

        dialogArea.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = dialogArea.create();
        dialog.show();
    }

    private void recuperarAnunciosPorArea() {

        servicosPublicosRef = FirebaseConfig.getFirebaseDatabase().child("serviços")
                .child(filtroArea);

        servicosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                servicoList.clear();
                for(DataSnapshot anuncios: snapshot.getChildren()){

                    Servico servico = anuncios.getValue(Servico.class);
                    servicoList.add( servico );

                }

                Collections.reverse(servicoList);
                adapterOfertas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    // Configuração de toolbar
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

}