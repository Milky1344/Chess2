import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Pawn extends Piece{
	private static final ImageIcon WHITE_PAWN = new ImageIcon("whitePawn.png");
	private static final ImageIcon BLACK_PAWN = new ImageIcon("blackPawn.png");
	
	/**
	 * Initializes a newly created Pawn object by calling the Piece's (superclass) constructor.
	 * @param white
	 */
	public Pawn(boolean white) {
		super(white);
	}

	/**
	 * Based on whether this Pawn piece is white or black, calls the corresponding paintIcon method.
	 * @param board
	 * @param g
	 * @param x
	 * @param y
	 */
	@Override
	public void print(Component board, Graphics g, int file, int rank) {
		if(this.isWhite()) 
			WHITE_PAWN.paintIcon(board,g,file,rank);
		else 
			BLACK_PAWN.paintIcon(board,g,file,rank);
	}
	
	/**
	 * Checks if the end square the pawn is planning to move to is at the end of the board, based on the pawn's color.
	 * If so, show the corresponding option pane for the player to choose the piece that they would like to promote to
	 * then promotes the pawn to said piece.
	 * @param board
	 * @param startSquare
	 * @param endSquare
	 */
	public void canPromote(Board board, Square startSquare, Square endSquare) {
		//If the pawn is white, has it reached it's promotion rank?
		if(this.isWhite() && endSquare.getRank() == 0) {
			//JOptionPane for showing the choices of promotion
			String[] options = new String[] {"Queen", "Rook", "Bishop", "Knight"};
			int response = JOptionPane.showOptionDialog(
					null,
					"Promote to:",
					"White Pawn Promotion", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
					null, options, 
					options[0]
					);
			
			//Removes both the pawn that was moved and piece that was on the endSquare from their corresponding piece list
			board.removePiece(startSquare.getPiece());
			board.removePiece(endSquare.getPiece());
			
			//Places the piece that was selected by the player where the pawn originally was
			switch(response) {
				case 0:
					startSquare.setPiece(new Queen(true));
					break;
				case 1:
					startSquare.setPiece(new Rook(true));
					break;
				case 2:
					startSquare.setPiece(new Bishop(true));
					break;
				case 3:
					startSquare.setPiece(new Knight(true));
					break;
			}
			
			//Adds the piece that the pawn promoted to white's piece list
			board.addPiece(startSquare.getPiece());
			
		//If the pawn is black, has it reached it's promotion rank?
		} else if(this.isBlack() && endSquare.getRank() == 7) {
			String[] options = new String[] {"Queen", "Rook", "Bishop", "Knight"};
			int response = JOptionPane.showOptionDialog(
					null,
					"Promote to:",
					"Black Pawn Promotion", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
					null, options, 
					options[0]
					);
			board.removePiece(startSquare.getPiece());
			board.removePiece(endSquare.getPiece());
			switch(response) {
				case 0:
					startSquare.setPiece(new Queen(false));
					break;
				case 1:
					startSquare.setPiece(new Rook(false));
					break;
				case 2:
					startSquare.setPiece(new Bishop(false));
					break;
				case 3:
					startSquare.setPiece(new Knight(false));
					break;
			}
			
			//Adds the piece that the pawn promoted to black's piece list
			board.addPiece(startSquare.getPiece());
		}
	}
	
	/**
	 * Checks whether or not a Pawn can perform En Passent.
	 * @param board
	 * @param startSquare
	 * @return A boolean of whether or not an En Passent is possible by any of the pawns.
	 */
	public boolean canEnPassent(Board board, Square startSquare) {
		Move lastMove = board.getLastMove();
		//If a lastMove has been played
		if(lastMove != null) {
			Piece prevPiece = lastMove.getMovedPiece();
			//If the lastMove was the movement of an enemy pawn
			if(prevPiece instanceof Pawn) {
				Square prevStart = lastMove.getStartSquare();
				Square prevEnd = lastMove.getEndSquare();
				//If the pawn is on the same rank as an enemy pawn that just double moved
				if(prevEnd.getRank() == startSquare.getRank() && Math.abs(prevEnd.getRank() - prevStart.getRank()) == 2) {
					//If the enemy pawn is to the right or left of the pawn being checked
					if(prevEnd.getFile() + 1 == startSquare.getFile() || prevEnd.getFile() - 1 == startSquare.getFile())
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gathers all squares that this Pawn can attack and move to.
	 * Checks whether those squares are valid.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that represent legal moves that Pawn can perform from a start square.
	 */
	@Override
	public ArrayList<Square> getLegal(Board board, Square startSquare) {
		ArrayList<Square> legalMoves = new ArrayList<Square>();
		ArrayList<Square> temp = new ArrayList<Square>();
		temp.addAll(this.getMoves(board, startSquare));
		temp.addAll(this.getAttacks(board, startSquare));
		
		//For each square that the pawn can move to/attack check:
		for(Square checkSquare : temp) {
			//if that square is occupied() and if it is occupied by a piece of the same color if so do not add this square/move
			if(checkSquare.isOccupied()) {
				if (this.isWhite() && checkSquare.isPieceWhite() || this.isBlack() && checkSquare.isPieceBlack())
					continue;
			}
			
			//check whether or not moving to this square will result in the king being in check if so do not add this square/move
			if(!board.performPseudoMove(board, startSquare, checkSquare))
				continue;
			
			legalMoves.add(checkSquare);
		}
		
		return legalMoves;
	}
	
	/**
	 * Because a Pawn's movement is not how it attacks/takes pieces, it makes sense to separate getting it's moves and attacks.
	 * @param board
	 * @param startSquare
	 * @return An array list of possible squares that the pawn can move to from a start square.
	 */
	public ArrayList<Square> getMoves(Board board, Square startSquare) {
		ArrayList<Square> moves = new ArrayList<Square>();
		int sY = startSquare.getRank();
		int sX = startSquare.getFile();
		Square square1InFront = null;
		Square square2InFront = null;
		
		//Initialize the squares that are in front of the pawn
		if(this.isWhite()) {
			square1InFront = board.getSquare(sY - 1, sX);
			if(this.hasNotMoved()) 
				square2InFront = board.getSquare(sY - 2, sX);
		} else { 
			square1InFront = board.getSquare(sY + 1, sX);
			if(this.hasNotMoved()) {
				square2InFront = board.getSquare(sY + 2, sX);
			}
		}
		
		//Since pawns cannot jump over pieces, only if the square in front of them is not occupied by any piece should the square be added as a legal move
		if(square1InFront.isNotOccupied()) {
			moves.add(square1InFront);
			if(this.hasNotMoved() && square2InFront.isNotOccupied()) {
				moves.add(square2InFront);
			}
		}
		
		return moves;
	}

	/**
	 * Generates all squares that the Pawn attacks. This includes en passent moves.
	 * @param board
	 * @param startSquare
	 * @return An array list of squares that a Pawn can attack from a start square.
	 */
	@Override
	public ArrayList<Square> getAttacks(Board board, Square startSquare) {
		ArrayList<Square> legalAttacks = new ArrayList<Square>();
		int sY = startSquare.getRank();
		int sX = startSquare.getFile();
		Square squareFrontEast = null;
		Square squareFrontWest = null;
		//Try to get this square, as the pawns on the edge of the board are unable to attack outside of the board
		try {
			if(this.isWhite()) {
				if(sX != 7) 
					squareFrontEast = board.getSquare(sY - 1, sX + 1);
				if(sX > 0)
					squareFrontWest = board.getSquare(sY - 1, sX - 1);
			} else {
				if(sX != 7) 
					squareFrontEast = board.getSquare(sY + 1, sX + 1);
				if(sX > 0)
					squareFrontWest = board.getSquare(sY + 1, sX - 1);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		//If the FrontEast and FrontWest squares are on the board, and occupied by a piece of the opposite color as the pawn, add that as a legal attack
		if(this.isWhite()) {
			if(squareFrontEast != null && squareFrontEast.isOccupied() && squareFrontEast.isPieceBlack())
				legalAttacks.add(squareFrontEast);
			if(squareFrontWest != null && squareFrontWest.isOccupied() && squareFrontWest.isPieceBlack())
				legalAttacks.add(squareFrontWest);
		} else {
			if(squareFrontEast != null && squareFrontEast.isOccupied() && squareFrontEast.isPieceWhite())
				legalAttacks.add(squareFrontEast);
			if(squareFrontWest != null && squareFrontWest.isOccupied() && squareFrontWest.isPieceWhite())
				legalAttacks.add(squareFrontWest);
		}
		
		//If the pawn can perform EnPassent add the corresponding move based on whether the pawn being checked is white or black
		if(canEnPassent(board, startSquare)) {
			Move lastMove = board.getLastMove();
			Square prevEnd = lastMove.getEndSquare();
			if(this.isWhite()) {
				legalAttacks.add(board.getSquare(prevEnd.getRank()-1,prevEnd.getFile()));
			} else {
				legalAttacks.add(board.getSquare(prevEnd.getRank()+1,prevEnd.getFile()));
			}
		}
		
		return legalAttacks;
	}
}
