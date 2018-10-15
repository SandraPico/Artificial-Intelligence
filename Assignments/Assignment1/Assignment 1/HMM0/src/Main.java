import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	private static Matrix Transition, Emission, Initial;
	private static NextGen f;
	
	 
	public static void main(String[] args) throws  IOException{

		BufferedReader reader = null;
		List <Double> array = new ArrayList<Double>();
		int counter = 0;
		int rows = 0;
		int columns = 0;
		
		try {
			//reader = new BufferedReader(new InputStreamReader(System.in));
			reader = new BufferedReader(new FileReader("src/Question2.txt"));
			String line = ""; 
			while((line = reader.readLine()) != null) {
				counter++;
				String parts[] = line.split(" ");		
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
					Transition = new Matrix(rows,columns);
					Transition.AddElements(array);
				}
				if (counter == 2) {
					Emission = new Matrix(rows,columns);
					Emission.AddElements(array);
				}
				if (counter == 3) {
					//Create Pi Matrix
					Initial = new Matrix(rows,columns);
					//Add elements
					Initial.AddElements(array);
				}
				array.clear();
			}	
			f = new NextGen(Transition,Emission,Initial);
			f.Calculus();
			
		} catch(FileNotFoundException e) {
			reader.close();
		}
	}

}