/*
 * Artificial Intelligence 
 * Assigment : A1 (HMM3)
 * Authors : Sandra Picó and Anna Hedström
 * Date: 9-September-2017
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

	//Matrices
	private static Matrix Transition1;
	private static Matrix Emission1;
	private static Matrix Initial1;
	private static Matrix Transition2;
	private static Matrix Emission2;
	private static Matrix Initial2;
	private static Matrix Obs;
	
	//Algorithm
	private static BaumWelch b;
	
	//Data for the big HMM
	private static double[] Adata = {0.7,0.05,0.25,0.1,0.8,0.1,0.2,0.3,0.5};
	private static double[] Bdata = {0.7,0.2,0.1,0,0.1,0.4,0.3,0.2,0,0.1,0.2,0.7};
	private static double [] pi = {1,0,0};
	
	//Data for Question 7
	private static double[] Adata2 = {0.54,0.26,0.20,0.19,0.53,0.28,0.22,0.18,0.6};
	private static double [] Bdata2 = {0.5,0.2,0.11,0.19,0.22,0.28,0.23,0.27,0.19,0.21,0.15,0.45};
	private static double [] pi2 = {0.3,0.2,0.5};
	
	
	//Data for Question 10_1 -> Uniform distribution?
	private static double[] Adata3 = {0.33333333333333333333333333,0.33333333333333333333333333,0.33333333333333333333333333,0.33333333333333333333333333,0.33333333333333333333333333,0.33333333333333333333333333,0.33333333333333333333333333,0.33333333333333333333333333,0.33333333333333333333333333};
	private static double[] Bdata3 = {0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25};
	private static double [] pi3 = {0.33333333333333333333333333,0.33333333333333333333333333,0.33333333333333333333333333};
	
	//Data for Question 10_2 -> A diagonal matrix
	private static double[] Adata4 = {1,0,0,0,1,0,0,0,1}; //Diagonal 1
	private static double[] Adata41 = {0,0,1,0,1,0,1,0,0}; //Diagonal 2
	private static double[] Bdata4 = {0.5,0.2,0.11,0.19,0.22,0.28,0.23,0.27,0.19,0.21,0.15,0.45};
	private static double [] pi4 = {1,0,0};
	
	
	//Data for Question 10_3 -> Data closed to the solution
	private static double[] Adata5 = {0.65,0.0525,0.2975,0.05,0.9,0.05,0.15,0.30,0.55};
	private static double[] Bdata5 = {0.65,0.26,0.09,0,0.05,0.45,0.28,0.22,0,0.1,0.25,0.65};
	private static double [] pi5 = {1,0,0};
	

	//Other necessary data
	private static List <Double> array = new ArrayList<Double>();
	private static int numberOfObservations = 0;
	
	public static void main(String[] args) throws  IOException{
		
		//Main variables
		BufferedReader reader = null;
	
		try {
			
			//1. Read all the observations from the file and just create our MatrixObs1000, MatrixObs10000
			reader = new BufferedReader(new FileReader("src/hmm_c_N1000.in"));
			String line = ""; 
			array.clear();
			while((line = reader.readLine()) != null) {
				String parts[] = line.split(" ");			
				for (int i = 0; i < parts.length ; i++) {
					numberOfObservations = Integer.valueOf(parts[0]);
					//Just start reading the observations
					if (i>1) {
						array.add(Double.valueOf(parts[i]));
					}
				}
			}
			
			Obs = new Matrix(1,numberOfObservations-1);
			Obs.AddElements(array);			
			//2. Create all the matrices for the HMM
			array.clear();
			for(int i = 0; i < Adata.length; i++) {
				double value = Adata[i];
				array.add(value);
			}
			Transition1 = new Matrix(3,3);
			Transition1.AddElements(array);
			array.clear();
			for(int i = 0; i < Bdata.length; i++) {
				double value = Bdata[i];
				array.add(value);
			}
			Emission1 = new Matrix(3,4);
			Emission1.AddElements(array);
			array.clear();
			for(int i = 0; i < pi.length; i++) {
				double value = pi[i];
				array.add(value);
			}
			Initial1 = new Matrix(1,3);
			Initial1.AddElements(array);
			
			//3. Call the question that you want to solve
			
			//Question7();
			//Question8();
			//Question9();
			//Question10_1();
			//Question10_2();
			Question10_3();
			
			
			
		}catch(FileNotFoundException e){
			reader.close();
		}	
	}
	
	
	//Using the data provided
	public static void Question7() {
		
		array.clear();
		for(int i = 0; i < Adata2.length; i++) {
			double value = Adata2[i];
			array.add(value);
		}
		Transition2 = new Matrix(3,3);
		Transition2.AddElements(array);
		array.clear();
		for(int i = 0; i < Bdata2.length; i++) {
			double value = Bdata2[i];
			array.add(value);
		}
		Emission2 = new Matrix(3,4);
		Emission2.AddElements(array);
		array.clear();
		for(int i = 0; i < pi2.length; i++) {
			double value = pi2[i];
			array.add(value);
		}
		Initial2 = new Matrix(1,3);
		Initial2.AddElements(array);
		
		
		//Baum Welch
		b = new BaumWelch(Transition2,Emission2,Initial2,Obs);
		b.Calculus();	
	}
	
	//Just to print the information
	public static void PrintInformation() {
		
		System.out.println("A matrix 1:");
		System.out.println(" ");
		Transition1.PrintMatrix(Transition1);
		System.out.println(" ");
		System.out.println("B matrix 1:");
		System.out.println(" ");
		Emission1.PrintMatrix(Emission1);
		System.out.println(" ");
		System.out.println("Pi matrix1:");
		Initial1.PrintMatrix(Initial1);
		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("A matrix 2:");
		System.out.println(" ");
		Transition1.PrintMatrix(Transition2);
		System.out.println(" ");
		System.out.println("B matrix 2:");
		System.out.println(" ");
		Emission2.PrintMatrix(Emission2);
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("Pi matrix 2:");
		System.out.println(" ");
		Initial2.PrintMatrix(Initial2);
		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("Observation sequence");
		System.out.println(" ");
		Obs.PrintMatrix(Obs);
		System.out.println(" ");
	}
	
	
	//Using random data but with 3x3 and 3x4 dimensions
	public static void Question8() {
	
		double value = 0;
		double amount = 0;
		double random = 0;
		
		//A MATRIX RANDOM
		Transition2 = new Matrix(3,3);
		for ( int i = 0; i < Transition2.getRows(); i++) {
			value = 0;
			amount = 0;
			for(int j = 0; j< Transition2.getColumns(); j++) {
				if ( j == (Transition2.getColumns()-1)){
					Transition2.getMatrix()[i][j] = 1 - amount;
				}else {
					random = Math.random();
					value = random/(Transition2.getColumns()-1); 
					amount = amount + value;
					Transition2.getMatrix()[i][j] = value; 	
				}
			}
		}

		//B MATRIX RANDOM
		Emission2 = new Matrix(3,4);
		for ( int i = 0; i < Emission2.getRows(); i++) {
			value = 0;
			amount = 0;
			for(int j = 0; j< Emission2.getColumns(); j++) {
				if ( j == (Emission2.getColumns()-1)){
					Emission2.getMatrix()[i][j] = 1 - amount;		
				}else {
					random = Math.random();
					value = random/(Emission2.getColumns()-1); 
					amount += value;
					Emission2.getMatrix()[i][j] = value; 
				}
			}
		}
			
		//PI MATRIX RANDOM
		Initial2 = new Matrix(1,3);
		amount = 0;
		value = 0;
		for ( int i = 0; i < Initial2.getColumns(); i++) {
			if (i == (Initial2.getColumns()-1)) {
				Initial2.getMatrix()[0][i] = 1- amount;
			}else {
				random = Math.random();
				value = random/(Initial2.getColumns()-1); 
				amount += value;
				Initial2.getMatrix()[0][i] = value; 
			}
		}
		
		PrintInformation();
		b = new BaumWelch(Transition2,Emission2,Initial2,Obs);
		b.Calculus();		
	}
	
	
	//Using random data but changing the dimensions of the matrices
	public static void Question9() {
		
		double value = 0;
		double amount = 0;
		double random = 0;
		
		int num_states = 7;
		int num_observations = 4;
		
		//A MATRIX RANDOM
		Transition2 = new Matrix(num_states,num_states);
		for ( int i = 0; i < Transition2.getRows(); i++) {
			value = 0;
			amount = 0;
			for(int j = 0; j< Transition2.getColumns(); j++) {
				if ( j == (Transition2.getColumns()-1)){
					Transition2.getMatrix()[i][j] = 1 - amount;
				}else {
					random = Math.random();
					value = random/(Transition2.getColumns()-1); 
					amount = amount + value;
					Transition2.getMatrix()[i][j] = value; 	
				}
			}
		}

		//B MATRIX RANDOM
		Emission2 = new Matrix(num_states,num_observations);
		for ( int i = 0; i < Emission2.getRows(); i++) {
			value = 0;
			amount = 0;
			for(int j = 0; j< Emission2.getColumns(); j++) {
				if ( j == (Emission2.getColumns()-1)){
					Emission2.getMatrix()[i][j] = 1 - amount;		
				}else {
					random = Math.random();
					value = random/(Emission2.getColumns()-1); 
					amount += value;
					Emission2.getMatrix()[i][j] = value; 
				}
			}
		}
			
		//PI MATRIX RANDOM
		Initial2 = new Matrix(1,num_states);
		amount = 0;
		value = 0;
		for ( int i = 0; i < Initial2.getColumns(); i++) {
			if (i == (Initial2.getColumns()-1)) {
				Initial2.getMatrix()[0][i] = 1- amount;
			}else {
				random = Math.random();
				value = random/(Initial2.getColumns()-1); 
				amount += value;
				Initial2.getMatrix()[0][i] = value; 
			}
		}
		
		b = new BaumWelch(Transition2,Emission2,Initial2,Obs);
		b.Calculus();	
		
	}
	
	//Uniform distribution
	public static void Question10_1() {
		array.clear();
		for(int i = 0; i < Adata3.length; i++) {
			double value = Adata3[i];
			array.add(value);
		}
		Transition2 = new Matrix(3,3);
		Transition2.AddElements(array);
		array.clear();
		for(int i = 0; i < Bdata3.length; i++) {
			double value = Bdata3[i];
			array.add(value);
		}
		Emission2 = new Matrix(3,4);
		Emission2.AddElements(array);
		array.clear();
		for(int i = 0; i < pi3.length; i++) {
			double value = pi3[i];
			array.add(value);
		}
		Initial2 = new Matrix(1,3);
		Initial2.AddElements(array);
		
		
		//Baum Welch
		b = new BaumWelch(Transition2,Emission2,Initial2,Obs);
		b.Calculus();	
	}
	
	//Diagonal A matrix and Pi = 0,0,1
	public static void Question10_2() {
		array.clear();
		for(int i = 0; i < Adata41.length; i++) {
			double value = Adata41[i];
			array.add(value);
		}
		Transition2 = new Matrix(3,3);
		Transition2.AddElements(array);
		array.clear();
		for(int i = 0; i < Bdata4.length; i++) {
			double value = Bdata4[i];
			array.add(value);
		}
		Emission2 = new Matrix(3,4);
		Emission2.AddElements(array);
		array.clear();
		for(int i = 0; i < pi4.length; i++) {
			double value = pi4[i];
			array.add(value);
		}
		Initial2 = new Matrix(1,3);
		Initial2.AddElements(array);
		
		
		//Baum Welch
		b = new BaumWelch(Transition2,Emission2,Initial2,Obs);
		b.Calculus();	
	}
	
	//Matrices close to the solution
	public static void Question10_3() {
		array.clear();
		for(int i = 0; i < Adata5.length; i++) {
			double value = Adata5[i];
			array.add(value);
		}
		Transition2 = new Matrix(3,3);
		Transition2.AddElements(array);
		array.clear();
		for(int i = 0; i < Bdata5.length; i++) {
			double value = Bdata5[i];
			array.add(value);
		}
		Emission2 = new Matrix(3,4);
		Emission2.AddElements(array);
		array.clear();
		for(int i = 0; i < pi5.length; i++) {
			double value = pi5[i];
			array.add(value);
		}
		Initial2 = new Matrix(1,3);
		Initial2.AddElements(array);
		PrintInformation();
		
		//Baum Welch
		b = new BaumWelch(Transition2,Emission2,Initial2,Obs);
		b.Calculus();	
		
	}

}

