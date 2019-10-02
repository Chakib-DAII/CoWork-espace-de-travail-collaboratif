package projetJava;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;


public class Test {
	static GuiLauncher launcher;
	public static void main(String[] args) {
		launcher=new GuiLauncher();
		launcher.setVisible(true);//On la rend visible
		for(int i=0;i<=100;i++){
		final int percent = i;
		try { 
			SwingUtilities.invokeLater(new Runnable() {
         public void run() {
        	 launcher.progressBar.setValue(percent);
         }
			}); 
	Thread.sleep(50);
		}catch (InterruptedException e) {}		
		}
		launcher.setVisible(false);
		GuiAuth m=new GuiAuth() ;
		m.setVisible(true);

	}

}
