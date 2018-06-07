import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Preporcessing {

	ArrayList<String> coordinateList; // = new ArrayList<String>();

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		Preporcessing obj = new Preporcessing();

////		String fileName = "/Users/bhumi/Documents/Capstone/Testfiles/" + "GradCouch3Mesh.txt";
//		File file = new File(fileName);
//		BufferedReader br = new BufferedReader(new FileReader(file));
//
//		String st = new String();
//		while ((st = br.readLine()) != null) {
//			if (st.length() != 0 && st.charAt(0) == 'v') {
//				obj.storeCoordinates(st);
//			} else if (st.length() != 0 && st.charAt(0) == 'f') {
//				obj.mapVertexToCoord(st);
//			} else if (st.length() != 0 && st.charAt(0) == 'o') {
//				GenerateCSV.writeFile();
//				obj.coordinateList = new ArrayList<String>();
//			}
//		}
//		// write last record
//		GenerateCSV.writeFile();
//		GenerateCSV.addFileTermination("\\.");
//		WriteToDB.writeToTable();
//		br.close();
	}

	void mapVertexToCoord(String st) {

		String s = st.substring(2);// ignore f
		String[] verticesArray = s.split(" ");
		
		String[] arr0 = verticesArray[0].split("//");// take the first and
		// ignore 2nd
		String[] arr1 = verticesArray[1].split("//");// take the first and
		// ignore 2nd
		String[] arr2 = verticesArray[2].split("//");// take the first and
		// ignore 2nd

		String v1 = coordinateList.get(Integer.parseInt(arr0[0]));
		String v2 = coordinateList.get(Integer.parseInt(arr1[0]));
		String v3 = coordinateList.get(Integer.parseInt(arr2[0]));
		
		if(isValidTrangle(v1, v2, v3)){
			//make a triangle 
			String v4 = v1;//v1.substring(0, v1.length()-2);
			String triangle = "((" + v1 + ", " + v2 + ", "+v3  + ", "+v4 + ")),";			
			GenerateCSV.writeLine( triangle);
		}
		else{
			//write incorrect coords to error file
			String v4 = v1.substring(0, v1.length()-2);
			String invalidTriangle = "((" + v1 + " " + v2 + " "+v3  + " "+v4 + ")),";
			GenerateCSV.logError( " Invalid triangle: "+invalidTriangle);
		}
	}

	private boolean isValidTrangle(String v1, String v2, String v3) {
		if(v1.equals(v2)|| v2.equals(v3)|| v3.equals(v1))
		return false;
		else return true;
	}

	void storeCoordinates(String st) {
		String s = st.substring(2);// starting from 2nd index store entire
		// coordinates
		String s1 = s.trim();
		coordinateList.add(s1);
	}
}
