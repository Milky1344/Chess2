import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class King extends Piece{
	private final ImageIcon WHITE_KING = new ImageIcon("whiteKing.png");
	private final ImageIcon BLACK_KING = new ImageIcon("blackKing.png");
	
	/**
	 * Initializes a newly created King object by calling the Piece's (superclass) constructor
	 * @param white
	 */
	public King(boolean white) {
		super(white);
	}

	/**
	 * Based on whether this King piece is white or black, calls the corresponding paintIcon method
	 * @param board
	 * @param g
	 * @param x
	 * @param y
	 */
	@Override
	public void print(Component b, Graphics g, int x, int y) {
		if(this.isWhite()) 
			WHITE_KING.paintIcon(b,g,x,y);
		else 
			BLACK_KING.paintIcon(b,g,x,y);
	}
	
	/**
	 * 
	 * @param board
	 * @param start
	 * @param end
	 * @return
	 */
	public boolean castled(Board board, Square start, Square end) {
		int sX = start.getFile();
		int eX = end.getFile();
		if(eX == sX + 2 || eX == sX - 2) {
			return true;
		}
		return false;
	}
	
	private boolean canCastleEast(Board board, Square king) {
		int sY = king.getRank();
		int sX = king.getFile();
		for(int c = sX + 1; c < 7; c++) {
			if(board.getSquare(sY,c).isOccupied())
				return false;
			if(king.isPieceWhite() && board.getSquare(sY,c).getAtkBPieces() > 0 || king.isPieceBlack() && board.getSquare(sY,c).getAtkWPieces() > 0 )
				return false;
		}
		return true;
	}
	
	private boolean canCastleWest(Board board, Square king) {
		int sY = king.getRank();
		int sX = king.getFile();
		for(int c = sX - 1; c > 0; c--) {
			if(board.getSquare(sY,c).isOccupied())
				return false;
			if(king.isPieceWhite() && board.getSquare(sY,c).getAtkBPieces() > 0 || king.isPieceBlack() && board.getSquare(sY,c).getAtkWPieces() > 0 )
				return false;
		}
		return true;
	}
	
	/**
	 * Gathers all squares that this King can attack.
	 * Checks whether those squares are valid.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that represent legal moves that King can perform from a start square.
	 */
	@Override
	public ArrayList<Square> getLegal(Board board, Square startSquare) {
		ArrayList<Square> legalMoves = new ArrayList<Square>();
		int sY = startSquare.getRank();
		int sX = startSquare.getFile();
		Square toEast = null;
		Square toWest = null;
		
		//For each square that the King can attack
		for(Square checkSquare : this.getAttacks(board,startSquare)) {
			//Based on the king's color, check whether or not checkSquare is attacked by any enemy piece, if so do not add this square as a possible move
			if(this.isWhite() && checkSquare.getAtkBPieces() > 0 || this.isBlack() && checkSquare.getAtkWPieces() > 0)
				continue;
			
			//If the square that is being checked is occupied by a piece that is the same color as this king object, do not add this square
			if(checkSquare.isOccupied()) {
				if (this.isWhite() && checkSquare.isPieceWhite() || this.isBlack() && checkSquare.isPieceBlack()) 
					continue;
			}
			
			//If moving to checkSquare results in the king being in check, do not add this square
			if(!board.performPseudoMove(board, startSquare, checkSquare))
				continue;
			
			legalMoves.add(checkSquare);
		}

		//If this king object has not moved yet, check whether or not it is capable of castling to the east or west
		if(this.hasNotMoved()) {
			//toEast is the square that the player presses to initiate castling towards East
			toEast = board.getSquare(sY,sX + 2);
			Piece pieceToEast = board.getSquare(sY, 7).getPiece();

			if(pieceToEast != null && pieceToEast instanceof Rook && pieceToEast.hasNotMoved() && this.canCastleEast(board, startSquare))
				legalMoves.add(toEast);
			
			//toWast is the square that the player presses to initiate castling towards West
			toWest = board.getSquare(sY,sX - 2);
			Piece pieceToWest = board.getSquare(sY, 0).getPiece();
			
			if(pieceToWest != null && pieceToWest instanceof Rook && pieceToWest.hasNotMoved() && this.canCastleWest(board, startSquare))
				legalMoves.add(toWest);
		}
		
		return legalMoves;
	}

	/**
	 * Generates all squares that a King can attacks.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that a King can attack from a start square.
	 */
	@Override
	public ArrayList<Square> getAttacks(Board board, Square start) {
		ArrayList<Square> attacks = new ArrayList<Square>();
		int sY = start.getRank();
		int sX = start.getFile();
		Square s;
		
		/*Kings move in the 8 adjacent squares around them. The rank and files of those 8 squares
		 * are offset from the startSquare by factors -1 0 1. So we can check them based on their offset.
		 * Obviously rank and file offsets cannot both equal 0, as that would be the original square.
		*/
		for(int rank = -1; rank <= 1; rank++) {
			for(int file = -1; file <= 1; file++) {
				if(!(rank == 0 && file == 0)) {
					try {
						s = board.getSquare(sY + rank, sX + file);
						attacks.add(s);
					} catch (ArrayIndexOutOfBoundsException e) {
						continue;
					}
				}
			}
		}
		return attacks;
	}

}
