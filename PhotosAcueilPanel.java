package projetJava;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PhotosAcueilPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel l;

	public PhotosAcueilPanel(){
		this.setSize((int)getToolkit().getScreenSize().getWidth(),220);
		ImageIcon im=new ImageIcon(getClass().getResource("/images/photospanel.PNG"));
	    l=new JLabel(im);
	    this.add(l);
		
	}
}
