import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Queen extends Piece{
	private final ImageIcon WHITE_QUEEN = new ImageIcon("whiteQueen.png");
	private final ImageIcon BLACK_QUEEN = new ImageIcon("blackQueen.png");
	
	/**
	 * Initializes a newly created Queen object by calling the Piece's (superclass) constructor
	 * @param white
	 */
	public Queen(boolean white) {
		super(white);
	}

	/**
	 * Based on whether this Queen piece is white or black, calls the corresponding paintIcon method
	 * @param board
	 * @param g
	 * @param x
	 * @param y
	 */
	@Override
	public void print(Component board, Graphics g, int file, int rank) {
		if(this.isWhite()) 
			WHITE_QUEEN.paintIcon(board,g,file,rank);
		else 
			BLACK_QUEEN.paintIcon(board,g,file,rank);
	}

	/**
	 * Gathers all squares that this Queen can attack
	 * Checks whether those squares are valid.
	 * @param board
	 * @param startSquare
	 * @return arraylist of squares that represent legal moves that Queen can perform from a start square
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
	 * Generates all squares that a Queen attacks.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that a Queen can attack from a start square.
	 */
	@Override
	public ArrayList<Square> getAttacks(Board board, Square startSquare) {
		ArrayList<Square> attacks = new ArrayList<Square>();
		//Since a Queen's attacks are essentially that of a Rook + Bishop call both straightAttacks and diagonalAttacks methods
		attacks.addAll(this.straightAttacks(board, startSquare));
		attacks.addAll(this.diagonalAttacks(board,startSquare));
		return attacks;
	}

}
