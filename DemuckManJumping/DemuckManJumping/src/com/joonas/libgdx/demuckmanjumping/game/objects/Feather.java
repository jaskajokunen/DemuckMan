package com.joonas.libgdx.demuckmanjumping.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joonas.libgdx.demuckmanjumping.game.Assets;

public class Feather extends AbstractGameObject {

		//Sis‰lt‰‰ tekstuuri atlaksen alikuvan, jossa sulka
		private TextureRegion regFeather;
		
		//Muuttujan avulla kerrotaan olion n‰kyvyys
		public boolean collected;
		
		//Konstruktori
		public Feather() {
			init();
		}

		//Alustetaan Feather-olio
		private void init() {
			
			//Asetetaan mittasuhteet, eli kolikon leveys ja korkeus
			//Eli leveys on 0.5m ja korkeus 0.5m
			dimension.set(0.5f, 0.5f);
			
			//Asetetaan muuttujaan tekstuuri atlaksen alikuva
			regFeather = Assets.instance.feather.feather;
			
			//Asetetaan sulan hitbox
			//set() parametrit: x, y, leveys, korkeus
			bounds.set(0,0, dimension.x, dimension.y);
			
			//Laitetaan olio n‰kyv‰ksi, koska sit‰ ei ole ker‰tty
			collected = false;
		}

		@Override
		public void render(SpriteBatch batch) {
			
			//Jos sulka on ker‰tty, eli se ei ole n‰kyviss‰,
			//poistutaan metodista
			if(collected) return;
			
			//K‰ytet‰‰n varastoimaan tekstuuri atlaksen alikuva
			TextureRegion reg = null;
			
			//Asetetaan alikuvaksi sulan kuva
			reg = regFeather;
			
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
			false, false);
			
		}
		
		//Metodi palauttaa pistem‰‰r‰n, jonka pelaaja saa, kun h‰n ker‰‰ sulan
		public int getScore() {
			return 250;
		}
}
