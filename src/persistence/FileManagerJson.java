package persistence;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import models.GameData;

public class FileManagerJson {

	private static final String TAG_LIST = "Data";
	private static final String TAG_X = "x";
	private static final String TAG_Y = "y";
	private static final String TAG_ADHERED = "adhered";     
	private static final String TAG_X_BEFORE_JUMP = "xBeforeJump";    
	private static final String TAG_Y_BEFORE_JUMP = "yBeforeJump";    
	private static final String TAG_FALL ="fall";        
	private static final String TAG_INDEX = "index";          
	private static final String TAG_IMAGE = "image";        
	private static final String TAG_X_SPRITE_COORDS ="xSpriteCoords";
	private static final String TAG_Y_SPRITE_COORDS ="ySpriteCoords";
	private static final String TAG_WIDTH_SPRITE_COORDS ="widthSpriteCoords";
	private static final String TAG_HEIGHT_XSPRITE_COORDS ="heightSpriteCoords";
	private static final String DB_PATH = "./db/list.json";

	public static void write(GameData list) {
		JSONObject jsonfile = new JSONObject();
		JSONArray jsonList = new JSONArray();
			JSONObject jsonProfile = new JSONObject();
			jsonProfile.put(TAG_X, list.getX());
			jsonProfile.put(TAG_Y, list.getY());
			jsonProfile.put(TAG_ADHERED, list.getAdhered());
			jsonProfile.put(TAG_X_BEFORE_JUMP, list.getxBeforeJump());
			jsonProfile.put(TAG_Y_BEFORE_JUMP, list.getyBeforeJump());
			jsonProfile.put(TAG_FALL, list.getFall());
			jsonProfile.put(TAG_IMAGE, list.getImage());
			jsonProfile.put(TAG_X_SPRITE_COORDS, list.getxSpriteCoords());
			jsonProfile.put(TAG_Y_SPRITE_COORDS, list.getySpriteCoords());
			jsonProfile.put(TAG_WIDTH_SPRITE_COORDS, list.getWidthSpriteCoords());
			jsonProfile.put(TAG_HEIGHT_XSPRITE_COORDS, list.getHeightSpriteCoords());
			jsonList.add(jsonProfile);
		jsonfile.put(TAG_LIST, jsonList);
		try (FileWriter file = new FileWriter(DB_PATH, false)) {
			file.write(jsonfile.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<GameData> read() {
		ArrayList<GameData> list = new ArrayList<GameData>();
		JSONParser parser = new JSONParser();
		try (Reader reader = new FileReader(DB_PATH)) {
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			JSONArray jsonList = (JSONArray) jsonObject.get(TAG_LIST);
			Iterator<JSONObject> iterator = jsonList.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonListComponent = (JSONObject) iterator.next();
				list.add(new GameData((Long)jsonListComponent.get(TAG_X),						
						(Long)jsonListComponent.get(TAG_Y),
						(String)jsonListComponent.get(TAG_ADHERED),
						(Long)jsonListComponent.get(TAG_X_BEFORE_JUMP),
						(Long)jsonListComponent.get(TAG_Y_BEFORE_JUMP),
						(String)jsonListComponent.get(TAG_FALL),
						(String)jsonListComponent.get(TAG_IMAGE),
						(Long)jsonListComponent.get(TAG_X_SPRITE_COORDS),						
						(Long)jsonListComponent.get(TAG_Y_SPRITE_COORDS),						
						(Long)jsonListComponent.get(TAG_WIDTH_SPRITE_COORDS),						
						(Long)jsonListComponent.get(TAG_HEIGHT_XSPRITE_COORDS)						
						));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

}