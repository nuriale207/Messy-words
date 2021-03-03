package com.example.practica1_desordenadas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorRecyclerNivel extends RecyclerView.Adapter<ViewHolderNivel> {
    private String[] losnombres;
    private int[] lasimagenes;
    private boolean[] seleccionados;
    public AdaptadorRecyclerNivel(String[] nombres, int[] imagenes)
    {
        losnombres=nombres;
        lasimagenes=imagenes;
        seleccionados=new boolean[nombres.length];
    }
    @NonNull

    @Override

    public ViewHolderNivel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View elLayoutDeCadaItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_nivel_item_layout,null);
        ViewHolderNivel evh = new ViewHolderNivel(elLayoutDeCadaItem);
        evh.seleccion = seleccionados;
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNivel holder, int position) {
        holder.eltexto.setText(losnombres[position]);
        holder.laimagen.setImageResource(lasimagenes[position]);
    }


    @Override
    public int getItemCount() {
        return losnombres.length;
    }
}
