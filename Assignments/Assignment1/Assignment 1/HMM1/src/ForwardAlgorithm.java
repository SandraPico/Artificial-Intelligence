import java.util.ArrayList;
import java.util.List;

public class ForwardAlgorithm {
	
	//Given...A,B,Pi and the observed sequence.
	private Matrix A;
	private Matrix B;
	private Matrix pi;
	private Matrix obs;

	public ForwardAlgorithm(Matrix A, Matrix B, Matrix pi, Matrix obs) {
		this.A = A;
		this.B = B;
		this.pi = pi;
		this.obs = obs;
	}
	
	//Calculus for the Forward algorithm.
	public double Calculus() {
	
		Matrix aux = new Matrix();
		Matrix newAux;
		Matrix vector;
		Matrix alfaaux;
		int rows = 0;
		double result = 0.0;
		
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
				newAux = new Matrix(rows,1);
				for( int j = 0; j< rows ; j++) {
					for ( int k = 0; k < A.getColumns(); k++) {
						newAux.getMatrix()[j][0] +=  aux.getMatrix()[k][0]*A.getMatrix()[k][j];
					}
				}
				vector = B.column((int)(obs.getMatrix()[0][i]));
				alfaaux = newAux.multiplycolumnVector(vector);
				//aux --> alfaaux for the next iteration.
				for (int q = 0; q < alfaaux.getRows(); q++) {
					for(int w = 0; w < alfaaux.getColumns(); w++ ) {
						aux.getMatrix()[q][w] = alfaaux.getMatrix()[q][w];
					}
				}
			}
		}
		for (int s = 0; s < aux.getRows(); s++) {
			result += aux.getMatrix()[s][0];
		}
		return result;
	}
}
