import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Bishop extends Piece{
	private static final ImageIcon WHITE_BISHOP = new ImageIcon("whiteBishop.png");
	private static final ImageIcon BLACK_BISHOP = new ImageIcon("blackBishop.png");
	
	/**
	 * Initializes a newly created Bishop object by calling the Piece's (superclass) constructor.
	 * @param white
	 */
	public Bishop(boolean white) {
		super(white);
	}

	/**
	 * Based on whether this Bishop piece is white or black, calls the corresponding paintIcon method.
	 * @param board
	 * @param g
	 * @param x
	 * @param y
	 */
	@Override
	public void print(Component board, Graphics g, int file, int rank) {
		if(this.isWhite()) 
			WHITE_BISHOP.paintIcon(board,g,file,rank);
		else 
			BLACK_BISHOP.paintIcon(board,g,file,rank);
	}

	/**
	 * Gathers all squares that this Bishop can attack.
	 * Checks whether those squares are valid.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that represent legal moves that Bishop can perform from a start square.
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
	 * Generates all squares that a Bishop attacks.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that a Bishop can attack from a start square.
	 */
	@Override
	public ArrayList<Square> getAttacks(Board board, Square startSquare) {
		//Since Bishops can only attack diagonally all that is needed is to call the diagonalAttacks method
		return this.diagonalAttacks(board,startSquare);
	}

}
