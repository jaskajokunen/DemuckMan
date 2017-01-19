package com.joonas.libgdx.demuckmanjumping.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

//Abstrakti-luokka, joka sis�lt�� game objectin yleiset attribuutit ja toiminnallisuudet
//Haarautuu game objecteihin
//Abstraktista-luokasta ei voi tehd� instanssia, mutta siit� voi haarautua
//Abstraktissa-luokassa ei my�sk��n tehd� toteutusta
//Esim. abstract void moveTo(double deltaX, double deltaY)
public abstract class AbstractGameObject {
	
	//Muuttujien avulla voidaan tallentaa game objectin:
	//sijainti, mittasuhde, nollapiste, skaalauskerroin ja rotaation kulma
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	
	//Olion nykyinen nopeus m/s
	public Vector2 velocity;
	
	//Olion positiivinen ja negatiivinen maksiminopeus m/s
	public Vector2 terminalVelocity;
	
	//Vastustava voima, joka hidastaa oliota, kunnes nopeus on nolla
	//Annetaan kertoimena, jolla ei ole ulottuvuutta
	//Jos friction on 0, niin silloin ei ole kitkaa --> T�ll�in nopeus ei pienene
	public Vector2 friction;
	
	//Olion kiihtyvyys m/s^2
	public Vector2 acceleration;
	
	//Olion fyysinen vartalo, jota k�ytet��n, kun tapahtuu yhteent�rm�ys muiden olioiden kanssa
	public Rectangle bounds;
	
	//Konstruktori
	public AbstractGameObject() {
		
		//Alustetaan muuttujat
		position = new Vector2();
		dimension = new Vector2(1,1);
		origin = new Vector2();
		scale = new Vector2(1,1);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2(1,1);
		friction = new Vector2();
		acceleration = new Vector2();
		bounds = new Rectangle();
	}
	
	//Kutsutaan WorldController-luokasta
	public void update(float deltaTime) {
		
		//P�ivitet��n nopeuden komponentit
		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		
		//Asetetaan olion siirtym� x-suunnassa
		//position.x --> Sis�lt�� x-akselin suuntaisen sijainnin, 
		//ennen kuin siihen on lis�tty siirtym��
		position.x += velocity.x * deltaTime;
		
		//Asetetaan olion siirtym� y-suunnassa
		//position.y --> Sis�lt�� y-akselin suuntaisen sijainnin, 
		//ennen kuin siihen on lis�tty siirtym��
		position.y += velocity.y * deltaTime;
	}
	
	//Kutsutaan jokaisen syklin aikana, jotta voidaan laskea olion nopeuden x-komponentti
	//Parametrina deltaTime --> Kuinka paljon aikaa on kulunut, kun viimeksi rendattiin frame
	//deltaTimen avulla voidaan asettaa p�ivityksi� pelimaailmaan
	protected void updateMotionX(float deltaTime) {
		
		//Jos olion x-nopeus ei ole nolla, niin silloin se on liikkeess�
		if(velocity.x != 0) {
			
			//Asetetaan kitka (friction)
			//Koska kitkakertoimen pit�� v�hent�� nopeutta ja
			//nopeus voi olla negatiivinen, pit�� suorittaa kaksi tarkistusta
			if(velocity.x > 0) {
				
				//max() --> Palauttaa kahdesta vertailtavasta arvosta suurimman
				velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
			}
			
			else {
				
				//min() --> Palauttaa kahdesta vertailtavasta arvosta pienimm�n
				velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
			}
		}
		
		//Asetetaan x-suuntaan kiihtyvyys
		velocity.x += acceleration.x * deltaTime;
		
		//Varmistetaan, ettei olion nopeus ylit� negatiivista tai positiivista nopeutta
		//clamp() --> Muokataan arvoa niin, ett� se on tietyss� vaihteluv�liss�
		//T�ss� tapauksessa vaihteluv�li on -terminalVelocity.x ja terminalVelocity.x
		//T�m� tehd��n, koska halutaan ett� sill� on  negatiivinen max nopeus ja positiivinen max nopeus
		//Eli nopeus voi kasvaa negatiiviseen suuntaan -terminalVelocity.x  ja pos. suuntaan terminalVelocity.x
		/*
		Parametrit: value, min, max
		Jos value pienempi kuin min --> Palautetaan min
		Jos value on suurempi kuin max --> Palautetaan max
		Muussa tapauksessa palautetaan value
		Esim. alkutilanteessa zoom on 1, jolloin se on suurempi kuin min
		ja pienempi kuin max, jolloin palautetaan value, eli 1
		*/
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
		
	}
	
	//Kutsutaan jokaisen syklin aikana, jotta voidaan laskea olion nopeuden y-komponentti
	//Parametrina deltaTime --> Kuinka paljon aikaa on kulunut, kun viimeksi rendattiin frame
	//deltaTimen avulla voidaan asettaa p�ivityksi� pelimaailmaan
	protected void updateMotionY(float deltaTime) {
		
		//Jos olion y-nopeus ei ole nolla, niin silloin se on liikkeess�
		if(velocity.y != 0) {
			
			//Asetetaan kitka (friction)
			//Koska kitkakertoimen pit�� v�hent�� nopeutta ja
			//nopeus voi olla negatiivinen, pit�� suorittaa kaksi tarkistusta
			if(velocity.y > 0) {
				
				//max() --> Palauttaa kahdesta vertailtavasta arvosta suurimman
				velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
			}
			
			else {
				
				//min() --> Palauttaa kahdesta vertailtavasta arvosta pienimm�n
				velocity.y = Math.min(velocity.y + friction.x * deltaTime, 0);
			}
		}
		
		//Asetetaan y-suuntaan kiihtyvyys
		velocity.y += acceleration.y * deltaTime;
		
		//Varmistetaan, ettei olion nopeus ylit� negatiivista tai positiivista nopeutta
		//clamp() --> Muokataan arvoa niin, ett� se on tietyss� vaihteluv�liss�
		//T�ss� tapauksessa vaihteluv�li on -terminalVelocity.y ja terminalVelocityy
		//T�m� tehd��n, koska halutaan ett� sill� on  negatiivinen max nopeus ja positiivinen max nopeus
		//Eli nopeus voi kasvaa negatiiviseen suuntaan -terminalVelocity.y  ja pos. suuntaan terminalVelocity.y
		/*
		Parametrit: value, min, max
		Jos value pienempi kuin min --> Palautetaan min
		Jos value on suurempi kuin max --> Palautetaan max
		Muussa tapauksessa palautetaan value
		Esim. alkutilanteessa zoom on 1, jolloin se on suurempi kuin min
		ja pienempi kuin max, jolloin palautetaan value, eli 1
		*/
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
		
	}

	//Kutsutaan WorldRenderer-luokasta
	public abstract void render (SpriteBatch batch);
	

}
