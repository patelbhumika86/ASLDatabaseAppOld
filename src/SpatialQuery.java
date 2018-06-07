import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;

import org.postgis.PGbox3d;
//import org.postgis.PGgeometry;
import org.postgis.Point;

public class SpatialQuery {
	public  String queryOutputFile = "/Users/bhumi/Documents/Capstone/Testfiles/" +"output.txt";
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		SpatialQuery q = new SpatialQuery();
		
//		PGgeometry geom = new PGgeometry();
		PGbox3d inputbox = new PGbox3d(new Point(1.0, 1.0, 1.0),
                new Point(5.0, 5.0, 5.0));
		q.findIntersectingObjs(inputbox);
	}
	
	String dbURL = "jdbc:postgresql://localhost:5432/asl";
	String user = "postgres";
	String password = "password";
	
	
	public Connection connect() throws SQLException, ClassNotFoundException {
		Connection con = DriverManager.getConnection(dbURL, user, password);
		((org.postgresql.PGConnection)con).addDataType("geometry",Class.forName("org.postgis.PGgeometry"));
	    ((org.postgresql.PGConnection)con).addDataType("box3d",Class.forName("org.postgis.PGbox3d"));
		return con;	
    }

	private void displayResultIDs(ResultSet rs) throws SQLException {
		
	
		try (FileWriter fw = new FileWriter(queryOutputFile, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			while (rs.next()) {
	        	out.println(rs.getString("metadata"));
//	        	 System.out.println(rs.getString("id"));
	        }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
	}
    
    public void findIntersectingObjs(PGbox3d inputbox) throws ClassNotFoundException {
    	String SQL ="SELECT id-1 id, metadata FROM t1_table WHERE geom &&& ?";
       
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {
        	
        	pstmt.setObject(1, inputbox);
            ResultSet rs = pstmt.executeQuery();
            displayResultIDs(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
