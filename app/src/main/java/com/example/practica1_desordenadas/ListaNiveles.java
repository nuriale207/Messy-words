package com.example.practica1_desordenadas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;

public class  ListaNiveles extends Observable {
	private ArrayList<Nivel> misNiveles;
	private Collection<Integer> imagenesNiveles;
	private Collection<String> nombresNiveles;
	private Collection<Integer> idNiveles;
	private static ListaNiveles miListaNiveles =new ListaNiveles();
	private int puntuacion=50 ;
	private int intentos=10;
	private Nivel nivelAct;


	//Clase que se encarga de la gestión de niveles
	public ListaNiveles() {
		misNiveles=new ArrayList<Nivel>();

	}
	
	public static ListaNiveles getListaNiveles() {
		return miListaNiveles;
	}

	public void setNivel(int idNivel, int pPuntuacion, int nAciertos, HashSet<String> acertadas, int nIntentos) {
		//Cambia los atributos del nivel indicado
		Nivel nivel =getNivel(idNivel);
		nivel.set(pPuntuacion, nAciertos, acertadas, nIntentos);
	}

	public void resetearNiveles(){
		Iterator<Integer> itr=idNiveles.iterator();
		while (itr.hasNext()){
			int idNivel=itr.next();
			resetNivel(idNivel);
		}
	}
	public void resetNivel(int idNivel){
		//Resetea el nivel
		Nivel nivel=getNivel(idNivel);
		int index=misNiveles.indexOf(nivel);
		misNiveles.remove(nivel);
		nivel=new Nivel(idNivel,nivel.getNombre(),nivel.getIdImagen());
		misNiveles.add(index,nivel);
		Log.i("MYAPP",nivel.getLetras());
		Log.i("MYAPP", String.valueOf(nivel.getNumeroPalabras()));
	}

	public void cargarNiveles(Context contexto){
		//Realiza la carga de los niveles de la base de datos
		if (misNiveles.isEmpty()){
			//Lista con las imágenes y las letras
			ArrayList<Integer> imagenes=new ArrayList<Integer>();
			ArrayList<String> letras=new ArrayList<String>();
			ArrayList<Integer> ids=new ArrayList<Integer>();

			//Se obtienen los niveles de la base de datos
			String[] campos = new String[]{"Codigo", "Letras","IdImagen"};
			BaseDeDatos GestorDB = new BaseDeDatos (contexto, "NombreBD", null, 1);
			SQLiteDatabase db = GestorDB.getWritableDatabase();
			Cursor cu = db.query("Niveles", campos, null, null, null, null, null);
			int i=0;
			while (cu.moveToNext()) {
				Log.i("MYAPP","Obteniendo elemento");

				Integer Cod = cu.getInt(0);
				ids.add(Cod);
				String letrasNivel = cu.getString(1);
				String idImagen = cu.getString(2);
				int idInteger=contexto.getResources().getIdentifier(idImagen,"drawable",contexto.getPackageName());
				imagenes.add(idInteger);
				Nivel nivel=new Nivel(Cod,letrasNivel,idInteger);
				this.misNiveles.add(nivel);
				Log.i("MYAPP",nivel.getLetras());
				Log.i("MYAPP", String.valueOf(nivel.getNumeroPalabras()));

				Log.i("MYAPP", String.valueOf(idInteger));
				letras.add(letrasNivel);

			}
			cu.close();
			db.close();
			this.imagenesNiveles=imagenes;
			this.nombresNiveles=letras;
			this.idNiveles=ids;

		}

	}

	public ArrayList<Integer> getImagenesNiveles(){
		//Obtiene las imágenes del nivel
		return (ArrayList<Integer>) this.imagenesNiveles;
	}

	public ArrayList<Integer> getIdNiveles(){
		//Obtiene los ids de los niveles
		return (ArrayList<Integer>) this.idNiveles;
	}

	public ArrayList<String> getNombresNiveles(){
		//Devuelve la lista con los nombres de los niveles
		return (ArrayList<String>) this.nombresNiveles;
	}

	public int  getSiguienteNivel(){
		//Devuelve el id del siguiente nivel
		int idNivel=this.nivelAct.getId();
		Iterator<Nivel> itr=this.misNiveles.iterator();
		Nivel actual;
		while (itr.hasNext()){
			if(itr.next().getId()==idNivel){
				if(itr.hasNext()){
					return itr.next().getId();
				}
				else {
					return 0;
				}
			}
		}
		return 0;
	}

	public Nivel getNivel(int idNivel){
		//Devuelve el nivel con el id indicado
		Iterator<Nivel> itr=this.misNiveles.iterator();
		Nivel actual;
		while (itr.hasNext()){
			actual=itr.next();
			if(actual.getId()==idNivel){
				return actual;
				}
			}
		return null;

	}


	public String jugar(String palabra, Nivel nivel) {
		//Juega una palabra en un nivel
		return nivel.jugar(palabra);
	}
	public void setNivelAct(Nivel pNivel){
		//Modifica el nivel actual
		this.nivelAct=pNivel;
	}
	public void anadirNivel(Nivel lvl) {
		misNiveles.add(lvl);

	}
	
	public int obtenerIntentos(Nivel pNivel) {
		return pNivel.getIntentos();
	}
	

	public String getNombreNivel(Nivel pNivel) {
		return pNivel.getNombre();
	}
	

	public String getLetrasNivel(Nivel pNivel) {
		// TODO Auto-generated method stub
		return pNivel.getLetras();
	}

	public int obtenerPuntuacion(Nivel pNivel) {
		// TODO Auto-generated method stub
		return pNivel.getPuntuacion();
	}
	
}
