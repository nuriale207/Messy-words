package com.example.practica1_desordenadas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorRecyclerNivel extends RecyclerView.Adapter<ViewHolderNivel> {
    //El adaptador del recycler view de los niveles contiene los nombres, las imágenes y los ids
    private ArrayList<String> losnombres;
    private ArrayList<Integer> lasimagenes;
    private ArrayList<Integer> losIds;

    private boolean[] seleccionados;
    public AdaptadorRecyclerNivel(ArrayList<String> nombres, ArrayList<Integer> imagenes,ArrayList<Integer> ids)
    {
        losnombres=nombres;
        lasimagenes=imagenes;
        losIds=ids;
        seleccionados=new boolean[nombres.size()];
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
        holder.eltexto.setText(losnombres.get(position));
        holder.laimagen.setImageResource(lasimagenes.get(position));
        holder.id=losIds.get(position);
    }


    @Override
    public int getItemCount() {
        return losnombres.size();
    }
}
