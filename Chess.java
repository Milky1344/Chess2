import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Chess extends JFrame{
	//GUI Elements
	private JPanel game;
	private Font font;
	
	private JPanel white;
	private String whiteName;
	private JLabel whiteLabel;
	private TakenPieces takenBlackPieces;
	
	private JPanel black;
	private String blackName;
	private JLabel blackLabel;
	private TakenPieces takenWhitePieces;

	static final Color GREY = new Color(49,46,43);
	private static final Color DARK_GREY = new Color(39,37,34);
	
	//Game Elements
	private Board board;
	private Square startSquare, endSquare;
	private Piece startPiece, endPiece;
	private int x1,y1,x2,y2;
	private int rank1,file1,rank2,file2;
	private GameState gameState = GameState.SELECTING_PIECE;
	private boolean whiteTurn = true;
	
	
	public Chess(){
		super("Chess");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.LEADING));
		addMouseListener(mouseAdapter);
		setResizable(false);
		
		int choice = welcome();
		
		whiteName = JOptionPane.showInputDialog(null, "Enter player 1 name: ", "White");
		blackName = JOptionPane.showInputDialog(null, "Enter player 2 name: ", "Black");

		board = new Board();
		font = new Font("Arial", Font.BOLD, 18);
		
		takenBlackPieces = new TakenPieces(false);
		
		whiteLabel = new JLabel(whiteName);
		whiteLabel.setFont(font);
		whiteLabel.setForeground(Color.white);
		whiteLabel.setPreferredSize(new Dimension(100,50));
		
		white = new JPanel();
		white.setBackground(GREY);
		white.setPreferredSize(new Dimension(512,60));
		white.setLayout(new FlowLayout(FlowLayout.LEADING));
		white.add(whiteLabel);
		white.add(takenBlackPieces);
		
		takenWhitePieces = new TakenPieces(true);
		
		blackLabel = new JLabel(blackName);
		blackLabel.setFont(font);
		blackLabel.setForeground(Color.white);
		blackLabel.setPreferredSize(new Dimension(100,50));
		
		black = new JPanel();
		black.setBackground(GREY);
		black.setPreferredSize(new Dimension(512,60));
		black.setLayout(new FlowLayout(FlowLayout.LEADING));
		black.add(blackLabel);
		black.add(takenWhitePieces);
		
		game = new JPanel();
		game.setBackground(GREY);
		game.setLayout(new BorderLayout());
		game.add(black, BorderLayout.NORTH);
		game.add(board);
		game.add(white, BorderLayout.SOUTH);

		add(game);
		pack();
		setLocationRelativeTo(null);
		getContentPane().setBackground(GREY);
		
		if(choice == 0)
			setVisible(true);
		else if(choice == 1) {
			ChessTwo chess = new ChessTwo();
		}else 
			rules();
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			switch (gameState) {
				case SELECTING_PIECE:
					x1 = e.getX() - 12;
					y1 = e.getY() - 94;
					if(x1 >= 0 && x1 < 512 && y1 >= 0 && y1 < 512) {
						rank1 = (int) (y1 / 64.0);
						file1 = (int) (x1 / 64.0);
						startSquare = board.getSquare(rank1,file1);
						
						
						if(startSquare.isOccupied()) {
							startPiece = startSquare.getPiece();
							
							if(startSquare.isPieceWhite() && whiteTurn) {
								selectedSquare();
								gameState = GameState.SELECTING_MOVE;
								break;
							} else if(startSquare.isPieceBlack() && !whiteTurn) {
								selectedSquare();
								gameState = GameState.SELECTING_MOVE;
								break;
							}
						}
					}
					break;
			
				case SELECTING_MOVE:
					x2 = e.getX() - 12;
					y2 = e.getY() - 94;
					
					if(x2 >= 0 && x2 < 512 && y2 >= 0 && y2 < 512) {
							rank2 = (int) (y2 / 64.0);
							file2 = (int) (x2 / 64.0);
							endSquare = board.getSquare(rank2,file2);
							endPiece = endSquare.getPiece();
							
							if(endSquare == startSquare) {
								deselect();
								break;
							}
							
							if(endSquare.isOccupied()) {
								if(whiteTurn && endSquare.isPieceWhite()) {
									reselect();
									break;
								} else if(!whiteTurn && endSquare.isPieceBlack()){
									reselect();
									break;
								}
							}
							
							if(board.isValid(endSquare)) {
								
								if(endSquare.isOccupied()) {
									if(whiteTurn) {
										takenBlackPieces.add(endPiece);
									} else {
										takenWhitePieces.add(endPiece);
									}
								}
								
								board.move(startSquare, endSquare);
								whiteTurn = !whiteTurn;
								gameState = GameState.SELECTING_PIECE;
								break;
							}
					}
					break;
					
				default:
					System.out.println("Unsure");
					break;
			}
			
			if(board.checkmate(whiteTurn)) {
				gameState = GameState.GAME_OVER;
				if(whiteTurn) {
					JOptionPane.showMessageDialog(null, blackName + " checkmated " + whiteName);
					System.exit(0);
				} else {
					JOptionPane.showMessageDialog(null, whiteName + " checkmated " + blackName);
					System.exit(0);
				}
			} else if (board.noMoveStalemate(whiteTurn) || board.noFPowerStalemate()) {
				gameState = GameState.STALEMATE;
				JOptionPane.showMessageDialog(null, "Stalemate");
				System.exit(0);
			}
		}
	};
	
	private void selectedSquare() {
		System.out.println("Selected: " + startSquare.getRank() + "," + startSquare.getFile() + " contains: " + startPiece + " has not moved: " + startPiece.hasNotMoved());
		
		board.setStartSquare(startSquare);
		board.setLegalMoves(startPiece.getLegal(board, startSquare));
		board.repaint();
	}
	
	private void reselect() {
		startSquare = endSquare;
		startPiece = endPiece;
		selectedSquare();
	}
	
	private void deselect() {
		System.out.println("Reselect a new piece");
		
		startSquare = null;
		startPiece = null;
		board.setStartSquare(null);
		board.setLegalMoves(null);
		gameState = GameState.SELECTING_PIECE;
		board.repaint();
	}
	
	public static int welcome() {
		String[] options = new String[] { "Chess", "Chess 2", "Rules" };
		int choice = 0;
		choice = JOptionPane.showOptionDialog(null, "Welcome to Chess", "Chess", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);
		return choice;
	}
	
	public void rules() {
		String[] options = new String[] {"Understood"};
		JOptionPane.showOptionDialog(null, 
				"Basic Rules\n"
				+ "In chess, each player takes turns to make a single move. Players cannot\n"
				+ "choose to skip a turn, and must move a piece. Each piece moves in a\n"
				+ "specific way. Excluding the knight, pieces are unable to move through\n"
				+ "pieces of any color. Pieces capture enemy pieces by landing on them.\n"
				+ "\nWin/Loss/Stalemate\n"
				+ "When a piece moves in a way that would allow a player to capture the \n"
				+ "opponent's king on their next turn, it is considered a check. If a player\n"
				+ "is in check, they can take the block the attack, take the attacking piece,\n"
				+ "and/or move their king to a safe spot if legal. If none of the 3 options\n"
				+ "above are legal then that player has been checkmated and loses, while the\n"
				+ "attacking player wins. Currently, there are two functioning stalemate \n"
				+ "conditions, if a player cannot make any legal moves and/or  both player's\n"
				+ "pieces consists of either: a long king, king & bishop, or king & knight.\n"
				+ "\nSpecial Moves\n"
				+ "Promotion of a Pawn: if a Pawn reaches the opponent's side of the board\n"
				+ "the pawn will be exchanged for another piece that isn't a pawn or king.\n"
				+ "Castling: if a player's king and a rook haven't moved during the game,\n"
				+ "there are no pieces in between the king & rook, and none of the squares\n"
				+ "would result in a check on the king, then the king and rook switch places\n",
				"Rules", 
				JOptionPane.YES_OPTION,JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		setVisible(true);
	}
	
	public static void main(String[] args){
		Chess chess = new Chess();
	}
	
}
