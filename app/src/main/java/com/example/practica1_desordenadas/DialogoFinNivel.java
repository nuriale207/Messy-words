package com.example.practica1_desordenadas;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogoFinNivel extends DialogFragment {

    public interface ListenerdelDialogoFinNivel {
        void alpulsarMenuNiveles();
        void alpulsarSiguienteNivel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Continuar");
        builder.setMessage("Has ganado!! " );
        DialogoFinNivel.ListenerdelDialogoFinNivel miListener = (DialogoFinNivel.ListenerdelDialogoFinNivel) getActivity();

        builder.setPositiveButton("Siguiente nivel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarSiguienteNivel();
            }
        });

        builder.setNegativeButton("Menú de niveles", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarMenuNiveles();
            }
        });


        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}