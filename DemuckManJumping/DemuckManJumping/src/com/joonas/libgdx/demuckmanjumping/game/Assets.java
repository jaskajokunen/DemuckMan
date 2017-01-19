package com.joonas.libgdx.demuckmanjumping.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.joonas.libgdx.demuckmanjumping.util.Constants;

//Luokka, joka toteuttaa interfacet: Disposable ja AssetErrorListener
public class Assets implements Disposable, AssetErrorListener {
	
	//Asetetaan muuttujaan luokan nimi stringina
	//Käytetään lokitukseen
	public static final String TAG = Assets.class.getName();
	
	//Viittaukset sisäisiin luokkiin
	public AssetPlayerCharacter playerChar;
	public AssetRock rock;
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetLevelDecoration levelDecoration;
	public AssetFonts fonts;
	
	//Sisältää Assets-luokan instanssin
	//Mahdollistaa lukuoikeuden tähän luokkaan
	public static final Assets instance = new Assets();
	
	//Viittaus AssetManager-luokkaan
	private AssetManager assetManager;
	
	//Privaatti konstruktori --> Tarkoittaa, että luokka on singleton
	//Privaatti konstuktori estää sen, etteivät muut luokat tee siitä instansseja
	//Singleton --> Luokasta voidaan tehdä ainoastaan yksi instanssi
	private Assets() {
		
	}
	
	//Alustaa asset managerin ja lataa assetit
	//Kutsutaan, kun peli alkaa
	public void init(AssetManager assetManager) {
		
		//Alustetaan asset manager
		this.assetManager = assetManager;
		
		//Asetetaan, että AssetErrorListeneria kutsutaan, jos assetin lataaminen epäonnistui
		//Tällöin kutsutaan error()
		//Parametrina luokka, joka toteuttaa AssetErrorListener-interfacen
		assetManager.setErrorListener(this);
		
		//Ladataan assetit asset managerin avulla
		//Parametrit:
		//Ensimmäinen parametri: Polku ja tiedoston nimi
		//Toinen parametri: Assetin tyyppi
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		
		//Aloitetaan latausprosessi
		//Odottaa, kunnes kaikki assetit on ladattu
		assetManager.finishLoading();
		
		//Lokitetaan, kuinka monta assettia ladattiin
		//getAssetNames() --> Ladattujen assettien tiedoston nimet
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		
		//Lokitetaan ladatut assetit
		for(String a : assetManager.getAssetNames()) {
			Gdx.app.debug(a, "asset: " + a);
		}
		
		//Haetaan viittaus ladattuun tekstuuri atlakseen get() avulla
		//Parametrina tekstuuri atlaksen description-tiedoston polku ja tiedoston nimi
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		//Käydään läpi tekstuuri atlaksessa olevat tekstuurit
		for(Texture t : atlas.getTextures()) {
			
			//Laitetaan tekstuurille filtteröinti
			//Ensimmäinen parametri kertoo, mitä filtteröintiä käytetään, kun pienennetään tekstuuria
			//Toinen parametri kertoo, mitä filtteröintä käytetään, kun suurennetaan tekstuuria
			//Käytettävä filtteröinti asettaa pikseleille pehmennyksen rendatessa
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		//Luodaan instanssi luokasta, joka sisältää fontit
		fonts = new AssetFonts();
		
		//Luodaan instanssit luokista, parametrina käytettävä tekstuuri atlas
		playerChar = new AssetPlayerCharacter(atlas);
		rock = new AssetRock(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		feather = new AssetFeather(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
	}

	@Override
	//Kutsutaan, kun asset managerissa tapahtuu virhe
	public void error(String fileName, Class type, Throwable throwable) {
		
		//Lokitetaan virhe-viesti
		Gdx.app.error(TAG, "Couldn't load asset '" + fileName + "'", (Exception)throwable);
	}
	
	@Override
	//Metodilla voidaan vapauttaa assetit, joita ei enää tarvita
	public void dispose() {
		
		//Delegoidaan assettien vapauttaminen asset managerille
		assetManager.dispose();
		
		//Vapautetaan bitmap fonttien resurssit
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
		
	}
	
	//Sisäinen luokka, joka sisältää pelaajan hahmon
	public class AssetPlayerCharacter{
		
		//Sisältää viittauksen tekstuuri atlaksen alikuvaan
		//Alikuva sijaitsee alunperin tiedostossa player_character.png
		public final AtlasRegion playerCharacter;
		
		//Konstruktori
		public AssetPlayerCharacter(TextureAtlas atlas) {
			//Suoritetaan alikuvan etsiminen, joka varastoitu tekstuuri atlakseen
			playerCharacter = atlas.findRegion("player_character");
		}
	}
	
	//Sisäinen luokka, joka sisältää kallion kuvat --> Toimii platformina
	public class AssetRock {
		
		//Viittaukset tekstuuri atlaksen alikuviin
		//Alikuva sijaitsee alunperin tiedostossa rock_edge.png
		public final AtlasRegion edge;
		//Alikuva saa alkunsa tiedostossa rock_middle.png
		public final AtlasRegion middle;
		
		//Konstruktori
		public AssetRock(TextureAtlas atlas) {
			//Suoritetaan alikuvien etsiminen, jotka varastoitu tekstuuri atlakseen
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}
	
	public class AssetGoldCoin {
		
		//Viittaus tekstuuri atlaksen alikuvaan
		//Alikuva sijaitsee alunperin tiedostossa item_gold_coin.png
		public final AtlasRegion goldCoin;
		
		//Konstruktori
		public AssetGoldCoin(TextureAtlas atlas) {
			//Suoritetaan alikuvan etsiminen, joka varastoitu tekstuuri atlakseen
			goldCoin = atlas.findRegion("item_gold_coin");
		}
	}
	
	public class AssetFeather {
		
		//Viittaus tekstuuri atlaksen alikuvaan
		//Alikuva sijaitsee alunperin tiedostossa item_feather.png
		public final AtlasRegion feather;
		
		//Konstruktori
		public AssetFeather(TextureAtlas atlas) {
			//Suoritetaan alikuvan etsiminen, joka varastoitu tekstuuri atlakseen
			feather = atlas.findRegion("item_feather");
		}
	}
	
	public class AssetLevelDecoration {
		//Viittaukset tekstuuri atlaksen alikuviin
		//Alikuva sijaitsee alunperin tiedostossa cloud01.png
		public final AtlasRegion cloud01;
		//Alikuva sijaitsee alunperin tiedostossa cloud02.png
		public final AtlasRegion cloud02;
		//Alikuva sijaitsee alunperin tiedostossa cloud03.png
		public final AtlasRegion cloud03;
		//Alikuva sijaitsee alunperin tiedostossa mountain_left.png
		public final AtlasRegion mountainLeft;
		//Alikuva sijaitsee alunperin tiedostossa mountain_right.png
		public final AtlasRegion mountainRight;
		//Alikuva sijaitsee alunperin tiedostossa waterOverlay;
		public final AtlasRegion waterOverlay;
		
		//Konstruktori
		public AssetLevelDecoration(TextureAtlas atlas) {
			//Suoritetaan alikuvien etsiminen, jotka varastoitu tekstuuri atlakseen
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
		}
	}
	
	public class AssetFonts {
		
		//Bitmap fontti kolmessa eri koossa
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		//Konstruktori
		public AssetFonts() {
			
			//Luodaan BitmapFontit
			//Parametrit:
			//Fontin sijainti
			//Käännetäänkö
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			
			//Asetetaan fonttien koot
			//Fonttien koot asetetaan skaalaamalla fonttia
			//Skaalataan fonttia 25% pienemmäksi
			defaultSmall.setScale(0.75f);
			
			//Annetaan fontin olla normaalin kokoinen
			defaultNormal.setScale(1.0f);
			
			//Skaalataan fonttia 2 kertaa suuremmaksi
			defaultBig.setScale(2.0f);
			
			//Asetetaan fonttien tekstuuri filtteröinniksi lineaarinen filtteröinti, jolloin saadaan pehmeät fontit
			//setFilter() parametrit: mitä tekstuuri filtteröintiä käytetään, kun: piennetään tekstuuria, suurennetaan tekstuuria
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}
	

}
