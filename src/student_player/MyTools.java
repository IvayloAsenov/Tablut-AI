package student_player;

public class MyTools {
    
	private int move = 0;
	
	/**
	 * Returns opening moves for black
	 * @return array of openings
	 */
	public int[][] getOpeningBlacks(){
		int[][] moves = { {1, 6} , {6, 7} , {7, 2} , {2, 1} };
		return moves;
	}
	
	/**
	 * Return starting positions for black
	 * @return array of starting positions
	 */
	public int[][] getStartingPositionsBlack(){
		int[][] pos = { {1, 4} , {4, 7} , {7, 4} , {4, 1} };
		return pos;
	}
	
	public int[][] getOpeningSwedes(){
		int[][] moves = { {3, 3} , {3, 5} , {5, 5} , {5, 3} };
		return moves;
	}
	
	public int[][] getStartingPositionsSwedes(){
		int[][] pos = { {3, 4} , {4, 5} , {5, 4} , {6, 3} };
		return pos;
	}
	
	public int getMove(){
		return move;
	}
	
	public void setMove(int i){
		move = i;
	}
}