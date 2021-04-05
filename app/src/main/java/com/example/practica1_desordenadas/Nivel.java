package com.example.practica1_desordenadas;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.stream.Stream;

public class Nivel extends Observable {
	//Contiene todos los atributos del nivel necesarios para el correcto funcionamiento del juego
	private String nombre;
	private int idImagen;
	private int id;
	private Collection<Character> listaLetras;
	private int puntuacion;
	private int aciertos;
	private HashSet<String> palabrasAcertadas;
	private static int puntuacionAcierto=4;
	private static int restaError=2;
	private int numeroDeIntentos=10;
	private StringBuilder output = new StringBuilder();
	private int palabras=0;
	private HashSet<String> palabrasNivel;


	public Nivel(int id,String pnombre, int idImagen) {
		//Constructora del nivel, partiendo de las letras calcula las palabras y el número de
		//intentos del nivel
		this.id=id;
		this.nombre=pnombre;
		this.idImagen=idImagen;
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

	public int getIdImagen() {
	//Devuelve el id de la imagen del nivel
		return this.idImagen;
	}

	
	private void anadirLetra(Character car) {
		//Añade una letra a la colección de letras del nivel
		listaLetras.add(car);
	}
	
	public void imprimir() {
		//Método para comprobar por pantalla como se desarrollaba la partida
		Iterator<Character> itr=listaLetras.iterator();
		System.out.println("Jugando el nivel "+ nombre);
	
		while(itr.hasNext()) {
			System.out.print(" "+ itr.next());
		}
		
		System.out.println("Suerte! Dispones de 10 intentos");
	}
	public String jugar(String palabra) {
		//Método que devuelve null en caso de no acierto y en caso de acierto devuelve la palabra
		//Actualiza la puntuación del nivel en base a si ha sido un acierto o no
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
		
		// Comprueba si una palabra ha sido acertada o no
		System.out.println(palabra);
		palabra=palabra.toLowerCase();
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
		// Devuelve el nombre del nivel
		return nombre;
	}

	public String getLetras() {
		// Devuelve las letras del nivel
		String letras= new String();
		Iterator<Character> itr=this.listaLetras.iterator();
		while(itr.hasNext()) {
			letras=letras+itr.next()+" ";
		}
		return letras;
	}


//
//	public void combinarPalabras() {

//		combinarPalabras( 0 ); }
//	private void combinarPalabras(int start ){
//		for( int i = start; i < nombre.length(); i++ ){
//			output.append( nombre.charAt(i) );
//			Log.i("MYAPP",output.toString());
//			if (Diccionario.getDiccionario().contiene(output.toString())){
//				//Log.i("MYAPP",output.toString());
//				this.palabras=this.palabras+1;
//			}
//			if ( i < nombre.length() )
//				combinarPalabras( i + 1);
//			output.setLength( output.length() - 1 );
//		}
//	}
	/**Código extraído de StackOverflow
	 Pregunta: https://stackoverflow.com/questions/31816647/how-do-i-get-not-just-all-substrings-but-all-possible-substrings-of-a-string
	 Autor: https://stackoverflow.com/users/4137489/marstran
	 Modificado por Nuria Lebeña para obtener también los substrings**/
	public void combinarPalabras(){
		//Método que realiza todas las combinaciones posibles de las letras para obtener qué palabras
		//incluye el nivel
		List<String> combinaciones= substrings(this.nombre);
		List<String> palabras=new ArrayList<String>();
		ArrayList<String> palabrasDic=new ArrayList<String>();
		for(int i=0;i<combinaciones.size();i++){
			palabras.addAll(permutation(combinaciones.get(i)));
		}
		Log.i("MYAPP", String.valueOf(palabras.size()));
		for(int i=0;i<palabras.size();i++){
			if (Diccionario.getDiccionario().contiene(palabras.get(i))){
				Log.i("MYAPP",output.toString());
				this.palabras=this.palabras+1;
				palabrasDic.add(palabras.get(i));
			}

		}

	}


	public List<String> permutation(String s) {
		//Método que realiza todas las combinaciones posibles de las letras para obtener qué palabras
		//incluye el nivel
		return permutation( "",s); }
	private List<String> permutation(String prefix, String str ){
		final List<String> list = new ArrayList<>();

		final int n = str.length();
		if (n == 0) {
			list.add(prefix);
		} else {
			for (int i = 0; i < n; i++) {
				list.addAll(permutation(prefix + str.charAt(i),
						str.substring(0, i) + str.substring(i + 1, n)));;
			}
		}

		return list;
	}

	public static List<String> substrings(final String source) {
		final List<String> list = new ArrayList<>();
		for (int i = 0; i < source.length(); i++) {
			for (int j = i + 1; j <= source.length(); j++) {
				list.add(source.substring(i, j));
			}
		}
		return list;
	}



	public int getNumeroPalabras(){
		//Devuelve el número de palabras del nivel
		return this.palabras;
	}

	public int getPuntuacion(){
		//Devuelve la puntuación del nivel
		return this.puntuacion;
	}

	public int getIntentos(){
		//Devuelve el número de intentos del nivel
		return this.numeroDeIntentos;
	}
	public void cambiarPuntuacion(char signo, int puntos) {
		//Método que actualiza la puntuación
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
		//Método que devuelve el número de palabras que quedan en el nivel
		return palabras-aciertos;
	}

	public int getAciertos() {
		//Método que devuelve el número de aciertos que quedan en el nivel
		return this.aciertos;
	}

	public ArrayList<String> getPalabrasAcertadas() {
		//Método que obtiene las palabras acertadas del nivel
		ArrayList<String> namesList = new ArrayList<>(this.palabrasAcertadas);

		return namesList;
	}

	public int getId(){
		//Devuelve el id del nivel
		return this.id;
	}

	public void set(int pPuntuacion, int nAciertos, HashSet<String> acertadas, int nIntentos) {
		//Cambia los atributos del nivel a los pasados como parámetros
		this.puntuacion=pPuntuacion;
		this.aciertos=nAciertos;
		this.palabrasAcertadas=acertadas;
		this.numeroDeIntentos=nIntentos;
	}
}
