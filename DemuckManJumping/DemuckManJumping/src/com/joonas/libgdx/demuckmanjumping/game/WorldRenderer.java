package com.joonas.libgdx.demuckmanjumping.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.joonas.libgdx.demuckmanjumping.util.Constants;

public class WorldRenderer implements Disposable {
	
	//Ortograafisen kameran avulla tehdään rendaaminen
	//Kyseistä kameraa voidaan käyttää kaksiulotteisten projektioiden tekemiseen
	private OrthographicCamera camera;
	
	//Toinen ortograafinen kamera, jota käytetään pelin GUI:n rendaamiseen
	private OrthographicCamera cameraGUI;
	
	//SpriteBatchin avulla voidaan piirtää kaikki oliot näytölle,
	//suhteessa kameran nykyisiin asetuksiin (sijainti, zoomaus yms.)
	//Koska SpriteBatch toteuttaa Disposable interfacen
	// --> Pitää kutsua sen dispose(), kun sitä ei tarvita
	private SpriteBatch batch;
	private WorldController worldController;
	
	//Konstruktori
	//Parametrina WorldController-luokan instanssi
	public WorldRenderer(WorldController worldController) {
		
		//Varastoidaan viittaus WorldController-luokan instanssiin
		//Vaaditaan sen takia, että renderer pääsisi käsiksi game objecteihin
		//,joita controller hallinnoi
		this.worldController = worldController;
		
		//Alustetaan luokka
		init();
	}
	
	//Metodi, jolla alustetaan luokka
	private void init() {
		
		//Luodaan SpriteBatch-luokan instanssi
		//Käytetään rendaus tehtäviin
		batch = new SpriteBatch();
		
		//Luodaan OrthographciCamera-luokasta instanssi
		//Kameran viewport --> Määrittelee kameran katsoman pelimaailman koon
		//Pelimaailmassa nähdään ainoastaan se, mitä kamera näkee
		//Parametrit:
		//Viewportin leveys
		//Viewportin korkeus
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		
		//Asetetaan kameran sijainti kohtaan 0,0,0 x,y,z-akseleilla
		camera.position.set(0,0,0);
		
		//Metodia kutsutaan, kun on muutettu cameran attribuutteja
		camera.update();
		
		//Luodaan toinen OrthographicCamera-luokan instanssi
		//Tällä kameralla on suurempi Viewport, koska muuten ei voitaisi rendata bitmapin fonttia
		//,jonka korkeus on 15 pikseliä
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		
		//Asetetaan kameran sijainti kohtaan 0,0,0 x,y,z-akseleilla
		cameraGUI.position.set(0,0,0);
		
		//Käännetään y-akseli
		cameraGUI.setToOrtho(true);
				
		//Metodia kutsutaan, kun on muutettu cameran attribuutteja
		cameraGUI.update();
		
	}
	
	//Sisältää logiikan, joka määrittelee, missä järjestyksessä
	//game objectit piirretään toistensa päälle
	public void render() {
		renderWorld(batch);
		renderGui(batch);
	}
	
	private void renderWorld(SpriteBatch batch) {
		
		//Päivitetään kameran attribuutit
		worldController.cameraHelper.applyTo(camera);
				
		//Asetetaan SpriteBatchin käyttämä projektio matriisi
		//Parametrina projekito matriisi
		//camera.combined --> Yhdistetty projektio ja view matriisi
		batch.setProjectionMatrix(camera.combined);
				
		//Asetetaan SpriteBatch valmiiksi piirtämiseen
		//Ennen kuin voidaan suorittaa piirtämiskomentoja, pitää kutsua tätä metodia
		batch.begin();
		
		//Piirretään taso
		worldController.level.render(batch);
				
		//Lopetetaan rendaaminen
		batch.end();
		
	}
	
	//Metodi, jolla voidaan piirtää kolikko ja pelaajan pistemäärä
	private void renderGuiScore (SpriteBatch batch ) {
		
		//Tekstuurin x ja y-sijainti
		float x = -15;
		float y = -15;
		
		//draw() --> Leikkaa suorakulmion määritellystä tekstuurista ja piirtää sen sijaintiin
		//Assets.instance --> Haetaan tekstuuri atlaksen alikuva
		//Parametrit:
		//Tekstuuri atlaksen alikuva, joka kolikko
		//Tekstuurin x-sijainti
		//Tekstuurin y-sijainti
		////nollapisteen x-sijainti, nollapisteen y-sijainti
		//määrittelee suhteelllisen sijainnin, jonne suorakulmio siirretään
		//nollapiste (0,0) tarkoittaa kulmaa, joka vasemmalla alhaalla
		//leveys, korkeus
		//määrittelevät näytettävän kuvan mittasuhteet
		//skaalaus x-suunnassa, skaalaus y-suunnassa
		//määrittelevät suorakulmion skaalauksen nollapisteen ympäri
		//rotaatio
		//määrittelee suorakulmion rotaation nollapisteen ympäri
		batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		
		//Piirretään string määriteltyyn sijaintiin
		//Assets.instance --> Haetaan Assets-luokan sisäinen luokka, joka määrittelee bitmap fontin
		//Parametrit:
		//Käytettävä SpriteBatch-olio
		//Kirjoitettava teksti
		//Tekstin x-sijainti
		//Tekstin y-sijainti
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x + 75, y + 37);
	}
	
	//Metodi, jolla voidaan piirtää pelaajan elämät
	private void renderGuiExtraLive(SpriteBatch batch) {
		
		//Tekstuurin x ja y-sijainti
		float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
		float y = -15;
		
		//Loopataan pelaajan elämien määrän verran
		for(int i = 0; i < Constants.LIVES_START; i++) {
			
			//Jos pelaajalla on elämiä yhtä paljon tai vähemmän kuin indeksin arvo
			//Eli, jos pelaaja on menettänyt elämän
			if(worldController.lives <= i) {
				//Muutetaan piirrettävää väriä tummemmaksi
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			}
			
			//draw() --> Leikkaa suorakulmion määritellystä tekstuurista ja piirtää sen sijaintiin
			//Assets.instance --> Haetaan tekstuuri atlaksen alikuva
			//Parametrit:
			//Tekstuuri atlaksen alikuva, joka pelaajan hahmo
			//Tekstuurin x-sijainti, jota liikutetaan, etteivät ne ole toisissaan kiinni
			//Tekstuurin y-sijainti
			////nollapisteen x-sijainti, nollapisteen y-sijainti
			//määrittelee suhteelllisen sijainnin, jonne suorakulmio siirretään
			//nollapiste (0,0) tarkoittaa kulmaa, joka vasemmalla alhaalla
			//leveys, korkeus
			//määrittelevät näytettävän kuvan mittasuhteet
			//skaalaus x-suunnassa, skaalaus y-suunnassa
			//määrittelevät suorakulmion skaalauksen nollapisteen ympäri
			//rotaatio
			//määrittelee suorakulmion rotaation nollapisteen ympäri
			batch.draw(Assets.instance.playerChar.playerCharacter, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
					
			//Muutetaan väri takaisin vaaleammaksi
			batch.setColor(1, 1, 1, 1);
		}
	}
	
	//Metodi, jolla voidaan piirtää FPS-laskuri
	//Laskurin väri vaihtelee FPS:n mukaan
	private void renderGuiFpsCounter (SpriteBatch batch) {
		
		//Tekstuurin x ja y-sijainti
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI . viewportHeight - 15;
		
		//Muuttujaan haetaan keskimääräinen fps
		int fps = Gdx.graphics.getFramesPerSecond();
		
		//Alustetaan käytettävä fontti
		//Assets.instance --> Haetaan Assets-luokan sisäinen luokka, joka määrittelee bitmap fontin
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		
		//Jos FPS 45 tai yli
		if(fps >= 45) {
			//Muutetaan fontin väri vihreäksi
			fpsFont.setColor(0,1,0,1);
		}
		//Jos FPS 30 tai yli
		else if(fps >= 30) {
			//Muutetaan fontin väri keltaiseksi
			fpsFont.setColor(1,1,0,1);
		}
		//Jos FPS alle 30
		else {
			//Muutetaan fontin väri punaiseksi
			fpsFont.setColor(1,0,0,1);
		}
		
		//Piirretään string määriteltyyn sijaintiin
		//Parametrit:
		//Käytettävä SpriteBatch-olio
		//Kirjoitettava teksti
		//Tekstin x-sijainti
		//Tekstin y-sijainti
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		
		//Muutetaan fontin väri valkoiseksi
		fpsFont.setColor(1,1,1,1);
		
	}
	
	//Metodi, jolla voidaan piirtää GUI
	private void renderGui (SpriteBatch batch) {
		
		//Asetetaan SpriteBatchin käyttämä projektio matriisi
		//Parametrina projekito matriisi
		//camera.combined --> Yhdistetty projektio ja view matriisi
		batch.setProjectionMatrix(cameraGUI.combined);
		
		//Asetetaan SpriteBatch valmiiksi piirtämiseen
		//Ennen kuin voidaan suorittaa piirtämiskomentoja, pitää kutsua tätä metodia
		batch.begin();
		
		//Piirretään kolikko ja pelaajan pistemäärä
		//Sijainti on vasen-ylä reunassa
		renderGuiScore(batch);
		
		//Pirretään kerätyn sulan-ikoni
		//Sijainti on vasen-ylä reunassa
		renderGUIFeatherPowerUp(batch);
		
		
		//Piirretään pelaajan elämät
		//Sijainti on oikea-ylä reunassa
		renderGuiExtraLive(batch);
		
		//Piiretään FPS-laskuri
		renderGuiFpsCounter(batch);
		
		//Piirretään game over-teksti
		renderGUIGameOverMessage(batch);
		
		//Lopetetaan rendaaminen
		batch.end();
	}
	
	//Metodi, jolla voidaan piirtää game over-viesti
	private void renderGUIGameOverMessage (SpriteBatch batch) {
		
		//Lasketaan GUI-kameran viewportin keskusta
		//Asetetaan x-sijainniksi, puolet cameraGUI:n viewportin leveydestä
		float x = cameraGUI.viewportWidth / 2;
		//Asetetaan y-sijainniksi, puolet cameraGUI:n viewportin korkeudesta
		float y = cameraGUI.viewportHeight / 2;
		
		//Jos pelaaja kuoli, kun hänellä oli nolla elämää
		if(worldController.isGameOver()) {
			
			//Alustetaan käytettävä fontti
			//Assets.instance --> Haetaan Assets-luokan sisäinen luokka, joka määrittelee bitmap fontin
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			
			//Asetetaan fontin väri
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			
			//Piirretään string, joka voi sisältää uusia rivejä
			//Parametrit:
			//spriteBatch --> Käytettävä SpriteBatch-olio 
			//str --> Kirjoitettava viesti
			//Viestin x-sijainti 
			//Viestin y-sijainti
			//alignmentWidth --> Suuntauksen leveys, joka on 0 
			//alignment --> Suuntaus, jonka avulla keskitetään teksti vaakasuorasti
			//Keskitys tehdään HAlignment.Center konstantin avulla
			fontGameOver.drawMultiLine(batch, "GAME OVER", x, y, 0, BitmapFont.HAlignment.CENTER);
			
			fontGameOver.setColor(1, 1, 1, 1);
			
		}
	}
	
	private void renderGUIFeatherPowerUp(SpriteBatch batch) {
		
		//x-sijainti
		float x = -15;
		
		//y-sijainti
		float y = 30;
		
		//Haetaan muuttujaan, miten pitkään power-up on voimassa
		float timeLeftFeatherPowerUp = worldController.level.demuckMan.timeLeftFeatherPowerUp;
		
		//Jos power-up on voimassa
		if(timeLeftFeatherPowerUp > 0) {
			
			//Jos power-up on voimassa alle 4 sekuntia
			if(timeLeftFeatherPowerUp < 4) {
				
				//Feidaus-efekti
				//Esim1. jos timeLeftFeatherPowerUp on 3
				//--> 3 * 5 --> 15 % 2 --> 1, jolloin muutetaan väriä
				//Esim2. jos timeLeftFeatherPowerUp on 2
				//--> 2 * 5 --> 10 % 2 --> 0, jolloin ei muuteta väriä
				//Esim3. jos timeLeftFeatherPowerUp on 1
				//--> 1 * 5 --> 5 % 2 --> 1, jolloin muutetaan väriä
				if (((int)(timeLeftFeatherPowerUp * 5) % 2) != 0) {
					
					//Muutetaan väriä
					batch.setColor(1, 1, 1, 0.5f);
				}
			}
			//draw() --> Leikkaa suorakulmion määritellystä tekstuurista ja piirtää sen sijaintiin
			batch.draw(
			//Assets.instance --> Haetaan tekstuuri atlaksen alikuva
			//Parametrit:
			//Tekstuuri atlaksen alikuva, joka sulka
			Assets.instance.feather.feather,
			//Tekstuurin x-sijainti
			x,
			//Tekstuurin y-sijainti
			y,
			////nollapisteen x-sijainti, nollapisteen y-sijainti
			50,
			50,
			//määrittelee suhteelllisen sijainnin, jonne suorakulmio siirretään
			//nollapiste (0,0) tarkoittaa kulmaa, joka vasemmalla alhaalla
			//leveys, korkeus
			100,
			100,
			//määrittelevät näytettävän kuvan mittasuhteet
			//skaalaus x-suunnassa, skaalaus y-suunnassa
			0.35f,
			-0.35f,
			//määrittelevät suorakulmion skaalauksen nollapisteen ympäri
			//rotaatio
			0
			//määrittelee suorakulmion rotaation nollapisteen ympäri
			);
			
			//Muutetaan väri valkoiseksi
			batch.setColor(1, 1, 1, 1);
			
			//Piirretään string määriteltyyn sijaintiin
			//Assets.instance --> Haetaan Assets-luokan sisäinen luokka, joka määrittelee bitmap fontin
			//Parametrit:
			//Käytettävä SpriteBatch-olio
			//Kirjoitettava teksti
			//Tekstin x-sijainti
			//Tekstin y-sijainti
			Assets.instance.fonts.defaultSmall.draw(batch, "" + (int)timeLeftFeatherPowerUp, x + 60, y + 57);
		}
	}
	
	

	//Kun näytön kokoa muutetaan, kutsutaan tätä metodia
	//Mahdollistaa mukautumaan uuden näytön kuvasuhteisiin
	//Parametrit: uusi leveys ja uusi korkeus pikseleissä
	public void resize (int width, int height) {
		
		//Lasketaan viewportin width, kun näytön kokoa muutettu
		//Laskun tuloksena, maailman näkyvissä oleva korkeus on aina laajimillaan
		//Maailman leveys skaalautuu sen mukaan, miten on laskettu kuvasuhde
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		
		//Lasketaan kameralle uudestaan projektio ja view matriisit, sekä katkaistun kartion tasot
		//Metodia kutsutaan, kun on muutettu cameran attribuutteja
		camera.update();
		
		//Asetetaan viewportin korkeus, kun näytön kokoa muutettu
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		
		//Lasketaan viewportin leveys, kun näytön kokoa muutettu
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float)height * (float)width);
		
		//Asetetaan kameran sijainti uudelleen, kun näytön kokoa muutettu
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		
		//Metodia kutsutaan, kun on muutettu cameran attribuutteja
		cameraGUI.update();
	}

	@Override
	//Metodissa suoritetaan putsaaminen ja resurssien vapauttaminen, joita sovellus käyttää
	public void dispose() {
		
		//Poistetaan kaikki resurssit, jotka liityvät tähän SpriteBatchiin
		batch.dispose();
	}
	
	

}
