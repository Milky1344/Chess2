import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Rook extends Piece{
	private final ImageIcon WHITE_ROOK = new ImageIcon("whiteRook.png");
	private final ImageIcon BLACK_ROOK = new ImageIcon("blackRook.png");
	
	/**
	 * Initializes a newly created Rook object by calling the Piece's (superclass) constructor
	 * @param white
	 */
	public Rook(boolean white) {
		super(white);
	}

	/**
	 * Based on whether this Rook piece is white or black, calls the corresponding paintIcon method
	 * @param board
	 * @param g
	 * @param x
	 * @param y
	 */
	@Override
	public void print(Component board, Graphics g, int file, int rank) {
		if(this.isWhite()) 
			WHITE_ROOK.paintIcon(board,g,file,rank);
		else 
			BLACK_ROOK.paintIcon(board,g,file,rank);
	}

	/**
	 * Gathers all squares that this Rook can attack
	 * Checks whether those squares are valid.
	 * @param board
	 * @param startSquare
	 * @return arraylist of squares that represent legal moves that Rook can perform from a start square
	 */
	@Override
	public ArrayList<Square> getLegal(Board board, Square startSquare) {
		ArrayList<Square> legalMoves = new ArrayList<Square>();
	
		for(Square checkSquare : this.getAttacks(board,startSquare)) {
			if(checkSquare.isOccupied()) {
				if (this.isWhite() && checkSquare.isPieceWhite() || this.isBlack() && checkSquare.isPieceBlack())
					continue;
			}
				
			if(!board.performPseudoMove(board, startSquare, checkSquare)) 
				continue;

			legalMoves.add(checkSquare);
		}
		
		return legalMoves;
	}

	/**
	 * Generates all squares that a Rook attacks.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that a Rook can attack from a start square.
	 */
	@Override
	public ArrayList<Square> getAttacks(Board board, Square startSquare) {
		//Since Rooks can only attack in straight lines all that is needed is to call the straightLines method
		return this.straightAttacks(board, startSquare);
	}
}
