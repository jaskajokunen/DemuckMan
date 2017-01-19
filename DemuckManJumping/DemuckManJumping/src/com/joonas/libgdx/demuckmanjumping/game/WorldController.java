package com.joonas.libgdx.demuckmanjumping.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.joonas.libgdx.demuckmanjumping.game.objects.DemuckMan;
import com.joonas.libgdx.demuckmanjumping.game.objects.DemuckMan.JUMP_STATE;
import com.joonas.libgdx.demuckmanjumping.game.objects.Feather;
import com.joonas.libgdx.demuckmanjumping.game.objects.GoldCoin;
import com.joonas.libgdx.demuckmanjumping.game.objects.Rock;
import com.joonas.libgdx.demuckmanjumping.util.CameraHelper;
import com.joonas.libgdx.demuckmanjumping.util.Constants;

//Luokka haarautuu InputAdapter-luokasta
public class WorldController extends InputAdapter {
	
	//Asetetaan muuttujaan luokan nimi stringina
	//K‰ytet‰‰n lokitukseen
	private static final String TAG = WorldController.class.getName();
	
	//Kaksi suorakulmioita, joiden avulla testataan, onko yhteentˆrm‰ys tapahtunut
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	//Muuttujan avulla voidaan kertoa, 
	//miten pitk‰‰n viel‰ n‰ytet‰‰n game over-viesti
	private float timeLeftGameOverDelay;
	
	//Viittaus Level-luokkaan
	public Level level;
	
	//Muuttujan avulla lasketaan pelaajan el‰m‰t
	public int lives;
	
	//Muuttujan avulla lasketaan pelaajan pisteet
	public int score;
	
	//Viittaus CameraHelper-luokkaan
	public CameraHelper cameraHelper;
	
	//Metodin avulla voidaan kertoa, onko peli loppu
	//Peli on loppu, jos el‰mi‰ on v‰hemm‰n kuin 0
	public boolean isGameOver() {
		return lives < 0;
	}
	
	//Metodin avulla voidaan kertoa, onko pelaaja tippunut veteen
	//Koska vesi on asetettu alareunassa kohtaan 0, 
	//pit‰‰ tarkistaa, onko pelaajan y-sijainti t‰t‰ pienempi
	//K‰ytet‰‰n -5, koska halutaan lis‰t‰ viivett‰
	public boolean isPlayerInWater() {
		return level.demuckMan.position.y < -5;
	}
	
	//Konstruktori
	public WorldController() {
		init();
	}
	
	//Metodi, joka alustaa levelin
	private void initLevel() {
		
		//Alustetaan pisteiksi 0
		score = 0;
		
		//Alustetaan Level-olio
		//Parametrina tiedosto, joka sis‰lt‰‰ levelin
		level = new Level(Constants.LEVEL_01);
		
		//Asetetaan, ett‰ kamera seuraa demuckMan game objectia
		cameraHelper.setTarget(level.demuckMan);
	}
	
	//Metodi, jolla alustetaan luokka
	//K‰ytet‰‰n erillist‰ alustusmetodia, koska ei haluta aina uudestaan rakentaa luokkaa
	//, kun resetoidaan olio
	//Eli halutaan k‰ytt‰‰ olemassaolevia olioita uudestaan, jolloin tehokkuus paranee
	private void init() {
		
		//setInputProcessor() --> Kerrotaan Libgdx:lle, minne se l‰hett‰‰ saadut input eventit
		Gdx.input.setInputProcessor(this);
		
		//Luodaan instanssi CameraHelper-luokasta
		cameraHelper = new CameraHelper();
		
		timeLeftGameOverDelay = 0;
		
		//Asetetaan pelaajan el‰m‰t
		lives = Constants.LIVES_START;
		
		//Alustetaan taso
		initLevel();
		
	}
	
	

	
	//Metodi sis‰lt‰‰ pelin logiikan
	//Kutsutaan useita kertoja sekunnissa
	//Parametrina deltaTime --> Kuinka paljon aikaa on kulunut, kun viimeksi rendattiin frame
	//deltaTimen avulla voidaan asettaa p‰ivityksi‰ pelimaailmaan
	public void update (float deltaTime) {
		
		//Metodissa k‰sitell‰‰n kameran input eventit
		handleDebugInput(deltaTime);
		
		//Jos peli on loppu
		if(isGameOver()) {
			
			//V‰hennet‰‰n aikaa, jonka verran n‰ytet‰‰n game over-viesti
			timeLeftGameOverDelay -= deltaTime;
			
			//Jos on n‰ytetty tarpeeksi pitk‰‰n game over-viesti‰
			if(timeLeftGameOverDelay <0) {
				//Alustetaan WorldController-luokka uudestaan
				init();
			}
		}
		
		//Jos peli ei ole loppu
		else {
			//Metodissa k‰sitell‰‰n pelaajan input eventit
			handleInputGame(deltaTime);
		}
		
		//P‰ivitet‰‰n levelissa olevat game objectit
		level.update(deltaTime);
		
		//Testataan, tapahtuiko game objectien v‰lill‰ tˆrm‰yksi‰
		testCollisions();
		
		//P‰ivitet‰‰n kameran sijainti
		cameraHelper.update(deltaTime);
		
		//Jos el‰mi‰ on viel‰ j‰ljell‰ ja pelaaja on vedess‰
		if(!isGameOver() && isPlayerInWater()) {
			
			//V‰hennet‰‰n yksi el‰m‰
			lives--;
			
			//Jos pelaajalta loppuivat el‰m‰t
			if(isGameOver()) {
				
				//Asetetaan muuttujaan viiv‰stys, joka on 
				//kolme sekuntia pitk‰
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			}
			else{
				//Alustetaan taso uudestaan
				initLevel();
			}
		}
	}
	
	//Parametrina deltaTime --> Kuinka paljon aikaa on kulunut, kun viimeksi rendattiin frame
	private void handleDebugInput(float deltaTime) {
		
		//Jos sovelluksen alusta ei ole tyˆpˆyt‰, niin menn‰‰n iffiin
		//T‰llˆin palataan takaisin
		//getType() --> Mik‰ alusta sovelluksella k‰ytˆss‰
		if(Gdx.app.getType() != ApplicationType.Desktop) return;
		
		//Jos kamera ei seuraa demuckMan-oliota
		if(!cameraHelper.hasTarget(level.demuckMan)) {
			//Kameran kontrollit (liikkuminen)
			//Kameran liikkumisnopeus, joka 5 m/s
			float camMoveSpeed = 5 * deltaTime;
			
			//Kameran nopeuden kiihtyvyyskerroin
			float camMoveSpeedAccelerationFactor = 5;
			
			//Jos painettiin vasenta shiftia
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				
				//Kasvatetaan kameran liikkumisnopeutta hetkellisesti, kiihtyvyys kertoimella
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			}
			
			//Jos painettiin vasenta nuolin‰pp‰int‰
			if(Gdx.input.isKeyPressed(Keys.LEFT)) {
				
				//Liikutetaan kameraa vasemmalle
				moveCamera(-camMoveSpeed, 0);
			}
			
			//Jos painettiin oikeaa nuolin‰pp‰int‰
			if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
				
				//Liikutetaan kameraa oikealle
				moveCamera(camMoveSpeed, 0);
			}
			
			//Jos painettiin ylˆs nuolin‰pp‰int‰
			if(Gdx.input.isKeyPressed(Keys.UP)) {
				
				//Liikutetaan kameraa ylˆs
				moveCamera(0, camMoveSpeed);
			}
			
			//Jos painettiin alas nuolin‰pp‰int‰
			if(Gdx.input.isKeyPressed(Keys.DOWN)) {
				
				//Liikutetaan kameraa alas
				moveCamera(0,-camMoveSpeed);
			}
			
			//Jos painettiin backspacea
			if(Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
				
				//Asetetaan kameran sijainti nollapisteeseen
				cameraHelper.setPosition(0, 0);
			}
			
		}
		
		//Kameran kontrollit (zoomaaminen)
		//Kameran zoomausnopeus, joka 1 m/s
		float camZoomSpeed = 1*deltaTime;
		
		//Kameran zoomauksen kiihtyvyyskerroin
		float camZoomSpeedAccelerationFactor = 5;
		
		//Jos vasenta shiftia painetaan
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			
			//Kasvatetaan kameran zoomausnopeutta hetkellisesti, kiihtyvyys kertoimella
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		}
		
		//Jos painetaan pilkku-n‰pp‰int‰
		if(Gdx.input.isKeyPressed(Keys.COMMA)) {
			
			//Kasvatetaan kameran zoomia, parametrina annetun arvon verran
			cameraHelper.addZoom(camZoomSpeed);
		}
		
		//Jos painetaan piste-n‰pp‰int‰
		if(Gdx.input.isKeyPressed(Keys.PERIOD)) {
			
			//V‰hennet‰‰n kameran zoomia, parametrina annetun arvon verran
			cameraHelper.addZoom(-camZoomSpeed);
		}
		
		//Jos painetaan t‰htimerkki‰ (HUOM. suomalaisessa n‰pp‰imistˆss‰)
		if(Gdx.input.isKeyPressed(Keys.SLASH)) {
			
			//Asetetaan zoom oletusarvoon
			cameraHelper.setZoom(1);
		}
	}
	
	private void handleInputGame(float deltaTime) {
		
		//Jos kamera seuraa demuckMan-oliota
		if(cameraHelper.hasTarget(level.demuckMan)) {
			
			//Jos painettiin vasenta-nuolin‰pp‰int‰
			if(Gdx.input.isKeyPressed(Keys.LEFT)) {
				
				//Asetetaan pelaajalle negatiivinen x-suuntaan menev‰ maksiminopeus
				//T‰m‰ tehd‰‰n siksi, koska oikea on positiivinen suunta
				level.demuckMan.velocity.x = -level.demuckMan.terminalVelocity.x;
			}
			
			//Jos painettiin oikeaa-nuolin‰pp‰int‰
			else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
				
				//Asetetaan pelaajalle positiivinen x-suuntaan menev‰ maksiminopeus
				level.demuckMan.velocity.x = level.demuckMan.terminalVelocity.x;
			}
			
			else {
				
				//Jos sovelluksen tyyppi ei ole tyˆpˆyt‰
				if(Gdx.app.getType() != ApplicationType.Desktop) {
					
					//Asetetaan x-suuntaiseksi nopeudeksi:
					//Olion maksiminopeus
					//Koska positiivinen nopeus, niin menn‰‰n oikealle
					level.demuckMan.velocity.x = level.demuckMan.terminalVelocity.x;
				}
			}
			
			//Jos n‰yttˆ‰ on kosketettu, tai on painettu v‰lilyˆnti‰
			if(Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				
				//Asetetaan hypp‰‰minen p‰‰lle
				level.demuckMan.setJumping(true);
			}
			
			else {
				//Otetaan hyp‰‰minen pois p‰‰lt‰
				level.demuckMan.setJumping(false);
			}
		}
	}
	
	//Metodi, jolla voidaan liikuttaa kameraa
	//Parametrina, kuinka paljon liikutetaan kameraa akseleilla
	private void moveCamera(float x, float y) {
		
		//Haetaan nykyinen kameran x-sijainti ja lis‰t‰‰n siihen m‰‰r‰,
		//jonka verran liikutetaan kameraa x-akselilla
		x += cameraHelper.getPosition().x;
		
		//Haetaan nykyinen kameran y-sijainti ja lis‰t‰‰n siihen m‰‰r‰,
		//jonka verran liikutetaan kameraa y-akselilla
		y += cameraHelper.getPosition().y;
		
		//Asetetaan kameralle uusi sijainti
		cameraHelper.setPosition(x, y);
	}
	
	//Overridataan p‰‰luokan metodi
	@Override
	//Kutsutaan, kun nappi vapautettiin
	public boolean keyUp (int keycode) {
		
		//Jos painettu R-nappia
		if(keycode == Keys.R) {
			
			//Alustetaan luokka uudestaan, joka vastaa resetointia
			init();
			
			//Lokitetaan debug-viesti
			Gdx.app.debug(TAG, "Game world resetted");
		}
		
		//Jos painettu Enter-nappia
		else if(keycode == Keys.ENTER) {
			
			//Jos kameralla on seurattava kohde, niin asetetaan, ettei kamera seuraa mit‰‰n game objectia
			//Jos kameralla ei ole kohdetta, niin asetetaan, ett‰ kamera seuraa demuckMan-oliota
			cameraHelper.setTarget(
					cameraHelper.hasTarget() ? null: level.demuckMan);
			
			//Lokitetaan viesti, joka kertoo seuraako kamera game objectia
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		
		return false;
	}
	
	//Metodin avulla voidaan k‰yd‰ game objectit l‰pi ja testata,
	//tapahtuiko tˆrm‰ys DemuckMan-olion ja toisen game objectin v‰lill‰
	private void testCollisions() {
		
		//Asetetaan pelaajan tˆrm‰yksen testaavan suorakulmion parametrit
		//Parametrit:
		//Suorakulmion x-sijainti --> Asetetaan pelaajan x-sijainti tasossa
		//Suorakulmion y-sijainti --> Asetetaan pelaajan y-sijainti tasossa
		//Suorakulmion leveys --> Asetetaan pelaajan hitboxin leveys tasossa
		//Suorakulmion korkeus --> Asetetaan pelaajan hitboxin korkeus tasossa
		r1.set(level.demuckMan.position.x,
				level.demuckMan.position.y,
				level.demuckMan.bounds.width,
				level.demuckMan.bounds.height);
		
		//Testataan, tapahtuiko tˆrm‰ys DemuckMan-olion ja Rock-olion v‰lill‰
		//K‰yd‰‰n kaikki Rock-oliot l‰pi
		for(Rock rock: level.rocks) {
			//Asetetaan kiven tˆrm‰yksen testaavan suorakulmion parametrit
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			
			//continue avulla lopetetaan koodin prosessointi, joka nykyisess‰ iteraatiossa ja
			//hyp‰t‰‰n loopin toiseen iteraatioon
			//Eli jos pelaajan suorakulmio ei ole kiven suorakulmion p‰‰ll‰, niin hyp‰t‰‰n
			//k‰sittelem‰‰n seuraavaa kive‰
			if(!r1.overlaps(r2)) continue;
			
			//K‰sitell‰‰n kiven ja pelaajan v‰linen yhteentˆrm‰ys
			onCollisionDemuckManWithRock(rock);
		}
		
		//Testataan, tapahtuiko tˆrm‰ys DemuckMan-olion ja GoldCoin-olion v‰lill‰
		//K‰yd‰‰n kaikki GoldCoin-oliot l‰pi
		for(GoldCoin goldCoin: level.goldCoins) {
			
			//Jos olio ei ole n‰kyviss‰, niin menn‰‰n toiseen iteraatioon
			//Eli jos collected on true, niin olio ei ole n‰kyviss‰
			if(goldCoin.collected) continue;
			
			//Asetetaan kolikon tˆrm‰yksen testaavan suorakulmion parametrit
			r2.set(goldCoin.position.x, goldCoin.position.y, goldCoin.bounds.width, goldCoin.bounds.height);
					
			//continue avulla lopetetaan koodin prosessointi, joka nykyisess‰ iteraatiossa ja
			//hyp‰t‰‰n loopin toiseen iteraatioon
			//Eli jos pelaajan suorakulmio ei ole kolikon suorakulmion p‰‰ll‰, niin hyp‰t‰‰n
			//k‰sittelem‰‰n seuraavaa kolikko
			if(!r1.overlaps(r2)) continue;
					
			//K‰sitell‰‰n kolikon ja pelaajan v‰linen yhteentˆrm‰ys
			onCollisionDemuckManWithGoldCoin(goldCoin);
			break;
		}
		
		//Testataan, tapahtuiko tˆrm‰ys DemuckMan-olion ja GoldCoin-olion v‰lill‰
		//K‰yd‰‰n kaikki GoldCoin-oliot l‰pi
		for(GoldCoin goldCoin: level.goldCoins) {
					
			//Jos olio ei ole n‰kyviss‰, niin menn‰‰n toiseen iteraatioon
			//Eli jos collected on true, niin olio ei ole n‰kyviss‰
			if(goldCoin.collected) continue;
					
			//Asetetaan kolikon tˆrm‰yksen testaavan suorakulmion parametrit
			r2.set(goldCoin.position.x, goldCoin.position.y, goldCoin.bounds.width, goldCoin.bounds.height);
							
			//continue avulla lopetetaan koodin prosessointi, joka nykyisess‰ iteraatiossa ja
			//hyp‰t‰‰n loopin toiseen iteraatioon
			//Eli jos pelaajan suorakulmio ei ole kolikon suorakulmion p‰‰ll‰, niin hyp‰t‰‰n
			//k‰sittelem‰‰n seuraavaa kolikko
			if(!r1.overlaps(r2)) continue;
							
			//K‰sitell‰‰n kolikon ja pelaajan v‰linen yhteentˆrm‰ys
			onCollisionDemuckManWithGoldCoin(goldCoin);
			break;
		}
		
		//Testataan, tapahtuiko tˆrm‰ys DemuckMan-olion ja Feather-olion v‰lill‰
		//K‰yd‰‰n kaikki Feather-oliot l‰pi
		for(Feather feather: level.feathers) {
							
			//Jos olio ei ole n‰kyviss‰, niin menn‰‰n toiseen iteraatioon
			//Eli jos collected on true, niin olio ei ole n‰kyviss‰
			if(feather.collected) continue;
							
			//Asetetaan sulan tˆrm‰yksen testaavan suorakulmion parametrit
			r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
									
			//continue avulla lopetetaan koodin prosessointi, joka nykyisess‰ iteraatiossa ja
			//hyp‰t‰‰n loopin toiseen iteraatioon
			//Eli jos pelaajan suorakulmio ei ole sulan suorakulmion p‰‰ll‰, niin hyp‰t‰‰n
			//k‰sittelem‰‰n seuraavaa sulkaa
			if(!r1.overlaps(r2)) continue;
									
			//K‰sitell‰‰n sulan ja pelaajan v‰linen yhteentˆrm‰ys
			onCollisionDemuckManWithFeather(feather);
			break;
		}
	}
	
	//K‰sittelee tˆrm‰ykset, jotka tapahtuvat DemuckMan-olion ja Rock-olion v‰lill‰
	//Kutsutaan, kun yhteentˆrm‰ys tapahtuu
	private void onCollisionDemuckManWithRock(Rock rock) {
		
		//Haetaan viittaus tasossa olevaa DemuckMan-olioon
		DemuckMan demuckMan = level.demuckMan;
		
		//Lasketaan kiven ja pelaajan v‰linen korkeusero
		//Pelaajan korkeuteen lasketaan sen y-sijainti
		//Kiven korkeuteen lasketaan kiven y-sijainti ja sen hitboxin korkeus
		//Math.abs() --> Palautetaan erotus positiivisena arvona
		float heightDifference = Math.abs(demuckMan.position.y -
				(rock.position.y + rock.bounds.height));
		
		//Jos pelaajan ja kiven v‰linen korkeusero on suurempi 0.25
		if(heightDifference > 0.25f) {
			
			//Boolean, joka kertoo, osuttiinko kiven vasempaan reunaan
			//Jos pelaajan x-sijainti on suurempi kuin kiven x-sijainti + puolet kiven hitboxin leveydest‰
			//, niin osuttiin vasempaan reunaan
			boolean hitLeftEdge = demuckMan.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			
			//Jos osuttiin kiven vasempaan reunaan
			if(hitLeftEdge) {
				
				//Asetetaan pelaajan uudeksi x-sijainniksi:
				//kiven x-sijainti + kiven hitboxin leveys
				demuckMan.position.x = rock.position.x + rock.bounds.width;
			}
			//Jos osuttiin kiven oikeaan reunaan
			else {
				
				//Asetetaan pelaajan uudeksi x-sijainniksi:
				//kiven x-sijainti - pelaajan hitboxin leveys
				demuckMan.position.x = rock.position.x - demuckMan.bounds.width;
			}
			return;
		}
		
		//K‰yd‰‰n pelaajan hyppy-tilat l‰pi
		switch(demuckMan.jumpState) {
			case GROUNDED:
				break;
			case FALLING:
			case JUMP_FALLING:		
				//Asetetaan pelaajan y-sijainniksi:
				//kiven y-sijainti + pelaajan hitboxin korkeus +
				//pelaajan nollapisteen y-sijainti
				demuckMan.position.y = rock.position.y
				+ demuckMan.bounds.height + demuckMan.origin.y;
				
				//Asetetaan hyppy-tilaksi --> Pelaaja on maassa
				demuckMan.jumpState = JUMP_STATE.GROUNDED;
				break;
			case JUMP_RISING:
				//Asetetaan pelaajan y-sijainniksi:
				//kiven y-sijainti + pelaajan hitboxin korkeus +
				//pelaajan nollapisteen y-sijainti
				demuckMan.position.y = rock.position.y +
				demuckMan.bounds.height + demuckMan.origin.y;				
				break;
		}
	}
			
	//K‰sittelee tˆrm‰ykset, jotka tapahtuvat DemuckMan-olion ja GoldCoin-olion v‰lill‰
	private void onCollisionDemuckManWithGoldCoin(GoldCoin goldCoin) {
		
		//Asetetaan, ett‰ kolikko on ker‰tty
		//T‰llˆin sit‰ ei piirret‰ ruudulle
		goldCoin.collected = true;
		
		//Kasvatetaan pelaajan pistem‰‰r‰‰
		score += goldCoin.getScore();
		
		//Tulostetaan viesti, kun ker‰t‰‰n kolikko
		Gdx.app.log(TAG, "Gold coin collected");
	}
			
	//K‰sittelee tˆrm‰ykset, jotka tapahtuvat DemuckMan-olion ja Feather-olion v‰lill‰
	private void onCollisionDemuckManWithFeather(Feather feather) {
		
		//Asetetaan, ett‰ sulka on ker‰tty
		//T‰llˆin sit‰ ei piirret‰ ruudulle
		feather.collected = true;
		
		//Kasvatetaan pelaajan pistem‰‰r‰‰
		score += feather.getScore();
		
		//Asetetaan pelaajalle power-up
		level.demuckMan.setFeatherPowerUp(true);
		
		//Tulostetaan viesti, kun ker‰t‰‰n sulka
		Gdx.app.log(TAG, "Feather collected");
	}

}
