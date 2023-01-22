import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

public class Board extends JPanel{
	private Square[][] squares;
	private Square startSquare = null;
	private ArrayList<Piece> whitePieces;
	private ArrayList<Piece> blackPieces;
	private ArrayList<Square> legalMoves;
	private static final Color GREY = new Color(49,46,43);
	private static final Color GREY_CIRCLE = new Color(0,0,0,40);
	private static final Color RED_CIRCLE = new Color(240,50,55,125);
	private static final Color TAN = new Color(118,150,86);
	private static final Color GREEN = new Color(238,238,210);
	private static final Color YELLOW = new Color(230,230,120,125);
	private Square wKing;
	private Square bKing;
	private Move lastMove = null;
	
	public Square getWKing() {
		return this.wKing;
	}
	
	public void setWKing(Square wKing) {
		this.wKing = wKing;
	}
	
	public Square getBKing() {
		return this.bKing;
	}
	public void setBKing(Square bKing) {
		this.bKing = bKing;
	}
	
	public Move getLastMove() {
		return this.lastMove;
	}
	
	public Board() {
		squares = new Square[8][8];
		legalMoves = new ArrayList<Square>();
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
		
		initializeGrid();
		//testPieceGrid();
		
		setUpArrays();
		genAttacks();
		
		setPreferredSize(new Dimension(512,512)); 

		setBackground(GREY);
        setVisible(true);
        repaint();
	}
	
	public Square findPiece(Piece p) {
		for(int r = 0; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				if(squares[r][c].isOccupied()) {
					if(p == squares[r][c].getPiece()) {
						return squares[r][c];
					}
				}
			}
		}
		return null;
	}
	
	public boolean isKingChecked(boolean whiteTurn) {
		if(whiteTurn && wKing.getAtkBPieces() > 0) {
			return true;
		}
		else if(!whiteTurn && bKing.getAtkWPieces() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean noMoveStalemate(boolean whiteTurn) {
		ArrayList<Square> moves = new ArrayList<Square>();
		if(whiteTurn) {
			for(Piece p : whitePieces) {
				moves.addAll(p.getLegal(this,findPiece(p)));
			}
		} else {
			for(Piece p : blackPieces) {
				moves.addAll(p.getLegal(this,findPiece(p)));
			}
		}
		
		if(moves.size() == 0)
			return true;
		
		return false;
	}
	
	public boolean noFPowerStalemate() {
		int whiteFP = whitePieces.size();
		int blackFP = blackPieces.size();
		boolean whiteSufficient = false;
		boolean blackSufficient = false;
		
		if(whiteFP <= 2 && blackFP <= 2) {
			for(Piece p : whitePieces) {
				System.out.println(p);
				if(p instanceof Queen || p instanceof Rook || p instanceof Pawn) {
					whiteSufficient = true;
					break;
				}
			}
			
			for(Piece p : blackPieces) {
				System.out.println(p);
				if(p instanceof Queen || p instanceof Rook || p instanceof Pawn) {
					blackSufficient = true;
					break;
				}
			}
		}
		
		return whiteSufficient || blackSufficient;
	}
		
	public boolean checkmate(boolean whiteTurn) {
		ArrayList<Square> kingMoves = new ArrayList<Square>();
		ArrayList<Square> pieceMoves = new ArrayList<Square>();
		if(isKingChecked(whiteTurn)) {
			if(whiteTurn) {
				System.out.println("White king in check");
				kingMoves.addAll(wKing.getPiece().getLegal(this, wKing));
				
				for(Piece p : whitePieces) {
					pieceMoves.addAll(p.getLegal(this, findPiece(p)));
				}
				
				if(kingMoves.size() == 0 && pieceMoves.size() == 0) {
					return true;
				}
			} else {
				System.out.println("Black king in check");
				kingMoves.addAll(bKing.getPiece().getLegal(this, bKing));
				
				for(Piece p : blackPieces) {
					pieceMoves.addAll(p.getLegal(this, findPiece(p)));
				}
				
				if(kingMoves.size() == 0 && pieceMoves.size() == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean performPseudoMove(Board board, Square startSquare, Square endSquare) {
		Piece start = startSquare.getPiece();
		Piece end = endSquare.getPiece();
		boolean result = true;

		removePiece(endSquare.getPiece());
		endSquare.setPiece(null);
		endSquare.setPiece(start);
		startSquare.setPiece(null);
		
		genAttacks();
		
		if(start instanceof King) {
			if(start.isWhite()) {
				setWKing(endSquare);
			} else {
				setBKing(endSquare);
			}
		}
			
		if(isKingChecked(start.isWhite())) {
			result = false;
		} else {
			result = true;
		}
		
		if(start instanceof King) {
			if(start.isWhite()) {
				setWKing(startSquare);
			} else {
				setBKing(startSquare);
			}
		}
		
		startSquare.setPiece(start);
		endSquare.setPiece(end);
		addPiece(end);
		genAttacks();
		return result;
	}
	
	public void setLegalMoves(ArrayList<Square> list) {
		this.legalMoves = list;
	}
	
	public void setStartSquare(Square square) {
		this.startSquare = square;
	}
	
	private void initializeGrid() {
		for(int r = 0; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				squares[r][c] = new Square(r,c);
			}
		}
		
		for(int c = 0; c < 8; c++) {
			squares[6][c].setPiece(new Pawn(true));
		}
		squares[7][0].setPiece(new Rook(true));
		squares[7][1].setPiece(new Knight(true));
		squares[7][2].setPiece(new Bishop(true));
		squares[7][3].setPiece(new Queen(true));
		squares[7][4].setPiece(new King(true));
		squares[7][5].setPiece(new Bishop(true));
		squares[7][6].setPiece(new Knight(true));
		squares[7][7].setPiece(new Rook(true));
		wKing = squares[7][4];
		
		for(int c = 0; c < 8; c++) {
			squares[1][c].setPiece(new Pawn(false));
		}
		squares[0][0].setPiece(new Rook(false));
		squares[0][1].setPiece(new Knight(false));
		squares[0][2].setPiece(new Bishop(false));
		squares[0][3].setPiece(new Queen(false));
		squares[0][4].setPiece(new King(false));
		squares[0][5].setPiece(new Bishop(false));
		squares[0][6].setPiece(new Knight(false));
		squares[0][7].setPiece(new Rook(false));
		bKing = squares[0][4];
	}

	private void setUpArrays() {
		Piece temp;
		for(int r = 0; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				temp = squares[r][c].getPiece();
				
				if(temp != null) {
					if(temp.isWhite())
						whitePieces.add(temp);
					else
						blackPieces.add(temp);
				}
			}
		}
	}
	
	public void removePiece(Piece piece) {
		if(piece != null) {
			if(piece.isWhite()) 
				whitePieces.remove(piece);
			else 
				blackPieces.remove(piece);
		}
	}
	
	public void addPiece(Piece piece) {
		if(piece != null) {
			if(piece.isWhite()) 
				whitePieces.add(piece);
			else 
				blackPieces.add(piece);
		}
	}

	public void resetAttacks() {
		for(int r = 0; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				squares[r][c].rmovAtkWPieces();
				squares[r][c].rmovAtkBPieces();
			}
		}
	}
	
	public void genAttacks() {
		resetAttacks();
		ArrayList<Square> whiteAttacks = new ArrayList<Square>();
		for(Piece p : whitePieces) {
			if(p instanceof Pawn) {
				Square s = findPiece(p);
				try {
					if(s.getFile() == 0) 
						whiteAttacks.add(squares[s.getRank()-1][1]);
					else if (s.getFile() == 7) 
						whiteAttacks.add(squares[s.getRank()-1][6]);
					else {
						whiteAttacks.add(squares[s.getRank()-1][s.getFile()-1]);
						whiteAttacks.add(squares[s.getRank()-1][s.getFile()+1]);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			} else
				whiteAttacks.addAll(p.getAttacks(this,findPiece(p)));
		}
		
		for(Square s: whiteAttacks) {
			squares[s.getRank()][s.getFile()].addAtkWPieces();
		}
		
		ArrayList<Square> blackAttacks = new ArrayList<Square>();
		for(Piece p : blackPieces) {
			if(p instanceof Pawn) {
				Square s = findPiece(p);
				try {
					if(s.getFile() == 0)
						blackAttacks.add(squares[s.getRank()+1][1]);
					else if (s.getFile() == 7) 
						blackAttacks.add(squares[s.getRank()+1][6]);
					else {
						blackAttacks.add(squares[s.getRank()+1][s.getFile()-1]);
						blackAttacks.add(squares[s.getRank()+1][s.getFile()+1]);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			} else
				blackAttacks.addAll(p.getAttacks(this,findPiece(p)));
		}
		
		for(Square s: blackAttacks) {
			squares[s.getRank()][s.getFile()].addAtkBPieces();
		}
	}

	public boolean isValid(Square end) {
		if(legalMoves != null) {
			for(int i = 0; i < legalMoves.size(); i++) {
				for(Square s : legalMoves) {
					if(s == end) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void move(Square startSquare, Square endSquare) {
		/*
		if(endSquare.isOccupied()) 
			System.out.println("Captured: " + endSquare.getRank() + "," + endSquare.getFile() + " contains: " + endSquare.getPiece());
		else 
			System.out.println("Moved to: " + endSquare.getRank() + "," + endSquare.getFile());
			*/
		
		//If the piece that is being moved is a king update the board's detection on where the king is & check if it castled
		if(startSquare.getPiece() instanceof King) {
			if(startSquare.isPieceWhite()) 
				this.wKing = endSquare;
			else
				this.bKing = endSquare;
			
			if(((King) startSquare.getPiece()).castled(this, startSquare, endSquare))
				this.castlingMove(startSquare, endSquare);
		}
		
		//If the piece that is being moved is a pawn check if it can promote and if it made an en passent move
		//If either are true do the 
		if(startSquare.getPiece() instanceof Pawn) {
			if(((Pawn) startSquare.getPiece()).canEnPassent(this, startSquare))
				this.enPassentMove(startSquare, endSquare);
			
			((Pawn) startSquare.getPiece()).canPromote(this, startSquare, endSquare);
		}
		
		lastMove = new Move(startSquare, endSquare, startSquare.isPieceWhite());
		
		startSquare.getPiece().moved();
		removePiece(endSquare.getPiece());
		endSquare.setPiece(null);
		endSquare.setPiece(startSquare.getPiece());
		
		
		startSquare.setPiece(null);
		setLegalMoves(null);
		
		genAttacks();
		repaint();
	}
	
	public void castlingMove(Square start, Square end) {
		int sY = start.getRank();
		int sX = start.getFile();
		int eX = end.getFile();
		if(eX == sX + 2) {
			squares[sY][sX + 1].setPiece(squares[sY][sX+3].getPiece());
			squares[sY][sX + 1].getPiece().moved();
			squares[sY][sX+3].setPiece(null);
		} else if (eX == sX - 2) {
			squares[sY][sX - 1].setPiece(squares[sY][sX-4].getPiece());
			squares[sY][sX - 1].getPiece().moved();
			squares[sY][sX-4].setPiece(null);
		}
	}
	
	public void enPassentMove(Square start, Square end) {
		int sY = start.getRank();
		int sX = start.getFile();
		int eX = end.getFile();
		int eY = end.getRank();
		if(Math.abs(lastMove.getEndSquare().getRank() - lastMove.getStartSquare().getRank()) == 2) {
			System.out.println("Last move was a double move");
			if(start.isPieceWhite()) {
				if(eY == sY - 1 && (eX == sX + 1 || eX == sX - 1)) {
					if(squares[sY][eX].isPieceBlack() && squares[sY][eX].getPiece() instanceof Pawn) {
						removePiece(squares[sY][eX].getPiece());
						squares[sY][eX].setPiece(null);
					}
				}
			} else {
				if(eY == sY + 1 && (eX == sX + 1 || eX == sX - 1)) {
					if(squares[sY][eX].isPieceWhite() && squares[sY][eX].getPiece() instanceof Pawn) {
						removePiece(squares[sY][eX].getPiece());
						squares[sY][eX].setPiece(null);
					}
				}
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		boolean flip = true;
		//prints the board;
		for(int r = 0; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				if(flip) {
					g.setColor(GREEN);
				} else {
					g.setColor(TAN);
				}
				g.fillRect(r*64,c*64,64,64);
				flip = !flip;
			}
			flip = !flip;
		}
		
		
		if(startSquare != null) {
			int y = startSquare.getRank();
			int x = startSquare.getFile();
			g.setColor(YELLOW);
			g.fillRect(x*64,y*64,64,64);
		}
		
		for(int r = 0; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				if(squares[r][c].isOccupied()) {
					squares[r][c].getPiece().print(this, g, c*64, r*64);
				}
			}
		}
		
		if(legalMoves != null) {
			for(Square s: legalMoves) {
				int x = s.getFile();
				int y = s.getRank();
				if(s.getPiece() != null) {
					if(startSquare.isPieceWhite() && s.isPieceBlack() || startSquare.isPieceBlack() && s.isPieceWhite()) 
						g.setColor(RED_CIRCLE);
				} else 
					g.setColor(GREY_CIRCLE);
				
				g.fillOval(x*64 + 20 ,y*64 + 20, 24, 24);
			}
		}
	}

	public Square getSquare(int rank, int file) {
		return squares[rank][file];
	}
}
