package student_player;

import java.util.List;
import java.util.Random;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;
import tablut.TablutBoardState.Piece;

/** A player file submitted by a student. */
public class StudentPlayer extends TablutPlayer {
		
	private TablutMove bestMove;
	
	private Random rand = new Random(1848);
	
	int counter = 0;
	
	MyTools myTools = new MyTools();
	Coordinates coords = new Coordinates();;
	
	TablutBoardState ns; // Next State (move applied to current state)
	TablutBoardState bs; // Current State
	
    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260742273");
    }

    /**
     * This is the primary method that you need to implement. The `boardState`
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(TablutBoardState boardState) {
   
    	int[][] openings;
    	int[][] startingPositions;
    	
    	// Load starting moves for player depending if Muscovites or Swedes
    	if(player_id == TablutBoardState.MUSCOVITE){
    		openings = myTools.getOpeningBlacks();
    		startingPositions = myTools.getStartingPositionsBlack();
    	}else{
    		openings = myTools.getOpeningSwedes();
    		startingPositions = myTools.getStartingPositionsSwedes();
    	}
    	
    	List<TablutMove> options = boardState.getAllLegalMoves();
    	bestMove = options.get(rand.nextInt(options.size()));;
    	
    	// Uses opening strategy for first 4 moves
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
    	
    	// For the rest of the game, uses minimax algorithm with 
    	// different estimation function 
    	else{
    		bs = (TablutBoardState) boardState.clone();
    		ns = (TablutBoardState) bs.clone();
    		
    		bestMove = minimax((TablutBoardState) bs.clone(),true, 2, Integer.MIN_VALUE, Integer.MAX_VALUE).move;
    	}
    	
        return bestMove;
    }
    
    public StudentInfo minimax(TablutBoardState ps, boolean playerTurn, int depth, int alpha, int beta){
    	
    	int score;
    	TablutMove bestCurrent = bestMove; 
    	
    	StudentInfo si = new StudentInfo(); 
    	
    	List<TablutMove> children = ns.getAllLegalMoves();
    	
    	// Calculate board state when reaching leaf node
    	// or no more possible moves
    	if(depth == 0 || children.isEmpty()){
    		
    		//what estimation function to choose
    		if(player_id == TablutBoardState.MUSCOVITE)
    			score = estimateMuscovite(ns);
    		else
    			score = estimateSwede(ns);
    		
    		si.score = score; 
    		si.move = bestCurrent;
    		
    		return si;
    	}
    	else{
    		
    		// Run minimax for every possible move from that board state
	    	for(TablutMove move : children){
	    	
	    		ps = (TablutBoardState) ns.clone();
	    		ns.processMove(move); // try this move for the current "player"
	    		
	    		if(playerTurn){ //if current player is black maximise
	    			score = minimax(ps, false, depth-1, alpha, beta).score;
	    				    			
	    			if(score > alpha){
	    				alpha = score; 
	    				bestCurrent = move;
	    			}
	    		}else{ //minimizing player
	    			
	    			score = minimax(ps, true, depth-1, alpha, beta).score;
	    			
	    			if(score < beta){
	    				beta = score;
	    			}
	    		}
	    		
	    		// Undo move
	    		ns = (TablutBoardState) ps.clone(); // Undo the move
	    		
	    		// Pruning
	    		if(alpha >= beta) break;
	    	}
    	}
    	
    	si.score = (playerTurn) ? alpha : beta;
    	si.move = bestCurrent;
    	
    	return si;
    }

    /**
     * @param boardState The board state reached by expanding the tree
     * @return The evaluation of the boardState compared to the currentState
     */
    private int estimateMuscovite(TablutBoardState bs){
    	int evaluation = 100;
    	
    	int opponent = bs.getOpponent();
    	int numberOfOpponentPieces = bs.getNumberPlayerPieces(opponent);
    	
    	// Greedy form of evaluation 
    	evaluation = evaluation - numberOfOpponentPieces;
    	
    	// If we can win the game then take this move
    	// Best thing that can happen
    	if(bs.getKingPosition() == null)
    		evaluation = 100000;
    	else{
    		Coord kingCoord = bs.getKingPosition();
    		
    		// If possible get to the square next to the king
    		// 2nd best thing to happen 
    		try{
	    		Coord minusY = coords.get(kingCoord.x, kingCoord.y + 1);
	    		Coord plusY = coords.get(kingCoord.x, kingCoord.y - 1);
	
	    		Coord minusX = coords.get(kingCoord.x + 1, kingCoord.y);
	    		Coord plusX = coords.get(kingCoord.x - 1, kingCoord.y);
    		
    
	    		if(bs.getPieceAt(minusY) == Piece.BLACK){
	    			evaluation += 5000;
	    		}
	    		
	    		if(bs.getPieceAt(plusY) == Piece.BLACK){
	    			evaluation += 5000;
	    		}
	    		
	    		if(bs.getPieceAt(minusX) == Piece.BLACK){
	    			evaluation += 5000;
	    		}
	    		
	    		if(bs.getPieceAt(plusX) == Piece.BLACK){
	    			evaluation += 5000;
	    		}
	    		
    		}catch(ArrayIndexOutOfBoundsException e){ // Catches error if King is next to a border
    			
    		}
    	}
    	
    	return evaluation;
    }
    
    /**
     * @param boardState The board state reached by expanding the tree
     * @return The evaluation of the boardState compared to the currentState
     */
    private int estimateSwede(TablutBoardState bs){
    	int evaluation = 100;
    	
    	int opponent = bs.getOpponent();
    	int numberOfOpponentPieces = bs.getNumberPlayerPieces(opponent);
    	
    	Coord kingCoord;
    	
    	// Greedy form of evaluation 
    	evaluation = evaluation - numberOfOpponentPieces;
    	
    	// If king was taken then we would lose the game 
    	// Worst possible move, never take it
    	if(bs.getKingPosition() == null)
    		return evaluation = -100;
    	else{
    		kingCoord = bs.getKingPosition();
    		
    		// If black piece next to the king, not very good 
    		// because we can get trapped. Try not to take those 
    		// kinds of moves 
    		try{
	    		Coord minusY = coords.get(kingCoord.x, kingCoord.y + 1);
	    		Coord plusY = coords.get(kingCoord.x, kingCoord.y - 1);
	
	    		Coord minusX = coords.get(kingCoord.x + 1, kingCoord.y);
	    		Coord plusX = coords.get(kingCoord.x - 1, kingCoord.y);
    		
    
	    		if(bs.getPieceAt(minusY) == Piece.BLACK){
	    			evaluation += -10;
	    		}
	    		
	    		if(bs.getPieceAt(plusY) == Piece.BLACK){
	    			evaluation += -10;
	    		}
	    		
	    		if(bs.getPieceAt(minusX) == Piece.BLACK){
	    			evaluation += -10;
	    		}
	    		
	    		if(bs.getPieceAt(plusX) == Piece.BLACK){
	    			evaluation += -10;
	    		}
	    		
    		}catch(ArrayIndexOutOfBoundsException e){
    			
    		}
    	}
    	
    	int distance = coords.distanceToClosestCorner(kingCoord);
    	
    	if(distance == 0)
    		return 1000000; // We win the game, best possible move
    	
    	// Simple addition and multiplication by constant to calculate 
    	// how close we are to the corner and if its better to get closer
    	// or to escape from a muscovite next to the king
    	distance = 10 - distance; 
    	
    	evaluation += distance * 10;
    	
    	return evaluation;
    }
}