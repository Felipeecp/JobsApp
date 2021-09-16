package com.luiz.jobsapp.model;

public class Portfolio {
    private String profissao;
    private String formacao;
    private String experiencia;
    private String tempo;
    private String foto;

    public Portfolio(){}

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "foto='" + foto + '\'' +
                "profissao='" + profissao + '\'' +
                ", formacao='" + formacao + '\'' +
                ", experiencia='" + experiencia + '\'' +
                ", tempo='" + tempo + '\'' +
                '}';
    }
}
