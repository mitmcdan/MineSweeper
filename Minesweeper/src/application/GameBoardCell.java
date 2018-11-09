package application;

public class GameBoardCell {
	
	// setting variables
	private boolean mine;
	private boolean flagged;
	private boolean clicked;
	private int bombsNear;
	
	// constructor method
	public GameBoardCell() {
		this.mine = false;
		this.bombsNear = 0;
		this.flagged = false;
		this.clicked = false;
	}
	
	// userLevel of board	
	//set clicked
	public void setClicked(boolean x) {
		this.clicked = true;
	}
	
	//get isClicked
	public boolean isClicked() {
		return this.clicked;
	}
	
	//set flagged 
	public void setFlagged(boolean f) {
		this.flagged = f;
	}
	//get flagged
	public boolean isFlagged() {
		return this.flagged; 
	}
	
	// dataLevel of board
	public boolean isEmpty() {
		if(this.bombsNear == 0)
			return true;
		return false;
	}
	
	//mine setter
	public void setMine(boolean m) {
		this.mine = m;
	}
	
	//mine getter
	public boolean isMine() {
		 return this.mine;
	}
	
	//bomb count setter
	public void setBombsNear(int bn) {
		this.bombsNear = bn;
	}
	
	//bomb count getter
	public int bombsNear() {
		return this.bombsNear;	
	}
	
}
