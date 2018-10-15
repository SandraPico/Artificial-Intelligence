/*
 * Artificial Intelligence 
 * Assigment : A1 (HMM2)
 * Authors : Sandra Picó and Anna Hedström
 * Date: 10-September-2017
 * 
*/

//Java libraries
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main{

	private static Matrix Transition;
	private static Matrix Emission;
	private static Matrix Initial;
	private static Matrix Obs;
	
	public static void main(String[] args) throws  IOException{
		
		//Main variables
		BufferedReader reader = null;
		List <Double> array = new ArrayList<Double>();
		int counter = 0;
		int rows = 0;
		int columns = 0;
		Viterbi v;
		
		//1. Read the input file to achieve A, B , pi and the observed O sequence.
		try {
			//If you want to try it in Kattis, then, you must use the commented line. 
			reader = new BufferedReader(new InputStreamReader(System.in));
			//reader = new BufferedReader(new FileReader("src/Input2.txt"));
			String line = ""; 
			//Transform each line in 1 array.
			while((line = reader.readLine()) != null) {
				counter++;
				String parts[] = line.split(" ");			
				if (counter != 4) {
					rows = 0;
					columns = 0;
					for (int i = 0; i < parts.length ; i++) {
						rows = Integer.valueOf(parts[0]);
						columns = Integer.valueOf(parts[1]);
						if (i>1) {
							array.add(Double.valueOf(parts[i]));
						}
					}
					if (counter == 1) {
						//Create A Matrix
						Transition = new Matrix(rows,columns);
						//Add elements
						Transition.AddElements(array);
					}
					if (counter == 2) {
						//Create B Matrix
						Emission = new Matrix(rows,columns);
						//Add elements
						Emission.AddElements(array);
					}
					if (counter == 3) {
						//Create Pi Matrix
						Initial = new Matrix(rows,columns);
						//Add elements
						Initial.AddElements(array);
					}
				 }else {
					 rows = 1;
					 for (int i = 0; i < parts.length ; i++) {
						columns = Integer.valueOf(parts[0]);
						if (i>0) {
							array.add(Double.valueOf(parts[i]));
						}
					 }
					 //Observation sequence.
					 Obs = new Matrix(rows,columns);
					 Obs.AddElements(array);

					 //We will use the Forward algorithm to calculate the P(O|model)
					 //Class ForwardAlgorithm is created in order to make all the forward algorithm calculus. 
					 v = new Viterbi(Transition,Emission,Initial,Obs);
					 //Calculus for the forward algorithm.
					 v.Calculus();
					
			
				 }
				array.clear();
			}		
			
		}catch(FileNotFoundException e){
			reader.close();
		}	
	}

}
