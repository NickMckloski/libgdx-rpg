package com.nickm.rpg.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


public class FontManager {

	public static BitmapFont generateFont(String fontName, String fontType, int size, boolean shadow) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skins/custom/fonts/"+fontName+"/"+fontName+"-"+fontType+".ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;
        if(shadow) {
            parameter.shadowColor = Color.BLACK;
            parameter.shadowOffsetY = 1;
        }
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
	}
	
}
