package db;

import utils.Helpers;

// to parser csv to data
public class Parser {
	private String workingDirectory = System.getProperty("user.dir");
	private String dataFileName = "/ressource/raw.csv";
	public void loadCSVFile() {
		String pathToFile = workingDirectory + dataFileName;
		Helpers.log(pathToFile);
	}
}
