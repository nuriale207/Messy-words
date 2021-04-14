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
import java.util.Random;


public class Diccionario {
	//Partiendo de un fichero de texto realiza la carga del diccionario utilizado
	// para comprobar las palabras del juego. Su finalidad es encargarse de comprobar
	// si realmente existen las palabras escritas por el jugador.
	private HashMap<String,Palabra> palabras;
	private static Diccionario miListaPalabras=new Diccionario();
	private int numPalabras;
	private Diccionario() {
		palabras=new HashMap<String,Palabra>();
		numPalabras=0;
	}
	
	public static Diccionario getDiccionario() {
		
		return miListaPalabras;
		
	}
	
	public void anadirPalabra(String pal) {
		//Añade palabras nuevas al diccionario
		Palabra p=new Palabra(pal);
		palabras.put(pal,p);
		this.numPalabras=this.numPalabras+1;
	}

	public String obtenerPalabraAleatoria(){
		Random r = new Random();
		int indice= r.nextInt(this.numPalabras);

		String palabra = (String) palabras.keySet().toArray()[indice];

		return palabra;
	}

	public boolean contiene(String pPalabra) {
		//Comprueba si la palabra pasada como parámetro está en el diccionario
		return this.palabras.containsKey(pPalabra);
	}

	public void cargar(Context pContexto) {
		// Realiza la carga del diccionario
		this.numPalabras=0;

		String palabra = "lista.txt",line;
		System.out.println(palabra);
        try {
			/**Diccionario extraído de Github
			 Repositorio: https://github.com/javierarce/palabras/blob/master/listado-general.txt
			 Autor: https://github.com/javierarce
			 **/
			//Fuente del diccionario:
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
