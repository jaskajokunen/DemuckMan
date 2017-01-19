package com.joonas.libgdx.demuckmanjumping.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.joonas.libgdx.demuckmanjumping.game.objects.AbstractGameObject;
import com.joonas.libgdx.demuckmanjumping.game.objects.Clouds;
import com.joonas.libgdx.demuckmanjumping.game.objects.DemuckMan;
import com.joonas.libgdx.demuckmanjumping.game.objects.Feather;
import com.joonas.libgdx.demuckmanjumping.game.objects.GoldCoin;
import com.joonas.libgdx.demuckmanjumping.game.objects.Mountains;
import com.joonas.libgdx.demuckmanjumping.game.objects.Rock;
import com.joonas.libgdx.demuckmanjumping.game.objects.WaterOverlay;

public class Level {
	
	//Asetetaan muuttujaan luokan nimi stringina
	//K‰ytet‰‰n lokitukseen
	public static final String TAG = Level.class.getName();
	
	//Viittaus DemuckMan-luokkaan, eli pelaajan hahmon sis‰lt‰v‰‰n luokkaan
	public DemuckMan demuckMan;
	
	//Array, joka sis‰lt‰‰ GoldCoin-olioita
	public Array<GoldCoin> goldCoins;
	
	//Array, joka sis‰lt‰‰ Feather-olioita
	public Array<Feather> feathers;
	
	//Enum-luokka
	public static enum BLOCK_TYPE{
		//Enumin oliot, jotka kutsuvat alla olevaa konstruktoria
		//Parametrina RGB
		EMPTY(0,0,0), //RGB arvo vastaa mustaa
		ROCK(0,255,0), //RGB arvo vastaa vihre‰‰
		PLAYER_SPAWNPOINT(255,255,255), //RGB arvo vastaa valkoista
		ITEM_FEATHER(255,0,255), //RGB arvo vastaa violettia
		ITEM_GOLD_COIN(255,255,0); //RGB arvo vastaa keltaista
		
		
		private int color;
		
		//Enumin konstruktori
		private BLOCK_TYPE (int r, int g, int b) {
			//TODO --> Selvit‰ mit‰ tekee
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
		
		//Metodilla saadaan tiet‰‰, ovatko kaksi v‰ri‰ t‰ysin samoja 
		public boolean sameColor (int color) {
			return this.color == color;
		}
		
		//Hakee v‰rin
		public int getColor() {
			return color;
		}
	}
	
	//Array, joka sis‰lt‰‰ Rock-luokan instansseja
	public Array<Rock> rocks;
	
	//Luokkien instanssit
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;
	
	//Konstruktori
	//Parametrina tiedoston nimi
	public Level (String filename) {
		init(filename);
	}

	//Alustaa luokan
	private void init(String filename) {
		
		//Alustetaan pelaajan hahmo tyhj‰ksi
		demuckMan = null;
		
		//Alustetaan array, joka sis‰lt‰‰ Rock-luokan instansseja
		rocks = new Array<Rock>();
		
		//Alustetaan array, joka sis‰lt‰‰ GoldCoin-luokan instansseja
		goldCoins = new Array<GoldCoin>();
		
		//Alustetaan array, joka sis‰lt‰‰ Feather-luokan instansseja
		feathers = new Array<Feather>();
		
		//Luodaan Pixmap-luokan instanssi parametrina olevasta tiedostosta
		//Parametrina oleva tiedosto, sis‰lt‰‰ tasoa esitt‰v‰n kuvan pikseli datan
		//Tiedoston tyypin pit‰‰ olla png, JPG tai Bitmap
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		
		//Sis‰lt‰‰ viimeisimm‰n skannatun pikselin v‰ri-arvon
		//Muuttujan avulla tunnistetaan vierekk‰iset kiven-pikselit
		int lastPixel = -1;
		
		//Skannataan pixmapin pikselit l‰pi
		//Skannaus aloitetaan vasemmalta ylh‰‰lt‰ ja lopetetaan oikealle alas
		for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for(int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				
				//Luodaan AbstractGameObject-luokasta instanssi
				AbstractGameObject obj = null;
				
				//Muuttujan avulla tehd‰‰n poikkeama, jotta olio mahtuu pelimaailmaan
				float offsetHeight = 0;
				
				//Arvoksi asetetaan levelin maksimi korkeus - skannatun pikselin korkeus
				//Saadaan k‰‰nnetty pystysuora sijainti
				//Tarkoittaa, ett‰ game objectit ilmestyv‰t oikeaan korkeuteen, vaikka
				//skannaus tehd‰‰n ylh‰‰lt‰ alas
				float baseHeight = pixmap.getHeight() - pixelY;
				
				//Muuttuja sis‰lt‰‰ skannatun pikselin v‰ri-arvon
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				
				//Verrataan skannatun pikselin v‰ri‰, olion v‰ri-koodiin
				//Tyhj‰ tila
				if(BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					//Ei tehd‰ mit‰‰n
				}
				
				//Jos skannatun pikselin v‰ri vastaa kiven v‰ri-koodia
				//Kivi
				else if(BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
					
					//Jos viimeisin skannatun pikselin v‰ri-arvo, 
					//ei vastaa skannatun pikselin v‰ri-arvoa 
					//Eli kyseess‰ ei ole vierekk‰iset kiven-pikselit
					if(lastPixel != currentPixel) {
						
						//Luodaan Rock-olio
						obj = new Rock();
						
						//Korkeuden kasvatuskerroin
						float heightIncreaseFactor = 0.25f;
						
						//Poikkeama korkeudessa
						offsetHeight = -2.5f;
						
						//Asetetaan Rock-olion sijainti
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
						
						//Lis‰t‰‰n Rock-olio listaan
						rocks.add((Rock)obj);
					}
					
					//Jos viimeisimm‰n skannatun pikselin v‰ri-arvo, vastaa
					//skannatun pikselin v‰ri-arvoa
					//Eli kyseess‰ on vierekk‰iset kiven-pikselit
					else {
						//Haetaan Rock-olioita sis‰lt‰v‰n arrayn, viimeisen indeksin Rock-olio
						//Kasvatetaan haetun Rock-olion keskiosien m‰‰r‰‰ yhdell‰
						rocks.get(rocks.size - 1).increaseLength(1);
					}
				}
				
				//Jos skannatun pikselin v‰ri vastaa spawnpointin v‰ri-koodia
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
					
					//Luodaan DemuckMan-olio
					obj = new DemuckMan();
					
					//Asetetaan DemuckMan-olion sijainti
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					
					//Asetetaan muuttujaan alustettu olio
					demuckMan = (DemuckMan)obj;
					
				}
				
				//Jos skannatun pikselin v‰ri vastaa sulan v‰ri-koodia
				else if(BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
					
					//Luodaan Feather-olio
					obj = new Feather();
					
					//Poikkeama korkeudessa
					offsetHeight = -1.5f;
					
					//Asetetaan Feather-olion sijainti
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					
					//Lis‰t‰‰n Feather-olio listaan
					feathers.add((Feather) obj);
					
				}
				
				//Jos skannatun pikselin v‰ri vastaa kolikon v‰ri-koodia
				else if(BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
					
					//Luodaan GoldCoin-olio
					obj = new GoldCoin();
					
					//Poikkeama korkeudessa
					offsetHeight = -1.5f;
					
					//Asetetaan GoldCoin-olion sijainti
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					
					//Lis‰t‰‰n Feather-olio listaan
					goldCoins.add((GoldCoin) obj);
				}
				
				//Jos pikseli ei vastaa mit‰‰n v‰ri-koodia
				else {
					
					//Haetaan R
					int r = 0xff & (currentPixel >>> 24);
					
					//Haetaan G
					int g = 0xff & (currentPixel >>> 16);
					
					//Haetaan B
					int b = 0xff & (currentPixel >>> 8);
					
					//Haetaan A
					int a = 0xff & currentPixel;
					
					//Tulostetaan virhe-viesti
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX
							+ "> y<" + pixelY
							+ ">: r<" + r
							+ "> g<" + g
							+ "> b<" + b
							+ "> a<" + a + ">");
				}
				
				//Asetetaan muuttujaan skannatun pikselin v‰ri-arvo
				lastPixel = currentPixel;
			} //Sisimm‰inen for-looppi p‰‰ttyy
		} //Ulommainen for-looppi p‰‰ttyy
		
		//Alustetaan Clouds-olio
		//Konstruktorin parametrina tasona olevan kuvan leveys
		clouds = new Clouds(pixmap.getWidth());
		
		//Asetetaan Clouds-oliolle sijainti
		clouds.position.set(0,2);
		
		//Alustetaan Mountains-olio
		//Konstruktorin parametrina tasona olevan kuvan leveys
		mountains = new Mountains(pixmap.getWidth());
		
		//Alustetaan WaterOverlay-olio
		//Konstruktorin parametrina tasona olevan kuvan leveys
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		
		//Asetetaan WaterOverlay-oliolle sijainti
		waterOverlay.position.set(0, -3.75f);
		
		//Vapautetaan kaikki Pixmappiin liittyv‰t resurssit
		pixmap.dispose();
		
		//Tulostetaan debug-viesti
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
		
	}
	
	public void render(SpriteBatch batch) {
		
		//Piirret‰‰n vuoret
		mountains.render(batch);
		
		//K‰yd‰‰n Rock-oliot l‰pi
		for(Rock rock : rocks) {
			//Piirret‰‰n Rock-olio
			rock.render(batch);
		}
		
		//K‰yd‰‰n GoldCoin-oliot l‰pi
		for(GoldCoin goldCoin : goldCoins) {
			//Piirret‰‰n GoldCoin-olio
			goldCoin.render(batch);
		}
		
		//K‰yd‰‰n Feather-oliot l‰pi
		for(Feather feather : feathers) {
			//Piirret‰‰n Feather-olio
			feather.render(batch);
		}
		
		//Piirret‰‰n pelaajan hahmo
		demuckMan.render(batch);

		//Piirret‰‰n Water Overlay
		waterOverlay.render(batch);
		
		//Piirret‰‰n pilvet
		clouds.render(batch);
	}
	
	public void update (float deltaTime) {
		
		//P‰ivitet‰‰n pelaajan hahmo
		demuckMan.update(deltaTime);
		
		//K‰yd‰‰n Rock-oliot l‰pi
		for(Rock rock : rocks) {
			//P‰vitet‰‰n Rock-olio
			rock.update(deltaTime);
		}
				
		//K‰yd‰‰n GoldCoin-oliot l‰pi
		for(GoldCoin goldCoin : goldCoins) {
			//P‰ivitet‰‰n GoldCoin-olio
			goldCoin.update(deltaTime);
		}
				
		//K‰yd‰‰n Feather-oliot l‰pi
		for(Feather feather : feathers) {
			//P‰ivitet‰‰n Feather-olio
			feather.update(deltaTime);
		}
		
		//P‰ivitet‰‰n Cloud-oliot
		clouds.update(deltaTime);
		
	}
	
}
