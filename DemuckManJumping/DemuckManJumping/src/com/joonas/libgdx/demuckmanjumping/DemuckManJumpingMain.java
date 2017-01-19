package com.joonas.libgdx.demuckmanjumping;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.joonas.libgdx.demuckmanjumping.game.Assets;
import com.joonas.libgdx.demuckmanjumping.game.WorldController;
import com.joonas.libgdx.demuckmanjumping.game.WorldRenderer;

//Luokka, joka toteuttaa ApplicationListener-interfacen
//Kun luokka toteuttaa ApplicationListenerin, siit� tulee starter-luokka
//Starter-luokka --> M��rittelee Libgdx-sovelluksen entry pointin
public class DemuckManJumpingMain implements ApplicationListener {
	
	//Asetetaan muuttujaan luokan nimi stringina
	//K�ytet��n lokitukseen
	private static final String TAG = DemuckManJumpingMain.class.getName();
	
	//Viittaukset WorldController ja WorldRenderer-luokkiin
	//Viittaukset mahdollistavat sen, ett� luokka voi p�ivitt��,
	//hallita pelin virtausta ja rendata pelin nykyisen tilan n�yt�lle
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	//Kun Android on pys�tetty, halutaan ett� peli on pys�ytetty
	private boolean paused;

	@Override
	//Kutsutaan, kun sovellus luodaan ensimm�isen kerran
	public void create() {
		
		//Asetetaan Libgdx:n lokitustaso DEBUG
		//T�ll�in lokiin tulostetaan kaikki lokitettavat asiat ajettaessa
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		//Aluksi kutsutaan Assets-luokan staattista muuttujaa
		//Muuttujan avulla saadaan lukuoikeus Assets-luokkaan
		//T�m�n j�lkeen kutsutaan Assets-luokan init()
		//Kyseinen metodi alustaa asset managerin ja lataa assetit
		Assets.instance.init(new AssetManager());
				
		//Luodaan instanssit luokista
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		
		//Koska pelimaailma on aktiivinen, asetetaan muuttuja falseksi
		paused = false;
	}

	@Override
	//Kutsutaan, kun n�yt�n kokoa muutetaan
	//Parametrit: uusi leveys ja uusi korkeus pikseleiss�
	//Ty�p�yd�ll� parametrien arvot asetetaan Main.java-luokassa
	public void resize(int width, int height) {
		
		//Koska halutaan, ett� WorldRenderer-luokka k�sittelee
		//resize eventin, kutsutaan resize()
		worldRenderer.resize(width, height);
		
	}

	@Override
	//Kutsutaan, kun sovelluksen tulisi rendata
	public void render() {
		
		//Jos ei ole pys�ytetty, niin p�ivitet��n pelimaailma
		if(!paused) {
			//update() --> Sis�lt�� pelilogiikan
			//Parametrina deltaTime --> Kuinka paljon aikaa on kulunut, kun viimeksi rendattiin frame
			//deltaTimen avulla voidaan asettaa p�ivityksi� pelimaailmaan
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		
		
		//Asetetetaan ruudun putsausv�ri: RGBA
		//V�rin vaihteluv�li on 0 -> 1 v�lill�
		//Nykyinen v�ri on sininen
		Gdx.gl.glClearColor(100/255.0f, 149/255.0f, 237/255.0f, 255/255.0f);
		
		//Suoritetaan ruudun putsaaminen, eli poistetaan ruudun aikaisempi sis�lt�
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//Rendataan pelinlogiikka n�yt�lle
		worldRenderer.render();
		
	}

	@Override
	//Kutsutaan, kun sovellus on pys�ytetty
	//Androidissa vastaa sit�, kun painetaan Home-nappia
	public void pause() {
		
		//Kun sovellus on pys�tetty, asetetaan muuttuja true
		paused = true;
	}

	@Override
	//Kutsutaan, kun sovellus palaa paused-tilasta
	//Androidissa vastaa sit�, kun activity saa fokuksen takaisin
	public void resume() {
		
		Assets.instance.init(new AssetManager());
		//Kun ei ole pys�tetty, asetetaan muuttujaan false
		paused = false;
	}

	@Override
	public void dispose() {
		
		//Koska halutaan, ett� WorldRenderer-luokka k�sittelee
		//dispose eventin, kutsutaan dispose()
		worldRenderer.dispose();
		
		//Vapautetaan assetit, joita ei en�� tarvita
		Assets.instance.dispose();
		
	}

}
