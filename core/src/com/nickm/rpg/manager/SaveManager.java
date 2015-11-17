package com.nickm.rpg.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;

public class SaveManager {
	
	private static FileHandle file = Gdx.files.local("bin/save.json");
	private static Save save = getSave();
	
	public SaveManager() {
		
	}
	private static Save getSave(){
        Save save = new Save();
        
        if(file.exists()) {
	        Json json = new Json();
	        save = json.fromJson(Save.class,Base64Coder.decodeString(file.readString()));
        }
        return save;
    }
        
    public static void saveToJson() {
        Json json = new Json();
        json.setOutputType(OutputType.json);
        file.writeString(Base64Coder.encodeString(json.prettyPrint(save)), false);
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T loadDataValue(String key, Class<?> type) {
        if(save.data.containsKey(key))return (T) save.data.get(key);
        else return null;   //this if() avoids and exception, but check for null on load.
    }
    
    public static void saveDataValue(String key, Object object) {
        save.data.put(key, object);
        saveToJson(); //Saves current save immediatly.
    }
    
    public ObjectMap<String, Object> getAllData() {
        return save.data;
    }
    
    public Boolean isEmpty() {
    	if (save.data.size == 0)
    		return true;
    	else
    		return false;
    }
    
	public static class Save {
        public ObjectMap<String, Object> data = new ObjectMap<String, Object>();
    }
	
}
