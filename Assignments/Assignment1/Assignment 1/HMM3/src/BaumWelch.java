import java.util.ArrayList;
import java.util.List;

public class BaumWelch {
	
	private Matrix A;
	private Matrix B;
	private Matrix pi;
	private Matrix obs;
	

	public BaumWelch(Matrix A, Matrix B, Matrix pi, Matrix obs) {
		this.A = A;
		this.B = B;
		this.pi = pi;
		this.obs = obs;
	}
	
	public void Calculus() {
		
		boolean iteration = false;
		int N = A.getColumns(); //number of states
		int T = obs.getColumns(); //number of obs sequence.
		int M = B.getColumns(); //number of observations types.
		Matrix alfa;
		Matrix beta;
		
		double [][][] digamma = new double[N][N][T];
		Matrix gamma = new Matrix(N,T);
		Matrix c;
		
		gamma = new Matrix(N,T);
		alfa = new Matrix(N,T);
		beta = new Matrix(N,T);
		c = new Matrix(1,T);
		int maxIters = 100;
		int iters = 0;
		double denom = 0;
		double numer = 0;
		double logProb =0;
		double oldLogProb = Math.log10(0); //-infinity
		
		while (iteration == false) {
		
		//co
		c.getMatrix()[0][0] = 0.0;		
		//Alfa
		//Compute alfa0.
		for ( int i = 0; i<= N-1; i++) {
			alfa.getMatrix()[i][0] = pi.getMatrix()[0][i]*B.getMatrix()[i][(int)obs.getMatrix()[0][0]];
			c.getMatrix()[0][0] = c.getMatrix()[0][0] + alfa.getMatrix()[i][0];
		}		
		//scale alfa0.
		c.getMatrix()[0][0] = 1/(c.getMatrix()[0][0]);
		for (int i = 0; i<= N-1; i++) {
			alfa.getMatrix()[i][0] = c.getMatrix()[0][0]*alfa.getMatrix()[i][0];
		}		
		//compute alfat
		for (int t = 1; t<= T-1; t++) {
			c.getMatrix()[0][t] = 0.0;
			for (int i = 0; i<= N-1; i++) {
				alfa.getMatrix()[i][t] = 0.0;
				for ( int j = 0; j<= N-1; j++) {
					alfa.getMatrix()[i][t] = alfa.getMatrix()[i][t] + alfa.getMatrix()[j][t-1]*A.getMatrix()[j][i];
				}
				alfa.getMatrix()[i][t] = alfa.getMatrix()[i][t]* B.getMatrix()[i][(int)obs.getMatrix()[0][t]];
				c.getMatrix()[0][t] = c.getMatrix()[0][t] + alfa.getMatrix()[i][t];
			}
			//scale alfat 
			c.getMatrix()[0][t] = 1/(c.getMatrix()[0][t] );
			for(int i = 0; i<=N-1;i++) {
				alfa.getMatrix()[i][t] = c.getMatrix()[0][t]*alfa.getMatrix()[i][t];
			}
		}		
		//Betta pass algorithm
		//Last column of betta matrix.
		for ( int i = 0; i <= N-1; i++) {
			beta.getMatrix()[i][T-1] = c.getMatrix()[0][T-1];
		}
				
		//Betta pass.
		for (int t = T-2; t>= 0; t--) {
			for ( int i = 0; i<= N-1; i++) {
				beta.getMatrix()[i][t] = 0.0;
				for ( int j = 0; j<= N-1; j++) {
					beta.getMatrix()[i][t] = beta.getMatrix()[i][t] + (A.getMatrix()[i][j]*B.getMatrix()[j][(int)obs.getMatrix()[0][t+1]]*beta.getMatrix()[j][t+1]);
				}
				//scale Betta with the same factor as alfa.
				beta.getMatrix()[i][t] = c.getMatrix()[0][t]*beta.getMatrix()[i][t];
			}
		}
				
		//Compute gamma(i,j) i gammat(i)
		for (int t = 0; t<=T-2; t++) {
			denom = 0;
			for( int i = 0; i<= N-1; i++) {
				for(int j = 0; j<= N-1; j++) {
					denom = denom + (alfa.getMatrix()[i][t]*A.getMatrix()[i][j]*B.getMatrix()[j][(int)obs.getMatrix()[0][t+1]]*beta.getMatrix()[j][t+1]);
				}
			}
			for ( int i = 0; i<= N-1; i++) {
				gamma.getMatrix()[i][t] = 0.0;
				for ( int j = 0; j<= N-1; j++) {
					digamma[i][j][t] = (alfa.getMatrix()[i][t]*A.getMatrix()[i][j]*B.getMatrix()[j][(int)obs.getMatrix()[0][t+1]]*beta.getMatrix()[j][t+1]) / denom;
					gamma.getMatrix()[i][t] = gamma.getMatrix()[i][t] + digamma[i][j][t];
				}
			}
		}
				
		//Special case for the last iteration.
		denom = 0;
		for ( int i = 0; i <= N-1; i++) {
			denom = denom + alfa.getMatrix()[i][T-1];
		}
		for ( int i = 0; i<= N-1; i++) {
			gamma.getMatrix()[i][T-1] = (alfa.getMatrix()[i][T-1] ) / denom;
		}
				
		//re-estimate pi
		for (int i = 0; i<= N-1; i++) {
			pi.getMatrix()[0][i] = gamma.getMatrix()[i][0];
		}
				
		//re-estimate A
		for( int i = 0; i <= N-1; i++) {
			for(int j = 0; j<= N-1; j++) {
				numer = 0;
				denom = 0;
				for (int t = 0; t<= T-2;t++) {
					numer = numer + digamma[i][j][t];
					denom = denom + gamma.getMatrix()[i][t];
				}
				A.getMatrix()[i][j]= numer/denom;
			}
		}
				
		//re-estimate B
		for ( int i = 0; i<= N-1; i++) {
			for ( int j = 0; j<= M-1; j++) {
				numer = 0;
				denom = 0;
				for ( int t = 0; t<= T-1; t++) {
					if (((int)obs.getMatrix()[0][t]) == j) {
						numer = numer + gamma.getMatrix()[i][t];
					}
					denom = denom + gamma.getMatrix()[i][t];
				}
				B.getMatrix()[i][j] = numer /denom;
			}
		}
				
		//compute log(P(0|lambda))
		logProb = 0;
		for ( int i = 0; i<= T-1; i++) {
			logProb = logProb + Math.log10(c.getMatrix()[0][i]);
		}
		logProb = -logProb;
		iters = iters +1;
		if ( (iters < maxIters) && (logProb> oldLogProb)) {
			oldLogProb = logProb;
			iteration = false;
		}else {
			iteration = true;
		}
		}
		System.out.print(A.getRows());
		System.out.print(" ");
		System.out.print(A.getColumns());
		System.out.print(" ");
		for(int i = 0; i < A.getRows(); i++) {
			for (int j = 0; j < A.getColumns(); j++) {
				System.out.print(A.getMatrix()[i][j]);
				System.out.print(" ");
			}
		}
		System.out.println();
		System.out.print(B.getRows());
		System.out.print(" ");
		System.out.print(B.getColumns());
		System.out.print(" ");
		for(int i = 0; i < B.getRows(); i++) {
			for (int j = 0; j < B.getColumns(); j++) {
				System.out.print(B.getMatrix()[i][j]);
				System.out.print(" ");
			}
		}
		
		
	}
}