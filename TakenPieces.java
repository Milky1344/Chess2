import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;


public class TakenPieces extends JPanel{
	private static final File BLACK_PIECES_ICONS_FILE = new File("blackPiecesIcons");
	private static final File WHITE_PIECES_ICONS_FILE = new File("whitePiecesIcons");
	private static final File[] BLACK_PIECES_ICONS = BLACK_PIECES_ICONS_FILE.listFiles();
	private static final File[] WHITE_PIECES_ICONS = WHITE_PIECES_ICONS_FILE.listFiles();
	
	private static ImageIcon BLACK_PAWN;
	private static ImageIcon BLACK_BISHOP;
	private static ImageIcon BLACK_KNIGHT;
	private static ImageIcon BLACK_ROOK;
	private static ImageIcon BLACK_QUEEN;
	
	private static ImageIcon WHITE_PAWN;
	private static ImageIcon WHITE_BISHOP;
	private static ImageIcon WHITE_KNIGHT;
	private static ImageIcon WHITE_ROOK;
	private static ImageIcon WHITE_QUEEN;
	
	private ArrayList<Piece> takenPieces;
	private boolean containsWhitePieces;
	
	public TakenPieces(boolean containsWhitePieces){
		setPreferredSize(new Dimension (402,50));
		setBackground(Chess.GREY);
		takenPieces = new ArrayList<Piece>();
		this.containsWhitePieces = containsWhitePieces;
		Image icon = null;
		if(containsWhitePieces) {
			for(File file : WHITE_PIECES_ICONS) {
				try {
					icon = ImageIO.read(file);
				} catch (IOException e) {
				}
				if(file.getName().indexOf("Pawn") != -1)
					WHITE_PAWN = new ImageIcon(icon);
				else if(file.getName().indexOf("Bishop") != -1)
					WHITE_BISHOP = new ImageIcon(icon);
				else if(file.getName().indexOf("Knight") != -1)
					WHITE_KNIGHT = new ImageIcon(icon);
				else if(file.getName().indexOf("Rook") != -1)
					WHITE_ROOK = new ImageIcon(icon);
				else if(file.getName().indexOf("Queen") != -1)
					WHITE_QUEEN = new ImageIcon(icon);
			}
		} else {
			for(File file : BLACK_PIECES_ICONS) {
				try {
					icon = ImageIO.read(file);
				} catch (IOException e) {
				}
				if(file.getName().indexOf("Pawn") != -1)
					BLACK_PAWN = new ImageIcon(icon);
				else if(file.getName().indexOf("Bishop") != -1)
					BLACK_BISHOP = new ImageIcon(icon);
				else if(file.getName().indexOf("Knight") != -1)
					BLACK_KNIGHT = new ImageIcon(icon);
				else if(file.getName().indexOf("Rook") != -1)
					BLACK_ROOK = new ImageIcon(icon);
				else if(file.getName().indexOf("Queen") != -1)
					BLACK_QUEEN = new ImageIcon(icon);
			}
		}
		repaint();
	}
	
	public void add(Piece piece) {
		this.takenPieces.add(piece);
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int pawnStartPos = 0;
		int bishopStartPos = 120;
		int knightStartPos = 180;
		int rookStartPos = 240;
		int queenStartPos = 300;
		if(this.containsWhitePieces) {
			for(Piece p : takenPieces) {
				if(p instanceof Pawn) {
					WHITE_PAWN.paintIcon(this,g,pawnStartPos,9);
					pawnStartPos += 10;
				} else if(p instanceof Bishop) {
					WHITE_BISHOP.paintIcon(this,g,bishopStartPos,9);
					bishopStartPos += 10;
				} else if(p instanceof Knight) {
					WHITE_KNIGHT.paintIcon(this,g,knightStartPos,9);
					knightStartPos += 10;
				} else if(p instanceof Rook) {
					WHITE_ROOK.paintIcon(this,g,rookStartPos,9);
					rookStartPos += 10;
				} else if(p instanceof Queen) {
					WHITE_QUEEN.paintIcon(this,g,queenStartPos,9);
					queenStartPos += 10;
				}
				
			}
		} else {
			for(Piece p : takenPieces) {
				if(p instanceof Pawn) {
					BLACK_PAWN.paintIcon(this,g,pawnStartPos,9);
					pawnStartPos += 10;
				} else if(p instanceof Bishop) {
					BLACK_BISHOP.paintIcon(this,g,bishopStartPos,9);
					bishopStartPos += 10;
				} else if(p instanceof Knight) {
					BLACK_KNIGHT.paintIcon(this,g,knightStartPos,9);
					knightStartPos += 10;
				} else if(p instanceof Rook) {
					BLACK_ROOK.paintIcon(this,g,rookStartPos,9);
					rookStartPos += 10;
				} else if(p instanceof Queen) {
					BLACK_QUEEN.paintIcon(this,g,queenStartPos,9);
					queenStartPos += 10;
				}
				
			}
		}
	}
}
