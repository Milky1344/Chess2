import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {
	
	private boolean white;
	private boolean moved = false;
	
	//Movement counters that help to calculate attacks (NEWS = North, East, West, South)
	private int north;
	private int east;
	private int west;
	private int south;
	
	/**
	 * Initializes a newly created Piece object that is either white or black. 
	 * @param white
	 */
	public Piece(boolean white) {
		this.white = white;
	}
	
	/**
	 * Returns a boolean of whether or not this piece is white.
	 * @return this.white.
	 */
	public boolean isWhite() {
		return this.white;
	}
	/**
	 * Returns a boolean of whether or not this piece is black in a "usable" form.
	 * @return !this.white.
	 */
	public boolean isBlack() {
		//If this.white = false (meaning the piece is black) by returning !this.white, it is "true" and it can used in an if statement directly
		return !this.white;
	}
	
	/**
	 * Sets whether or not this piece has been moved.
	 */
	public void moved() {
		this.moved = true;
	}
	/**
	 * Gets whether or not this piece has moved.
	 */
	public boolean hasNotMoved() {
		//If this.moved = false (meaning the piece has not moved) by returning !this.moved, it is "true" and can be used in an if statement directly
		return !this.moved;
	}
	
	/**
	 * Determines the directions on the board based from directional offsets from a starting Square.
	 * Also resets them if they have been changed (especially in the case of diagonal attacks.
	 * @param startRank
	 * @param startFile
	 */
	private void resetNEWS(int startRank, int startFile) {
		north = startRank - 1;
		east = startFile + 1;
		west = startFile - 1;
		south = startRank + 1;
	}
	
	/**
	 * Based on a start square, Checks all NEWS directions for the squares that a Rook/Queen can attack.
	 * If there is a piece in the way of one of the directions that square is considered attacked by the piece.
	 * Since only the knight can jump over pieces, any other squares after that piece is considered not attacked.
	 * @param board
	 * @param startSquare 
	 * @return A list of squares the piece can attack based on a start square.
	 */
	public ArrayList<Square> straightAttacks(Board board, Square startSquare) {
		ArrayList<Square> legalMoves = new ArrayList<Square>();
		//Gets the rank and file of the startSquare
		int startRank = startSquare.getRank();
		int startFile = startSquare.getFile();
		resetNEWS(startRank,startFile);
		Square checkSquare;
		//Check all squares directly East of the startSquare
		for(int file = east; file < 8; file++) {
			//Get the square that is being checked from the board
			checkSquare = board.getSquare(startRank,file);
			//If the square is occupied by any color of piece add it and stop checking in this direction
			if(checkSquare.isOccupied()) {
				legalMoves.add(checkSquare);
				break;
			} else {
			//If the square is not occupied add the square to possible attacks
				legalMoves.add(checkSquare);
			}
		}
		//Check all squares directly West of the startSquare
		for(int file = west; file >= 0; file--) {
			checkSquare = board.getSquare(startRank,file);
			if(checkSquare.isOccupied()) {
				legalMoves.add(checkSquare);
				break;
			} else {
				legalMoves.add(checkSquare);
			}
		}
		//Check all squares directly South of the startSquare
		for(int rank = south; rank < 8; rank++) {
			checkSquare = board.getSquare(rank,startFile);
			if(checkSquare.isOccupied()) {
				legalMoves.add(checkSquare);
				break;
			} else {
				legalMoves.add(checkSquare);
			}
		}
		//Check all squares directly North of the startSquare
		for(int rank = north; rank >= 0; rank--) {
			checkSquare = board.getSquare(rank,startFile);
			if(checkSquare.isOccupied()) {
				legalMoves.add(checkSquare);
				break;
			} else {
				legalMoves.add(checkSquare);
			}
		}
		return legalMoves;
	}

	/**
	 * Based on a start square, Checks all diagonal directions (NorthEast, SouthEast, NorthWest, SouthWest) for the squares that a Bishop/Queen can attack.
	 * If there is a piece in the way of one of the directions that square is considered attacked by the piece.
	 * Since only the knight can jump over pieces, any other squares after that piece is considered not attacked.
	 * @param board
	 * @param startSquare 
	 * @return An array list of squares the piece can attack diagonally based on a start square
	 */
	public ArrayList<Square> diagonalAttacks(Board board, Square startSquare) {
		ArrayList<Square> legalMoves = new ArrayList<Square>();
		//Gets the rank and file of the startSquare
		resetNEWS(startSquare.getRank(),startSquare.getFile());
		Square checkSquare;
		//Check all squares directly Southeast of the startSquare
		while(south < 8 && east < 8) {
			//Get the square that is being checked from the board
			checkSquare = board.getSquare(south,east);
			//If the square that is being check is occupied add the square then stop checking in that direction
			if(checkSquare.isOccupied()) {
				legalMoves.add(checkSquare);
				break;
			} else {
				legalMoves.add(checkSquare);
			}
			//Increment the directional counters. Represents the change of these while loops
			south++;
			east++;
		}
		//Check all squares directly Northwest of the startSquare
		while(north >= 0 && west >= 0) {
			checkSquare = board.getSquare(north,west);
			if(checkSquare.isOccupied()) {
				legalMoves.add(checkSquare);
				break;
			} else {
				legalMoves.add(checkSquare);
			}
			north--;
			west--;
		}
		resetNEWS(startSquare.getRank(),startSquare.getFile());
		//Check all squares directly Southwest of the startSquare
		while(south < 8 && west >= 0) {
			checkSquare = board.getSquare(south,west);
			if(checkSquare.isOccupied()) {
				legalMoves.add(checkSquare);
				break;
			} else {
				legalMoves.add(checkSquare);
			}
			south++;
			west--;
		}
		//Check all squares directly Northeast of the startSquare
		while(north >= 0 && east < 8) {
			checkSquare = board.getSquare(north,east);
			if(checkSquare.isOccupied()) {
				legalMoves.add(checkSquare);
				break;
			} else {
				legalMoves.add(checkSquare);
			}
			north--;
			east++;
		}
			
		return legalMoves;
	}
	
	/**
	 * Based on whether the piece is white or black, calls the representing paintIcon method for each piece.
	 * @param board
	 * @param g
	 * @param x
	 * @param y
	 */
	public abstract void print(Component board, Graphics g, int file, int rank);
	
	/**
	 * Generates all squares that a particular piece attacks. Especially useful for evaluating checks and attacks on pieces.
	 * For most pieces (except Pawns/King castling) a piece's attacks are the MOST of the squares it can move to.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that a particular piece can attack from a starting square.
	 */
	public abstract ArrayList<Square> getAttacks(Board board, Square startSquare);
	
	/**
	 * Gathers all squares that a piece can attack and move to (only for King castling and Pawn).
	 * Checks whether those squares are valid. Meaning, check if there is a piece of the same color 
	 * as the moving piece on that square and/or if moving to that square results in a check on the king.
	 * If either of those are true, that square is illegal to move to and is removed. Otherwise it is valid.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that represent legal moves that a particular piece can perform from a start square.
	 */
	public abstract ArrayList<Square> getLegal(Board board, Square startSquare);
}