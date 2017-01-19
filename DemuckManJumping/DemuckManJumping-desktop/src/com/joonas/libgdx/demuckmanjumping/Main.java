package com.joonas.libgdx.demuckmanjumping;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main {
	
	//Kontrolloi, rakennetaanko atlas uudestaan käynnistyksessä
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = false;
	
	public static void main(String[] args) {
		
		//Jos rebuildAtlas on true
		if(rebuildAtlas) {
			
			//Luodaan Setting-luokasta instanssi
			Settings settings = new Settings();
			
			//Asetetaan tekstuuri atlaksen maksimi leveys
			settings.maxWidth = 1024;
			
			//Asetetaan tekstuuri atlaksen maksimi korkeus
			settings.maxHeight = 1024;
			
			//debug --> Kontrolloi, piirretäänkö debug-viivat atlakseen
			//Tässä tapauksessa käytetään drawDebugOutline-muuttujaa asettamaan debuggauksen arvo
			settings.debug = drawDebugOutline;
			
			//Luodaan tekstuuri atlas
			//Parametrit:
			//Ensimmäinen parametri: Settings-olio, jolla voidaan konffata, millä tavalla texture atlas generoidaan
			//Toinen parametri:Lähdekansio, joka sisältää kuva-tiedostot
			//Kolmas parametri:Kohdekansio, jonne generoitu atlas luodaan
			//Neljäs parametri:Kuvaustiedoston nimi --> Tarvitaan tekstuuri atlaksen latauksessa ja käytössä
			//Sisältää tiedot alikuvista, kuten niiden sijainnin tekstuuri atlaksessa, koon, offsetit
			TexturePacker2.process(settings, "assets-raw/images", 
			"../DemuckManJumping-android/assets/images", "demuckmanjumping.pack");
		}
		
		
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "DemuckManJumping";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
		
		new LwjglApplication(new DemuckManJumpingMain(), cfg);
	}
}
