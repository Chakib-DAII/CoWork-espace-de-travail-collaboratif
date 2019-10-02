package projetJava;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

public class GuiLauncher extends JWindow{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JProgressBar progressBar;
	 JLabel l;
	 
	public GuiLauncher(){
		this.setSize(600, 250);
		this.setLocationRelativeTo(null);
		progressBar = new JProgressBar();
		progressBar.setForeground(Color.GREEN);
		progressBar.setBorderPainted(false);
		//progressBar.setStringPainted(true);
		this.setLayout(new BorderLayout());
		this.add(progressBar,BorderLayout.SOUTH);
	    ImageIcon im=new ImageIcon(getClass().getResource("/images/Launcher.jpg"));
	    l=new JLabel(im);
	    this.add(l);
	    
	}

}
