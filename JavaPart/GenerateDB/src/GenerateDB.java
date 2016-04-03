import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.io.FileOutputStream;

import com.mysql.jdbc.Statement;


public class GenerateDB {

	/**
	 * @param args 
	 */
	private java.sql.Connection conn = null;
	String pwd = "abcde";
	String name = "dbuser";
	protected ResultSet rs;
	PreparedStatement maxStmt = null, insertStmt=null;	
	void connecttodb() throws Exception 
	{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/whizsearch";
		conn = DriverManager.getConnection(url, name, pwd);
		System.out.println("got conn");
		//stmt = (Statement) conn.createStatement();
		String maxIdQuery=null, insertQuery = null;
		maxIdQuery = "select max(id) from notes";
		insertQuery = "insert into notes values( ?, ?)";
		maxStmt = conn.prepareStatement(maxIdQuery);
		insertStmt = conn.prepareStatement(insertQuery);
	}
	
	void generate() throws IOException, SQLException
    {
		FileInputStream fstream = new FileInputStream("C:\\Users\\tanmay\\Desktop\\songsNote.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		int id;
		String notation;
		ResultSet rst = maxStmt.executeQuery();
		rst.first();
		
		Object temp=rst.getString(1);
		System.out.println(rst.getString(1));
		System.out.println("temp is "+temp);
		
		if(rst.getString(1) == null)
			id = 0;
		else
			id=Integer.parseInt(temp.toString());
		
		id++;
		
		System.out.println("id --- > "+id);
		
		while ((notation = br.readLine()) != null)
		{
			notation = notation.trim();
			
				System.out.println("insert -->  "+notation);
				insertStmt.setInt(1, id);
				insertStmt.setString(2, notation);
				insertStmt.executeUpdate();
				System.out.println();
				id++;
		}

		br.close();

	}
	public static void main(String[] args) {
			
		// TODO Auto-generated method stub
		GenerateDB g = new GenerateDB();
		
		try {
			g.connecttodb();
			g.generate();
			g.conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
