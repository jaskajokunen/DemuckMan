package com.joonas.libgdx.demuckmanjumping.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joonas.libgdx.demuckmanjumping.game.Assets;

public class WaterOverlay extends AbstractGameObject {
	
	//Sis‰lt‰‰ tekstuuri atlaksen alikuvan, jossa water overlay
	private TextureRegion regWaterOverlay;
	
	//Muuttujan avulla kerrotaan, kuinka monta kertaa kuva pit‰‰ toistaa
	private float length;
	
	
	//Konstruktori
	//Parametri kertoo, kuinka monta kertaa kuva toistetaan
	public WaterOverlay (int length) {
		this.length = length;
		init();
	}

	private void init() {
		
		//Asetetaan mittasuhteet, eli water overlayn leveys ja korkeus
		//Eli leveys on pituus * 10m ja korkeus 3m
		dimension.set(length * 10, 3);
		
		//Asetetaan muuttujaan tekstuuri atlaksen alikuva
		regWaterOverlay = Assets.instance.levelDecoration.waterOverlay;
		
		//Muutetaan water overlayn nollapistett‰
		//T‰llˆin saadaan siirretty‰ sit‰ x-akselin mukaisesti
		origin.x = -dimension.x / 2;
			
		}

	@Override
	public void render(SpriteBatch batch) {
		
		//K‰ytet‰‰n varastoimaan tekstuuri atlaksen alikuva
		TextureRegion reg = null;
		
		//Asetetaan alikuvaksi water overlay
		reg = regWaterOverlay;
		
		//draw() --> Leikkaa suorakulmion m‰‰ritellyst‰ tekstuurista ja piirt‰‰ sen sijaintiin
		//Parametrit:
		//Tekstuuri --> Haetaan tekstuuri atlaksen alikuvan tekstuuri
		batch.draw(reg.getTexture(), 
		//x-sijainti, y-sijainti
		//sijainteihin lis‰t‰‰n nollapisteen sijainti
		position.x + origin.x, position.y + origin.y, 
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
	}

}
