package com.example.jediacademy.model;


import com.example.jediacademy.config.ConfiguracaoFirebase;
import com.example.jediacademy.helper.Base64Custom;
import com.example.jediacademy.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Agenda implements Serializable {
    private int id;
    private String curso;
    private String materia;
    private String local;
    private String data, hora;
    private String resumo;

    public Agenda() {
    }

    public void salvar() {
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("agendaProfessor")
                .child(idUsuario)
                .push()
                .setValue(this);
    }

    public void alterar() {
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        String agendaId = firebase.child("agendaProfessor").child(idUsuario).push().getKey();

        // Crie um Map com os valores que você deseja atualizar
        Map<String, Object> atualizacao = new HashMap<>();
        atualizacao.put(agendaId, this);

        // Atualize os valores no Firebase Database
        firebase.child("agendaProfessor")
                .child(idUsuario)
                .updateChildren(atualizacao);
    }
    public Agenda(int id, String curso, String materia, String local, String data, String hora, String resumo) {
        this.id = id;
        this.curso = curso;
        this.materia = materia;
        this.local = local;
        this.data = data;
        this.hora = hora;
        this.resumo = resumo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Agenda{" +
                "id=" + id +
                ", curso='" + curso + '\'' +
                ", materia='" + materia + '\'' +
                ", local='" + local + '\'' +
                ", data='" + data + '\'' +
                ", hora='" + hora + '\'' +
                ", resumo='" + resumo + '\'' +
                '}';
    }
}