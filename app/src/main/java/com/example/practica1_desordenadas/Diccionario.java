package com.example.practica1_desordenadas;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;


public class Diccionario {
	//Partiendo de un fichero de texto realiza la carga del diccionario utilizado
	// para comprobar las palabras del juego. Su finalidad es encargarse de comprobar
	// si realmente existen las palabras escritas por el jugador.
	private HashMap<String,Palabra> palabras;
	private static Diccionario miListaPalabras=new Diccionario();
	
	private Diccionario() {
		palabras=new HashMap<String,Palabra>();

	}
	
	public static Diccionario getDiccionario() {
		
		return miListaPalabras;
		
	}
	
	public void anadirPalabra(String pal) {
		//Añade palabras nuevas al diccionario
		Palabra p=new Palabra(pal);
		palabras.put(pal,p);
	}
	
	public boolean contiene(String pPalabra) {
		//Comprueba si la palabra pasada como parámetro está en el diccionario
		return this.palabras.containsKey(pPalabra);
	}

	public void cargar(Context pContexto) {
		// Realiza la carga del diccionario


		String palabra = "lista.txt",line;
		System.out.println(palabra);
        try {
        	InputStream fich = pContexto.getResources().openRawResource(R.raw.diccionario);
			BufferedReader br = new BufferedReader(new InputStreamReader(fich));

            while ((line = br.readLine()) != null) {
            	line=line.replaceAll("\\s+","");
            	this.anadirPalabra(line);

            }
            br.close();


        } catch (FileNotFoundException e) {
			Log.i("MYAPP","File not Found");

			e.printStackTrace();
        } catch (IOException e) {
        	System.out.println("Couldn't read buffer line");
            e.printStackTrace();
        }
	}
	
	
}
