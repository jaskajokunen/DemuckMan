package com.joonas.libgdx.demuckmanjumping.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.joonas.libgdx.demuckmanjumping.util.Constants;

public class WorldRenderer implements Disposable {
	
	//Ortograafisen kameran avulla tehd��n rendaaminen
	//Kyseist� kameraa voidaan k�ytt�� kaksiulotteisten projektioiden tekemiseen
	private OrthographicCamera camera;
	
	//Toinen ortograafinen kamera, jota k�ytet��n pelin GUI:n rendaamiseen
	private OrthographicCamera cameraGUI;
	
	//SpriteBatchin avulla voidaan piirt�� kaikki oliot n�yt�lle,
	//suhteessa kameran nykyisiin asetuksiin (sijainti, zoomaus yms.)
	//Koska SpriteBatch toteuttaa Disposable interfacen
	// --> Pit�� kutsua sen dispose(), kun sit� ei tarvita
	private SpriteBatch batch;
	private WorldController worldController;
	
	//Konstruktori
	//Parametrina WorldController-luokan instanssi
	public WorldRenderer(WorldController worldController) {
		
		//Varastoidaan viittaus WorldController-luokan instanssiin
		//Vaaditaan sen takia, ett� renderer p��sisi k�siksi game objecteihin
		//,joita controller hallinnoi
		this.worldController = worldController;
		
		//Alustetaan luokka
		init();
	}
	
	//Metodi, jolla alustetaan luokka
	private void init() {
		
		//Luodaan SpriteBatch-luokan instanssi
		//K�ytet��n rendaus teht�viin
		batch = new SpriteBatch();
		
		//Luodaan OrthographciCamera-luokasta instanssi
		//Kameran viewport --> M��rittelee kameran katsoman pelimaailman koon
		//Pelimaailmassa n�hd��n ainoastaan se, mit� kamera n�kee
		//Parametrit:
		//Viewportin leveys
		//Viewportin korkeus
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		
		//Asetetaan kameran sijainti kohtaan 0,0,0 x,y,z-akseleilla
		camera.position.set(0,0,0);
		
		//Metodia kutsutaan, kun on muutettu cameran attribuutteja
		camera.update();
		
		//Luodaan toinen OrthographicCamera-luokan instanssi
		//T�ll� kameralla on suurempi Viewport, koska muuten ei voitaisi rendata bitmapin fonttia
		//,jonka korkeus on 15 pikseli�
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		
		//Asetetaan kameran sijainti kohtaan 0,0,0 x,y,z-akseleilla
		cameraGUI.position.set(0,0,0);
		
		//K��nnet��n y-akseli
		cameraGUI.setToOrtho(true);
				
		//Metodia kutsutaan, kun on muutettu cameran attribuutteja
		cameraGUI.update();
		
	}
	
	//Sis�lt�� logiikan, joka m��rittelee, miss� j�rjestyksess�
	//game objectit piirret��n toistensa p��lle
	public void render() {
		renderWorld(batch);
		renderGui(batch);
	}
	
	private void renderWorld(SpriteBatch batch) {
		
		//P�ivitet��n kameran attribuutit
		worldController.cameraHelper.applyTo(camera);
				
		//Asetetaan SpriteBatchin k�ytt�m� projektio matriisi
		//Parametrina projekito matriisi
		//camera.combined --> Yhdistetty projektio ja view matriisi
		batch.setProjectionMatrix(camera.combined);
				
		//Asetetaan SpriteBatch valmiiksi piirt�miseen
		//Ennen kuin voidaan suorittaa piirt�miskomentoja, pit�� kutsua t�t� metodia
		batch.begin();
		
		//Piirret��n taso
		worldController.level.render(batch);
				
		//Lopetetaan rendaaminen
		batch.end();
		
	}
	
	//Metodi, jolla voidaan piirt�� kolikko ja pelaajan pistem��r�
	private void renderGuiScore (SpriteBatch batch ) {
		
		//Tekstuurin x ja y-sijainti
		float x = -15;
		float y = -15;
		
		//draw() --> Leikkaa suorakulmion m��ritellyst� tekstuurista ja piirt�� sen sijaintiin
		//Assets.instance --> Haetaan tekstuuri atlaksen alikuva
		//Parametrit:
		//Tekstuuri atlaksen alikuva, joka kolikko
		//Tekstuurin x-sijainti
		//Tekstuurin y-sijainti
		////nollapisteen x-sijainti, nollapisteen y-sijainti
		//m��rittelee suhteelllisen sijainnin, jonne suorakulmio siirret��n
		//nollapiste (0,0) tarkoittaa kulmaa, joka vasemmalla alhaalla
		//leveys, korkeus
		//m��rittelev�t n�ytett�v�n kuvan mittasuhteet
		//skaalaus x-suunnassa, skaalaus y-suunnassa
		//m��rittelev�t suorakulmion skaalauksen nollapisteen ymp�ri
		//rotaatio
		//m��rittelee suorakulmion rotaation nollapisteen ymp�ri
		batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		
		//Piirret��n string m��riteltyyn sijaintiin
		//Assets.instance --> Haetaan Assets-luokan sis�inen luokka, joka m��rittelee bitmap fontin
		//Parametrit:
		//K�ytett�v� SpriteBatch-olio
		//Kirjoitettava teksti
		//Tekstin x-sijainti
		//Tekstin y-sijainti
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x + 75, y + 37);
	}
	
	//Metodi, jolla voidaan piirt�� pelaajan el�m�t
	private void renderGuiExtraLive(SpriteBatch batch) {
		
		//Tekstuurin x ja y-sijainti
		float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
		float y = -15;
		
		//Loopataan pelaajan el�mien m��r�n verran
		for(int i = 0; i < Constants.LIVES_START; i++) {
			
			//Jos pelaajalla on el�mi� yht� paljon tai v�hemm�n kuin indeksin arvo
			//Eli, jos pelaaja on menett�nyt el�m�n
			if(worldController.lives <= i) {
				//Muutetaan piirrett�v�� v�ri� tummemmaksi
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			}
			
			//draw() --> Leikkaa suorakulmion m��ritellyst� tekstuurista ja piirt�� sen sijaintiin
			//Assets.instance --> Haetaan tekstuuri atlaksen alikuva
			//Parametrit:
			//Tekstuuri atlaksen alikuva, joka pelaajan hahmo
			//Tekstuurin x-sijainti, jota liikutetaan, etteiv�t ne ole toisissaan kiinni
			//Tekstuurin y-sijainti
			////nollapisteen x-sijainti, nollapisteen y-sijainti
			//m��rittelee suhteelllisen sijainnin, jonne suorakulmio siirret��n
			//nollapiste (0,0) tarkoittaa kulmaa, joka vasemmalla alhaalla
			//leveys, korkeus
			//m��rittelev�t n�ytett�v�n kuvan mittasuhteet
			//skaalaus x-suunnassa, skaalaus y-suunnassa
			//m��rittelev�t suorakulmion skaalauksen nollapisteen ymp�ri
			//rotaatio
			//m��rittelee suorakulmion rotaation nollapisteen ymp�ri
			batch.draw(Assets.instance.playerChar.playerCharacter, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
					
			//Muutetaan v�ri takaisin vaaleammaksi
			batch.setColor(1, 1, 1, 1);
		}
	}
	
	//Metodi, jolla voidaan piirt�� FPS-laskuri
	//Laskurin v�ri vaihtelee FPS:n mukaan
	private void renderGuiFpsCounter (SpriteBatch batch) {
		
		//Tekstuurin x ja y-sijainti
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI . viewportHeight - 15;
		
		//Muuttujaan haetaan keskim��r�inen fps
		int fps = Gdx.graphics.getFramesPerSecond();
		
		//Alustetaan k�ytett�v� fontti
		//Assets.instance --> Haetaan Assets-luokan sis�inen luokka, joka m��rittelee bitmap fontin
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		
		//Jos FPS 45 tai yli
		if(fps >= 45) {
			//Muutetaan fontin v�ri vihre�ksi
			fpsFont.setColor(0,1,0,1);
		}
		//Jos FPS 30 tai yli
		else if(fps >= 30) {
			//Muutetaan fontin v�ri keltaiseksi
			fpsFont.setColor(1,1,0,1);
		}
		//Jos FPS alle 30
		else {
			//Muutetaan fontin v�ri punaiseksi
			fpsFont.setColor(1,0,0,1);
		}
		
		//Piirret��n string m��riteltyyn sijaintiin
		//Parametrit:
		//K�ytett�v� SpriteBatch-olio
		//Kirjoitettava teksti
		//Tekstin x-sijainti
		//Tekstin y-sijainti
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		
		//Muutetaan fontin v�ri valkoiseksi
		fpsFont.setColor(1,1,1,1);
		
	}
	
	//Metodi, jolla voidaan piirt�� GUI
	private void renderGui (SpriteBatch batch) {
		
		//Asetetaan SpriteBatchin k�ytt�m� projektio matriisi
		//Parametrina projekito matriisi
		//camera.combined --> Yhdistetty projektio ja view matriisi
		batch.setProjectionMatrix(cameraGUI.combined);
		
		//Asetetaan SpriteBatch valmiiksi piirt�miseen
		//Ennen kuin voidaan suorittaa piirt�miskomentoja, pit�� kutsua t�t� metodia
		batch.begin();
		
		//Piirret��n kolikko ja pelaajan pistem��r�
		//Sijainti on vasen-yl� reunassa
		renderGuiScore(batch);
		
		//Pirret��n ker�tyn sulan-ikoni
		//Sijainti on vasen-yl� reunassa
		renderGUIFeatherPowerUp(batch);
		
		
		//Piirret��n pelaajan el�m�t
		//Sijainti on oikea-yl� reunassa
		renderGuiExtraLive(batch);
		
		//Piiret��n FPS-laskuri
		renderGuiFpsCounter(batch);
		
		//Piirret��n game over-teksti
		renderGUIGameOverMessage(batch);
		
		//Lopetetaan rendaaminen
		batch.end();
	}
	
	//Metodi, jolla voidaan piirt�� game over-viesti
	private void renderGUIGameOverMessage (SpriteBatch batch) {
		
		//Lasketaan GUI-kameran viewportin keskusta
		//Asetetaan x-sijainniksi, puolet cameraGUI:n viewportin leveydest�
		float x = cameraGUI.viewportWidth / 2;
		//Asetetaan y-sijainniksi, puolet cameraGUI:n viewportin korkeudesta
		float y = cameraGUI.viewportHeight / 2;
		
		//Jos pelaaja kuoli, kun h�nell� oli nolla el�m��
		if(worldController.isGameOver()) {
			
			//Alustetaan k�ytett�v� fontti
			//Assets.instance --> Haetaan Assets-luokan sis�inen luokka, joka m��rittelee bitmap fontin
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			
			//Asetetaan fontin v�ri
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			
			//Piirret��n string, joka voi sis�lt�� uusia rivej�
			//Parametrit:
			//spriteBatch --> K�ytett�v� SpriteBatch-olio 
			//str --> Kirjoitettava viesti
			//Viestin x-sijainti 
			//Viestin y-sijainti
			//alignmentWidth --> Suuntauksen leveys, joka on 0 
			//alignment --> Suuntaus, jonka avulla keskitet��n teksti vaakasuorasti
			//Keskitys tehd��n HAlignment.Center konstantin avulla
			fontGameOver.drawMultiLine(batch, "GAME OVER", x, y, 0, BitmapFont.HAlignment.CENTER);
			
			fontGameOver.setColor(1, 1, 1, 1);
			
		}
	}
	
	private void renderGUIFeatherPowerUp(SpriteBatch batch) {
		
		//x-sijainti
		float x = -15;
		
		//y-sijainti
		float y = 30;
		
		//Haetaan muuttujaan, miten pitk��n power-up on voimassa
		float timeLeftFeatherPowerUp = worldController.level.demuckMan.timeLeftFeatherPowerUp;
		
		//Jos power-up on voimassa
		if(timeLeftFeatherPowerUp > 0) {
			
			//Jos power-up on voimassa alle 4 sekuntia
			if(timeLeftFeatherPowerUp < 4) {
				
				//Feidaus-efekti
				//Esim1. jos timeLeftFeatherPowerUp on 3
				//--> 3 * 5 --> 15 % 2 --> 1, jolloin muutetaan v�ri�
				//Esim2. jos timeLeftFeatherPowerUp on 2
				//--> 2 * 5 --> 10 % 2 --> 0, jolloin ei muuteta v�ri�
				//Esim3. jos timeLeftFeatherPowerUp on 1
				//--> 1 * 5 --> 5 % 2 --> 1, jolloin muutetaan v�ri�
				if (((int)(timeLeftFeatherPowerUp * 5) % 2) != 0) {
					
					//Muutetaan v�ri�
					batch.setColor(1, 1, 1, 0.5f);
				}
			}
			//draw() --> Leikkaa suorakulmion m��ritellyst� tekstuurista ja piirt�� sen sijaintiin
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
			//m��rittelee suhteelllisen sijainnin, jonne suorakulmio siirret��n
			//nollapiste (0,0) tarkoittaa kulmaa, joka vasemmalla alhaalla
			//leveys, korkeus
			100,
			100,
			//m��rittelev�t n�ytett�v�n kuvan mittasuhteet
			//skaalaus x-suunnassa, skaalaus y-suunnassa
			0.35f,
			-0.35f,
			//m��rittelev�t suorakulmion skaalauksen nollapisteen ymp�ri
			//rotaatio
			0
			//m��rittelee suorakulmion rotaation nollapisteen ymp�ri
			);
			
			//Muutetaan v�ri valkoiseksi
			batch.setColor(1, 1, 1, 1);
			
			//Piirret��n string m��riteltyyn sijaintiin
			//Assets.instance --> Haetaan Assets-luokan sis�inen luokka, joka m��rittelee bitmap fontin
			//Parametrit:
			//K�ytett�v� SpriteBatch-olio
			//Kirjoitettava teksti
			//Tekstin x-sijainti
			//Tekstin y-sijainti
			Assets.instance.fonts.defaultSmall.draw(batch, "" + (int)timeLeftFeatherPowerUp, x + 60, y + 57);
		}
	}
	
	

	//Kun n�yt�n kokoa muutetaan, kutsutaan t�t� metodia
	//Mahdollistaa mukautumaan uuden n�yt�n kuvasuhteisiin
	//Parametrit: uusi leveys ja uusi korkeus pikseleiss�
	public void resize (int width, int height) {
		
		//Lasketaan viewportin width, kun n�yt�n kokoa muutettu
		//Laskun tuloksena, maailman n�kyviss� oleva korkeus on aina laajimillaan
		//Maailman leveys skaalautuu sen mukaan, miten on laskettu kuvasuhde
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		
		//Lasketaan kameralle uudestaan projektio ja view matriisit, sek� katkaistun kartion tasot
		//Metodia kutsutaan, kun on muutettu cameran attribuutteja
		camera.update();
		
		//Asetetaan viewportin korkeus, kun n�yt�n kokoa muutettu
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		
		//Lasketaan viewportin leveys, kun n�yt�n kokoa muutettu
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float)height * (float)width);
		
		//Asetetaan kameran sijainti uudelleen, kun n�yt�n kokoa muutettu
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		
		//Metodia kutsutaan, kun on muutettu cameran attribuutteja
		cameraGUI.update();
	}

	@Override
	//Metodissa suoritetaan putsaaminen ja resurssien vapauttaminen, joita sovellus k�ytt��
	public void dispose() {
		
		//Poistetaan kaikki resurssit, jotka liityv�t t�h�n SpriteBatchiin
		batch.dispose();
	}
	
	

}
