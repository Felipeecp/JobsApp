package com.luiz.jobsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luiz.jobsapp.R;
import com.luiz.jobsapp.model.Servico;

import java.util.List;

public class AdapterOfertas extends RecyclerView.Adapter<AdapterOfertas.ViewHolderOfertas>{

    private List<Servico> servicos;
    private Context context;

    public AdapterOfertas(List<Servico> servicos, Context context) {
        this.servicos = servicos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderOfertas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servico, parent, false);
        return new ViewHolderOfertas(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderOfertas holder, int position) {

        Servico servico = servicos.get(position);

        holder.titulo.setText(servico.getTitulo());
        holder.salario.setText(servico.getSalario());
        holder.qtdVagas.setText(servico.getQtdVagas());


    }

    @Override
    public int getItemCount() {
        return servicos.size();
    }

    public class ViewHolderOfertas extends RecyclerView.ViewHolder{
        TextView titulo, salario, qtdVagas;

        public ViewHolderOfertas(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.txtv_titulo_servico);
            salario = itemView.findViewById(R.id.txtv_salario_valor_servico);
            qtdVagas = itemView.findViewById(R.id.txtv_vagas_valor_servico);
        }
    }

}
