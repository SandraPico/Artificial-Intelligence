import java.util.ArrayList;
import java.util.List;

public class Viterbi {
	
	//Given...A,B,Pi and the observed sequence.
	private Matrix A;
	private Matrix B;
	private Matrix pi;
	private Matrix obs;
	private int state = 0;

	public Viterbi(Matrix A, Matrix B, Matrix pi, Matrix obs) {
		this.A = A;
		this.B = B;
		this.pi = pi;
		this.obs = obs;
	}
	
	//Calculus for the Viterbi algorithm.
	public void Calculus() {
		
		Matrix aux = new Matrix();
		Matrix vector;
		Matrix vector2;
		Matrix vector3;

		Matrix auxVectors;
		Matrix maxVector;
		Matrix viterbiProb;
		Matrix viterbiState;
		int rows = 0;
		int numState = 0;
		int numStatePosition = 0;
		
		Matrix result;

		
		
		viterbiProb = new Matrix(A.getRows(), obs.getColumns()-1);
		viterbiState = new Matrix(A.getRows(),obs.getColumns()-1);
		
		
	
		
		//We need to make as many iterations as observations we have observed.
		for(int i = 0; i< obs.getColumns(); i++ ) {
			//First iteration--> We need Pi.
			if ( i == 0) {
				//First observation
				vector = B.column((int)(obs.getMatrix()[0][i]));
				aux = pi.multiplyVector(vector);	
				rows = aux.getRows();
			}else {
				//Rest of the observed sequence.
				auxVectors = new Matrix(A.getRows(),A.getColumns());
				vector3 = B.column((int)(obs.getMatrix()[0][i]));
					
				for (int k = 0; k < auxVectors.getRows(); k++) {
					vector2 = A.column(k);	
					for (int q = 0; q < auxVectors.getColumns(); q++) {
						auxVectors.getMatrix()[k][q] =  aux.getMatrix()[q][0]*vector2.getMatrix()[q][0]*vector3.getMatrix()[k][0];
					}
				}
				
				//Maximized probability.
				maxVector = new Matrix(auxVectors.getRows(),2);
				for(int l= 0; l< maxVector.getRows();l++) {
					state = 0;
					maxVector.getMatrix()[l][0] = MaximumRow(auxVectors,l);
					maxVector.getMatrix()[l][1] = state-1;
				}
				//Write the information to the Viterbi matrices.
				for (int p = 0; p < aux.getRows(); p++) {
					viterbiProb.getMatrix()[p][i-1] = maxVector.getMatrix()[p][0];
					viterbiState.getMatrix()[p][i-1] = maxVector.getMatrix()[p][1];
				}
				for(int f = 0; f< aux.getRows(); f++) {
					aux.getMatrix()[f][0]= maxVector.getMatrix()[f][0];
				}
				
				
			}
		}
		//Here we have the max state matrix and the max probability matrix.
		//We must take the last column first.
		
		
		
		result = new Matrix(1,obs.getColumns());
		double maxProb = 0.0;
		for( int i = 0; i < viterbiProb.getRows(); i++) {
			double element = viterbiProb.getMatrix()[i][viterbiProb.getColumns()-1];
			if(element>maxProb) {
				maxProb= element;
				result.getMatrix()[0][result.getColumns()-1] = i;
			}
		}
		
		
		
		for (int j = obs.getColumns()-1; j>0 ;j-- ) {
			int position = (int)(result.getMatrix()[0][j]);		
			result.getMatrix()[0][j-1] = viterbiState.getMatrix()[position][j-1];
		}
		for(int i = 0; i < result.getColumns(); i++) {
			System.out.print((int)result.getMatrix()[0][i]);
			System.out.print(" ");
		}
		
	}
	

	
	
	public int MaximumStatePosition(Matrix m, int column) {
		int state = 0;
		double maximum = 0.0;
		for(int i = 0; i < m.getRows(); i++) {
			if (m.getMatrix()[i][column] > maximum) {
				maximum = m.getMatrix()[i][column];		
				state = i;
			}
		}
		return state;
	}
	
	public double MaximumRow(Matrix m, int row) {
		double maximum = 0.0;
		for(int i = 0; i < m.getColumns(); i++) {
			if (m.getMatrix()[row][i] > maximum) {
				maximum = m.getMatrix()[row][i];
				state = i + 1;
			}
		}
		return maximum;
	}
}
