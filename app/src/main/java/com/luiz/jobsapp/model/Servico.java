package com.luiz.jobsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.luiz.jobsapp.helper.FirebaseConfig;

import java.io.Serializable;

public class Servico implements Serializable {

    private String idServico;
    private String titulo;
    private String salario;
    private String qtdVagas;
    private String descricao;
    private String foto;
    private String local;
    private String area;
    private String tempo;
    private String telefone;

    public Servico() {
        DatabaseReference servicoRef = FirebaseConfig.getFirebaseDatabase()
                .child("minhas_ofertas");
        setIdServico( servicoRef.push().getKey() );
    }

    public void salvar(){

        String idUsuario = FirebaseConfig.getIdUsuario();
        DatabaseReference servicoRef = FirebaseConfig.getFirebaseDatabase()
                .child("minhas_ofertas");

        servicoRef.child(idUsuario)
                .child(getIdServico())
                .setValue(this);

        salvarAnuncioPublico();
    }

    private void salvarAnuncioPublico() {

        DatabaseReference servicoRef = FirebaseConfig.getFirebaseDatabase().child("serviços");

        servicoRef.child(getArea())
                .child(getIdServico())
                .setValue(this);

    }

    public void removerAnuncio(){

        String idUsuario = FirebaseConfig.getIdUsuario();
        DatabaseReference servicoRef = FirebaseConfig.getFirebaseDatabase()
                .child("meus_servicos")
                .child(idUsuario)
                .child(getIdServico());

        removerAnuncioPublico();
    }

    private void removerAnuncioPublico() {

        DatabaseReference serviçoRef = FirebaseConfig.getFirebaseDatabase()
                .child("serviços")
                .child(getArea())
                .child(getIdServico());

        serviçoRef.removeValue();

    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdServico() {
        return idServico;
    }

    public void setIdServico(String idServico) {
        this.idServico = idServico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getSalario() {
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }

    public String getQtdVagas() {
        return qtdVagas;
    }

    public void setQtdVagas(String qtdVagas) {
        this.qtdVagas = qtdVagas;
    }
}
