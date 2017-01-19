package com.joonas.libgdx.demuckmanjumping.util;

public class Constants {
	
	//M‰‰ritell‰‰n maailman koko, joka voidaan n‰hd‰ kerralla
	//, kun ei liikuta pelimaailmassa
	//N‰kyv‰n maailman leveys on 5 metri‰ leve‰
	public static final float VIEWPORT_WIDTH = 5.0F;
	//N‰kyv‰n maailman korkeus on 5 metri‰ korkea
	public static final float VIEWPORT_HEIGHT = 5.0F;
	
	//GUI:n leveys
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	
	//GUI:n korkeus
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	
	//Konstantti osoittaa tekstuuri atlaksen description-tiedostoon
	public static final String TEXTURE_ATLAS_OBJECTS = "images/demuckmanjumping.pack";
	
	//Level 01 kuva-tiedoston sijainti
	public static final String LEVEL_01 = "levels/level-01.png";
	
	//Kertoo el‰mien m‰‰r‰n, kun aloitetaan taso
	public static final int LIVES_START = 3;
	
	//Kertoo feather power-upin keston
	public static final float ITEM_FEATHER_POWERUP_DURATION = 9;
	
	//Kun pelaajalle tulee game over,
	//niin on kolmen sekunnin viiv‰stys, ennen kuin voi k‰ynnist‰‰ pelin uudestaan
	public static final float TIME_DELAY_GAME_OVER = 3;

}
