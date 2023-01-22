import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Knight extends Piece{
	private static final ImageIcon WHITE_KNIGHT = new ImageIcon("whiteKnight.png");
	private static final ImageIcon BLACK_KNIGHT = new ImageIcon("blackKnight.png");
	
	/**
	 * Initializes a newly created Knight object by calling the Piece's (superclass) constructor
	 * @param white
	 */
	public Knight(boolean white) {
		super(white);
	}

	/**
	 * Based on whether this Knight piece is white or black, calls the corresponding paintIcon method.
	 * @param board
	 * @param g
	 * @param x
	 * @param y
	 */
	@Override
	public void print(Component board, Graphics g, int file, int rank) {
		if(this.isWhite()) 
			WHITE_KNIGHT.paintIcon(board,g,file,rank);
		else 
			BLACK_KNIGHT.paintIcon(board,g,file,rank);
	}

	/**
	 * Gathers all squares that this Knight can attack.
	 * Checks whether those squares are valid.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that represent legal moves that Knight can perform from a start square.
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
	 * Generates all squares that a Knight attacks.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that a Knight can attack from a starting square.
	 */
	@Override
	public ArrayList<Square> getAttacks(Board board, Square start) {
		ArrayList<Square> attacks = new ArrayList<Square>();
		int sY = start.getRank();
		int sX = start.getFile();
		Square s;
		
		/*The pattern of a knights move (clockwise) are:
		 * Rank(Y) {2,1,-1,-2,-2,-1,1,2} ignoring(-) {2,1,1,2,2,1,1,2}
		 * File(X) {1,2,2,1,-1,-2,-2,-1} ignoring(-) {1,2,2,1,1,2,2,1}
		 * If we ignore the (-) they are never both the same number
		 * If |x| != |y| then it will be a square the horse can attack
		*/
		for (int rank = 2; rank >= -2; rank--) {
			for (int file = 2; file >= -2; file--) {
				if(Math.abs(rank) != Math.abs(file)) {
					if(rank != 0 && file != 0) {
						try {
							s = board.getSquare(sY + rank ,sX + file);
							attacks.add(s);
		                } catch (ArrayIndexOutOfBoundsException e) {
		                    continue;
		                }
					}
				}
	        }
	    }
		
		return attacks;
	}
}
