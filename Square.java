
public class Square {
	private Piece piece = null;
	private int rank;
	//Rank means row or Y
	private int file;
	//File means column or X
	private int atkWPieces = 0;
	private int atkBPieces = 0;
	
	public int getAtkWPieces() {
		return this.atkWPieces;
	}
	public void addAtkWPieces() {
		this.atkWPieces++;
	}
	public void rmovAtkWPieces() {
		this.atkWPieces = 0;
	}
	
	public int getAtkBPieces() {
		return this.atkBPieces;
	}
	public void addAtkBPieces() {
		this.atkBPieces++;
	}
	public void rmovAtkBPieces() {
		this.atkBPieces = 0;
	}
	
	public Square(int rank, int file) {
		this.rank = rank;
		this.file = file;
	}
	
	public int getRank() {
		return rank;
	}
	public int getFile() {
		return file;
	}
	
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	public Piece getPiece() { 
		return this.piece;
	}
	
	public boolean isOccupied() {
		return this.piece != null;
	}
	public boolean isNotOccupied() {
		return this.piece == null;
	}
	
	public boolean isPieceWhite() {
		return this.piece.isWhite();
	}
	public boolean isPieceBlack() {
		return this.piece.isBlack();
	}
}
