import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ChessTwo extends JFrame{
	
	private JLabel chessTwo;

	public ChessTwo() {
		super("Please note this is a massive joke");
		setSize(400,400);
		
		chessTwo = new JLabel();
		chessTwo.setIcon(new ImageIcon("chess2.png"));
		
		add(chessTwo);
		setResizable(false);
		setVisible(true);
	}
}
