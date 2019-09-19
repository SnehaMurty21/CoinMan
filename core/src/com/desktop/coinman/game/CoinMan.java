package com.desktop.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState=0;
	int pause=0;
	float gravity=0.2f;
	float velocity=0;
	int manY=0;
	Random random;
	Rectangle manRectangle;

	ArrayList<Integer> CoinXs= new ArrayList<>();
	ArrayList<Integer> CoinYs= new ArrayList<>();
	ArrayList<Rectangle> coinRectangle= new ArrayList<Rectangle>();
	Texture Coins;
	int CoinCount;

	ArrayList<Integer> bombXs= new ArrayList<>();
	ArrayList<Integer> bombYs= new ArrayList<>();
	ArrayList<Rectangle> bombRectangle= new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount;

	int score=0;
	BitmapFont font;

	int gameState=0;
	Texture dizzy;



	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight() / 2;

		Coins = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();
		font= new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		dizzy=new Texture("dizzy-1.png");


	}

        public void makeCoins(){

		float height=random.nextFloat()*Gdx.graphics.getHeight();
		CoinYs.add((int)height);
		CoinXs.add(Gdx.graphics.getWidth());
		}

	public void makeBomb(){

		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}



	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		velocity=velocity+gravity;
		manY-=velocity;

		if (gameState==1){
			// game is live

//coin code
			if(CoinCount < 100){
				CoinCount++;
			}else{
				CoinCount=0;
				makeCoins();;
			}
			coinRectangle.clear();
			for(int i=0;i<CoinXs.size();i++){
				batch.draw(Coins,CoinXs.get(i),CoinYs.get(i));
				CoinXs.set(i,CoinXs.get(i)-6);
				coinRectangle.add(new Rectangle(CoinXs.get(i),CoinYs.get(i),Coins.getWidth(),Coins.getHeight()));
			}
// bomb code
			if(bombCount < 250){
				bombCount++;
			}else{
				bombCount=0;
				makeBomb();;
			}

			bombRectangle.clear();
			for(int i=0;i<bombXs.size();i++){
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i)-8);
				bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
			}

			if(Gdx.input.justTouched()){
				velocity= -10;
			}

			if(pause<4){
				pause++;
			}else {
				pause=0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			if(manY<=0){
				manY=0;
			}

		}else if(gameState==0){
			//waiting to start

			if(Gdx.input.justTouched()){
				gameState=1;
			}

		}



		else  if(gameState==2) {
			//game over
			if (Gdx.input.justTouched()) {
				gameState = 1;
				manY = Gdx.graphics.getHeight() / 2;
				score = 0;
				velocity = 0;
				CoinXs.clear();
				CoinYs.clear();
				coinRectangle.clear();
				CoinCount = 0;
				bombXs.clear();
				bombYs.clear();
				bombRectangle.clear();
				bombCount = 0;

			}
		}

		if (gameState==2){
			batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[manState].getWidth()/2, manY);
			manY=0;
		}else{
			batch.draw(man[manState],Gdx.graphics.getWidth()/2-man[manState].getWidth()/2, manY);
		}

		//coin collision
		manRectangle= new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth()/2, manY,man[manState].getWidth(),man[manState].getHeight());

		for (int i=0;i<coinRectangle.size();i++)
		{
			if(Intersector.overlaps(manRectangle,coinRectangle.get(i))){
				score++;
				coinRectangle.remove(i);
				CoinXs.remove(i);
				CoinYs.remove(i);
				break;
			}
		}
         font.draw(batch,String.valueOf(score), 100,200);
//bomb collision


		for (int i=0;i < bombRectangle.size();i++)
		{
			if(Intersector.overlaps(manRectangle,bombRectangle.get(i))){
				Gdx.app.log("bomb", "bomb collision");
				gameState=2;
			}
		}
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
