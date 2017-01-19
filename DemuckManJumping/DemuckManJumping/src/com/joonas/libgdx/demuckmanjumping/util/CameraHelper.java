package com.joonas.libgdx.demuckmanjumping.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.joonas.libgdx.demuckmanjumping.game.objects.AbstractGameObject;

public class CameraHelper {
	
	//Asetetaan muuttujaan luokan nimi stringina
	//K�ytet��n lokitukseen
	private static final String TAG = CameraHelper.class.getName();
	
	//M��ritell��n konstantit, jotka kertovat zoomauksen min ja max
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	
	//Muuttuja kertoo kameran nykyisen sijainnin
	private Vector2 position;
	
	//Muuttuja kertoo, kuinka paljon kameraa on zoomattu
	private float zoom;
	
	//Viittaus AbstractGameObject-luokkaan
	//Muuttujan avulla voidaan kertoa, mit� game objectia kamera seuraa
	private AbstractGameObject target;
	
	//Konstruktori
	public CameraHelper() {
		
		//Luodaan kaksiulotteinen vektori kohtaan (0,0)
		position = new Vector2();
		
		//Alustetaan zoomin arvoksi 1
		zoom = 1.0f;
	}
	
	//Metodia pit�� kutsua jokaisessa p�ivitys sykliss�
	//P�ivitt�� kameran sijainnin
	public void update(float deltaTime) {
		
		//Jos kamera ei seuraa gameobjecti, niin palataan
		if(!hasTarget()) return;
		
		//Muutetaan muuttujaa, joka sis�lt�� kameran uuden x-sijainnin
		position.x = target.position.x + target.origin.x;
		
		//Muutetaan muuttujaa, joka sis�lt�� kameran uuden y-sijainnin
		position.y = target.position.y + target.origin.y;
		
		//Estet��n, ettei kamera mene liian alas
		//Math.max() --> Palauttaa kahdesta vertailtavasta arvon,
		//joka on l�hemp�n� positiivista arvoa
		//Koska position.y voi alittaa -1, niin k�ytet��n Math.max
		//metodia rajoittamaan kameran y-sijaintia
		position.y = Math.max(-1f, position.y);
	}
	
	//Metodilla voidaan asettaa kameralle uusi sijainti
	//Parametrit: asetettavat uudet x ja y-sijainnit
	public void setPosition(float x, float y) {
		this.position.set(x,y);
	}
	
	//Metodilla voidaan hakea kameran nykyinen sijainti
	public Vector2 getPosition() {
		return position;
	}
	
	//Metodilla voidaan kasvattaa zoom-arvoa
	//Parametrina, kuinka paljon kasvatetaan zoom-arvoa
	public void addZoom (float amount) {
		setZoom(zoom + amount);
	}

	//Metodilla voidaan asettaa kameran zoom-arvo
	public void setZoom(float zoom) {
		
		//clamp() --> Muokataan arvoa niin, ett� se on tietyss� vaihteluv�liss�
		//T�ss� tapauksessa vaihteluv�li on MAX_ZOOM_IN ja MAX_ZOOM_OUT v�liss�
		//T�m� tehd��n, koska halutaan ett� sill� on maksimi zoomi in ja maksimi zoom out
		/*
		Parametrit: value, min, max
		Jos value pienempi kuin min --> Palautetaan min
		Jos value on suurempi kuin max --> Palautetaan max
		Muussa tapauksessa palautetaan value
		Esim. alkutilanteessa zoom on 1, jolloin se on suurempi kuin min
		ja pienempi kuin max, jolloin palautetaan value, eli 1
		*/
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}
	
	//Metodilla voidaan hakea kameran zoom-arvo
	public float getZoom() {
		return zoom;
	}
	
	//Metodin avulla asetetaan game object, jota kamera seuraa
	public void setTarget(AbstractGameObject target) {
		
		//Asetetaan, ett� kamera seuraa parametrina annettua game objectia
		this.target = target;
	}
	
	//Metodilla voidaan hakea, mit� game objectia kamera seuraa
	public AbstractGameObject getTarget() {
		return target;
	}

	//Palauttaa booleanin, joka kertoo, seuraako kamera game objectia
	public boolean hasTarget() {
		//Palauttaa booleanin, joka kertoo, onko muuttuja target tyhj�
		//True, jos target ei ole tyhj�, eli silloin seurataan game objectia
		return target != null;
	}
	
	//Metodilla saadaan selville, seuraako kamera parametrina annettua game objectia
	public boolean hasTarget(AbstractGameObject target) {
		
		//Jos kamera seuraa game objectia ja seurattava game object on sama kuin parametrina annettu
		//T�ll�in palautetaan true
		return hasTarget() && this.target.equals(target);
	}
	
	//Metodia tulee kutsua, kun aloitetaan uuden framen rendaaminen
	//Metodilla p�ivitet��n kameran attribuutit
	public void applyTo(OrthographicCamera camera) {
		
		//Asetetaan kameraan uusi x-sijainti
		camera.position.x = position.x;
		
		//Asetetaan kameraan uusi y-sijainti
		camera.position.y = position.y;
		
		//Asetetaan kameraan uusi zoomaus-arvo
		camera.zoom = zoom;
		
		//Lasketaan kameralle uudestaan projektio ja view matriisit, sek� katkaistun kartion tasot
		//Metodia kutsutaan, kun on muutettu cameran attribuutteja
		camera.update();
	}

}
