package com.example.practica1_desordenadas;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;

public class Nivel extends Observable {
	private String nombre;
	private Collection<Character> listaLetras;
	private int puntuacion;
	private int aciertos;
	private HashSet<String> palabrasAcertadas;
	private static int puntuacionAcierto=4;
	private static int restaError=2;
	private int numeroDeIntentos=10;
	private StringBuilder output = new StringBuilder();
	private int palabras=0;


	public Nivel(String pnombre) {
		this.nombre=pnombre;
		listaLetras=new HashSet<Character>();
		palabrasAcertadas=new HashSet<String>();

		int i=0;
		while (i<pnombre.length()){
			anadirLetra(pnombre.charAt(i));
			i=i+1;
		}
		combinarPalabras();
		numeroDeIntentos=this.palabras*2;
	}
	
	private void anadirLetra(Character car) {
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
	public String jugar(String palabra) {
		if(esAcierto(palabra)&& !palabrasAcertadas.contains(palabra)) {
			cambiarPuntuacion('+', puntuacionAcierto);
			palabrasAcertadas.add(palabra);
			numeroDeIntentos=numeroDeIntentos-1;
			aciertos=aciertos+1;
			return palabra;
		}
		else if(!esAcierto(palabra)){
			cambiarPuntuacion('-', restaError);
			Log.i("MYAPP","noooo es correctaaaa");
			numeroDeIntentos=numeroDeIntentos-1;

			return null;
			
		}
		return null;
	}

	public boolean esAcierto(String palabra) {
		
		// TODO Auto-generated method stub
		System.out.println(palabra);
		System.out.println(!(palabra.length()>this.listaLetras.size()));
		Log.i("MYAPP", String.valueOf(Diccionario.getDiccionario().contiene(palabra)));
		System.out.println(Diccionario.getDiccionario().contiene(palabra));
		if((palabra.length()>this.nombre.length()) || !Diccionario.getDiccionario().contiene(palabra)) {
			Log.i("MYAPP", "no es correcta");
			Log.i("MYAPP", String.valueOf(palabra.length()));
			Log.i("MYAPP", String.valueOf(nombre.length()));



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



	public void combinarPalabras() {combinarPalabras( 0 ); }
	private void combinarPalabras(int start ){
		for( int i = start; i < nombre.length(); i++ ){
			output.append( nombre.charAt(i) );
			if (Diccionario.getDiccionario().contiene(output.toString())){
				palabras=palabras+1;
				Log.i("MYAPP", String.valueOf(output));
			}
			if ( i < nombre.length() )
				combinarPalabras( i + 1);
			output.setLength( output.length() - 1 );
		}
	}

	public int getNumeroPalabras(){
		return this.palabras;
	}

	public int getPuntuacion(){
		return this.puntuacion;
	}

	public int getIntentos(){
		return this.numeroDeIntentos;
	}
	public void cambiarPuntuacion(char signo, int puntos) {
		if(signo=='-') {
			puntuacion=puntuacion-puntos;
		}
		else {
			puntuacion=puntuacion+puntos;
		}

		setChanged();
		notifyObservers(puntuacion);

	}

	public int getPalabrasRestantes(){
		return palabras-aciertos;
	}
	
}
