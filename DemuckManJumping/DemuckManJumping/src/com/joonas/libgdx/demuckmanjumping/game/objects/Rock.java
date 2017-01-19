package com.joonas.libgdx.demuckmanjumping.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joonas.libgdx.demuckmanjumping.game.Assets;

//Luokka haarautuu AbstractGameObject-luokasta
public class Rock extends AbstractGameObject {
	
	//Sis‰lt‰‰ tekstuuri atlaksen alikuvan, jossa kiven reuna
	private TextureRegion regEdge;
	
	//Sis‰lt‰‰ tekstuuri atlaksen alikuvan, jossa kiven keskusta
	private TextureRegion regMiddle;
	
	//Muuttuja kertoo, kuinka monta keskiosaa k‰ytet‰‰n kivess‰
	private int length;
	
	//Konstruktori
	public Rock() {
		init();
	}

	//Alustetaan Rock-olio
	private void init() {
		
		//Asetetaan mittasuhteet, eli kiven leveys ja korkeus
		//Eli leveys on 1m ja korkeus 1.5m
		dimension.set(1f, 1.5f);
		
		//Asetetaan muuttujiin tekstuuri atlaksen alikuvat 
		regEdge = Assets.instance.rock.edge;
		regMiddle = Assets.instance.rock.middle;
		
		//Asetetaan, ett‰ kivess‰ k‰ytet‰‰n yht‰ keskiosaa
		setLength(1);
	}

	//Metodilla voidaan asettaa kiven keskiosien m‰‰r‰
	public void setLength(int length) {
		this.length = length;
		
		//Asetetaan kiven hitbox
		//set() parametrit: x, y, leveys * kiven keskiosien m‰‰r‰, korkeus
		bounds.set(0,0, dimension.x * length, dimension.y);
	}
	
	//Metodilla voidaan kasvattaa kiven keskiosien m‰‰r‰‰
	//Parametrina luku, jolla kiven kokoa kasvatetaan
	public void increaseLength(int amount) {
		setLength(length + amount);
	}

	@Override
	public void render(SpriteBatch batch) {
		
		//K‰ytet‰‰n varastoimaan tekstuuri atlaksen alikuva
		TextureRegion reg = null;
		
		//Suhteellinen x-arvo
		float relX = 0;
		
		//Suhteellinen y-arvo
		float relY = 0;
		
		//Vasemman reunan piirt‰minen
		//Asetetaan alikuvaksi kiven reuna
		reg = regEdge;
		
		//V‰hennet‰‰n suhteellisesta x-arvosta vasemman reunan leveys / 4
		//T‰llˆin kiven keskiosa alkaa kohdasta 0 x-akselilla
		relX = relX - dimension.x / 4;
		
		//draw() --> Leikkaa suorakulmion m‰‰ritellyst‰ tekstuurista ja piirt‰‰ sen sijaintiin
		//Parametrit:
		//Tekstuuri --> Haetaan tekstuuri atlaksen alikuvan tekstuuri
		batch.draw(reg.getTexture(), 
		//x-sijainti, y-sijainti
		//sijainteihin lis‰t‰‰n suhteelliset sijainnit
		//suhteellisten sijaintien avulla saadaan linjattua vasen reuna olion vasempaan reunaan
		position.x + relX, position.y + relY, 
		//nollapisteen x-sijainti, nollapisteen y-sijainti
		//m‰‰rittelee suhteelllisen sijainnin, jonne suorakulmio siirret‰‰n
		//nollapiste (0,0) tarkoittaa kulmaa, joka vasemmalla alhaalla
		origin.x, origin.y,
		//leveys, korkeus
		//m‰‰rittelev‰t n‰ytett‰v‰n kuvan mittasuhteet
		dimension.x / 4, dimension.y,
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
		
		//Keskustan piirt‰minen
		//Asetetaan suhteelliseksi x-sijainniksi 0
		//Kyseinen sijainti vastaa sijaintia, jossa kiven vasen reuna loppuu
		relX = 0;
		
		//Asetetaan alikuvaksi kiven keskusta
		reg = regMiddle;
				
		//K‰yd‰‰n kiven keskiosat l‰pi
		for(int i = 0; i < length; i++) {
			//draw() --> Leikkaa suorakulmion m‰‰ritellyst‰ tekstuurista ja piirt‰‰ sen sijaintiin
			//Parametrit:
			//Tekstuuri --> Haetaan tekstuuri atlaksen alikuvan tekstuuri
			batch.draw(reg.getTexture(),
			//x-sijainti, y-sijainti
			position.x + relX, position.y + relY,
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
			
			//lis‰t‰‰n suhteelliseen x-arvoon keskiosan leveys
			//suhteellisen x-arvon avulla, voidaan liikuttaa keskiosaa x-akselilla
			//t‰llˆin voidaan piirt‰‰ keskiosa, toisen keskiosan viereen
			relX = relX + dimension.x;
		}
		
		//Oikean reunan piirt‰minen
		//Piirret‰‰n kohtaan, jossa viimeinen kiven keskikohta loppuu
		//Asetetaan alikuvaksi kiven reuna
		reg = regEdge;
				
		//draw() --> Leikkaa suorakulmion m‰‰ritellyst‰ tekstuurista ja piirt‰‰ sen sijaintiin
		//Parametrit:
		//Tekstuuri --> Haetaan tekstuuri atlaksen alikuvan tekstuuri
		batch.draw(reg.getTexture(), 
		//x-sijainti, y-sijainti
		position.x + relX, position.y + relY,
		//nollapisteen x-sijainti, nollapisteen y-sijainti
		//m‰‰rittelee suhteelllisen sijainnin, jonne suorakulmio siirret‰‰n
		//nollapiste (0,0) tarkoittaa kulmaa, joka vasemmalla alhaalla
		origin.x + dimension.x / 8, origin.y,
		//leveys, korkeus
		//m‰‰rittelev‰t n‰ytett‰v‰n kuvan mittasuhteet
		dimension.x / 4, dimension.y,
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
		//koska k‰‰nnet‰‰n x-suunnassa, niin piirret‰‰n x-akselin suhteessa peilikuva
		true, false);
		
	}

}
