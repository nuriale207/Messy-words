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

public class ListaNiveles extends Observable {
	private Collection<Nivel> misNiveles;
	private Collection<Integer> imagenesNiveles;
	private Collection<String> nombresNiveles;
	private Collection<Integer> idNiveles;
	private static ListaNiveles miListaNiveles =new ListaNiveles();
	private int puntuacion=50 ;
	private int intentos=10;
	private Nivel nivelAct;
	
	public ListaNiveles() {
		misNiveles=new ArrayList<Nivel>();

		
	}
	
//	public void jugarPar(Nivel pnivel) {
//
//		Iterator<Nivel> itr= misNiveles.iterator();
//		int i=misNiveles.size();
//		while(puntuacion>= 0 && intentos>=0 && itr.hasNext()) {
//			Nivel lv=itr.next();
//			nivelAct= lv;
//			intentos=10;
//			setChanged();
//			notifyObservers(nivelAct);
//		}
//	}
	
	public static ListaNiveles getListaNiveles() {
		return miListaNiveles;
	}

	public void setNivel(int idNivel, int pPuntuacion, int nAciertos, HashSet<String> acertadas, int nIntentos) {
		Nivel nivel =getNivel(idNivel);
		nivel.set(pPuntuacion, nAciertos, acertadas, nIntentos);
	}

	public void cargarNiveles(Context contexto){
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
				this.misNiveles.add(new Nivel(Cod,letrasNivel,idInteger));

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
		return (ArrayList<Integer>) this.imagenesNiveles;
	}

	public ArrayList<Integer> getIdNiveles(){
		return (ArrayList<Integer>) this.idNiveles;
	}

	public ArrayList<String> getNombresNiveles(){
		return (ArrayList<String>) this.nombresNiveles;
	}

	public int  getSiguienteNivel(){
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
//		System.out.println(palabra);
//		nivelAct.jugar(palabra);
//		intentos--;
//		setChanged();
//		notifyObservers(1);
//
//		if(intentos==0) {
//			System.out.println("dentro");
//			Iterator<Nivel> itr= misNiveles.iterator();
//			if(itr.hasNext()) {
//				nivelAct=itr.next();
//				intentos=10;
//				setChanged();
//				notifyObservers(nivelAct);
//
//			}
//		}
//		if(puntuacion==0) {
//			return;
//		}
		return nivel.jugar(palabra);
	}
	public void setNivelAct(Nivel pNivel){
		this.nivelAct=pNivel;
	}
	public void anadirNivel(Nivel lvl) {
		misNiveles.add(lvl);
//		nivelAct=lvl;
//		setChanged();
//		notifyObservers(nivelAct);
	}
	
	public int obtenerIntentos(Nivel pNivel) {
		return pNivel.getIntentos();
	}
	
//	public void cambiarPuntuacion(char signo, int puntos) {
//		if(signo=='-') {
//			puntuacion=puntuacion-puntos;
//		}
//		else {
//			puntuacion=puntuacion+puntos;
//		}
//
//		setChanged();
//		notifyObservers(puntuacion);
//
//	}
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
