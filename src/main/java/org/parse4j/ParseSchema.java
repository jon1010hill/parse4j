package org.parse4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@ParseClassName("schemas")
public class ParseSchema extends ParseObject {
	
	public ParseSchema() {
		
	}
	
	protected ParseSchema(Parse parseContext) {
		
		super(parseContext);
		
	}


	// TODO need to make sure Schema Queries always get master key - beware race conditions
	public static class DataType {

		private String typeName;
		private String targetClass;

		public DataType(String typeName) {
			this.typeName = typeName;
			this.targetClass = null;
		}

		DataType(String typeName, String targetClass) {
			this.typeName = typeName;
			this.targetClass = targetClass;
		}

		public boolean isPointer() {
			return this.targetClass != null;
		}

		public String getTypeName() {
			return typeName;
		}

		public String getTargetClass() {
			return targetClass;
		}

	}

	public static class Field {

		private String name;
		private DataType type;

		public Field(String name, DataType type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public DataType getType() {
			return type;
		}
		
		@Override
		public String toString() {
			
			return this.type.typeName + " : " + this.name;
		}

	}

	public String getClassName() {

		return getString("className");

	}

	
	@SuppressWarnings("unchecked")
	public List<Field> getFields() {

		List<Field> fields = new ArrayList<Field>();
		if (this.keySet().contains("fields")) {
			HashMap<String, Map<String, String>> map = (HashMap<String, Map<String, String>>) this
					.get("fields");
			for (Entry<String, Map<String, String>> entry : map.entrySet()) {

				String fieldName = entry.getKey();
				String typeName = entry.getValue().get("type");

				DataType t;
				if (entry.getValue().containsKey("targetClass")) {
					String targetClass = entry.getValue().get("targetClass");
					t = new DataType(typeName, targetClass);
				} else {
					t = new DataType(typeName);
				}
				Field f = new Field(fieldName, t);
				
				fields.add(f);
				
			}

		}
		return fields;
	}

}
