package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public abstract class FileReadWrite {
	private static final String sDelim = ",";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Vector Load(String aPath) {
		Vector data = new Vector();
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(aPath));
			String row;
			while ((row = csvReader.readLine()) != null) {
				String[] dataString = row.split(sDelim);
				for(int i=0; i<dataString.length; i++) {
					data.add(Integer.parseInt(dataString[i]));
				}
			}
			csvReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	@SuppressWarnings("rawtypes")
	public static final void Save(String aPath, Vector aData) {
		try {
			FileWriter csvWriter = new FileWriter(aPath);
			for(int i=0; i<aData.size(); i++) {
				csvWriter.append(Integer.toString((int)aData.elementAt(i)));
				csvWriter.append(sDelim);
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
