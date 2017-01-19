package com.joonas.libgdx.demuckmanjumping.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.joonas.libgdx.demuckmanjumping.game.Assets;

public class Mountains extends AbstractGameObject {
	
	//Sis‰lt‰‰ tekstuuri atlaksen alikuvan, jossa vuoren vasenpuoli
	private TextureRegion regMountainLeft;
	
	//Sis‰lt‰‰ tekstuuri atlaksen alikuvan, jossa vuoren oikeapuoli
	private TextureRegion regMountainRight;
	
	//Muuttujan avulla kerrotaan, kuinka monta kertaa kuva pit‰‰ toistaa
	private int length;
	
	//Konstruktori
	//Parametri kertoo, kuinka monta kertaa kuva toistetaan
	public Mountains (int length) {
		this.length = length;
		init();
	}
	
	//Alustetaan Mountains-olio
	private void init() {
		
		//Asetetaan mittasuhteet, eli vuoren leveys ja korkeus
		//Eli leveys on 10m ja korkeus 2m
		dimension.set(10, 2);
		
		//Asetetaan muuttujiin tekstuuri atlaksen alikuvat 
		regMountainLeft = Assets.instance.levelDecoration.mountainLeft;
		regMountainRight = Assets.instance.levelDecoration.mountainRight;
		
		//Muutetaan vuoren nollapistett‰
		//T‰llˆin saadaan siirretty‰ sit‰ x-akselin mukaisesti
		origin.x = -dimension.x * 2;
		
		//Kasvatetaan m‰‰r‰‰, joka kertoo, kuinka monta kertaa kuva toistetaan
		length += dimension.x * 2;
	}
	
	private void drawMountain(SpriteBatch batch, float offsetX, float offsetY, float tintColor) {
		
		//K‰ytet‰‰n varastoimaan tekstuuri atlaksen alikuva
		TextureRegion reg = null;
		
		//Asetetaan v‰ri, jolla suoritetaan piirt‰minen
		batch.setColor(tintColor, tintColor, tintColor, 1);
		
		//Asetetaan suhteelliseksi x-arvoksi --> vuoren leveys * x-arvon poikkeama
		float xRel = dimension.x * offsetX;
		
		//Asetetaan suhteelliseksi y-arvoksi --> vuoren korkeus * y-arvon poikkeama
		float yRel = dimension.y * offsetY;
		
		//Vuoren pituus
		//Vuoret kulkevat koko tason ymp‰ri
		int mountainLength = 0;
		
		//Kasvatetaan vuoren pituutta
		//ceil() --> Palauttaa pienimm‰n kokonaisluvun, joka on suurempi tai sama kuin parametri
		/*
		Esim. ceil(125.9) --> 126.0
		Esim2. ceil(0.4873) --> 1.0
		Esim3. ceil(-0.65) --> -0.0
		 */
		mountainLength += MathUtils.ceil(length / (2 * dimension.x));
		mountainLength += MathUtils.ceil(0.5f + offsetX);
		
		//K‰yd‰‰n piirrett‰v‰t vuoret l‰pi
		for(int i = 0; i < mountainLength; i++) {
			
			//Vasen vuori
			//Asetetaan alikuvaksi vasemmalla oleva vuori
			reg = regMountainLeft;
			
			//draw() --> Leikkaa suorakulmion m‰‰ritellyst‰ tekstuurista ja piirt‰‰ sen sijaintiin
			//Parametrit:
			//Tekstuuri --> Haetaan tekstuuri atlaksen alikuvan tekstuuri
			batch.draw(reg.getTexture(), 
			//x-sijainti, y-sijainti
			//sijainteihin lis‰t‰‰n suhteelliset sijainnit
			origin.x + xRel, position.y + origin.y + yRel, 
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
			false, false);
			
			//lis‰t‰‰n suhteelliseen x-arvoon vuoren leveys
			//suhteellisen x-arvon avulla, voidaan liikuttaa vuorta x-akselilla
			//t‰llˆin voidaan piirt‰‰ vasen vuori, oikean vuoren viereen
			xRel += dimension.x;
			
			
			//Oikea vuori
			//Asetetaan alikuvaksi oikealla oleva vuori
			reg = regMountainRight;
			
			//draw() --> Leikkaa suorakulmion m‰‰ritellyst‰ tekstuurista ja piirt‰‰ sen sijaintiin
			//Parametrit:
			//Tekstuuri --> Haetaan tekstuuri atlaksen alikuvan tekstuuri
			batch.draw(reg.getTexture(), 
			//x-sijainti, y-sijainti
			//sijainteihin lis‰t‰‰n suhteelliset sijainnit
			origin.x + xRel, position.y + origin.y + yRel, 
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
			false, false);
			
			//lis‰t‰‰n suhteelliseen x-arvoon vuoren leveys
			//suhteellisen x-arvon avulla, voidaan liikuttaa vuorta x-akselilla
			//t‰llˆin voidaan piirt‰‰ vasen vuori, oikean vuoren viereen
			xRel += dimension.x;
		}
		
		//Resetoidaan v‰ri valkoiseksi
		batch.setColor(1,1,1,1);
		
	}

	@Override
	public void render(SpriteBatch batch) {
		
		//Tumman harmaa vuori
		//Parametrit:
		//K‰ytett‰v‰ SpriteBatch-olio
		//Poikkeama x-suunnassa
		//Poikkeama y-suunnassa
		//S‰vyn v‰ri
		drawMountain(batch, 0.5f, 0.5f, 0.5f);
		
		//Harmaa vuori
		drawMountain(batch, 0.25f, 0.25f, 0.7f);
		
		//Vaalean harmaa vuori
		drawMountain(batch, 0.0f, 0.0f, 0.9f);
		
	}

}
