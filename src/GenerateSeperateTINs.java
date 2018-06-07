import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GenerateSeperateTINs {

	public static void main(String[] args) throws IOException {
		String fileName = "/Users/bhumi/Documents/Capstone/Testfiles/" + "opnonInterExample1.txt";
		File file = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(file));

		String st = new String();
		String[] arr = {};
		while ((st = br.readLine()) != null) {
			arr = st.split("\\),");
		}
		System.out.println(arr.length);//3328
		String tin ="";
		for(int i=0; i<3; i++){
			tin= "";
			for(int j= 0; j<=i; j++){
				tin += arr[j] + "),";
			}
			String t1 = tin.substring(0, tin.length()-1);
			t1 += ")";
			System.out.println(t1);//(i+" : "+arr[i]);
		}
	}

}
