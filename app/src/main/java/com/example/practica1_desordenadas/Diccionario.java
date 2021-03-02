package com.example.practica1_desordenadas;

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
	private HashMap<String,Palabra> palabras;
	private static Diccionario miListaPalabras=new Diccionario();
	
	private Diccionario() {
		palabras=new HashMap<String,Palabra>();
		cargar();
	}
	
	public static Diccionario getDiccionario() {
		
		return miListaPalabras;
		
	}
	
	public void anadirPalabra(String pal) {
		Palabra p=new Palabra(pal);
		palabras.put(pal,p);
	}
	
	public boolean contiene(String pPalabra) {
		/*
		 * System.out.println(this.palabras.get(pPalabra)); Iterator<String>
		 * itr=this.palabras.keySet().iterator(); while(itr.hasNext()) {
		 * System.out.println(itr.next());
		 * 
		 * }
		 */
		return this.palabras.containsKey(pPalabra);
	}

	private void cargar() {
		// TODO Auto-generated method stub
		System.out.println("Dentro");
		String palabra = "lista.txt",line;
		System.out.println(palabra);
        try {
        	InputStream in = this.getClass().getResourceAsStream("lista.txt");
        	//BufferedReader br = new BufferedReader(new FileReader("lista"));
        	Reader reader = new InputStreamReader(in, "utf-8");
        	BufferedReader br = new BufferedReader(reader);
            while ((line = br.readLine()) != null) {
            	System.out.println(line);
            	line=line.replaceAll("\\s+","");
            	Palabra pal=new Palabra(line);
            	this.palabras.put(pal.getNombre(), pal);
            	//System.out.println(pal.getNombre());
            }
            br.close();
            //System.out.println("CARGA DE PALABRAS [OK]");
 

        } catch (FileNotFoundException e) {
        	System.out.println("File not Found");
            e.printStackTrace();
        } catch (IOException e) {
        	System.out.println("Couldn't read buffer line");
            e.printStackTrace();
        }
	}
	
	
}
