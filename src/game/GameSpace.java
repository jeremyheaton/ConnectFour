package game;
public class GameSpace {
	int x;
	int y;
	private char color;
	
	public GameSpace( GameSpace gamespace) {
		this.x = gamespace.x;
		this.y = gamespace.y;
		this.color = gamespace.color;
	}
	public GameSpace( int x, int y) {
		this.x = x;
		this.y = y;
		this.color = '-';
	}
	public void setColor(char color){
		this.color = color;
	}
	public char getColor(){
		return this.color;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}
