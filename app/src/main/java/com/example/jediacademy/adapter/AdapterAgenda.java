package com.example.jediacademy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jediacademy.R;
import com.example.jediacademy.activity.Resumo;
import com.example.jediacademy.holder.HolderAgenda;
import com.example.jediacademy.model.Agenda;

import java.util.List;

public class AdapterAgenda extends RecyclerView.Adapter<HolderAgenda>{
    List<Agenda> listaAgenda;
    Context context;
    public AdapterAgenda(List<Agenda> listaAgenda, Context context) {
        this.listaAgenda = listaAgenda;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderAgenda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderAgenda(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_agenda, parent,
                                false));
    }
    @Override
    public void onBindViewHolder(@NonNull HolderAgenda holder, int position) {
        Agenda agenda = listaAgenda.get(position);
        holder.txtNome.setText(agenda.getCurso());
        holder.txtMateria.setText(agenda.getMateria());
        holder.txtLocal.setText(agenda.getLocal());
        holder.txtData.setText(agenda.getData());
        holder.txtHora.setText(agenda.getHora());

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), Resumo.class);
                intent.putExtra("agenda", agenda);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return listaAgenda.size();
    }
}