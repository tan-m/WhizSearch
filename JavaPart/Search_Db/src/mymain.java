import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;


public class mymain {
	
	static int noteLength;
	
	public static void main(String[] args) 
	{
		int start=(int) System.currentTimeMillis();
		
		String str1;
		Vector<Double> finalResult = new Vector<Double> ();
		BufferedReader br1 = null;
		try {
			//br1 = new BufferedReader(new FileReader(".\\TempFile\\pitch.txt"));
			br1 = new BufferedReader(new FileReader("C:\\Users\\tanmay\\Desktop\\pitch.txt"));
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try 
		{
			while( (str1 = br1.readLine()) != null)
			{
				double d1=Double.parseDouble(str1);
				finalResult.add(d1);	
			}
		} 
		catch (NumberFormatException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace(); 
		}
		
		Notations n= new Notations(finalResult);	
		Vector<Integer> Q =n.extractNotations();
		noteLength = Q.size();
		
		// keep this syso
		System.out.println(Q);
		
		Vector<Vector<Integer>> transposedInput = new Vector<Vector<Integer>>();
		MusciallyTransposedInput inp = new MusciallyTransposedInput(Q, transposedInput);
		inp.transpose();
		
		Search sch = new Search();
		try 
		{
			sch.connecttodb();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		try 
		{
			sch.startSearch(transposedInput);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		int end=(int) System.currentTimeMillis();
		System.out.println("exec time "+(end-start));
	}

}
