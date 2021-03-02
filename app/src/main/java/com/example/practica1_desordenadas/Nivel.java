package com.example.practica1_desordenadas;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;

public class Nivel extends Observable {
	private String nombre;
	private Collection<Character> listaLetras;
	private int aciertos;
	private int errores;
	private HashSet<String> palabrasAcertadas;
	private static int puntuacionAcierto=4;
	private static int restaError=2;
	private static int numeroDeIntentos=10;
	
	public Nivel(String pnombre) {
		this.nombre=pnombre;
		listaLetras=new HashSet<Character>();
		palabrasAcertadas=new HashSet<String>();
	}
	
	public void anadirLetra(Character car) {
		listaLetras.add(car);
	}
	
	public void imprimir() {
		Iterator<Character> itr=listaLetras.iterator();
		System.out.println("Jugando el nivel "+ nombre);
	
		while(itr.hasNext()) {
			System.out.print(" "+ itr.next());
		}
		
		System.out.println("Suerte! Dispones de 10 intentos");
	}
	public void jugar(String palabra) {
		if(esAcierto(palabra)&& !palabrasAcertadas.contains(palabra)) {
			JugarPartida.getPartida().cambiarPuntuacion('+', puntuacionAcierto);
			palabrasAcertadas.add(palabra);
		}
		else if(!esAcierto(palabra)){
			JugarPartida.getPartida().cambiarPuntuacion('-', restaError);
			
		}
	}

	public boolean esAcierto(String palabra) {
		
		// TODO Auto-generated method stub
		System.out.println(palabra);
		System.out.println(!(palabra.length()>this.listaLetras.size()));
		System.out.println(Diccionario.getDiccionario().contiene(palabra));
		if(!(palabra.length()>this.listaLetras.size()) || !Diccionario.getDiccionario().contiene(palabra)) {
			System.out.println("no es correcta");

			return false;
		}
		int i=0;
		HashSet<Character> letras= new HashSet<Character>();
		Iterator<Character> itr=this.listaLetras.iterator();
		while(itr.hasNext()) {
			letras.add(itr.next());
		}
		
		while(i<palabra.length()) {
			if(!letras.remove(palabra.charAt(i))) {
				return false;
			};
			i++;
		}
	
		return true;
	}

	public String getNombre() {
		// TODO Auto-generated method stub
		return nombre;
	}

	public String getLetras() {
		// TODO Auto-generated method stub
		String letras= new String();
		Iterator<Character> itr=this.listaLetras.iterator();
		while(itr.hasNext()) {
			letras=letras+itr.next()+" ";
		}
		return letras;
	}
	
	
}
