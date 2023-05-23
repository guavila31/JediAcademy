package com.example.jediacademy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.jediacademy.R;
import com.example.jediacademy.adapter.AdapterAgenda;
import com.example.jediacademy.config.ConfiguracaoFirebase;
import com.example.jediacademy.helper.Base64Custom;
import com.example.jediacademy.model.Agenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AgendaActivity extends AppCompatActivity {

    private RecyclerView recyclerAgenda;
    private AdapterAgenda adapterAgenda;
    private List<Agenda> listaAgenda = new ArrayList<>();
    private Button btnCriarAgenda;
    private FirebaseAuth autenticacao;
    private DatabaseReference agendaRef;
    private String mesAnoSelecionado;
    private ValueEventListener valueEventListenerAgendas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        recyclerAgenda = findViewById(R.id.recyclerAgenda);
        btnCriarAgenda = findViewById(R.id.btnCriarAgenda);
        agendaRef = ConfiguracaoFirebase.getFirebaseDatabase();

        // Configurar o adapter
        adapterAgenda = new AdapterAgenda(listaAgenda, this);

        // Configurar o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAgenda.setLayoutManager(layoutManager);
        recyclerAgenda.setHasFixedSize(true);
        recyclerAgenda.setAdapter(adapterAgenda);

        btnCriarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgendaActivity.this, CriarAgenda.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void fecharTeclado(View view) {
        InputMethodManager fecharTeclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fecharTeclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AgendaActivity.this);
        builder.setMessage("Você quer sair?");
        builder.setCancelable(true);

        builder.setPositiveButton("Fechar o App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("Deslogar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AgendaActivity.this, Login.class);
                startActivity(intent);
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                finish();
            }
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        System.out.println(">>>>>>>>>>ENTROUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
        super.onStart();
        System.out.println(">>>>>>>>>>Vaiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+valueEventListenerAgendas);
        //if (valueEventListenerAgendas != null) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            recuperarAgendas();
        //}
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListenerAgendas != null) {
            agendaRef.removeEventListener(valueEventListenerAgendas);
        }
    }

    public void recuperarAgendas() {
        autenticacao = FirebaseAuth.getInstance();
        System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"+autenticacao.getCurrentUser().getEmail());
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        System.out.println("************************************"+autenticacao.getCurrentUser().getEmail());
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference agendasUsuarioRef = agendaRef
                .child("agendaProfessor")
                .child(idUsuario);

        valueEventListenerAgendas = agendasUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAgenda.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Agenda agenda = dados.getValue(Agenda.class);
                    System.out.println(">>>>>>>>>>Agenda: "+agenda);
                    listaAgenda.add(agenda);
                    System.out.println(">>>>>>>>>>Lista Agenda: "+listaAgenda);
                }
                adapterAgenda.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Tratar erro de leitura dos dados do Firebase
            }
        });
    }
}
