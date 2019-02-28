package structure;

// Each column has a name, type and can be indexed or not
public class Column {
	protected String name;
	protected String type;
	protected Boolean isIndex;

	public Column() {}
	public Column(String name, String type) {
		this.name = name;
		this.type = type;
		this.isIndex = false;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public Boolean isIndex() {
		return this.isIndex;
	}

	public void setIndex() {
		this.isIndex = true;
	}

	public String toString() {
		return "{ name: " + this.name + ", " + "type: "+ this.type +" }";
	}
}
