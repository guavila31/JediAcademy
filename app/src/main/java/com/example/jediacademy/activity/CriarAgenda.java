package com.example.jediacademy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jediacademy.R;
import com.example.jediacademy.model.Agenda;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CriarAgenda extends AppCompatActivity {
    TextView txtCurso, txtMateria, txtLocal, txtDataAula, txtHoraAula;
    Button btnCriarNovaAgenda, btnCancelar;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private Agenda agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_agenda);

        txtCurso = findViewById(R.id.txtEmail);
        txtMateria = findViewById(R.id.txtSenha);
        txtLocal = findViewById(R.id.txtConfirmarSenha);
        txtDataAula = findViewById(R.id.txtDataAula);
        txtHoraAula = findViewById(R.id.txtHoraAula);
        btnCriarNovaAgenda = findViewById(R.id.btnCriarNovaAgenda);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ir = new Intent(CriarAgenda.this, AgendaActivity.class);
                startActivity(ir);
                finish();
            }
        });

        btnCriarNovaAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curso = txtCurso.getText().toString();
                String materia = txtMateria.getText().toString();
                String local = txtLocal.getText().toString();
                String dataAula = txtDataAula.getText().toString();
                String horaAula = txtHoraAula.getText().toString();

                if (curso.isEmpty() || materia.isEmpty() || local.isEmpty() || dataAula.isEmpty() || horaAula.isEmpty()) {
                    Toast.makeText(CriarAgenda.this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
                } else {
                    String chaveAgenda = curso + dataAula + horaAula;
                    agenda = new Agenda();
                    //String data = ;
                    agenda.setCurso(txtCurso.getText().toString());
                    agenda.setMateria(txtMateria.getText().toString());
                    agenda.setLocal(txtLocal.getText().toString());
                    agenda.setData(txtDataAula.getText().toString());
                    agenda.setHora(txtHoraAula.getText().toString());
                    agenda.setId(chaveAgenda);
                    agenda.salvar(chaveAgenda);
                    Toast.makeText(CriarAgenda.this, "Agenda Criada com sucesso!", Toast.LENGTH_SHORT).show();
                    abrirTelaAgenda();
                    finish();
                }
            }
        });
    }

    public void fecharTeclado(View view) {
        InputMethodManager fecharTeclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fecharTeclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        Intent ir = new Intent(CriarAgenda.this, Agenda.class);
        startActivity(ir);
        finish();
    }
    public void abrirTelaAgenda(){
        startActivity(new Intent(this, AgendaActivity.class));
    }
}
