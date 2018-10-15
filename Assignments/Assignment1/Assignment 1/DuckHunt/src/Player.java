/** ****************************************************************
 *
 * File: Player.java
 * Authors: Anna Hedström and Sandra Picó
 * Date: 17 September 2017
 * 
 ******************************************************************/
import java.util.ArrayList;


/** *****************************************************************
 * Main java file to program for the Duck Hunt problem 
 * 
 ****************************************************************** */
class Player {

	//Here we tried two options:
	//1. Store only one HMM for specie -> Time error .
	//2. Store an HMM for each bird.
	@SuppressWarnings("unchecked")
	private ArrayList<HMM>[] BirdsProvedInformation1  = new ArrayList[6] ;
	//We also decide to store an HMM for each bird that appears in the shooting round. 
	private ArrayList<HMM> BirdsToShoot = new ArrayList<HMM>();
	
	//Variables
	int [] birdsInformation = new int[6];
	boolean fullInformation = false;
	boolean BlackAppears = false;
	int ActualBirdType = 0;
	int[] myBirds;
	int correctGuesses = 0;
	int failedGuesses = 0;
	int totalGuess = 0;
	int birdsHitted = 0;
	int shoots = 0;
	int CurrentRound = 100;
	boolean FirstTime = true;
	
	public Player() {
		
		for(int i =0; i < BirdsProvedInformation1.length; i++) {
			BirdsProvedInformation1[i] = new ArrayList<HMM>();
		}
		//Full information initial conditions.
		for ( int j = 0; j < birdsInformation.length; j++) {
			//We don't have information of any bird...
			birdsInformation[j] = 0;
		}
		correctGuesses = 0;
		failedGuesses = 0;
		totalGuess = 0;
		shoots = 0;
		birdsHitted = 0;
    }

    /**
     * Shoot!
     *
     * This is the function where you start your work.
     *
     * You will receive a variable pState, which contains information about all
     * birds, both dead and alive. Each bird contains all past moves.
     *
     * The state also contains the scores for all players and the number of
     * time steps elapsed since the last time this function was called.
     *
     * @param pState the GameState object with observations etc
     * @param pDue time before which we must have returned
     * @return the prediction of a bird we want to shoot at, or cDontShoot to pass
     */
   
	public Action shoot(GameState pState, Deadline pDue) {
		
		//Variables
    		Action shoot = new Action(-1,-1);
    		Bird bird;
    		Matrix observations;
    		int lenght = 0;
    		int [] trainedBirds = new int[1];
    		//We need to control that we are not in the first round...
    		//New round
    		
    		if (CurrentRound != pState.getRound()) {
    			
    			trainedBirds = new int[pState.getNumBirds()];
    			for (int i = 0; i < trainedBirds.length; i++) {
    				trainedBirds[i] = 0;
    			}
    			CurrentRound = pState.getRound();
    			//Remove the last models..
    			if (!FirstTime) {
    				for (int i = 0; i < BirdsToShoot.size(); i++) {
    					BirdsToShoot.remove(i);
    				}
    			}
    			//Add new ones...
    			for (int k = 0; k < pState.getNumBirds(); k++) {
    				BirdsToShoot.add(new HMM()); //Random HMM..	
    			}
    			//Reset time step.
    			System.err.println("Round number: " +pState.getRound());
    			FirstTime = false;
    		}
    		
    		//90 observations...
    		//For each bird
    		for (int osc = 0; osc < pState.getNumBirds(); osc++ ) {
    			if (pState.getBird(osc).getSeqLength() > 30) {
    				bird = pState.getBird(osc);
    				int BirdType = WhichBirdIsIt(bird);
    				if (BirdType != Constants.SPECIES_BLACK_STORK) {
    					//We need to be sure that our bird is alive...
    					if (bird.isAlive()) {
    						Train(bird,osc);
    						lenght = 0;
    						//Add the right observations...
    						//Was, in this observation, the bird alive?
    						for (int k = 0; k < bird.getSeqLength();k++) {
    							if (bird.wasAlive(k)) {
    								lenght++;
    							}
    						}
    						observations = new Matrix(1,lenght);
    						for(int k = 0; k < lenght; k++) {
    							observations.getMatrix()[0][k] = bird.getObservation(k);
    						}
    						//The bird model is trained now.. with Baum Welch Algorithm...
    						BirdsToShoot.get(osc).AddObservation(observations);
    						//Just give the most likely observation
    						int movement = BirdsToShoot.get(osc).MostLikelyNextObservation();
    						if (movement != -1) {
    							if (bird.isAlive()) {
    								shoots++;
    								//Time to shoot!
    								shoot = new Action(osc,movement);
    								return shoot;
    							}else {
    								//If somebody kill the bird before us, just not shoot.
    								shoot = new Action(-1,-1);
    							}
    						}else {
    							//We are not sure enough with the next movement.
    							shoot = new Action(-1,-1);
    						}
    					}else {
    						//If the bird is not alive, we can not shoot it.
    						shoot = new Action(-1,-1);
    					}
    				}else {
    					//We can not shoot the black stork.
    					shoot = new Action(-1,-1);
    				}
    			}else {
    				//Not enough observations to start shooting.
    				shoot = new Action(-1,-1);
    			}
    	}
    	return shoot;
}
    /** *******************************************************************
     * This method is used to train the birds that you want to shoot.
     * The trained is made with bird observations and Baum Welch algorithm.
     * 
     *********************************************************************/
    public void Train(Bird bird, int index) {
    		//Variables
    		int longitud = 0;
    		Matrix observations;	
    		longitud = 0;
    		
    		//We need to be sure that the bird is alive in each observation.
    		for (int k = 0; k < bird.getSeqLength();k++) {
			if (bird.wasAlive(k)) {
				longitud++;
			}
		}
		observations = new Matrix(1,longitud);	
		for(int k = 0; k < longitud; k++) {
			observations.getMatrix()[0][k] = bird.getObservation(k);
		}
		  
		//We set the new observations from the new bird..
    		BirdsToShoot.get(index).AddObservation(observations);
    		BirdsToShoot.get(index).BaumWelch();
   		
    }

 
    /**
     * Guess the species!
     * This function will be called at the end of each round, to give you
     * a chance to identify the species of the birds for extra points.
     *
     * Fill the vector with guesses for the all birds.
     * Use SPECIES_UNKNOWN to avoid guessing.
     *
     * @param pState the GameState object with observations etc
     * @param pDue time before which we must have returned
     * @return a vector with guesses for all the birds
     */
    public int[] guess(GameState pState, Deadline pDue) {
        
    		//Variables
    		Bird b ;
    		int Birdtype = 0;
    		int round = pState.getRound();
    		
        int[] Guess = new int[pState.getNumBirds()];
        	myBirds = new int[pState.getNumBirds()];
        
        	for (int i = 0; i < pState.getNumBirds(); ++i) {
            Guess[i] = Constants.SPECIES_UNKNOWN;
        }
        
        //The first round must be a random guess.
        if( round == 0 ) {
        		for(int i = 0; i < pState.getNumBirds(); i++) {
        			Guess[i] = Constants.SPECIES_PIGEON; //We will be sending always a Pigeon type.
        		}
        } else {
        		//Is not the first round, just start guessing!
        		for ( int k = 0; k < pState.getNumBirds(); k++) {
        			b = pState.getBird(k);
        			//Main function used to guess which specie is it.
        			//WhichBirdIsIt is using Forward algorithm and the whole information that we have thanks to reveal function. 
        			Birdtype = WhichBirdIsIt(b);
        			Guess[k] = Birdtype;
        		}
        }
        for ( int i = 0; i < Guess.length; i++) {
        		myBirds[i] = Guess[i];
        }
		return Guess;
    }

    /**
     * If you hit the bird you were trying to shoot, you will be notified
     * through this function.
     *
     * @param pState the GameState object with observations etc
     * @param pBird the bird you hit
     * @param pDue time before which we must have returned
     */
    public void hit(GameState pState, int pBird, Deadline pDue) {
        System.err.println("HIT BIRD!!!");
        birdsHitted++;
    }

    
    /**
     * If you made any guesses, you will find out the true species of those
     * birds through this function.
     *
     * @param pState the GameState object with observations etc
     * @param pSpecies the vector with species
     * @param pDue time before which we must have returned
     */
    public void reveal(GameState pState, int[] pSpecies, Deadline pDue) {
    		
    		//In that case, the species will be revealed .
    		//Here, we know for sure that the information is right.
   		for ( int j = 0; j < pSpecies.length; j++) {
   			int specie = pSpecies[j];
   			System.err.print("  "+pSpecies[j]);
   			//We check if the black appears...
   			if (specie == Constants.SPECIES_BLACK_STORK) {
   				BlackAppears = true;
   			}
   			birdsInformation[specie] = 1;
   		}
   		//Here, we had an idea based on...
   		//If I don't have all the birds species, what should I do?
   		int counter = 0;
   		if ( fullInformation == false) {
   			for ( int k = 0; k < birdsInformation.length; k++) {
   				if ( birdsInformation[k] == 1) {
   					counter = counter + 1;
   				}
   			}
   			if ( counter == 6) {
   				fullInformation = true;
   			}
   		}
   		
   		
   		//Just to print our guess rate...
   		for (int t = 0; t< pSpecies.length; t++) {
   			if (myBirds[t] != pSpecies[t]) {
   				failedGuesses++;
   			}else {
   				correctGuesses++;
   			}
   			totalGuess ++;
   		}
   
   		//Save all the reveal information.
    		for ( int i = 0; i < pSpecies.length; i++) {
    			int specie = pSpecies[i]; //Which specie
    			Bird b = pState.getBird(i); //Bird information (	+observations..	+wasAlive(observation) +isAlive...)
    			CreateNewModel(specie, b);
    		}
    		
    		System.err.println("GUESSES");
    		double rate = (double)(correctGuesses/(double)totalGuess);
    		System.err.println("Right " + correctGuesses + " Failed " + failedGuesses + "Rate:" +rate );
    		System.err.println("SHOOOTING");
    		double shootrate = (double)(birdsHitted/(double)shoots);
    		System.err.println("All shoots: " + shoots + " Killed " + birdsHitted +"Rate shoot"+shootrate);
    		
    }
    
    /** **************************************************************
     * Here, we must need to:
     *  1. Create a new HMM.
     *  2. Estimate it taking into account, the bird observation.
     *  3. Store the information in our List.
     ****************************************************************/
    public void CreateNewModel(int specie, Bird b) {
    		
    		HMM h = new HMM(); //Random A,B and Pi -> We create it in the HMM constructor.
    		Matrix observations;
    		int columns = 0;
    		
    		//Is the bird alive? //We need to count how many right observations we have.
    		columns = 0;
    		for( int i = 0; i < b.getSeqLength(); i++) {
    			if (b.wasAlive(i)) {
    				columns ++;
    			}
    		}	
    		//Once we know how many right observations we have, then , create the matrix.
    		observations = new Matrix(1,columns);
    		
    		//Copy the observations from the bird information..
    		for( int i = 0; i < columns; i++) {
    			observations.getMatrix()[0][i] =  b.getObservation(i);
    		}
    		
    		
    		//Add the HMM created before...
    		BirdsProvedInformation1[specie].add(h);
    		//Set the right observations from the bird.. We needed it, in order to create the Baum Welch algorithm
    		BirdsProvedInformation1[specie].get(BirdsProvedInformation1[specie].size()-1).setObs(observations);
    		//Execute the baum welch algorithm.
    		BirdsProvedInformation1[specie].get(BirdsProvedInformation1[specie].size()-1).BaumWelch();
    		
    		
    	 }
    	
    /** *********************************************************************
     * This method was used to know which HMM of our right information stored
     * was more similar with the new bird that we want to shoot.
     * It was one idea that we have in the beginning of shooting implementation.
     * 
     ***********************************************************************/
	public int[] WhichBirdandHMMisIt(Bird b) {
		
		int[] information = new int[2];
   		double max = -Double.MAX_VALUE;
		Matrix observations;
		
		if (b.isAlive()) {	
			observations = new Matrix(1,b.getSeqLength());
			for( int i = 0; i < b.getSeqLength(); i++) {
				observations.getMatrix()[0][i] =  b.getObservation(i);
			}
			
			information[0] = Constants.SPECIES_UNKNOWN;
			information[1] = 0;
			for ( int i = 0; i < BirdsProvedInformation1.length; i++) {
				int howManyHMM = BirdsProvedInformation1[i].size();
				//System.err.println("The bird INFORMATION size is:" + howManyHMM);
				if (howManyHMM > 0) {
					for ( int k = 0; k < howManyHMM; k++) {
						//We need to set the new observations, and then , just calculate the forward algorithm..
						BirdsProvedInformation1[i].get(k).setObs(observations);
						double pAlgorithm = BirdsProvedInformation1[i].get(k).ForwardAlgorithm();
						if (pAlgorithm > max) {
							information[1] = k;
							max = pAlgorithm;
							information[0] = i;
						}
					}
				}
			}
		}
		return information;
	}
	
    /** ***********************************************************************************************
     * This method is used for the guessing and shooting part.
     * In guessing, is the method that return which specie is it.
     * In the shooting part, is used to identify if it's the black stork, because we can not shoot it.
     * 
     **************************************************************************************************/
    public int WhichBirdIsIt(Bird b) {
    	
    		//Variables
    		double max= -Double.MAX_VALUE;
    		int BirdType = 0;
    		Matrix observations;
    		double [] probability = new double[6];
    		int columns = 0;
    		
    		for (int k = 0; k < probability.length; k++) {
    			probability[k]= -Double.MAX_VALUE;
    		}
   
    		//Is the bird alive? 
    		columns = 0;
    		for( int i = 0; i < b.getSeqLength(); i++) {
    			if (b.wasAlive(i)) {
    				columns ++;
    			}
    		}	
    		//Right observations...
    		observations = new Matrix(1,columns);
    		for( int i = 0; i < columns; i++) {
    			observations.getMatrix()[0][i] =  b.getObservation(i);
    		}
    		
    		double pAlgorithm;
    		for ( int i = 0; i < BirdsProvedInformation1.length; i++) {
    			int howManyHMM = BirdsProvedInformation1[i].size();
    			//We tried with average and without it.
    			//Finally we decide to use only the maximum --> Better results.
    			//double amount = 0;
    			//double average = 0;
    			double maxProb = 0;
    			for ( int k = 0; k < howManyHMM; k++) {	
    				BirdsProvedInformation1[i].get(k).setObs(observations);
    				pAlgorithm = BirdsProvedInformation1[i].get(k).ForwardAlgorithm();
    				if (maxProb < pAlgorithm) {
    					maxProb = pAlgorithm;
    				}
    				//amount = amount + pAlgorithm;
    			}
    			//if( howManyHMM > 0) {
    				//average = amount/howManyHMM;
    			probability[i] = maxProb; 
    			//}
    		}
    		for (int k = 0; k < probability.length; k++) {
    			double value = probability[k];
    			if (value > max) {
    				max = value;
    				BirdType = k;
    				ActualBirdType = k;
    			}
    		}
    		return BirdType;
    }
    
    //Action of not shooting.
    public static final Action cDontShoot = new Action(-1, -1);
}
