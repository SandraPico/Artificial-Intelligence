/** ****************************************************************
 *
 * File: HMM.java
 * Authors: Anna Hedström and Sandra Picó
 * Date: 17 September 2017
 * 
 ******************************************************************/
import java.util.ArrayList;
import java.util.List;

public class HMM {
	
	//Properties
	private Matrix A;
	private Matrix B;
	private Matrix pi;
	private Matrix obs;
	private Matrix ViterbiLast;
	
	private Matrix alfa;
	boolean IhaveBirds = false;
	
	//Re-estimated Matrices after calling the Baum Welch Algorithm
	private Matrix reestimateA;
	private Matrix reestimateB;
	private Matrix reestimatePi;
	private static List <Double> array = new ArrayList<Double>();
	
	//Data for the big HMM
	private static double[] Adata = {0.19,0.21,0.19,0.21,0.2,0.19,0.21,0.19,0.21,0.2,0.19,0.21,0.19,0.21,0.2,0.19,0.21,0.19,0.21,0.2,0.19,0.21,0.19,0.21,0.2};
	private static double[] Bdata = {0.12,0.1,0.12,0.1,0.12,0.1,0.12,0.1,0.12,0.12,0.1,0.12,0.1,0.12,0.1,0.12,0.1,0.12,0.12,0.1,0.12,0.1,0.12,0.1,0.12,0.1,0.12,0.12,0.1,0.12,0.1,0.12,0.1,0.12,0.1,0.12,0.12,0.1,0.12,0.1,0.12,0.1,0.12,0.1,0.12};
	private static double [] Pidata = {0.19,0.21,0.19,0.21,0.2};
	

	/** ***************************************************************************************************************
	 * 
	 * CONSTRUCTORS FOR HMM
	 * 
	 ************************************************************************************************************* */
	
	//Normal constructor. The HMM will receive the A,B, Pi and Obs matrices. All the information is given.
	public HMM(Matrix A, Matrix B, Matrix pi, Matrix obs) {
		this.A = A;
		this.B = B;
		this.pi = pi;
		this.obs = obs;
	}
	
	public HMM() {
		
		A = new Matrix(5,5);
		array.clear();
		for(int i = 0; i < Adata.length; i++) {
			double value = Adata[i];
			array.add(value);
		}
		A.AddElements(array);
		array.clear();
		
		for(int i = 0; i < Bdata.length; i++) {
			double value = Bdata[i];
			array.add(value);
		}
		B = new Matrix(5,9);
		B.AddElements(array);
		array.clear();	
		
		for(int i = 0; i < Pidata.length; i++) {
			double value = Pidata[i];
			array.add(value);
		}
		pi = new Matrix(1,5);
		pi.AddElements(array);
		

		obs = new Matrix(1,9);
		for (int i = 0; i < 9; i++) {
			obs.getMatrix()[0][i] = 0;
		}
	
	}
	
	
	/** *****************************************************
	 * Print matrix is a method used to print matrix value's
	 * 
	 *******************************************************/	
	public void PrintMatrix(Matrix m) {
		for ( int i = 0; i < m.getRows(); i++) {
			for(int j = 0; j< m.getColumns(); j++) {
					System.err.print(m.getMatrix()[i][j] + " ");
			}
			System.err.println(" ");
		}
	}
	
	/** *****************************************************************
	 * Method used to change the observations of an HMM.
	 * It is called before the BaumWelch algorithm, in the shoot method.
	 *******************************************************************/	
	public void AddObservation(Matrix newobs) {
		
		List<Double> data = new ArrayList<Double>();
		
		data.clear();
		for(int i = 0; i < newobs.getColumns(); i++) {
			//Agafem els elements de la matriu i els posem a l'array.
			double element = newobs.getMatrix()[0][i];
			data.add(element);
		}
		
		obs = new Matrix(1,newobs.getColumns());
		obs.AddElements(data);	
	}

	/** **********************************************************************************************************************
	 *
	 * HMMO - PROBABILITY OF NEXT EMISSION DISTRIBUTION
	 * This method is modified in order to find the most likely next movement for a bird.
	 * Basically, we use Viterbi to find the most likely path for the hidden state and, based on that , we create a new pi.
	 * 
	 ***********************************************************************************************************************/
	
	 public int MostLikelyNextObservation() {
		
		 Matrix nextgen;
		 Matrix current;
		 double max ;
		 int observation = 0;
		 
		 //Viterbi information
		 Matrix v = Viterbi();
		 int lastColumn = v.getColumns()-1;
		 double lastState = (v.getMatrix()[0][lastColumn]); //Last state of Viterbi path
		 
		 //Create a new pi based on the last state given by Viterbi algorithm.
		 Matrix newpi = new Matrix(1,A.getColumns());
		 for (int i =0 ; i < newpi.getColumns(); i++) {
			 if (i == lastState) {
				 newpi.getMatrix()[0][i] = 1;
			 }else {
				 newpi.getMatrix()[0][i] = 0;
			 }
		 }
		 
		 //Now, in order to know the next observation sequence...
		 //We must need to multiply *A and B*
		 //Using the next observation distribution in the Ex. HMM0.
		 //current = ViterbiLast.multiply(A);
		 current = newpi.multiply(A);
         nextgen = current.multiply(B);
         observation = -1;
         //Thredshold defined : 0.6
         
         //max = 0.5;
         max = 0.79; 
         // max = 0.83;
         for(int j = 0; j< nextgen.getColumns(); j++ ) {
        	 	if (nextgen.getMatrix()[0][j] > max) {
            		max = nextgen.getMatrix()[0][j];
            		observation = j;
            	} 
          }
		 return observation;
	 }
	
	/** ******************************************************************************************************************
	 *
	 * HMM1
	 * FORWARD ALGORITHM
	 * PROBABILITY OF THE EMISSION SEQUENCE 
	 * We use this function to Guess which bird is it..
	 * 
	 *******************************************************************************************************************/
	public double ForwardAlgorithm() {
		
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
	
	
	/** *******************************************************************************************************************
	 *
	 * HMM2
	 * VITERBI ALGORITHM
	 * MOST LIKELY HIDDEN STATES
	 *
	 ***************************************************************************************************************** */
	public Matrix Viterbi() {
		
		Matrix result;
		int States = pi.getColumns();
		int emissions = obs.getColumns();
		Matrix viterbi = new Matrix(States,emissions);
		Matrix viterbi2 = new Matrix(States,emissions);
		double maxprob = 0;
		double currentProb = 0;
		result = new Matrix(1,emissions);
		double max = 0;
		int maxr = 0;
		int finalrow = 0;
		
		for(int k = 0; k < States; k++) {
			viterbi.getMatrix()[k][0] = pi.getMatrix()[0][k]*B.getMatrix()[k][(int)obs.getMatrix()[0][0]];
		}
		for (int i = 1; i < emissions; i++) {
			for(int x = 0; x < States; x++) {
				maxprob =0 ;
				for (int j = 0; j< States; j++) {
					currentProb = viterbi.getMatrix()[j][i-1]*A.getMatrix()[j][x]*B.getMatrix()[x][(int)obs.getMatrix()[0][i]];
					if(currentProb>maxprob) {
						maxprob = currentProb;
						viterbi.getMatrix()[x][i] = currentProb;
						viterbi2.getMatrix()[x][i] = j;
					}
				}
			}
		}
		max = 0;
		maxr = 0;
		for (int i = 0; i < States; i++) {
			if(viterbi.getMatrix()[i][emissions-1]>max) {
				maxr =i;
				max = viterbi.getMatrix()[i][emissions-1];
			}
		}
		
		
		finalrow = maxr;
		for (int i = emissions-1; i>= 0; i--) {
			result.getMatrix()[0][i] = finalrow;
			finalrow = (int)viterbi2.getMatrix()[finalrow][i];
		}
		
		ViterbiLast = new Matrix(1,viterbi.getRows());
		for(int k = 0; k < viterbi.getRows(); k++) {
			ViterbiLast.getMatrix()[0][k] = viterbi.getMatrix()[k][viterbi.getColumns()-1];
		}
		
	
		return result;
	}
	

	/** ******************************************************************************************************************
	 * 
	 * FORWARD ALGORITHM NORMALIZED
	 * This method is the method used by Baum Welch algorithm.
	 * 
	 *******************************************************************************************************************/
	 public double ForwardNormalitzat() {

			int N = A.getColumns(); 	   //number of states
			int T = obs.getColumns();   //number of observation sequence.
			Matrix c;	
			double prob = 0;
			alfa = new Matrix(N,T);
			c = new Matrix(1,T);

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
			prob = 0;
			for ( int i = 0; i < alfa.getRows(); i++) {
				prob = alfa.getMatrix()[i][alfa.getColumns()-1];
			}
			return prob;
	 }
	 
	 
	/** *******************************************************************************************************************
	 * 
	 * HMM4
	 * BAUM WELCH ALGORITHM
	 * RE-ESTIMATE HMM PARAMETERS.
	 * This method is used to train the matrices in our system. 
	 *
	 ******************************************************************************************************************** */	
	public void BaumWelch() {
		
		boolean iteration = false;
		int N = A.getColumns();    //number of states
		int T = obs.getColumns();  //number of observation sequence.
		int M = B.getColumns();    //number of observations types.
		Matrix alfa;
		Matrix beta;
		double difference = Double.MAX_VALUE;
		
		double [][][] digamma = new double[N][N][T];
		Matrix gamma = new Matrix(N,T);
		Matrix c;
		
		gamma = new Matrix(N,T);
		alfa = new Matrix(N,T);
		beta = new Matrix(N,T);
		c = new Matrix(1,T);
		int maxIters = 200;
		int iters = 0;
		double denom = 0;
		double numer = 0;
		double logProb =0;
		double error = 200;
		double oldLogProb = -Double.MAX_VALUE; //-infinity
		
		while(iteration == false && error> 0.01) {
			//co
			c.getMatrix()[0][0] = 0.0;		
			//Alfa
			//Compute alfa0.
			for ( int i = 0; i<= N-1; i++) {
				alfa.getMatrix()[i][0] = pi.getMatrix()[0][i]*B.getMatrix()[i][(int)obs.getMatrix()[0][0]];
				alfa.getMatrix()[i][0] += 0.000000000000000000000000000001; //Underflow. NaN problems.
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
					alfa.getMatrix()[i][t] += 0.000000000000000000000000000001; //Underflow. NaN problems.
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
						denom = denom + (alfa.getMatrix()[i][t]*A.getMatrix()[i][j]*B.getMatrix()[j][(int)obs.getMatrix()[0][t+1]]*beta.getMatrix()[j][t+1]) ;
						denom += 0.000000000000000000000000000001; //Underflow. NaN problems.
					}
				}
				for ( int i = 0; i<= N-1; i++) {
					gamma.getMatrix()[i][t] = 0.0;
					for ( int j = 0; j<= N-1; j++) {
						digamma[i][j][t] = ((alfa.getMatrix()[i][t]*A.getMatrix()[i][j]*B.getMatrix()[j][(int)obs.getMatrix()[0][t+1]]*beta.getMatrix()[j][t+1]) / denom );
						digamma[i][j][t] += 0.000000000000000000000000000001;
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
					A.getMatrix()[i][j]= (numer/denom);
					A.getMatrix()[i][j] += 0.000000000000000000000000000001;
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
					B.getMatrix()[i][j] = (numer /denom);
					B.getMatrix()[i][j] += 0.000000000000000000000000000001;
				}
			}	
			
			//compute log(P(0|lambda))
			logProb = 0;
			for ( int i = 0; i<= T-1; i++) {
				logProb = logProb + Math.log10(c.getMatrix()[0][i]);
			}
			logProb = -logProb;
			iters = iters +1;
		
			error = Math.abs(logProb-oldLogProb);		
	
			if ( (iters<maxIters)) {
				oldLogProb = logProb;
				iteration = false;
			}else {
				iteration = true;
			}
		}
		//Set the new re-estimated Matrices for the HMM model lambda ( A,B and Pi).
		setReestimateA(A);
		setReestimateB(B);
		setReestimatePi(pi);
	}
	
	
	/**************************** 
	 * Getters and setters
	 *
	 ****************************/
	public Matrix getReestimatePi() {
		return reestimatePi;
	}
	
	public Matrix getObs() {
		return obs;
	}

	public void setObs(Matrix obs) {
		this.obs = obs;
	}

	public void setReestimatePi(Matrix reestimatePi) {
		this.reestimatePi = reestimatePi;
	}


	public Matrix getReestimateA() {
		return reestimateA;
	}


	public void setReestimateA(Matrix reestimateA) {
		this.reestimateA = reestimateA;
	}

	public boolean isIhaveBirds() {
		return IhaveBirds;
	}

	public void setIhaveBirds(boolean ihaveBirds) {
		IhaveBirds = ihaveBirds;
	}

	public Matrix getReestimateB() {
		return reestimateB;
	}


	public void setReestimateB(Matrix reestimateB) {
		this.reestimateB = reestimateB;
	}
}
