package student_player;

public class MyTools {
    
	int move = 0;
	
	public int[][] getOpeningBlacks(){
		int[][] moves = { {1, 6} , {6, 7} , {7, 2} , {2, 1} };
		return moves;
	}
	
	public int[][] getStartingPositionsBlack(){
		int[][] pos = { {1, 4} , {4, 7} , {7, 4} , {4, 1} };
		return pos;
	}
	
	public String getOpeningWhites(){
		
		return "whites";
	}
	
	public int getMove(){
		return move;
	}
	
	public void setMove(int i){
		move = i;
	}
}
