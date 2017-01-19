package com.joonas.libgdx.demuckmanjumping.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.joonas.libgdx.demuckmanjumping.game.Assets;

//Container-luokka, joka sis‰lt‰‰ Cloud-olioita
public class Clouds extends AbstractGameObject {
	
	//Muuttujan avulla voidaan p‰‰tell‰, kuinka monta pilve‰ piirret‰‰n
	private float length;
	
	//Lista tekstuuri atlaksen alikuvista, joissa pilven kuva
	private Array<TextureRegion> regClouds;
	
	//Sis‰lt‰‰ listan Cloud-olioita
	private Array<Cloud> clouds;
	
	//Sis‰inen luokka, joka kuvaa yht‰ Cloud-olioita
	private class Cloud extends AbstractGameObject {
		
		//Sis‰lt‰‰ tekstuuri atlaksen alikuvan, jossa pilven kuva
		private TextureRegion regCloud;
		
		//Cloud-luokan tyhj‰ konstruktori
		public Cloud() {}
		
		//Metodilla voidaan asettaa tekstuuri atlaksen alikuva
		public void setRegion(TextureRegion region) {
			regCloud = region;
		}

		@Override
		public void render(SpriteBatch batch) {
			
			//Asetetaan alikuvaksi pilven kuva
			TextureRegion reg = regCloud;
			
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
		
	}//Cloud-luokan m‰‰rittely p‰‰ttyy
	
	//Clouds-luokan konstruktori
	//Parametrina, kuinka monta pilve‰ piirret‰‰n
	public Clouds(float length) {
		this.length = length;
		init();
	}
	
	//Alustetaan Clouds-luokka
	private void init() {
		
		//Asetetaan mittasuhteet, eli pilven leveys ja korkeus
		//Eli leveys on 3m ja korkeus 1.5m
		dimension.set(3.0f, 1.5f);
		
		//Alustetaan array
		//Array sis‰lt‰‰ tekstuuri atlaksen alikuvat, joissa pilven kuva
		regClouds = new Array<TextureRegion>();
		
		//Lis‰t‰‰n listaan tekstuuri atlaksen alikuvia, joissa pilven kuva
		regClouds.add(Assets.instance.levelDecoration.cloud01);
		regClouds.add(Assets.instance.levelDecoration.cloud02);
		regClouds.add(Assets.instance.levelDecoration.cloud03);
		
		//Kertoo, ett‰ viiden metrin v‰lein on pilvi
		int distFac = 5;
		
		//Lasketaan pilvien m‰‰r‰ tasossa
		int numClouds = (int) (length / distFac);
		
		//Alustetaan array, joka sis‰lt‰‰ Cloud-olioita
		//Cloud-olioiden m‰‰r‰ saadaan numClouds-muuttujan avulla
		clouds = new Array <Cloud> (2 * numClouds);
		
		//K‰yd‰‰n tasossa olevat pilvet l‰pi
		for(int i = 0; i < numClouds; i++) {
			
			//Luodaan Cloud-luokasta instanssi, kutsumalla spawnCloud()
			Cloud cloud = spawnCloud();
			
			//Muutetaan pilven x-sijaintia
			//Sijoitetaan pilvet 5 metrin p‰‰h‰n toisistaan
			cloud.position.x = i * distFac;
			
			//Lis‰t‰‰n array listaan, luotu Cloud-luokan instanssi
			clouds.add(cloud);
		}
	}
	
	//Metodilla voi luoda uuden Cloud-luokan instanssin
	//Palauttaa luodun Cloud-luokan instanssin
	private Cloud spawnCloud() {
		
		//Luodaan Cloud-luokan instanssi
		Cloud cloud = new Cloud();
		
		//Asetetaan pilvelle mittasuhteet, eli leveys ja korkeus
		cloud.dimension.set(dimension);
		
		//Valitaan arraysta, joka sis‰lt‰‰ tekstuuri atlaksen alikuvat, random pilvikuva
		//T‰m‰n j‰lkeen asetetaan setRegion(), tekstuuri atlaksen alikuva
		//Eli asetetaan Cloud-luokan instanssille random pilvikuva
		cloud.setRegion(regClouds.random());
		
		//Sis‰lt‰‰ pilven sijainnin
		Vector2 pos = new Vector2();
		
		//Asetetaan pilven alkuper‰inen x-sijainti
		pos.x = length + 10;
		
		//Pilven pohjana oleva y-sijainti
		pos.y += 1.75;
		
		//Lis‰t‰‰n y-sijaintiin random y-sijainti, jolloin pilvet eiv‰t ole samalla tasolla
		//random y-sijainti saadaan kahden muuttujan v‰lisen‰ tulona
		//random() --> Palauttaa satunnaisen numeron (0 - 0.2 v‰lilt‰)
		//randomBoolean() --> Palauttaa randomBooleanin
		pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1);
		
		//Asetetaan pilven sijainti
		cloud.position.set(pos);
		
		return cloud;
	}

	@Override
	public void render(SpriteBatch batch) {
		
		//K‰yd‰‰n Cloud-oliot l‰pi
		for(Cloud cloud : clouds) {
			
			//Rendataan pilvi
			cloud.render(batch);
		}
		
	}
}
