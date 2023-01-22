import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PlayedMoves extends JPanel{
	
	private ArrayList<Move> playedMoves = new ArrayList<Move>();
	private JList list;

	public PlayedMoves() {
		setLayout(new GridLayout(50,3));
		setPreferredSize(new Dimension(200,622));
		setBackground(Color.blue);
		repaint();
	}

	public void addMove(Move lastMove) {
		playedMoves.add(lastMove);
		repaint();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(Move move : playedMoves) {
			add(new JButton("Hi"));
		}
	}
}
