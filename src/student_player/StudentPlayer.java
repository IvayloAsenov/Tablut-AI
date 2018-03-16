package student_player;

import java.util.List;
import java.util.Random;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

/** A player file submitted by a student. */
public class StudentPlayer extends TablutPlayer {
	
	private static final int INFINITY = 9999999;
	private boolean black; // Black player ??
	
	private TablutMove bestMove;
	
	private Random rand = new Random(1848);
	
	MyTools myTools;
	Coordinates coords;
	
    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260742273");
        myTools = new MyTools();
        coords = new Coordinates();
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(TablutBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
    	
    	int[][] openings;
    	int[][] startingPositions;
    	
    	if(player_id == TablutBoardState.MUSCOVITE){
    		openings = myTools.getOpeningBlacks();
    		startingPositions = myTools.getStartingPositionsBlack();
    	}else{
    		openings = myTools.getOpeningBlacks();
    		startingPositions = myTools.getStartingPositionsBlack();
    	}
    	
    	List<TablutMove> options = boardState.getAllLegalMoves();
    	bestMove = options.get(rand.nextInt(options.size()));;
    	
    	/**
    	 * Uses opening strategy for first 4 moves 
    	 */
    	if(myTools.getMove() < 4){
	    		
	    	int i = 0;
	    	
	    	for(TablutMove move : options){
	    		i = myTools.getMove();
	    		
	    		Coord start = coords.get(startingPositions[i][0], startingPositions[i][1]);
	    		Coord end = coords.get(openings[i][0], openings[i][1]);
	    		
	    		if(move.getStartPosition() == start && move.getEndPosition() == end){
	    			bestMove = move;
	    		}
	    	}	
	    	myTools.setMove(i+1);
    	}
    	
    	else{
    		System.out.println("YOLLO");
    		minimax((TablutBoardState) boardState.clone(), true, 15);
    	}
    	
        return bestMove;
    }
    
    public void minimax(TablutBoardState boardState, boolean black, int depth){
    	int val = 0;
    	
    	if(black)
    		val = Max(boardState, depth); // Maximise black
    	else
    		val = Min(boardState, depth); // Minimise white
    	
    	return;
    }
    
    private int Max(TablutBoardState boardState, int depth){
    	if(depth == 0)
    		return estimate(boardState); // We have reached leaf node!
    	
    	int best = -INFINITY;
    	
    	List<TablutMove> options = boardState.getAllLegalMoves();
    	
    	int size = options.size();
    	
    	while(options.size() > 0){
    		TablutMove move = options.remove(0); // List acts as a queue
    		boardState.processMove(move); // Process the move on nextBoard
    		
    		int val = -Min(boardState, depth-1);
    		
    		if(val > best){
    			best = val;
    			
    			if(black){
    				bestMove = move;
    			}
    		}
    	}
    	
    	return best;
    }
    
    private int Min(TablutBoardState boardState, int depth){
    	if(depth == 0)
    		return estimate(boardState); // We have reached leaf node!
    	
    	int best = -INFINITY;
    	
    	List<TablutMove> options = boardState.getAllLegalMoves();
    	
    	int size = options.size();
    	
    	while(options.size() > 0){
    		TablutMove move = options.remove(0); // List acts as a queue
    		boardState.processMove(move); // Process the move on nextBoard
    		
    		int val = -Max(boardState, depth-1);
    		
    		if(val > best){
    			best = val;
    			
    			if(!black){
    				bestMove = move;
    			}
    		}
    	}
    	
    	return best;
    }
    
    /**
     * @param boardState The board state reached by expanding the tree
     * @return The evaluation of the boardState compared to the currentState
     */
    private int estimate(TablutBoardState boardState){
    	int evaluation = 100;
    	
    	int opponent = boardState.getOpponent();
    	int numberOfOpponentPieces = boardState.getNumberPlayerPieces(opponent);
    	
    	evaluation = evaluation - numberOfOpponentPieces;
    	System.out.println(evaluation);
    	return evaluation;
    }
}