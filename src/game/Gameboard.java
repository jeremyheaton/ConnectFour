package game;
import java.util.ArrayList;

public class Gameboard  implements Cloneable{
	ArrayList<ArrayList<GameSpace>> spaces = new ArrayList<ArrayList<GameSpace>>();
	int height;
	int width;
	boolean playing = true;
	char winner = 'X';
	public char playerColor;
	public char computerColor;
	GameSpace lastSpacePlayed;
	int turnCount = 0;

	  public Gameboard(Gameboard copy) {
	    for(ArrayList<GameSpace> board : copy.spaces){
	    	ArrayList<GameSpace> gamespaces = new ArrayList<GameSpace>();	
	    	for(GameSpace gs : board){
	        gamespaces.add(new GameSpace(gs));
	    	}
	    	spaces.add(gamespaces);
	    }
	    this.height = copy.height;
	    this.width = copy.width;
	    this.playing = copy.playing;
	    this.winner = copy.winner;
	    this.playerColor = copy.playerColor;
	    this.computerColor = copy.computerColor;
	    this.lastSpacePlayed = copy.lastSpacePlayed;// you can access  
	  }
	public Gameboard(int width, int height) {
		this.width = width;
		this.height = height;
		populateBoard();
	}

	private void populateBoard() {
		for (int h = 0; h<height; h++) {
			ArrayList<GameSpace> board = new ArrayList<GameSpace>();
			for (int w = 0; w < width; w++ ) {
				board.add(new GameSpace(w,h ));
			}
			spaces.add(board);
		}
	}
	
	public void PrintBoard() {
		System.out.print("\n\n\n\n\n");
		for (int h = height-1; h >=0; h--) {
			for (int w = 0; w < width; w++) {
				System.out.print(spaces.get(h).get(w).getColor());
			}
			System.out.print("\n");
		}
	}
	public int PlacePiece(int width, char c) {
		int space = 0;
		boolean placePiece = true;
		GameSpace gs = spaces.get(space).get(width);
		while (placePiece) {
			if (gs.getColor() != '-') {
				space++;
				if(space>5){
					return -1;
				}
				gs = spaces.get(space).get(width);
				
			} else {
				gs.setColor(c);
				lastSpacePlayed = gs;
				playing = !CheckForWinner(gs);
				if(!playing && !CheckForTie()){
					winner = gs.getColor();	
				}
				placePiece = false;
				turnCount++;
				return gs.getY();
			}
		}
		return -1;
	}
	
	public boolean CheckForTie(){
		if(turnCount > 41){
			return true;
		}else{
			return false;
		}
	}
	public boolean CheckForWinner(GameSpace gs) {
		return (CheckVertical(gs) || CheckHorizontal(gs) || CheckDiagonal(gs));
	}
	
	public boolean CheckHorizontal(GameSpace gs) {
		int x = 0;
		int counter = 1;
		boolean goleft = true;
		boolean goright = true;
		while (x <= 4) {
			x++;
			if (gs.getX() + x < width && goright) {
				if (spaces.get(gs.getY()).get( gs.getX()+x).getColor() == gs
						.getColor()) {
					counter++;
				}else{
					goright = false;
				}
			}
			if (gs.getX() - x >= 0 && goleft) {
				if (spaces.get(gs.getY()).get( gs.getX() - x).getColor() == gs
						.getColor()) {
					counter++;
				} else {
					goleft = false;					
				}
			}
			if (counter >= 4)
				return true;
		}
		return false;

	}

	public boolean CheckVertical(GameSpace gs) {
		if (gs.getY() > 2) {
			int count = 1;
			for (int i = 0; i < 4; i++) {
				if (spaces.get(gs.getY() - i ).get(gs.getX()).getColor() == gs
						.getColor()) {
					count++;
				}else{
					count=0;
				}
				
			}
			if (count >= 4) {
				return true;
			}
		}
		return false;
	}

	public boolean CheckDiagonal(GameSpace gs) {
		boolean goupleft = true;
		boolean goupright = true;
		boolean godownleft = true;
		boolean godownright = true;
	
		int x = 0, y = 0;
		int countBRTR = 1;
		int countTRBR = 1;
		while (x <= 4 && y <= 4) {
			x++;
			y++;
			if (gs.getX() + x < width && gs.getY() - y >= 0 && godownright) {
				if (spaces.get(gs.getY() - y).get(  gs.getX() + x ).getColor() == gs
						.getColor()) {
					countBRTR++;
				}else{
					godownright = false;
				}
			}
			if (gs.getX() - x >= 0 && gs.getY() + y < height  && goupleft ) {
				if (spaces.get(gs.getY() + y).get( gs.getX() - x).getColor() == gs
						.getColor()) {
					countBRTR++;
				}else{
					goupleft = false;
				}
			}
			if (gs.getX() + x < width && gs.getY() + y < height && goupright ) {
				if (spaces.get(gs.getY() + y ).get( gs.getX() + x).getColor() == gs
						.getColor()) {
					countTRBR++;
				}else{
					goupright = false;
				}
			}
			if (gs.getX() - x >= 0 && gs.getY() - y >= 0 && godownleft) {
				if (spaces.get(gs.getY() - y).get( gs.getX() - x).getColor() == gs
						.getColor()) {
					countTRBR++;
				}else{
					godownleft = false;
				}
			}
			if (countTRBR >= 4 || countBRTR >= 4) {
				return true;
			}
		}
		return false;

	}
}
