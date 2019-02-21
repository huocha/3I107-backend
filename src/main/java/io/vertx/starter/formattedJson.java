package io.vertx.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


public class formattedJson {
	protected JsonObject object; 
	
	public formattedJson() {}
	
	public formattedJson(JsonObject object) {
		this.object = object;
	}
	
	public JsonArray addToArray(String key, JsonObject element) {
		JsonArray arr = new JsonArray(this.object.getValue(key).toString());
		arr.add(element);
		
		return arr;
	}
	
	public JsonObject jsonId() {
		return new JsonObject().put("name", "id").put("type", "uuid");
	}
	
	public JsonObject addIdToObject(String field, String tableName) {
		JsonObject id = jsonId();
		
		JsonArray newArray = addToArray(field, id);
		
		JsonObject formattedJson = new JsonObject().put("table", tableName).put(field, newArray);
		
		return formattedJson;
			
	}
	
	public JsonObject loadKey(String tableName) {
		JsonArray arr = new JsonArray();
		
		this.object.forEach(entry -> { 
			JsonObject field = new JsonObject();
			field.put("name", "id").put("type", "uuid");
			field.put("name", entry.getKey()).put("type", ""); 
			arr.add(field);
		});
		
		JsonObject jsonWithField = new JsonObject();
		jsonWithField.put("table", tableName).put("field", arr);
		
		return jsonWithField;
	}
}
