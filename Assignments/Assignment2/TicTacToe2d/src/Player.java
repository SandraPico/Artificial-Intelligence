import java.util.*;

public class Player {

    /**
     * Performs a move
     * @param gameState :  the current state of the board
     * @param deadline : time before which we must have returned
     * @return the next state the board is in after our move
     */

	int Player = 0;
	int depth = 4;
    public GameState nextMove;
    int initial_depth;
	
    public GameState play(final GameState gameState, final Deadline deadline) {
    	
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);
        
        //Our main player -> Root node
        Player = gameState.getNextPlayer();
        	     
        //Just to check if there are any possible moves...If not, just create another game state situation.
        if (nextStates.size() == 0) {
            return new GameState(gameState, new Move());
        }
        
        initial_depth =  depth;
        //We send the complete game state to minmax algorithm.
        //We are going to start with the maximizer always.
        int v  = minmaxalphaBeta(gameState,depth,-Integer.MAX_VALUE,Integer.MAX_VALUE,true);        
        return nextMove;
    }
    
    
    public int evaluationFunction(boolean maximizer,GameState gameState) {
    		 
    		 int totalScore = 0;
    		 
    		 @SuppressWarnings("unchecked")
	     ArrayList<Integer>[] row = new ArrayList[4] ;
    		 @SuppressWarnings("unchecked")
		 ArrayList<Integer>[] column = new ArrayList[4] ;
    		 @SuppressWarnings("unchecked")
		 ArrayList<Integer>[] diagonal = new ArrayList[2] ;
    		 
    		 
    		 //Checking who is the X player and who is the O player.
    		 int opponent = Constants.CELL_O;   
         if (Player == Constants.CELL_O) {
              opponent = Constants.CELL_X;
         }
         
    		 //Build the necessary arrays
    		 for (int i = 0; i < 4; i++) {
    			 row[i] = new ArrayList<Integer>();
    			 column[i] = new ArrayList<Integer>();
    		 }
    		 for (int i = 0; i < 2; i++) {
    			 diagonal[i] = new ArrayList<Integer>();
    		 }
 		 	 
    		//Create these arrays (for columns, rows and diagonals)
    	    	for ( int k = 0; k < 4; k ++) {
    	    		 for ( int y = 0; y < 4; y++) {
    	    		 	row[k].add(gameState.at(k,y));
    	    		 	column[y].add(gameState.at(k,y));
    	    		 }
    	    	}
    	    	
    	    	//Create the diagonals
    	    	diagonal[0].add(row[0].get(0));
    	    	diagonal[0].add(row[1].get(1));
    	    diagonal[0].add(row[2].get(2));
    	    	diagonal[0].add(row[3].get(3));
    	    diagonal[1].add(row[0].get(3));
    	    	diagonal[1].add(row[1].get(2));
    	    	diagonal[1].add(row[2].get(1));
    	    	diagonal[1].add(row[3].get(0));
    	    	
    	    	//If its the end of the game, just check who wins.
    	    	if (gameState.isEOG()) { 
    	    		 if (Player == Constants.CELL_O && gameState.isOWin()) {
    	               totalScore = Integer.MAX_VALUE;
    	         }else if (Player == Constants.CELL_X && gameState.isXWin()) {
    	               totalScore  = Integer.MAX_VALUE; 
    	         }else if (Player == Constants.CELL_X && gameState.isOWin()) {
    	               totalScore  = Integer.MIN_VALUE;
    	         }else if (Player == Constants.CELL_O && gameState.isXWin()) {
    	               totalScore  = Integer.MIN_VALUE;
    	         }
    	    //If not, apply the heuristics function
    	    	}else {
    	     	
    	    		//Check the diagonals
    	    		for (int i = 0; i <2;i++) {
    	    			
    	    			int howMany_O_Diagonal = Collections.frequency(diagonal[i], Constants.CELL_O);
    	    			int howMany_X_Diagonal = Collections.frequency(diagonal[i], Constants.CELL_X);
    	    		
    	    			if (howMany_X_Diagonal == 4) {
    	    				totalScore = totalScore + 1000;
    	    			}else {
    	    				if (howMany_O_Diagonal == 4) {
    	    					totalScore = totalScore - 1000;
    	    				}
    	    			}
    	    		
    	    			if (howMany_X_Diagonal == 3) {
    	    				//We need to be sure that O is in the last or in the first element
    	    				if((diagonal[i].get(0) == Constants.CELL_O ) | (diagonal[i].get(3) == Constants.CELL_O)) {
    	    					totalScore = totalScore + 100;
    	    				}else {
    	    					totalScore = totalScore +10;
    	    				}
    	    			}else {
    	    				if (howMany_O_Diagonal == 3) {
    	    					if((diagonal[i].get(0) == Constants.CELL_X ) | (diagonal[i].get(3) == Constants.CELL_X)) {
    	    						totalScore = totalScore - 100;
        	    				}else {
        	    					totalScore = totalScore -10;
        	    				}
    	    				}
    	    			}

    	    			if (howMany_O_Diagonal == 2) {
    	    				//We need to be sure that O is in the last or in the first element
    	    				if((diagonal[i].get(1) == Constants.CELL_O ) | (diagonal[i].get(2) == Constants.CELL_O)) {
    	    					totalScore = totalScore - 10;
    	    				}   			
    	    			}else {
    	    				if (howMany_X_Diagonal == 2) {
    	    					if((diagonal[i].get(1) == Constants.CELL_X ) | (diagonal[i].get(2) == Constants.CELL_X)) {
        	    					totalScore = totalScore +10;
        	    				}
    	    				}
    	    			}
    	    		}
    	    	
    	    		//Check the 4 case (XXXX, OOOO)
    	    		for(int i = 0; i < 4; i ++) { 
    	    			
    	    			int howMany_X_Row = Collections.frequency(row[i], Constants.CELL_X);
    	    			int howMany_X_Column = Collections.frequency(column[i], Constants.CELL_X);
    	    			int howMany_O_Row = Collections.frequency(row[i], Constants.CELL_O);
    	    			int howMany_O_Column = Collections.frequency(column[i], Constants.CELL_O);
    	    		
    	    			//4 X or 4 O in a row , diagonal or in a column.
    	    			if (howMany_X_Row == 4) {
    	    				totalScore = totalScore + 1000;
    	    			}else {
    	    				if (howMany_O_Row == 4) {
    	    					totalScore = totalScore - 1000;
    	    				}
    	    			}
    	    		
    	    			if (howMany_X_Column == 4) {
    	    				totalScore = totalScore + 1000;
    	    			}else {
    	    				if (howMany_O_Column == 4) {
    	    					totalScore = totalScore - 1000;
    	    				}
    	    			}	
    	    		  		
    	    			//Check the XXX, OOO
    	    			if (howMany_X_Row == 3) {
    	    				//We need to be sure that O is in the last or in the first element
    	    				if((row[i].get(0) == Constants.CELL_O ) | (row[i].get(3) == Constants.CELL_O)) {
    	    					totalScore = totalScore + 100;
    	    				}else {
    	    					totalScore = totalScore + 10;
    	    				}
    	    			}else {
    	    				if (howMany_O_Row == 3) {
    	    					if((row[i].get(0) == Constants.CELL_X ) | (row[i].get(3) == Constants.CELL_X)) {
    	    						totalScore = totalScore - 100;
    	    					}else {
    	    						totalScore = totalScore -10;
    	    					}
    	    				}
    	    			}

    	    			if (howMany_X_Column == 3) {
    	    				//We need to be sure that O is in the last or in the first element
    	    				if((column[i].get(0) == Constants.CELL_O ) | (column[i].get(3) == Constants.CELL_O)) {
    	    					totalScore = totalScore + 100;
    	    				}else {
    	    					totalScore = totalScore + 10;
    	    				}
    	    			}else {
    	    				if (howMany_O_Column == 3) {
    	    					if((column[i].get(0) == Constants.CELL_X ) | (column[i].get(3) == Constants.CELL_X)) {
    	    						totalScore = totalScore - 100;
    	    					}else {
    	    						totalScore = totalScore -10;
    	    					}
    	    				}
    	    			}
    	       	    	
    	    			if (howMany_O_Column == 2) {
    	    				//We need to be sure that O is in the last or in the first element
    	    				if((column[i].get(1) == Constants.CELL_O ) | (column[i].get(2) == Constants.CELL_O)) {
    	    					totalScore = totalScore - 10;
    	    				}   			
    	    			}else {
    	    				if (howMany_X_Column == 2) {
    	    					if((column[i].get(1) == Constants.CELL_X ) | (column[i].get(2) == Constants.CELL_X)) {
    	    						totalScore = totalScore +10;
    	    					}
    	    				}
    	    			}
    	      	
    	    			if (howMany_O_Row == 2) {
    	    				//We need to be sure that O is in the last or in the first element
    	    				if((row[i].get(1) == Constants.CELL_O ) | (row[i].get(2) == Constants.CELL_O)) {
    	    					totalScore = totalScore - 10;
    	    				}   			
    	    			}else {
    	    				if (howMany_X_Row == 2) {
    	    					if((row[i].get(1) == Constants.CELL_X ) | (row[i].get(2) == Constants.CELL_X)) {
    	    						totalScore = totalScore +10;
    	    					}
    	    				}
    	    			}
    	     	
    	    		}

    	  	}
    	    	return totalScore;
    }
    
    
    
    public int minmaxalphaBeta( GameState gameState, int depth, int alfa, int beta, boolean maximizer) {
    	
    		  Vector<GameState> nextStates = new Vector<GameState>();
          gameState.findPossibleMoves(nextStates); 	
    
    	      int  value = 0;
    	      
    	      if (nextStates.size() == 0 || depth == 0 || gameState.isEOG()) {
    	        value = evaluationFunction (maximizer,gameState);
    	        return value;
    	      }
    	           
    	      //Player = MAX
    	      //We are going to start as a maximizer.
    	      if (maximizer == true){
    	          value = -Integer.MAX_VALUE; //-infinity
    	          for (int x = 0; x < nextStates.size(); x++){
    	            int result = minmaxalphaBeta(nextStates.elementAt(x), depth-1, alfa, beta,false);
    	            if (result> value && depth == initial_depth) {
    	            		nextMove = nextStates.elementAt(x);
    	            	}
    	            value = Math.max(value, result);
    	            alfa = Math.max(alfa, value);;
    	            if (beta <= alfa) {
    	                break;
    	            }
    	          }
    	            return value;
    	      }else{
    	        	  //Minimizer 	  
    	          value = Integer.MAX_VALUE; //infinity
    	          for (int x = 0; x < nextStates.size(); x++){
    	             int result = minmaxalphaBeta(nextStates.elementAt(x),depth-1,alfa,beta,true);
    	             value = Math.min(value, result);
    	            	 beta = Math.min(beta, value);
    	            	 if (beta <= alfa) {
    	            		 break;
    	            	 }
    	          }
    	          return value;
    	      }
    	}
}
