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
	//K�ytet��n lokitukseen
	public static final String TAG = Assets.class.getName();
	
	//Viittaukset sis�isiin luokkiin
	public AssetPlayerCharacter playerChar;
	public AssetRock rock;
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetLevelDecoration levelDecoration;
	public AssetFonts fonts;
	
	//Sis�lt�� Assets-luokan instanssin
	//Mahdollistaa lukuoikeuden t�h�n luokkaan
	public static final Assets instance = new Assets();
	
	//Viittaus AssetManager-luokkaan
	private AssetManager assetManager;
	
	//Privaatti konstruktori --> Tarkoittaa, ett� luokka on singleton
	//Privaatti konstuktori est�� sen, etteiv�t muut luokat tee siit� instansseja
	//Singleton --> Luokasta voidaan tehd� ainoastaan yksi instanssi
	private Assets() {
		
	}
	
	//Alustaa asset managerin ja lataa assetit
	//Kutsutaan, kun peli alkaa
	public void init(AssetManager assetManager) {
		
		//Alustetaan asset manager
		this.assetManager = assetManager;
		
		//Asetetaan, ett� AssetErrorListeneria kutsutaan, jos assetin lataaminen ep�onnistui
		//T�ll�in kutsutaan error()
		//Parametrina luokka, joka toteuttaa AssetErrorListener-interfacen
		assetManager.setErrorListener(this);
		
		//Ladataan assetit asset managerin avulla
		//Parametrit:
		//Ensimm�inen parametri: Polku ja tiedoston nimi
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
		
		//K�yd��n l�pi tekstuuri atlaksessa olevat tekstuurit
		for(Texture t : atlas.getTextures()) {
			
			//Laitetaan tekstuurille filtter�inti
			//Ensimm�inen parametri kertoo, mit� filtter�inti� k�ytet��n, kun pienennet��n tekstuuria
			//Toinen parametri kertoo, mit� filtter�int� k�ytet��n, kun suurennetaan tekstuuria
			//K�ytett�v� filtter�inti asettaa pikseleille pehmennyksen rendatessa
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		//Luodaan instanssi luokasta, joka sis�lt�� fontit
		fonts = new AssetFonts();
		
		//Luodaan instanssit luokista, parametrina k�ytett�v� tekstuuri atlas
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
	//Metodilla voidaan vapauttaa assetit, joita ei en�� tarvita
	public void dispose() {
		
		//Delegoidaan assettien vapauttaminen asset managerille
		assetManager.dispose();
		
		//Vapautetaan bitmap fonttien resurssit
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
		
	}
	
	//Sis�inen luokka, joka sis�lt�� pelaajan hahmon
	public class AssetPlayerCharacter{
		
		//Sis�lt�� viittauksen tekstuuri atlaksen alikuvaan
		//Alikuva sijaitsee alunperin tiedostossa player_character.png
		public final AtlasRegion playerCharacter;
		
		//Konstruktori
		public AssetPlayerCharacter(TextureAtlas atlas) {
			//Suoritetaan alikuvan etsiminen, joka varastoitu tekstuuri atlakseen
			playerCharacter = atlas.findRegion("player_character");
		}
	}
	
	//Sis�inen luokka, joka sis�lt�� kallion kuvat --> Toimii platformina
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
			//K��nnet��nk�
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			
			//Asetetaan fonttien koot
			//Fonttien koot asetetaan skaalaamalla fonttia
			//Skaalataan fonttia 25% pienemm�ksi
			defaultSmall.setScale(0.75f);
			
			//Annetaan fontin olla normaalin kokoinen
			defaultNormal.setScale(1.0f);
			
			//Skaalataan fonttia 2 kertaa suuremmaksi
			defaultBig.setScale(2.0f);
			
			//Asetetaan fonttien tekstuuri filtter�inniksi lineaarinen filtter�inti, jolloin saadaan pehme�t fontit
			//setFilter() parametrit: mit� tekstuuri filtter�inti� k�ytet��n, kun: piennet��n tekstuuria, suurennetaan tekstuuria
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}
	

}
