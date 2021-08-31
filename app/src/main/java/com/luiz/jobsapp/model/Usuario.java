package com.luiz.jobsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.luiz.jobsapp.helper.FirebaseConfig;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }



    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
