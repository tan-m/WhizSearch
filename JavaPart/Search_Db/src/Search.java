import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.mysql.jdbc.Statement;

public class Search {


	// match = 1, mismatch = -1, indel = -1

	private java.sql.Connection conn = null;
	String pwd = "abcde";
	String name = "dbuser";
	ResultSet rs;
	PreparedStatement stmt = null;	
	void connecttodb() throws Exception 
	{

		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/project";
		conn = DriverManager.getConnection(url, name, pwd);
		//stmt = (Statement) conn.createStatement();
		//rs = stmt.executeQuery("select * from note where subid=1");
		String queryString = "select * from note where subid=1";
		stmt = conn.prepareStatement(queryString);
        rs = stmt.executeQuery();
	}
 

	int MAX_DB_SIZE = 100;
	
	public void startSearch(Vector<Vector<Integer>> userQ) throws SQLException {
		
		SearchThread[] st = new SearchThread[MAX_DB_SIZE];

		ConcurrentHashMap<Integer,Integer> scoresForAllSongs = new ConcurrentHashMap<Integer,Integer>();
		ConcurrentHashMap<Integer,Integer> matchCountForAllSongs = new ConcurrentHashMap<Integer,Integer>();
		Vector<Integer> song = new Vector<Integer>();
		int threadCnt=0;
		while(rs.next())
		{
			String line =(String)rs.getString(3);
			String [] s = line.split(" ");
			for (int i = 0; i < s.length; i++) 
			{
				song.add(Integer.parseInt(s[i]));
			}
			int id = Integer.parseInt(rs.getString(1));
			st[threadCnt++] = new SearchThread((id),userQ,song, scoresForAllSongs, matchCountForAllSongs );
			song = new Vector<Integer>();
			
		}
		int ACTUAL_DB_SIZE=threadCnt;
		int i=0;	//----------here 1
		while(i<ACTUAL_DB_SIZE)
		{

			try 
			{
				st[i].t.join();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			i++;
		}
		conn.close();
		SearchResults sr = new SearchResults(scoresForAllSongs,matchCountForAllSongs,ACTUAL_DB_SIZE);		
	}	
}