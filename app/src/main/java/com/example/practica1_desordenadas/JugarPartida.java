package com.example.practica1_desordenadas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class JugarPartida extends Observable {
	private Collection<Nivel> misNiveles;
	private static JugarPartida miPartida=new JugarPartida();
	private int puntuacion=50 ;
	private int intentos=10;
	private Nivel nivelAct;
	
	public JugarPartida() {
		misNiveles=new ArrayList<Nivel>();

		
	}
	
	public void jugarPar(Nivel pnivel) {

		Iterator<Nivel> itr= misNiveles.iterator();
		int i=misNiveles.size();
		while(puntuacion>= 0 && intentos>=0 && itr.hasNext()) {
			Nivel lv=itr.next();
			nivelAct= lv;
			intentos=10;
			setChanged();
			notifyObservers(nivelAct);
		}
	}
	
	public static JugarPartida getPartida() {
		return miPartida;
	}
	
	public void jugar(String palabra,Nivel nivel) {
		System.out.println(palabra);
		nivelAct.jugar(palabra);
		intentos--;
		setChanged();
		notifyObservers(1);
		
		if(intentos==0) {
			System.out.println("dentro");
			Iterator<Nivel> itr= misNiveles.iterator();
			if(itr.hasNext()) {
				nivelAct=itr.next();
				intentos=10;
				setChanged();
				notifyObservers(nivelAct);
				
			}
		}
		if(puntuacion==0) {
			return;
		}
	}
	public void anadirNivel(Nivel lvl) {
		misNiveles.add(lvl);
//		nivelAct=lvl;
//		setChanged();
//		notifyObservers(nivelAct);
	}
	
	public int obtenerIntentos() {
		return intentos;
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
	public String getNombreNivel() {
		return nivelAct.getNombre();
	}
	

	public String getLetrasNivel() {
		// TODO Auto-generated method stub
		return nivelAct.getLetras();
	}

	public int obtenerPuntuacion() {
		// TODO Auto-generated method stub
		return puntuacion;
	}
	
}
