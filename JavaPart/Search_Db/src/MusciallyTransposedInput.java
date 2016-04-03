import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;


public class MusciallyTransposedInput {

	Vector<Integer> q;
	Vector<Integer> temp;
	Vector<Vector<Integer>> transposedInput;

	MusciallyTransposedInput(  Vector<Integer>q, Vector<Vector<Integer>> transposedInput ){
		this.q = q;
		this.transposedInput = transposedInput;
	}


	/*
	 * for (int i = 0; i < max; i++) {
				for (int j = 0; j < s.length; j++) {
					val1[j] = val[j] - i;
					notation=notation.concat(val1[j]+" ");

				}
				System.out.println("insert -->  "+notation);
				this.stmt.executeUpdate("insert into note values('"
						+ id + "','" + subid
						+ "','" + notation + 
						 "')");
				System.out.println();
				subid++;
				notation = notation.replaceAll("(.*)","");
				//op.newLine();
			}
			//op.newLine();
			//op.newLine();
			for (int i = 1; i <= (36 - min); i++) {
				for (int j = 0; j < s.length; j++) {
					val1[j] = val[j] + i;
					notation=notation.concat(val1[j]+" ");
					//op.write(val1[j]+" ");

				}
				System.out.println();
				System.out.println("insert -->  "+notation);
				this.stmt.executeUpdate("insert into note values('"
						+ id + "','" + subid
						+ "','" + notation + 
						 "')");
				System.out.println();
				subid++;
				notation = notation.replaceAll("(.*)","");
				//op.newLine();
			}
	 * 
	 * */

	void transpose(){

		Iterator itr = q.iterator();
		int max = Collections.max(q);
		int min = Collections.min(q);
		int val[] = new int[q.size()];

		// making an array
		for (int i = 0; i < val.length; i++) {
			val[i] = (Integer)itr.next();
		}

		for (int i = 0; i < max; i++) {

			temp = new Vector<Integer>();

			for (int j = 0; j < val.length; j++) {
				temp.add(val[j] - i);
			}
			transposedInput.add(temp);
		}

		for (int i = 1; i <= (36 - min); i++) {

			temp = new Vector<Integer>();

			for (int j = 0; j < val.length; j++) {
				temp.add(val[j] + i);
			}

			transposedInput.add(temp);
		}


	}

}
