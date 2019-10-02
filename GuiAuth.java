package projetJava;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

//import projetJava.GuiBase;
//import projetJava.Membre;
//import projetJava.TrayIconDemo;

public class GuiAuth extends JFrame implements ActionListener,KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel idMem,pswMem;
	JTextField tId;
	JPasswordField tPsw;
	JButton connect;
	JPanel auth;
    JLabel err;
    Container c;
	JLabel imgpnl;
	GuiAdmin fenadmin;
	GuiEmp fenemp;
	public GuiAuth(){
		this.setTitle("CoWork");
		this.setIconImage((new ImageIcon(this.getClass().getResource("/images/Icon.png"))).getImage());
		this.setSize(600,290);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent arg0) {
				//ConnectionDataBase.closeStatement();
				ConnectionDataBase.deconnection();
				GuiBase.deconnection();
			}
			public void windowClosed(WindowEvent arg0) {
				//ConnectionDataBase.closeStatement();
				ConnectionDataBase.deconnection();
				GuiBase.deconnection();
			}
		});
		TrayIconDemo.openTray();
		
	auth=new JPanel();
	auth.setSize((int)getToolkit().getScreenSize().getWidth(), 50);
	idMem = new JLabel("ID :");
	tId = new JTextField("Donner votre ID ",15);
	tId.addKeyListener(this);
	pswMem = new JLabel("      mot de passe :");
	tPsw = new JPasswordField(15);
	tPsw.addKeyListener(this);
	connect= new JButton("connecter");
	connect.setOpaque(false);
	connect.addActionListener(this);
	err = new JLabel();
	JLabel background=new JLabel((new ImageIcon(this.getClass().getResource("/images/backGround.jpg"))));
	background.setLayout(new BorderLayout());
	auth.add(idMem);
	auth.add(tId);
	auth.add(pswMem);
	auth.add(tPsw);
	auth.add(connect);
	auth.setOpaque(false);
	//auth.add(err);
	c=this.getContentPane();
	background.add(auth,BorderLayout.NORTH);
    ImageIcon im=new ImageIcon(getClass().getResource("/images/Launcher.jpg"));
    imgpnl=new JLabel(im);
    background.add(imgpnl,BorderLayout.CENTER);
    c.add(background);
    tId.addFocusListener(new FocusListener() {
		
		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			if(tId.getText().equals("")||tId.getText().equals("Donner votre ID "))
				tId.setText("Donner votre ID ");
		}
		
		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			if(tId.getText().equals("")||tId.getText().equals("Donner votre ID "))
				tId.setText("");
		}
	});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==connect)
		{

			GuiBase.connect();
			try {
				GuiBase.select("select * from emp where id="+tId.getText()+";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try { 
				boolean t=GuiBase.rs.next();
				if(t==false)
				{
					JOptionPane.showMessageDialog(auth,"id inxistant");
					System.out.println("id invalide");
				}
				else
				{
					//System.out.println("start");
					
					while(t)
					{//System.out.println("jamais entrée");

						if(Integer.parseInt(tId.getText())==GuiBase.rs.getInt(1) && tPsw.getText().equals(GuiBase.rs.getString(4)))
						{//System.out.println("existe");
							this.setVisible(false);
						//si admin
							if(Integer.parseInt(tId.getText())==1)
							{//System.out.println("admin");
							try {
								System.out.println(InetAddress.getLocalHost().getHostAddress());
								Membre admin=new Membre(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getString(3),InetAddress.getLocalHost().getHostAddress(),GuiBase.rs.getString(4));
								GuiBase.miseAjour("update emp set Ip='"+InetAddress.getLocalHost().getHostAddress()+"' where id="+GuiBase.rs.getInt(1)+";");
								fenadmin=new GuiAdmin(admin);
							fenadmin.setVisible(true);
	
							} catch (UnknownHostException ee) {
								// TODO Auto-generated catch block
								ee.printStackTrace();
							}
							}
							//si employé
							else{//System.out.println("employé");
								try{
								//System.out.println(InetAddress.getLocalHost().getHostAddress());
								Membre emp=new Membre(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getString(3),InetAddress.getLocalHost().getHostAddress(),GuiBase.rs.getString(4));
								GuiBase.miseAjour("update emp set Ip='"+InetAddress.getLocalHost().getHostAddress()+"' where id="+GuiBase.rs.getInt(1)+";");
								fenemp=new GuiEmp(emp);
								fenemp.setVisible(true);
								} catch (UnknownHostException ee) {
									// TODO Auto-generated catch block
									ee.printStackTrace();
								}
								fenemp.addProject(fenemp.modelPr);
								//fenemp.receive();
								//fenemp.refreshFileList();
								
								for(int i =0;i<fenemp.modelPr.size();i++)
								{
									String pseudo = (String)fenemp.modelPr.get(i);	

									boolean test=false;
									int c=0,x=0;
									while(test==false && c<fenemp.ALProjet.size())
									{
										if(fenemp.ALProjet.get(c).getTitre().equals(pseudo)){
											test=true;
											x=c;}
										c++;
									}
									Project p =(Project)fenemp.ALProjet.get(x);

									if(fenemp.tabpan.indexOfTab(pseudo)==-1)
										fenemp.tabpan.add(new ProjectPanel(p,fenemp.employeur),pseudo);
								}
								fenemp.tabpan.setSelectedIndex(fenemp.tabpan.indexOfTab((String)fenemp.modelPr.get(0)));
							}
		
							t=false;
							System.out.println("connecter");
						}
						else							
						{JOptionPane.showMessageDialog(auth,"mot de passe incorrect");
						t=GuiBase.rs.next();}


					}
					//GuiBase.closeStatement();
					//GuiBase.deconnection();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if((e.getKeyCode()==KeyEvent.VK_ENTER)&&((!tId.getText().equals(""))||(!tId.getText().equals("Donner votre ID ")))&&(!tPsw.getText().equals("")))
		{

			GuiBase.connect();
			try {
				GuiBase.select("select * from emp where id="+tId.getText()+";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try { 
				boolean t=GuiBase.rs.next();
				if(t==false)
				{
					JOptionPane.showMessageDialog(auth,"id inxistant");
					System.out.println("id invalide");
				}
				else
				{
					//System.out.println("start");
					
					while(t)
					{//System.out.println("jamais entrée");

						if(Integer.parseInt(tId.getText())==GuiBase.rs.getInt(1) && tPsw.getText().equals(GuiBase.rs.getString(4)))
						{//System.out.println("existe");
							this.setVisible(false);
						//si admin
							if(Integer.parseInt(tId.getText())==1)
							{//System.out.println("admin");
							try {
								System.out.println(InetAddress.getLocalHost().getHostAddress());
								Membre admin=new Membre(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getString(3),InetAddress.getLocalHost().getHostAddress(),GuiBase.rs.getString(4));
								GuiBase.miseAjour("update emp set Ip='"+InetAddress.getLocalHost().getHostAddress()+"' where id="+GuiBase.rs.getInt(1)+";");
								fenadmin=new GuiAdmin(admin);
							fenadmin.setVisible(true);
	
							} catch (UnknownHostException ee) {
								// TODO Auto-generated catch block
								ee.printStackTrace();
							}
							}
							//si employé
							else{//System.out.println("employé");
								try{
								//System.out.println(InetAddress.getLocalHost().getHostAddress());
								Membre emp=new Membre(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getString(3),InetAddress.getLocalHost().getHostAddress(),GuiBase.rs.getString(4));
								GuiBase.miseAjour("update emp set Ip='"+InetAddress.getLocalHost().getHostAddress()+"' where id="+GuiBase.rs.getInt(1)+";");
								fenemp=new GuiEmp(emp);
								fenemp.setVisible(true);
								} catch (UnknownHostException ee) {
									// TODO Auto-generated catch block
									ee.printStackTrace();
								}
								fenemp.addProject(fenemp.modelPr);
								//fenemp.receive();
								//fenemp.refreshFileList();
								
								for(int i =0;i<fenemp.modelPr.size();i++)
								{
									String pseudo = (String)fenemp.modelPr.get(i);	

									boolean test=false;
									int c=0,x=0;
									while(test==false && c<fenemp.ALProjet.size())
									{
										if(fenemp.ALProjet.get(c).getTitre().equals(pseudo)){
											test=true;
											x=c;}
										c++;
									}
									Project p =(Project)fenemp.ALProjet.get(x);

									if(fenemp.tabpan.indexOfTab(pseudo)==-1)
										fenemp.tabpan.add(new ProjectPanel(p,fenemp.employeur),pseudo);
								}
								fenemp.tabpan.setSelectedIndex(fenemp.tabpan.indexOfTab((String)fenemp.modelPr.get(0)));
							}
		
							t=false;
							System.out.println("connecter");
						}
						else							
						{JOptionPane.showMessageDialog(auth,"mot de passe incorrect");
						t=GuiBase.rs.next();}


					}
					//GuiBase.closeStatement();
					//GuiBase.deconnection();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
