package com.example.practica1_desordenadas;

import java.util.HashSet;

public class Palabra {
	private String nombre;
	private HashSet<Character> letras;
	
	public Palabra(String pNombre) {
		this.nombre=pNombre;
		letras=new HashSet<Character>();
		for (int i = 0; i < nombre.length(); i++){
		    Character letra = nombre.charAt(i);
		   if(!letras.contains(letra)) {
			   letras.add(letra);
		   }
		   
		   
		}
	}
	
	public String getNombre() {
		//Devuelve el nombre de las palabras
		return this.nombre;
	}
	
	
}
