package com.luiz.jobsapp.model;

public class Dados{
    private String profissao;
    private String formacao;
    private String experiencia;
    private String tempo;

    public Dados(){}


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
        return "Dados{" +
                "profissao='" + profissao + '\'' +
                ", formacao='" + formacao + '\'' +
                ", experiencia='" + experiencia + '\'' +
                ", tempo='" + tempo + '\'' +
                '}';
    }
}
