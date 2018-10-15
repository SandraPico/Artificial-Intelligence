import java.util.*;
import java.util.HashMap;
import java.util.Map;


public class Player {
   
    public static Long[][] zobrist;
    int Player = 0;
    public int initial_depth;
    public int depth = 11;
    public GameState nextMove ;
    
    public int [] object = new int[2];
    public Deadline d;
    
    HashMap<Long,int[]> hashmap = new HashMap<Long,int[]>();
    
    
    /**
     * Performs a move
     * @param pState the current state of the board
     * @param pDue : time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState pState, final Deadline pDue) {
        
    	  	Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);
    	
        //Deadline
        d = pDue;
        
        //Object for HashMap
        object = new int[2];
        object[0] = 0;
        object[1] = 0;
        hashmap = new HashMap<>();
        
        //Our player
        Player = pState.getNextPlayer();
                
        if (lNextStates.size() == 0) {
            return new GameState(pState, new Move());
        }
        
        initial_depth =  depth;  
        int v = minmaxalphaBeta(pState,depth,Integer.MIN_VALUE,Integer.MAX_VALUE,true);   
        return nextMove;
    }
    
   public Long Zobrist(GameState gamestate) {
        
        int p = gamestate.getNextPlayer();
        Long hash = 0l;
        int piece =0;
        if(zobrist == null) {   
            zobrist = new Long[32][8];
            Random r = new Random(32L);
            for (int i = 0; i < 32; i++) {
                for (int j = 0; j < 8; j++) {
                    zobrist[i][j] = Math.abs(r.nextLong());
                }
            }
        }
        hash = 0l;
        for (int i = 0; i < 32; i ++) {
            if (gamestate.get(i) != Constants.CELL_EMPTY) {
            		piece = gamestate.get(i);
                hash ^= zobrist[i][piece];
            }
        }
        hash ^= zobrist[0][0+p];
        return hash;
    }
    
   
    //Finally not used...
    public int WhichType(int piece, int p) {

    		int type = 0;  
        if (Player == p) {
            if (piece == Constants.CELL_RED) {
                if ( piece == Constants.CELL_KING) {
                    type = 0;
                }else{
                    type = 1;   
                }
            }else if(piece == Constants.CELL_WHITE) {
                if (piece == Constants.CELL_KING) {
                    type = 2;
                }else{
                    type = 3;   
                }
            }
        }else {
            if (piece == Constants.CELL_RED) {
                if ( piece == Constants.CELL_KING) {
                    type = 1;
                }else{
                    type = 2;   
                }
            }else if(piece == Constants.CELL_WHITE) {
                if (piece == Constants.CELL_KING) {
                    type = 3;
                }else{
                    type = 4;   
                }
            }
            
        }
        return type;
    }

    
    
    public int evaluationFunction(boolean max, GameState gameState) {
            
            int totalScoreFinal = 0;   
            int Red = 0;
            int White = 0;
            int Red_King = 0;
            int White_King = 0;
            
            
            int opponent = Constants.CELL_RED;
            int[] value = new int[2];
            if (Player == Constants.CELL_RED) {
                opponent = Constants.CELL_WHITE;
            }
        
            if (gameState.isEOG()) {
                if (Player == Constants.CELL_RED && gameState.isRedWin()) {
                    totalScoreFinal = Integer.MAX_VALUE;
                }else if (Player == Constants.CELL_RED && gameState.isWhiteWin()) {
                    totalScoreFinal  = Integer.MIN_VALUE; 
                }else if (Player == Constants.CELL_WHITE && gameState.isWhiteWin()) {
                    totalScoreFinal  = Integer.MAX_VALUE;
                }else if (Player == Constants.CELL_WHITE && gameState.isRedWin()) {
                    totalScoreFinal  = Integer.MIN_VALUE;
                }   
            }else {
                //Look for the whole board
                for (int i = 0; i < 32; i++) {      
                    int piece = gameState.get(i);
                    if (i < 16) { 
                        if (piece == Constants.CELL_RED ) {
                            if (gameState.cellToCol(piece) == 0 | gameState.cellToCol(piece) == 7 | gameState.cellToRow(piece) == 0) {
                                if (piece == Constants.CELL_KING) {
                                    Red_King = Red_King +5;
                                }else {
                                    Red = Red + 20;
                                }
                            }else{
                                if ((gameState.cellToRow(piece) == 2 | gameState.cellToRow(piece) == 1) && (gameState.cellToCol(piece) > 1 && gameState.cellToCol(piece) > 6)) {
                                    if (piece == Constants.CELL_KING) {
                                        Red_King = Red_King + 20;
                                    }else {
                                        Red = Red + 1;
                                    }
                                }else {
                                    if (piece == Constants.CELL_KING) {
                                        Red_King = Red_King + 10;
                                    }else {
                                        Red = Red + 10;
                                    }
                                }
                            }   
                        }else if(piece == Constants.CELL_WHITE ) {
                            if (gameState.cellToCol(piece) == 0 | gameState.cellToCol(piece) == 7 | gameState.cellToRow(piece) == 0) {
                                if (piece == Constants.CELL_KING) {
                                    White_King = White_King + 20;
                                }else {
                                    White = White + 30;
                                }
                            }else{  
                                if ((gameState.cellToRow(piece) == 2 |gameState.cellToRow(piece) == 2) && (gameState.cellToCol(piece) > 1 && gameState.cellToCol(piece) > 6)) {
                                    if (piece == Constants.CELL_KING) {
                                        White_King = White_King + 30;
                                    }else {
                                        White = White + 3;
                                    }
                                }else {
                                    if (piece == Constants.CELL_KING) {
                                        White_King = White_King + 60;
                                    }else {
                                        White = White + 15;
                                    }
                                }
                            }       
                        }
                    }else {
                        if (piece == Constants.CELL_RED) {
                            if (gameState.cellToCol(piece) == 0 | gameState.cellToCol(piece) == 7 | gameState.cellToRow(piece) == 7) {
                                if (piece == Constants.CELL_KING) {
                                    Red_King = Red_King +20;
                                }else {
                                    Red = Red+ 30;
                                }
                            }else{
                                if ((gameState.cellToRow(piece) == 4 |gameState.cellToRow(piece) == 5) && (gameState.cellToCol(piece) > 1 && gameState.cellToCol(piece) > 6)) {
                                    if (piece == Constants.CELL_KING) {
                                        Red_King = Red_King +30;
                                    }else {
                                        Red = Red+ 3;
                                    }
                                }else {     
                                    if (piece == Constants.CELL_KING) {
                                        Red_King = Red_King +60;
                                    }else {
                                        Red = Red+ 15;
                                    }
                                }
                            }   
                        }else if(piece == Constants.CELL_WHITE) {
                            if (gameState.cellToCol(piece) == 0 | gameState.cellToCol(piece) == 7 | gameState.cellToRow(piece) == 7) {
                                if (piece == Constants.CELL_KING) {
                                    White_King = White_King + 5;
                                }else {
                                    White = White + 20;
                                }
                            }else{
                                if ((gameState.cellToRow(piece) == 4 |gameState.cellToRow(piece) == 5) && (gameState.cellToCol(piece) > 1 && gameState.cellToCol(piece) > 6)) {
                                    if (piece == Constants.CELL_KING) {
                                        White_King = White_King + 20;
                                    }else {
                                        White = White + 1;
                                    }
                                }else {
                                    if (piece == Constants.CELL_KING) {
                                        White_King = White_King + 10;
                                    }else {
                                        White = White + 10;
                                    }
                                }
                            }       
                        }
                    }   
                
                }
                int Red_Distance = 0;
                int White_Distance = 0;
                int totalScore_Distance = 0;
                for (int i = 0; i < 32; i++) { 
                    if ( 0!= (gameState.get(i) & Constants.CELL_RED)) {
                            Red_Distance = Red_Distance + (gameState.cellToRow(gameState.get(i))*gameState.cellToRow(gameState.get(i)));
                    }else if (0!= (gameState.get(i) & Constants.CELL_WHITE)) {
                            White_Distance = White_Distance + ((7-gameState.cellToRow(gameState.get(i)))*(7-gameState.cellToRow(gameState.get(i))));
                    }   
                }
            
                if ( Player == Constants.CELL_RED) {
                    totalScoreFinal = Red_King + Red -White -White_King+ Red_Distance - White_Distance;
                }else if(Player == Constants.CELL_WHITE) {
                    totalScoreFinal = - Red_King - Red +White +White_King + White_Distance - Red_Distance;
                }
            }
            return totalScoreFinal;
    }
    
    
    
    public int minmaxalphaBeta( GameState gameState, int depth, int alfa, int beta, boolean Max_player) {
        
    		
        Long matrix = Zobrist(gameState);
        int [] value  = new int[2];
        int v = 0;
        int old_v = 0;
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);  
                
        if(hashmap.containsKey(matrix) && initial_depth != depth) {
            		object = new int[2];
            		object[0] = 0;
            		object[1] = 0;
            		object = hashmap.get(matrix);
            		
            		if (object[1] >= depth) {
            			return object[0];
            		}
        }
       
        if (gameState.isEOG() || depth == 0) {
                int eval = evaluationFunction (Max_player,gameState);
                
                object = new int[2];
                object[0] = eval;
                object[1] = depth;
                hashmap.put(matrix, object);
                return eval;
        }
        
        if (Max_player){
               
             v = Integer.MIN_VALUE; 
             
             for (GameState nextState : nextStates){
                int result = minmaxalphaBeta(nextState, depth-1, alfa, beta,false);
                if (result > v && depth == initial_depth) {
                    nextMove = nextState;
                }
                v = Math.max(v,result );
                alfa = Math.max(alfa, v);
                
                 if (beta <= alfa) {
                      break;
                 }
             }
             object = new int[2];
             object[0] = v;
             object[1] = depth;
             hashmap.put(matrix, object);
             return v;  
       } else {     
              v = Integer.MAX_VALUE; //infinity
              for (GameState nextState : nextStates){
            	  		v = Math.min(v, minmaxalphaBeta(nextState, depth-1, alfa, beta,true));
                     beta= Math.min(beta, v);
                     if (beta <= alfa  ) {
                         break;
                     }
              }  
              object = new int[2];
              object[0] =v;
              object[1] = depth;
              hashmap.put(matrix, object);
              return v;
      }   
   } 
}