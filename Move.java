
public class Move {

	private Square startSquare;
	private Square endSquare;
	private Piece movedPiece;
	private boolean whiteTurn;

	public Move(Square startSquare, Square endSquare, boolean whiteTurn) {
		this.startSquare = startSquare;
		this.endSquare = endSquare;
		this.whiteTurn = whiteTurn;
		this.movedPiece = startSquare.getPiece();
	}
	
	private String convertFile(int file) {
		switch(file) {
			case 0:
				return "a";
			case 1:
				return "b";
			case 2:
				return "c";
			case 3:
				return "d";
			case 4:
				return "e";
			case 5:
				return "f";
			case 6:
				return "g";
			case 7:
				return "h";
			default:
				return "";
		}
	}
	
	public String toString() {
		String move = "";
		String startRank = "" + Math.abs(startSquare.getRank() - 8);
		String startFile = convertFile(startSquare.getFile());
		String endRank = "" + Math.abs(endSquare.getRank() - 8);
		String endFile = convertFile(endSquare.getFile());
		
		if(this.whiteTurn)
			move += "W";
		else
			move += "B";
		
		if(this.movedPiece instanceof Bishop)
			move += "B";
		else if(this.movedPiece instanceof Knight)
			move += "N" + startFile + startRank;
		else if(this.movedPiece instanceof Rook)
			move += "R" + startFile + startRank;
		else if(this.movedPiece instanceof Queen)
			move += "Q";
		else if(this.movedPiece instanceof King)
			move += "K";
		
		if(endSquare.isOccupied())
			move += "x";
		
		move += startFile + startRank + endFile + endRank;
		return move;
	}

	public Piece getMovedPiece() {
		return this.movedPiece;
	}
	
	public Square getStartSquare() {
		return this.startSquare;
	}
	public Square getEndSquare() {
		return this.endSquare;
	}
}
