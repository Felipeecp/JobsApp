package com.luiz.jobsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.luiz.jobsapp.R;
import com.luiz.jobsapp.adapter.AdapterOfertas;
import com.luiz.jobsapp.databinding.ActivityMinhasOfertasBinding;
import com.luiz.jobsapp.helper.FirebaseConfig;
import com.luiz.jobsapp.helper.RecyclerItemClickListener;
import com.luiz.jobsapp.model.Servico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinhasOfertasActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMinhasOfertasBinding binding;

    private List<Servico> servicoList = new ArrayList<>();
    private RecyclerView recyclerOfertas;
    private AdapterOfertas adapterOfertas;
    private DatabaseReference servicoUsarioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMinhasOfertasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        servicoUsarioRef = FirebaseConfig.getFirebaseDatabase().child("minhas_ofertas")
                .child( FirebaseConfig.getIdUsuario() );

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MinhasOfertasActivity.this, CadastroOfertaActivity.class));
            }
        });

        adapterOfertas = new AdapterOfertas(servicoList, this);

        // Configurando RecyclerView
        recyclerOfertas = findViewById(R.id.rv_minhas_ofertas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerOfertas.setLayoutManager(layoutManager);
        recyclerOfertas.setHasFixedSize(true);
        recyclerOfertas.setAdapter(adapterOfertas);

        recuperarAnuncios();

        recyclerOfertas.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerOfertas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Servico servicoSelecionado = servicoList.get(position);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(MinhasOfertasActivity.this);

                        dialog.setTitle("Confirmar Exclusão");
                        dialog.setMessage("Deseja excluir oferta ?");

                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                servicoSelecionado.removerOferta();
                                adapterOfertas.notifyDataSetChanged();
                            }
                        });
                        dialog.setNegativeButton("Não", null);

                        dialog.create();
                        dialog.show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

    }

    private void recuperarAnuncios(){
        servicoUsarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                servicoList.clear();
                for( DataSnapshot ds: snapshot.getChildren()){
                    servicoList.add(ds.getValue(Servico.class));
                }

                Collections.reverse( servicoList );
                adapterOfertas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}