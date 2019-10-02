package projetJava;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;


public class GuiAbout extends JFrame{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		JTextArea l;
	 
	public GuiAbout(){
		this.setTitle("CoWork");
		this.setIconImage((new ImageIcon(this.getClass().getResource("/images/Icon.png"))).getImage());
		this.setSize(710,150);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		JLabel background=new JLabel((new ImageIcon(this.getClass().getResource("/images/backGround2.jpg"))));
		background.setLayout(new BorderLayout());
	    l=new JTextArea("\n                              Cowork est un espace de travail collaboratif permettant de rassembler"
	    			  + "\n                       des acteurs autour du centre d’intérêt \" gestion de projet \".Les membres de cet "
	    			  + "\n                         espace disposent d’outils de communication pratiques et faciles à utiliser pour "
	    			  + "\n                             s’informer, échanger des fichiers et discuter \" envoi de messages \".");
	    l.setEditable(false);
	    l.setFont(new Font("comic sans ms", Font.BOLD, 12));;
	    l.setOpaque(false);
	    //l.setBackground(new Color(245,245,245));
	    background.add(l);
	    this.getContentPane().add(background);
	}

}
