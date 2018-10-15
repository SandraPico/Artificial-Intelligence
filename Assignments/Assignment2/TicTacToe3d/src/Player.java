import java.util.*;

public class Player {

	int Player = 0;
	int depth = 1;
    public GameState nextMove;
    int initial_depth;
	
	/**
     * Performs a move 
     * @param gameState: the current state of the board
     * @param deadline: time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);
        
        //Our player, root node
        Player = gameState.getNextPlayer();
        
        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }
        initial_depth = depth;
        //We are going to start as a maximizer
        int v = minmaxalphaBeta(gameState,depth,-Integer.MAX_VALUE,Integer.MAX_VALUE,true);
        return nextMove;
    }

    public int evaluationFunction(boolean maximizer, GameState gameState) {
         
         int totalScore = 0;
         int num_X = 0;
         int num_O = 0;
         int num_Empty = 0;
         
         int num_X_1 = 0; //2nd diagonal
         int num_X_2 = 0; //3rd diagonal
         int num_X_3 = 0; //4th diagonal
         int num_O_1 = 0; //2nd diagonal
         int num_O_2 = 0; //3rd diagonal
         int num_O_3 = 0; //4th diagonal
         int num_Empty_1 = 0; //2nd diagonal
         int num_Empty_2 = 0; //3rd diagonal
         int num_Empty_3 = 0; //4th diagonal
         
         
         //Who is our team (X? O?)
         int opponent = Constants.CELL_O;   
         if (Player == Constants.CELL_O) {
              opponent = Constants.CELL_X;
         }
         
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
         }else {
        	 
        	 	//Diagonals
        	 	for (int layer = 0; layer < 4; layer++) {
        	 		if (gameState.at(layer,layer,layer) == Constants.CELL_X) {
        	            num_X++;
        	 		}else {
        	 			if(gameState.at(layer,layer,layer) == Constants.CELL_O) {
        	 				num_O++;
        	 			}else {
        	 				num_Empty++;
        	 			}
        	 		}
        	 		if (gameState.at(layer,layer,3-layer) == Constants.CELL_X) {
        	 			num_X_1++;
        	 		}else {
        	 			if(gameState.at(layer,layer,3-layer) == Constants.CELL_O) {
        	 				num_O_1++;
        	 			}else {
        	 				num_Empty_1++;
        	 			}
        	 		}
        	        if (gameState.at(layer,3-layer,layer) == Constants.CELL_X) {
        	        	 	num_X_2++;
        	         }else {
        	             if(gameState.at(layer,3-layer,layer) == Constants.CELL_O) {
        	                  num_O_2++;
        	             }else {
        	                  num_Empty_2++;
        	             }
        	         }
        	         if (gameState.at(3-layer,layer,layer) == Constants.CELL_X) {
        	              num_X_3++;
        	         }else {
        	              if(gameState.at(3-layer,layer,layer) == Constants.CELL_O) {
        	                   num_O_3++;
        	               }else {
        	                   num_Empty_3++;
        	               }
        	         }   
        	     }
        	         
        	     //Any winning combination? 
        	     if (num_X == 4 | num_X_1 == 4 | num_X_2 == 4 | num_X_3 == 4) {
        	        	 totalScore += 1000;
        	     }else if(num_O == 4 | num_O_1 == 4 | num_O_2 == 4 | num_O_3 == 4) {
        	        	 totalScore -= 1000;
        	     }else if(num_X == 3 && num_Empty == 1) {
        	        	 totalScore += 100;
        	     }else if(num_O == 3 && num_Empty == 1) {
        	     	 totalScore -= 100;
        	     } else if (num_X == 2 && num_Empty == 2) {
        	        	 totalScore += 10;
        	     }else if(num_O == 2 && num_Empty == 2) {
        	        	 totalScore -= 10;
        	     }else if(num_X == 1 && num_Empty == 3) {
        	         totalScore += 1;
        	     }else if (num_O == 1 && num_Empty == 3) {
        	         totalScore -= 1;
        	     }
        	     
        	     //2nd diagonal
        	     if(num_X_1 == 3 && num_Empty_1 == 1) {
    	        	 	totalScore += 100;
        	     }else if(num_O_1 == 3 && num_Empty_1 == 1) {
        	        	totalScore -= 100;
        	     } else if (num_X_1 == 2 && num_Empty_1 == 2) {
    	        	 	totalScore += 10;
        	     }else if(num_O_1 == 2 && num_Empty_1 == 2) {
        	        totalScore -= 10;
        	     }else if(num_X_1 == 1 && num_Empty_1 == 3) {
    	        	    totalScore += 1;
        	     }else if (num_O_1 == 1 && num_Empty_1 == 3) {
    	        		totalScore -= 1;
        	     }
        	        
        	     //3rd diagonal
        	     if(num_X_2 == 3 && num_Empty_2 == 1) {
        	    	 	totalScore += 100;
        	     }else if(num_O_2 == 3 && num_Empty_2 == 1) {
    	        		totalScore -= 100;
        	     } else if (num_X_2 == 2 && num_Empty_2 == 2) {
	        	 	totalScore += 10;
        	     }else if(num_O_2 == 2 && num_Empty_2 == 2) {
    	        		totalScore -= 10;
        	     }else if(num_X_2 == 1 && num_Empty_2 == 3) {
	        	 	totalScore += 1;
        	     }else if (num_O_2 == 1 && num_Empty_2 == 3) {
	        		totalScore -= 1;
        	     }
    	        
        	     //4th diagonal
        	     if(num_X_3 == 3 && num_Empty_3 == 1) {
	        	 	totalScore += 100;
        	     }else if(num_O_3 == 3 && num_Empty_3 == 1) {
    	        		totalScore -= 100;
        	     } else if (num_X_3 == 2 && num_Empty_3 == 2) {
	        	    totalScore += 10;
        	     }else if(num_O_3 == 2 && num_Empty_3 == 2) {
    	        		totalScore -= 10;
        	     }else if(num_X_3 == 1 && num_Empty_3 == 3) {
	        	    totalScore += 1;
        	     }else if (num_O_3 == 1 && num_Empty_3 == 3) {
        	    	 	totalScore -= 1;
        	     }
  
        	     //For all the layers
        	     for (int layer = 0; layer < 4; layer++) {
        	    	 	//Per each column
        	    	 	for(int column =0; column < 4; column++) {
        	    	 		num_X = 0;
        	    	 		num_O = 0;
        	    	 		num_Empty = 0;
        	    	 		//Per each column
        	    	 		for(int row = 0; row < 4; row++) {
        	    	 			if (gameState.at(row,column,layer) == Constants.CELL_X) {
        	    	 				num_X++;
        	    	 			}else {
        	    	 				if(gameState.at(row,column,layer) == Constants.CELL_O) {
        	    	 					num_O++;
        	    	 				}else {
        	    	 					num_Empty++;
        	    	 				}
        	    	 			}
        	    	 		}         
        	    	 		//Calculus
        	    	 		if (num_X == 4 ) {
        	    	 			totalScore += 1000;
        	    	 		}else if(num_O == 4 ) {
        	    	 			totalScore -= 1000;
        	    	 		}else if(num_X == 3 && num_Empty == 1) {
        	    	 			totalScore += 100;
        	    	 		}else if(num_O == 3 && num_Empty == 1) {
        	    	 			totalScore -= 100;
        	    	 		} else if (num_X == 2 && num_Empty == 2) {
        	    	 			totalScore  += 10;
        	    	 		}else if(num_O == 2 && num_Empty == 2) {
        	    	 			totalScore  -= 10;
        	    	 		}else if(num_X == 1 && num_Empty == 3) {
        	    	 			totalScore  += 1;
        	    	 		}else if (num_O == 1 && num_Empty == 3) {
        	    	 			totalScore  -= 1;
        	    	 		}   
        	    	 	}
        	     }
        
         
        	     //For all the layers -> Mirem per files.
        	     for (int layer = 0; layer < 4; layer++) {
        	    	 	//Per each row
        	    	 	for(int row =0; row < 4; row++) {
        	    	 		num_X = 0;
        	    	 		num_O = 0;
        	    	 		num_Empty = 0;
        	    	 		//Per each column
        	    	 		for(int column = 0; column < 4; column++) {
        	    	 			if (gameState.at(row,column,layer) == Constants.CELL_X) {
        	    	 				num_X++;
        	    	 			}else {
        	    	 				if(gameState.at(row,column,layer) == Constants.CELL_O) {
        	    	 					num_O++;
        	    	 				}else {
        	    	 					num_Empty++;
        	    	 				}
        	    	 			}
        	    	 		}
        	    	 		if (num_X == 4 ) {
        	    	 			totalScore += 1000;
        	    	 		}else if(num_O == 4 ) {
        	    	 			totalScore -= 1000;
        	    	 		}else if(num_X == 3 && num_Empty == 1) {
        	    	 			totalScore += 100;
        	    	 		}else if(num_O == 3 && num_Empty == 1) {
        	    	 			totalScore -= 100;
        	    	 		} else if (num_X == 2 && num_Empty == 2) {
        	    	 			totalScore  += 10;
        	    	 		}else if(num_O == 2 && num_Empty == 2) {
        	    	 			totalScore  -= 10;
        	    	 		}else if(num_X == 1 && num_Empty == 3) {
        	    	 			totalScore  += 1;
        	    	 		}else if (num_O == 1 && num_Empty == 3) {
        	    	 			totalScore  -= 1;
        	    	 		}   
        	    	 	}
        	     }


        	     //For all the rows
        	     for (int row = 0; row < 4; row++) {
        	    	 	//Per each row
        	    	 	for(int column =0; column < 4; column++) {
        	    	 		num_X = 0;
        	    	 		num_O = 0;
        	    	 		num_Empty = 0;
        	    	 		//Per each column
        	    	 		for(int layer = 0; layer < 4; layer++) {
        	    	 			if (gameState.at(row,column,layer) == Constants.CELL_X) {
        	    	 				num_X++;
        	    	 			}else {
        	    	 				if(gameState.at(row,column,layer) == Constants.CELL_O) {
        	    	 					num_O++;
        	    	 				}else {
        	    	 					num_Empty++;
        	    	 				}
        	    	 			}
        	    	 		}
        	    	 		if (num_X == 4 ) {
        	    	 			totalScore += 1000;
        	    	 		}else if(num_O == 4 ) {
        	    	 			totalScore -= 1000;
        	    	 		}else if(num_X == 3 && num_Empty == 1) {
        	    	 			totalScore += 100;
        	    	 		}else if(num_O == 3 && num_Empty == 1) {
        	    	 			totalScore -= 100;
        	    	 		} else if (num_X == 2 && num_Empty == 2) {
        	    	 			totalScore  += 10;
        	    	 		}else if(num_O == 2 && num_Empty == 2) {
        	    	 			totalScore  -= 10;
        	    	 		}else if(num_X == 1 && num_Empty == 3) {
        	    	 			totalScore  += 1;
        	    	 		}else if (num_O == 1 && num_Empty == 3) {
        	    	 			totalScore  -= 1;
        	    	 		}   
        	    	 	}
        	     }

         
        	     //Diagonals en els plans
        	     //X-Z
        	     for (int column = 0; column < 4; column++) {
        	    	 	num_X = 0;
        	    	 	num_O = 0;
        	    	 	num_Empty = 0;
        	    	 	for (int row = 0; row < 4; row++) {
        	    	 		int layer =  row;
        	    	 		if (gameState.at(row,column,layer) == Constants.CELL_X) {
        	    	 			num_X++;
        	    	 		}else {
        	    	 			if(gameState.at(row,column,layer) == Constants.CELL_O) {
        	    	 				num_O++;
        	    	 			}else {
        	    	 				num_Empty++;
        	    	 			}
        	    	 		}
        	    	 		if (num_X == 4 ) {
        	    	 			totalScore += 1000;
        	    	 		}else if(num_O == 4 ) {
        	    	 			totalScore -= 1000;
        	    	 		}else if(num_X == 3 && num_Empty == 1) {
        	    	 			totalScore += 100;
        	    	 		}else if(num_O == 3 && num_Empty == 1) {
        	    	 			totalScore -= 100;
        	    	 		} else if (num_X == 2 && num_Empty == 2) {
        	    	 			totalScore  += 10;
        	    	 		}else if(num_O == 2 && num_Empty == 2) {
        	    	 			totalScore  -= 10;
        	    	 		}else if(num_X == 1 && num_Empty == 3) {
        	    	 			totalScore  += 1;
        	    	 		}else if (num_O == 1 && num_Empty == 3) {
        	    	 			totalScore  -= 1;
        	    	 		}   
        	    	 	}   
        	     }
         
        	     for (int column = 0; column < 4; column++) {
        	    	 	num_X = 0;
        	    	 	num_O = 0;
        	    	 	num_Empty = 0;
        	    	 	for (int row = 0; row < 4; row++) {
        	    	 		int layer =  3-row;
        	    	 		if (gameState.at(row,column,layer) == Constants.CELL_X) {
        	    	 			num_X++;
        	    	 		}else {
        	    	 			if(gameState.at(row,column,layer) == Constants.CELL_O) {
        	    	 				num_O++;
        	    	 			}else {
        	    	 				num_Empty++;
        	    	 			}
        	    	 		}
        	    	 		//Calculus
        	    	 		if (num_X == 4 ) {
        	    	 			totalScore += 1000;
        	    	 		}else if(num_O == 4 ) {
        	    	 			totalScore -= 1000;
        	    	 		}else if(num_X == 3 && num_Empty == 1) {
        	    	 			totalScore += 100;
        	    	 		}else if(num_O == 3 && num_Empty == 1) {
        	    	 			totalScore -= 100;
        	    	 		} else if (num_X == 2 && num_Empty == 2) {
        	    	 			totalScore  += 10;
        	    	 		}else if(num_O == 2 && num_Empty == 2) {
        	    	 			totalScore  -= 10;
        	    	 		}else if(num_X == 1 && num_Empty == 3) {
        	    	 			totalScore  += 1;
        	    	 		}else if (num_O == 1 && num_Empty == 3) {
        	    	 			totalScore  -= 1;
        	    	 		}   
        	    	 	}   
        	     }
       
        	     //X-Y
        	     for (int layer = 0; layer < 4; layer++) {
        	    	 	num_X = 0;
        	    	 	num_O = 0;
        	    	 	num_Empty = 0;
        	    	 	for (int row = 0; row < 4; row++) {
        	    	 		int column =  row;
        	    	 		if (gameState.at(row,column,layer) == Constants.CELL_X) {
        	    	 			num_X++;
        	    	 		}else {
        	    	 			if(gameState.at(row,column,layer) == Constants.CELL_O) {
        	    	 				num_O++;
        	    	 			}else {
        	    	 				num_Empty++;
        	    	 			}
        	    	 		}
        	    	 		if (num_X == 4 ) {
        	    	 			totalScore += 1000;
        	    	 		}else if(num_O == 4 ) {
        	    	 			totalScore -= 1000;
        	    	 		}else if(num_X == 3 && num_Empty == 1) {
        	    	 			totalScore += 100;
        	    	 		}else if(num_O == 3 && num_Empty == 1) {
        	    	 			totalScore -= 100;
        	    	 		} else if (num_X == 2 && num_Empty == 2) {
        	    	 			totalScore  += 10;
        	    	 		}else if(num_O == 2 && num_Empty == 2) {
        	    	 			totalScore  -= 10;
        	    	 		}else if(num_X == 1 && num_Empty == 3) {
        	    	 			totalScore  += 1;
        	    	 		}else if (num_O == 1 && num_Empty == 3) {
        	    	 			totalScore  -= 1;
        	    	 		}   
        	    	 	}   
        	     }
         
        	     //The other diagonal 3-x
        	     for (int layer = 0; layer < 4; layer++) {
        	    	 	num_X = 0;
        	    	 	num_O = 0;
        	    	 	num_Empty = 0;
        	    	 	for (int row = 0; row < 4; row++) {
        	    	 		int column =  3- row;
        	    	 		if (gameState.at(row,column,layer) == Constants.CELL_X) {
        	    	 			num_X++;
        	    	 		}else {
        	    	 			if(gameState.at(row,column,layer) == Constants.CELL_O) {
        	    	 				num_O++;
        	    	 			}else {
        	    	 				num_Empty++;
        	    	 			}
        	    	 		}
        	    	 		if (num_X == 4 ) {
        	    	 			totalScore += 1000;
        	    	 		}else if(num_O == 4 ) {
        	    	 			totalScore -= 1000;
        	    	 		}else if(num_X == 3 && num_Empty == 1) {
        	    	 			totalScore += 100;
        	    	 		}else if(num_O == 3 && num_Empty == 1) {
        	    	 			totalScore -= 100;
        	    	 		} else if (num_X == 2 && num_Empty == 2) {
        	    	 			totalScore  += 10;
        	    	 		}else if(num_O == 2 && num_Empty == 2) {
        	    	 			totalScore  -= 10;
        	    	 		}else if(num_X == 1 && num_Empty == 3) {
        	    	 			totalScore  += 1;
        	    	 		}else if (num_O == 1 && num_Empty == 3) {
        	    	 			totalScore  -= 1;
        	    	 		}	   
        	    	 	}   
        	     }
                        
        	     //Y-Z
        	     for (int row = 0; row < 4; row++) {
        	    	 	num_X = 0;
        	    	 	num_O = 0;
        	    	 	num_Empty = 0;
        	    	 	for (int column = 0; column < 4; column++) {
        	    	 		int layer =  column;
        	    	 		if (gameState.at(row,column,layer) == Constants.CELL_X) {
        	    	 			num_X++;
        	    	 		}else {
        	    	 			if(gameState.at(row,column,layer) == Constants.CELL_O) {
        	    	 				num_O++;
        	    	 			}else {
        	    	 				num_Empty++;
        	    	 			}
        	    	 		}
        	    	 		if (num_X == 4 ) {
        	    	 			totalScore += 1000;
        	    	 		}else if(num_O == 4 ) {
        	    	 			totalScore -= 1000;
        	    	 		}else if(num_X == 3 && num_Empty == 1) {
        	    	 			totalScore += 100;
        	    	 		}else if(num_O == 3 && num_Empty == 1) {
        	    	 			totalScore -= 100;
        	    	 		} else if (num_X == 2 && num_Empty == 2) {
        	    	 			totalScore  += 10;
        	    	 		}else if(num_O == 2 && num_Empty == 2) {
        	    	 			totalScore  -= 10;
        	    	 		}else if(num_X == 1 && num_Empty == 3) {
        	    	 			totalScore  += 1;
        	    	 		}else if (num_O == 1 && num_Empty == 3) {
        	    	 			totalScore  -= 1;
        	    	 		}   
        	    	 	}   
        	     }
    
        	     //diagonals
        	     for (int row = 0; row < 4; row++) {
        	    	 	num_X = 0;
        	    	 	num_O = 0;
        	    	 	num_Empty = 0;
        	    	 	for (int column = 0; column < 4; column++) {
        	    	 		int layer =  column;
        	    	 		column = 3- layer;
        	    	 		if (gameState.at(row,column,layer) == Constants.CELL_X) {
        	    	 			num_X++;
        	    	 		}else {
        	    	 			if(gameState.at(row,column,layer) == Constants.CELL_O) {
        	    	 				num_O++;
        	    	 			}else {
        	    	 				num_Empty++;
        	    	 			}
        	    	 		}
        	    	 		//Count
        	    	 		if (num_X == 4 ) {
        	    	 			totalScore += 1000;
        	    	 		}else if(num_O == 4 ) {
        	    	 			totalScore -= 1000;
        	    	 		}else if(num_X == 3 && num_Empty == 1) {
        	    	 			totalScore += 100;
        	    	 		}else if(num_O == 3 && num_Empty == 1) {
        	    	 			totalScore -= 100;
        	    	 		} else if (num_X == 2 && num_Empty == 2) {
        	    	 			totalScore  += 10;
        	    	 		}else if(num_O == 2 && num_Empty == 2) {
        	    	 			totalScore  -= 10;
        	    	 		}else if(num_X == 1 && num_Empty == 3) {
        	    	 			totalScore  += 1;
        	    	 		}else if (num_O == 1 && num_Empty == 3) {
        	    	 			totalScore  -= 1;
        	    	 		}   
        	    	 	}
        	     }
         }
     return totalScore;
}

      
    public int minmaxalphaBeta( GameState gameState, int depth, int alfa, int beta, boolean maximizer) {
        
          Vector<GameState> nextStates = new Vector<GameState>();
          gameState.findPossibleMoves(nextStates);           
          int value = 0;
          int result  = 0;
          
          if (nextStates.size() == 0 || depth == 0) {
            int eval = evaluationFunction (maximizer,gameState);
            return eval;
          }
          
          //Player = MAX
          if (maximizer == true){
                value = -Integer.MAX_VALUE; //-infinity
                for (int x = 0; x < nextStates.size(); x++){
                		result = minmaxalphaBeta(nextStates.elementAt(x), depth-1, alfa, beta,false);
                		if (result > value && depth == initial_depth) {
                			nextMove = nextStates.elementAt(x);
                		}
                		value = Math.max(value, result);
                		alfa = Math.max(alfa, value);
                		if (beta <= alfa) {
                			break;
                		}
                 }
                	return value;             
           } else {        	   
                value = Integer.MAX_VALUE; //infinity
                for (int x = 0; x < nextStates.size(); x++){
                	     result = minmaxalphaBeta(nextStates.elementAt(x),depth-1,alfa,beta,true);
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
