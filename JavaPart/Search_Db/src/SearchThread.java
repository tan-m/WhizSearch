
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;




public class SearchThread implements Runnable 
{
	Thread t;
	Vector<Integer> db;
	Vector<Vector<Integer>> q;
	Vector<Integer> matchCnt;
	Vector<Integer> editSc;
	int id;
	int[][] F;
	int [][] FforScore;
	int i,j;
	int match, delete, insert, d;
	int maxEditSc, maxMatchCnt;
	ConcurrentHashMap<Integer,Integer> scoresForAllSongs,  matchCountForAllSongs;

	SearchThread(int id, Vector<Vector<Integer>> q, Vector<Integer>  db, ConcurrentHashMap<Integer,Integer> scoresForAllSongs, ConcurrentHashMap<Integer,Integer> matchCountForAllSongs)
	{
		this.id=id;
		this.db=db;
		this.q=q;
		editSc = new Vector<Integer>(); 
		this.scoresForAllSongs = scoresForAllSongs;
		this.matchCountForAllSongs = matchCountForAllSongs;
		matchCnt= new Vector<Integer>() ;
		F = new int[100][100];
		FforScore = new int[100][100];
		t = new Thread(this);
		t.start();
	}
	int delSc()
	{
		return -1;
	}
	int simSc(int a, int b)
	{
		if(a == b)
		{
			return 1;
		}
		return -1;
		//return (Math.abs(a-b)*-1);
	}


	int align(int F[][],Vector<Integer> q, Vector<Integer> db)
	{
		String AlignmentA = ""; // query
		String AlignmentB = ""; // db
		int i = q.size();
		int j = db.size();
		while (i >0 || j > 0)
		{
			if (i > 0 && j > 0 && F[i][j] == (F[i-1][j-1] + simSc(q.get(i-1),db.get(j-1))))
			{
				AlignmentA = q.get(i-1).toString()+" "  + AlignmentA;
				AlignmentB = db.get(j-1).toString()+" "  + AlignmentB;
				i = i-1;
				j = j-1;
			}
			else if (i > 0 && F[i][j] == (F[i-1][j] + delSc()))
			{
				AlignmentA = q.get(i-1).toString() +" "+ AlignmentA;
				AlignmentB = "-" +" " + AlignmentB;
				i= i-1;
			}
			else if (j > 0 && F[i][j] == F[i][j-1] + delSc())
			{
				AlignmentA 	= "-"+" "  + AlignmentA;
				AlignmentB 	= db.get(j-1).toString()+" "  + AlignmentB;
				j=j-1;
			}
		}
		int match=0;
		String[] qarr = AlignmentA.split(" ");
		String[] dbarr =AlignmentB.split(" ");
		for (int j2 = 0; j2 < qarr.length; j2++) {
			if(qarr[j2].equals(dbarr[j2]) && (!qarr[j2].equals("-")))
			{
				match++;
			}
		}
		return match;
	}

	// needleman wunch algo
	//       db
	//     -------------
	//     |
	//  q  |
	//     |

	public int needlemanWunsch(int F[][],Vector<Integer> curr,Vector<Integer> q, int flag) // flag = 0 the it is for score
	{
		d = delSc();

		for(int i=0;i<=q.size();i++)
		{
			F[i][0] = d * i;
		}
		for(int i=0;i<=curr.size();i++)
		{
			if(flag == 0) 
				F[0][i] = 0; // score only
			else
				F[0][i] = d * i; 
		}
		for(i=1;i<=q.size();i++)
		{
			for(j=1;j<=curr.size();j++)
			{
				match = F[i-1][j-1] + simSc(q.get(i-1),curr.get(j-1));
				delete = F[i-1][j] + d;
				insert = F[i][j-1] + d;
				F[i][j] = Math.max(delete, Math.max(match,insert));                    
			}
		}


		int max=0;


		if(flag == 0) // this is for no end gap penalty
		{
			max = F[q.size()][0];
			for (int i = 0; i <= curr.size(); i++) 
			{
				if(F[q.size()][i] > max)
					max = F[q.size()][i];
			}
			editSc.add(max);
		}
		else 
		{
			// calling the align function 
			maxMatchCnt = align(F,q,curr);
		}	

		return max;
	}

	public void run() 
	{

		maxEditSc=Integer.MIN_VALUE;
		maxMatchCnt=Integer.MIN_VALUE;
		Vector<Integer> max_curr = null;

		for (int ver = 1; ver < q.size(); ver++) 
		{

			Vector<Integer> curr = q.get(ver-1);
  
			needlemanWunsch(FforScore,db,curr,0);

			if(maxEditSc < editSc.get(ver-1))
			{
				maxEditSc = editSc.get(ver-1);
				max_curr = curr;
			}

		} // end of for for loop

		needlemanWunsch(F,db,max_curr ,1); // find alignment for the best

		matchCountForAllSongs.put(id,maxMatchCnt);
		scoresForAllSongs.put(id,maxEditSc); 

	}
}