import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GenerateCSV {
	
	public static String outputFileName ="/Users/bhumi/Documents/Capstone/Testfiles/" + "op.txt";
	static String ErrorLog = "/Users/bhumi/Documents/Capstone/Testfiles/"+ "Err1.txt";
	static StringBuffer tinObject = new StringBuffer();
	static String firstString = "TIN Z(";

	public static void writeLine(String string) {
		if (tinObject.length() == 0) {
			tinObject.append(firstString);
		}
		tinObject.append(string);
	}

	public static void writeFile(StringBuffer metadata) {
		if (tinObject.length() != 0) {

			try (FileWriter fw = new FileWriter(outputFileName, true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				
				tinObject.deleteCharAt(tinObject.length() - 1);// remove last
				// comma
				tinObject.append(")");// close polygon
				out.println(tinObject+ "|" + metadata);
				
				
				// System.out.println(tinObject);
				tinObject.delete(0, tinObject.length());// reset res for next
				// object
				// System.out.println(tinObject);

			} catch (IOException e) {
				// exception handling left 
			}
		}
	}

	
	public static void logError(String invalidTriangle) {
		try (FileWriter fw = new FileWriter(ErrorLog, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
				out.println(invalidTriangle);

		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

	public static void addFileTermination(String string) {
		try (FileWriter fw = new FileWriter(outputFileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
		out.println(string);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
