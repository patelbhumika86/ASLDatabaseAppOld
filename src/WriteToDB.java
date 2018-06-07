
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class WriteToDB {
	static String file = GenerateCSV.outputFileName;
	public static void writeToTable() throws ClassNotFoundException, SQLException, IOException{
		Connection con = connectToDB();
        
        CopyManager copyManager = new CopyManager((BaseConnection) con);
        File fileDir = new File(WriteToDB.file);
        Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF-8"));
        copyManager.copyIn("COPY submeshes(geom,metadata) FROM STDIN WITH(DELIMITER '|')", in );

	}
	
	public static void deletefromTable() throws ClassNotFoundException, SQLException, IOException{
		Connection con = connectToDB();
		Statement stmt = null;
		String geom="";

        File fileDir = new File(WriteToDB.file);
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF-8"));
        System.out.println("delete started...");
        stmt = con.createStatement();
        
        while ((geom = in.readLine()) != null) {
            
            String sql = "DELETE FROM submeshes WHERE geom='"+geom+"'";
            System.out.println(geom);
            stmt.executeUpdate(sql);
            geom = "";
        }          
//        stmt.executeUpdate(sql);
        System.out.println("delete done...");
        in.close();

	}
	private static Connection connectToDB() throws ClassNotFoundException, SQLException {
		String dbURL = "jdbc:postgresql://localhost:5432/asl";
		String user = "postgres";
		String password = "password";
        Class.forName("org.postgresql.Driver");

        Connection con = DriverManager.getConnection(dbURL, user, password);
		return con;
	}
}
