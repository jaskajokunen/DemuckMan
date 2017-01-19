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
//Kun luokka toteuttaa ApplicationListenerin, siitä tulee starter-luokka
//Starter-luokka --> Määrittelee Libgdx-sovelluksen entry pointin
public class DemuckManJumpingMain implements ApplicationListener {
	
	//Asetetaan muuttujaan luokan nimi stringina
	//Käytetään lokitukseen
	private static final String TAG = DemuckManJumpingMain.class.getName();
	
	//Viittaukset WorldController ja WorldRenderer-luokkiin
	//Viittaukset mahdollistavat sen, että luokka voi päivittää,
	//hallita pelin virtausta ja rendata pelin nykyisen tilan näytölle
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	//Kun Android on pysätetty, halutaan että peli on pysäytetty
	private boolean paused;

	@Override
	//Kutsutaan, kun sovellus luodaan ensimmäisen kerran
	public void create() {
		
		//Asetetaan Libgdx:n lokitustaso DEBUG
		//Tällöin lokiin tulostetaan kaikki lokitettavat asiat ajettaessa
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		//Aluksi kutsutaan Assets-luokan staattista muuttujaa
		//Muuttujan avulla saadaan lukuoikeus Assets-luokkaan
		//Tämän jälkeen kutsutaan Assets-luokan init()
		//Kyseinen metodi alustaa asset managerin ja lataa assetit
		Assets.instance.init(new AssetManager());
				
		//Luodaan instanssit luokista
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		
		//Koska pelimaailma on aktiivinen, asetetaan muuttuja falseksi
		paused = false;
	}

	@Override
	//Kutsutaan, kun näytön kokoa muutetaan
	//Parametrit: uusi leveys ja uusi korkeus pikseleissä
	//Työpöydällä parametrien arvot asetetaan Main.java-luokassa
	public void resize(int width, int height) {
		
		//Koska halutaan, että WorldRenderer-luokka käsittelee
		//resize eventin, kutsutaan resize()
		worldRenderer.resize(width, height);
		
	}

	@Override
	//Kutsutaan, kun sovelluksen tulisi rendata
	public void render() {
		
		//Jos ei ole pysäytetty, niin päivitetään pelimaailma
		if(!paused) {
			//update() --> Sisältää pelilogiikan
			//Parametrina deltaTime --> Kuinka paljon aikaa on kulunut, kun viimeksi rendattiin frame
			//deltaTimen avulla voidaan asettaa päivityksiä pelimaailmaan
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		
		
		//Asetetetaan ruudun putsausväri: RGBA
		//Värin vaihteluväli on 0 -> 1 välillä
		//Nykyinen väri on sininen
		Gdx.gl.glClearColor(100/255.0f, 149/255.0f, 237/255.0f, 255/255.0f);
		
		//Suoritetaan ruudun putsaaminen, eli poistetaan ruudun aikaisempi sisältö
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//Rendataan pelinlogiikka näytölle
		worldRenderer.render();
		
	}

	@Override
	//Kutsutaan, kun sovellus on pysäytetty
	//Androidissa vastaa sitä, kun painetaan Home-nappia
	public void pause() {
		
		//Kun sovellus on pysätetty, asetetaan muuttuja true
		paused = true;
	}

	@Override
	//Kutsutaan, kun sovellus palaa paused-tilasta
	//Androidissa vastaa sitä, kun activity saa fokuksen takaisin
	public void resume() {
		
		Assets.instance.init(new AssetManager());
		//Kun ei ole pysätetty, asetetaan muuttujaan false
		paused = false;
	}

	@Override
	public void dispose() {
		
		//Koska halutaan, että WorldRenderer-luokka käsittelee
		//dispose eventin, kutsutaan dispose()
		worldRenderer.dispose();
		
		//Vapautetaan assetit, joita ei enää tarvita
		Assets.instance.dispose();
		
	}

}
