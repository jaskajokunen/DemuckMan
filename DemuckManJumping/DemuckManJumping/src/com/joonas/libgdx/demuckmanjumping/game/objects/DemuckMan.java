package com.joonas.libgdx.demuckmanjumping.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joonas.libgdx.demuckmanjumping.game.Assets;
import com.joonas.libgdx.demuckmanjumping.util.Constants;

public class DemuckMan extends AbstractGameObject {
	
	//Asetetaan muuttujaan luokan nimi stringina
	//K‰ytet‰‰n lokitukseen
	public static final String TAG = DemuckMan.class.getName();
	
	//Hypp‰‰misen maksimi ja minimiaika
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	
	//Hypp‰‰misajan poikkeama, kun lennet‰‰n
	//K‰ytet‰‰n power-upissa
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;
	
	//Enum, joka sis‰lt‰‰ katselusuunnat
	public enum VIEW_DIRECTION {LEFT, RIGHT}
	
	//Enum, joka sis‰lt‰‰ hypp‰‰misen tilat
	public enum JUMP_STATE {
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}
	
	//Sis‰lt‰‰ tekstuuri atlaksen alikuvan, jossa pelaajan hahmo
	private TextureRegion regPlayerCharacter;
	
	//Sis‰lt‰‰ viittauksen enumiin, jossa katselusuunnat
	public VIEW_DIRECTION viewDirection;
	
	//Muuttuja, joka kertoo, kuinka pitk‰‰n on hyp‰tty
	public float timeJumping;
	
	//Sis‰lt‰‰ viittauksen enumiin, jossa hyppy-tilat
	public JUMP_STATE jumpState;
	
	//Muuttuja, joka kertoo, onko pelaajalla power-up
	public boolean hasFeatherPowerUp;
	
	//Muuttuja, joka kertoo, miten pitk‰‰n power-up on voimassa
	public float timeLeftFeatherPowerUp;
	
	//Konstruktori
	public DemuckMan() {
		init();
	}
	
	
	
	//Metodi, joka alustaa DemuckMan-olion
	private void init() {
		
		//Asetetaan mittasuhteet, eli pelaajan korkeus ja leveys
		//Eli leveys on 1m ja korkeus 1m
		dimension.set(1, 1);
		
		//Asetetaan muuttujaan tekstuuri atlaksen alikuva
		regPlayerCharacter = Assets.instance.playerChar.playerCharacter;
		
		//Keskitet‰‰n kuva game objectiin
		//Tehd‰‰n niin, ett‰ asetetaan nollapiste pisteeseen,
		//joka puolet leveydest‰ ja korkeudesta
		origin.set(dimension.x / 2, dimension.y / 2);
		
		//Asetetaan pelaajan hitbox
		//set() parametrit: x, y, leveys, korkeus
		bounds.set(0,0, dimension.x, dimension.y);
		
		//Asetetaan olion maksiminopeus m/s
		terminalVelocity.set(3.0f, 4.0f);
		
		//Asetetaan oliolle kitka
		//Koska y:n suuntainen kitka on 0, niin silt‰ suunnalta ei tule vastustavaa voimaa
		friction.set(12.0f, 0.0f);
		
		//Asetetaan olion kiihtyvyys, joka on y-suunnassa vastustava
		acceleration.set(0.0f, -25.0f);
		
		//Asetetaan katselusuunta
		viewDirection = VIEW_DIRECTION.RIGHT;
		
		//Asetetaan hypyn-tila
		jumpState = JUMP_STATE.FALLING;
		
		//Asetetaan, kuinka pitk‰‰n on hypitty
		timeJumping = 0;
		
		//Asetetaan, ettei pelaajalla ole power-uppia
		hasFeatherPowerUp = false;
		
		//Asetetaan, ettei power-up ole voimassa
		timeLeftFeatherPowerUp = 0;
	}
	
	//Metodilla triggeroidaan uusi hyppy
	//Parametri kertoo, onko pelaaja painanut hyppy-nappia
	public void setJumping(boolean jumpKeyPressed) {
		
		//K‰yd‰‰n hypyn-tilat l‰pi
		switch(jumpState) {
			
			//Kun hahmo seisoo alustalla
			case GROUNDED:
				
				//Jos on painettu hyppy-nappia
				if(jumpKeyPressed) {
					
					//Nollataan aika, joka on hyp‰tty
					//Eli aloitetaan hypp‰‰misajan laskeminen alusta
					timeJumping = 0;
					
					//Muutetaan hypp‰yksen-tilaa
					jumpState = JUMP_STATE.JUMP_RISING;
				}
				break;
			
			//Hahmo on suorittanut hypyn ja on nousemassa
			//Maksimi korkeutta ei ole saavutettu
			case JUMP_RISING:
				
				//Jos ei ole painettu hyppy-nappia
				if(!jumpKeyPressed) {
					
					//Muutetaan hypp‰yksen tilaa
					jumpState = JUMP_STATE.JUMP_FALLING;
				}
				break;
			
			//Kun hahmo tippuu alasp‰in
			case FALLING:				
			//Kun hahmo tippuu alasp‰in hypp‰‰misen j‰lkeen
			case JUMP_FALLING:
					
					//Jos painettu hyppy-nappia ja pelaajalla on power-up
					if(jumpKeyPressed && hasFeatherPowerUp) {
						
						//Muutetaan aikaa, joka on hyp‰tty
						//Ajaksi laitetaan hypp‰‰misajan poikkeama, kun lennet‰‰n
						timeJumping = JUMP_TIME_OFFSET_FLYING;
						
						//Muutetaan hypp‰yksen-tila nousevaksi
						jumpState = JUMP_STATE.JUMP_RISING;
					}			
				break;
		}
	}
	
	//Metodilla voidaan asettaa pelaajalle power-up
	//Parametrina boolean, joka kertoo, onko pelaaja poiminut power-upin
	public void setFeatherPowerUp(boolean pickedUp) {
		
		//Asetetaan muuttujaan boolean, joka kertoo onko pelaaja poiminut power-upin
		hasFeatherPowerUp = pickedUp;
		
		//Jos pelaaja on poiminut power-upin
		if(pickedUp) {
			
			//Asetetaan, ett‰ power-up on voimassa, konstantin m‰‰rittelem‰n ajan
			timeLeftFeatherPowerUp = Constants.ITEM_FEATHER_POWERUP_DURATION;
		}
	}
	
	//Metodilla voidaan selvitt‰‰, onko power-up aktiivinen ja onko se ker‰tty
	//Palauttaa booleanin, joka kertoo, onko pelaajalla power-up
	// ja onko power-up voimassa
	public boolean hasFeatherPowerUp() {
		return hasFeatherPowerUp && timeLeftFeatherPowerUp > 0;
	}
	
	@Override
	public void update (float deltaTime) {
		
		//Kutsutaan p‰‰luokan toteutusta metodista
		super.update(deltaTime);
		
		//Jos olion x-nopeus ei ole nolla, niin silloin se on liikkeess‰
		if(velocity.x != 0) {
			
			//Jos x-suuntainen nopeus on negatiivinen
			//T‰llˆin asetetaan arvoksi VIEW_DIRECTION.LEFT
			//Jos x-suuntainen nopeus on positiivinen, niin arvoksi asetetaan VIEW_DIRECTION.RIGHT
			viewDirection = (velocity.x < 0) ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
		}
		
		//Jos power-up on voimassa
		if(timeLeftFeatherPowerUp > 0) {
			
			//V‰hennet‰‰n aikaa, jonka power-up on voimassa
			timeLeftFeatherPowerUp -= deltaTime;
		}
		
		//Jos power-up ei ole voimassa
		if(timeLeftFeatherPowerUp < 0) {
			
			//Asetetaan, ettei power-up ole voimassa
			//Ensiksi asetetaan power-upin voimassaolo aika nollaksi
			//T‰m‰n j‰lkeen asetetaan, ettei pelaajalla ole power-uppia
			timeLeftFeatherPowerUp = 0;
			setFeatherPowerUp(false);
		}
	}
	
	@Override
	//Metodilla voidaan p‰ivitt‰‰ pelaajan y-suuntainen liike
	protected void updateMotionY (float deltaTime) {
		
		//K‰yd‰‰n hyppy-tilat l‰pi
		switch(jumpState) {
			
			case GROUNDED:
				//Muutetaan hypp‰yksen-tilaa
				jumpState = JUMP_STATE.FALLING;
				break;
			case JUMP_RISING:
				
				//Pidet‰‰n kirjaa hypp‰ykseen k‰ytetyst‰ ajasta
				//Eli kasvatetaan hypp‰yksen aikaa, kun ollaan ilmassa
				timeJumping += deltaTime;
				
				//Jos on hypp‰ysaikaa j‰ljell‰
				//Eli hypp‰ykseen k‰ytetty aika, ei ylit‰ sen maksimiarvoa
				if(timeJumping <= JUMP_TIME_MAX) {
					
					//Asetetaan y-suuntaiseksi nopeudeksi, y-suuntaan maksiminopeus
					velocity.y = terminalVelocity.y;
				}				
				break;
			case FALLING:
				break;
			case JUMP_FALLING:
				
				//Kasvatetaan hypp‰‰miseen k‰ytetty‰ aikaa
				//, kun hahmo tippuu hypp‰‰misen j‰lkeen alasp‰in
				timeJumping += deltaTime;
				
				//Jos hypp‰‰miseen on k‰ytetty aikaa yli 0 verran (eli on hyp‰tty) ja
				//hypp‰‰miseen k‰ytetty aika on pienempi tai yht‰ suuri kuin sen minimiarvo
				//Eli k‰ytt‰j‰ on pit‰nyt hyppy-nappia v‰h‰n aikaa pohjassa
				if(timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
					
					//Asetetaan y-suuntaiseksi nopeudeksi, y-suuntaan maksiminopeus
					velocity.y = terminalVelocity.y;
				}
				break;
		}
		
		//Jos hahmon hyppy-tila ei ole se, ett‰ seisoo alusta p‰‰ll‰
		if(jumpState != JUMP_STATE.GROUNDED) {
			
			//Kutsutaan p‰‰luokan toteutusta metodista
			//T‰t‰ kutsutaan, kun liikkeen pit‰‰ tapahtua
			super.updateMotionY(deltaTime);
		}
		
	}
	@Override
	public void render(SpriteBatch batch) {
		
		//K‰ytet‰‰n varastoimaan tekstuuri atlaksen alikuva
		TextureRegion reg = null;
		
		//Jos pelaajalla on feather power-up
		if(hasFeatherPowerUp) {
			
			//Muutetaan pelaajan v‰ri‰ oranssiksi
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		}
		
		//Asetetaan alikuvaksi pelaajan hahmon kuva
		reg = regPlayerCharacter;
		
		//draw() --> Leikkaa suorakulmion m‰‰ritellyst‰ tekstuurista ja piirt‰‰ sen sijaintiin
		//Parametrit:
		//Tekstuuri --> Haetaan tekstuuri atlaksen alikuvan tekstuuri
		batch.draw(reg.getTexture(), 
		//x-sijainti, y-sijainti
		position.x, position.y, 
		//nollapisteen x-sijainti, nollapisteen y-sijainti
		//m‰‰rittelee suhteelllisen sijainnin, jonne suorakulmio siirret‰‰n
		//nollapiste (0,0) tarkoittaa kulmaa, joka vasemmalla alhaalla
		origin.x, origin.y,
		//leveys, korkeus
		//m‰‰rittelev‰t n‰ytett‰v‰n kuvan mittasuhteet
		dimension.x, dimension.y,
		//skaalaus x-suunnassa, skaalaus y-suunnassa
		//m‰‰rittelev‰t suorakulmion skaalauksen nollapisteen ymp‰ri
		scale.x, scale.y,
		//rotaatio
		//m‰‰rittelee suorakulmion rotaation nollapisteen ymp‰ri
		rotation,
		//l‰hteen‰ olevan tekstuuri atlaksen: x-sijainti ja y-sijainti
		//m‰‰rittelee tekstuurin leikkaavaan suorakulmion pisteet
		reg.getRegionX(), reg.getRegionY(),
		//l‰hteen‰ olevan tekstuuri atlaksen: leveys ja korkeus
		//m‰‰rittelee tekstuurin leikkaavan suorakulmion leveyden ja korkeuden
		reg.getRegionWidth(), reg.getRegionHeight(),
		//k‰‰nnet‰‰nkˆ x-suunnassa, k‰‰nnet‰‰nkˆ y-suunnassa
		//jos jompikumpi akseleista k‰‰nnet‰‰n, niin silloin tehd‰‰n peilikuva akselin suhteen
		//K‰‰nnet‰‰n x-suunnassa, jos katselusuunta vastaa vasenta
		//T‰llˆin x-suuntainen nopeus on negatiivinen
		viewDirection == VIEW_DIRECTION.LEFT, false);
		
		//Resetoidaan v‰ri valkoiseksi
		batch.setColor(1, 1, 1, 1);
		
	}

}
