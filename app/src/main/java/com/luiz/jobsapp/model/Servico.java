package com.luiz.jobsapp.model;

public class Servico {

    String titulo;
    String salario;
    String qtdVagas;

    public Servico(String titulo, String salario, String qtdVagas) {
        this.titulo = titulo;
        this.salario = salario;
        this.qtdVagas = qtdVagas;
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
