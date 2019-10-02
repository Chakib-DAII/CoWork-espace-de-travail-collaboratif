package projetJava;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


public class GuiEmp extends JFrame implements ActionListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static private boolean StopListening = false;
	Membre employeur;
	Container container;
	static JList projectList;
	JLabel proLabel,help,idMem,pswMem;
	static JLabel err;
	JTextField tId;
	JPasswordField tPsw;
	static DefaultListModel modelPr;
	static JTabbedPane tabpan;
	JPanel projectPanel,authPanel;
	JButton connect;
	JList filesList;
	static JFrame auth;
    ArrayList<Project> ALProjet;
    String receivedfile;
    String sentence;
    String path;
    private JMenu fichierMenu, HelpMenu;
	private JMenuItem sauver, quitter,editerMenu,copier, couper, coller,about;
	private JMenuBar Jmenu;

	public GuiEmp(Membre e) throws SQLException {
		path = System.getProperty("user.home") + File.separator + "Documents/Cowork/"+e.getNom()+" "+e.getPrenom();
		File customDir = new File(path);
		if (!customDir.exists())customDir.mkdirs();
		this.employeur = new Membre(e.getIdM(),e.getNom(), e.getPrenom(), e.getIp(), e.getMot_passe());
		this.setTitle("CoWork : "+employeur.getNom()+" "+employeur.getPrenom());
		new Receive().start();
		new ReceiveFile().start();
		this.setSize(900, 720);
		this.setLocationRelativeTo(null);
		this.setIconImage((new ImageIcon(this.getClass().getResource("/images/Icon.png"))).getImage());
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		BarreMenu();
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent arg0) {
				//ConnectionDataBase.closeStatement();
				/*try {
					//GuiBase.connect();
					GuiBase.miseAjour("update emp set Ip= NULL where id="+employeur.getIdM()+";");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				ConnectionDataBase.deconnection();
				GuiBase.deconnection();
			}
			public void windowClosed(WindowEvent arg0) {
				//ConnectionDataBase.closeStatement();
				/*try {
				//GuiBase.connect();
				GuiBase.miseAjour("update emp set Ip= NULL where id="+employeur.getIdM()+";");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
				ConnectionDataBase.deconnection();
				GuiBase.deconnection();
			}
		});
		container = new Container();
		modelPr = new DefaultListModel<Project>();
		ALProjet = new ArrayList<Project>();
		

		tabpan = new JTabbedPane();
		tabpan.setOpaque(false);
		proLabel = new JLabel("Projets");
	
		projectList = new JList(){
			public String getToolTipText(MouseEvent e){
				int x=0;
				x=locationToIndex(e.getPoint());
				return (employeur.getNom()+" "+employeur.getPrenom().toUpperCase());
			}
		};
		projectList.setModel(modelPr);
		projectList.setBackground(new Color(245,245,245));
		projectList.addMouseListener(this);

		projectPanel = new JPanel();
		projectPanel.setLayout(new BorderLayout());
		projectPanel.add(new JScrollPane(projectList));
		projectPanel.add(new PhotosAcueilPanel(), BorderLayout.SOUTH);

		tabpan.add("Liste des Projets",projectPanel);

		proLabel.setHorizontalAlignment(JLabel.CENTER);
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) projectList.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);

		help = new JLabel("help");

		container = this.getContentPane();
		JLabel background=new JLabel((new ImageIcon(this.getClass().getResource("/images/backGround.jpg"))));
		background.setLayout(new BorderLayout());
		background.add(proLabel, BorderLayout.NORTH);
		background.add(tabpan, BorderLayout.CENTER);
		container.add(background);

	}

	public void addProject(DefaultListModel listM) throws SQLException
	{
		GuiBase.select("select Id,titre,date_deb from projet,emp_pro where "+this.employeur.getIdM()+"= idEmp and idPro=Id;");
		while(GuiBase.rs.next())
		{
			//System.out.println(GuiBase.rs.getString(1));
			Project p = new Project(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getDate(3));
			ALProjet.add(p);
			path = System.getProperty("user.home") + File.separator + "Documents/Cowork/"+employeur.getNom()+" "+employeur.getPrenom()+"/"+p.getTitre();
			File customDir = new File(path);
			if (!customDir.exists())customDir.mkdirs();
			listM.addElement(GuiBase.rs.getString(2));		
		}
	}
	
	public class Receive extends Thread {
			

			public void run() {
				try {
					int port=5000+employeur.getIdM();
					//System.out.println(port);
					DatagramSocket serverSocket = new DatagramSocket(port);
					while (true) {
						byte[] receiveData = new byte[1024];
						//System.out.println("waitListenTakeAction...");
						DatagramPacket receivePacket = new DatagramPacket(
								receiveData, receiveData.length);
						serverSocket.receive(receivePacket);
					    sentence = new String(receivePacket.getData());
						//System.out.println(tabpan.indexOfTab((sentence.split("/"))[0]));
						path=System.getProperty("user.home") + File.separator + "Documents/Cowork/"+employeur.getNom()+" "+employeur.getPrenom()+(sentence.split("/"))[0];
						ProjectPanel c=(ProjectPanel)tabpan.getComponentAt(tabpan.indexOfTab((sentence.split("/"))[0]));
						c.chattA.append('\n' + (sentence.split("/"))[1]);
						//System.out.println("RECEIVED: " + sentence);

						if (StopListening) {
							serverSocket.close();
							//System.out.println("StopListening");
							Thread.currentThread().interrupt();
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		
	}
	
	// a demarrer plutot
	public  class ReceiveFile extends Thread {
		ServerSocket ssock;

		public  void run() {
			
				int port=22000+employeur.getIdM();
				//System.out.println(port);
				//new File("C:/Users/Chakib/Desktop/projetJava/"+employeur.getNom()+" "+employeur.getPrenom()).mkdir();
				try {ssock = new ServerSocket(port, 1);	
				//System.out.println("Serveur lancé!");
				while(true){
					Socket csock =ssock.accept();
					//System.out.println("Une connexion entrante...\n\tRéception du fichier en cours...");
					//LECTURE DU SOCKET
					BufferedInputStream bis =new BufferedInputStream(csock.getInputStream());
					//creation rep
					//new File("C:/Users/Chakib/Desktop/projetJava/"+employeur.getNom()+" "+employeur.getPrenom()).mkdir();
					//ECRITURE DANS LE NOUVEAU FICHIER
					
					//GuiBase.connect();
					try {
						GuiBase.select("select * from sharedfile ORDER BY ID DESC LIMIT 1;");
						while(GuiBase.rs.next())
						{
							receivedfile=GuiBase.rs.getString(7);
						}
						//System.out.println(receivedfile);
						ProjectPanel c=(ProjectPanel)tabpan.getComponentAt(tabpan.indexOfTab(ALProjet.get(tabpan.getSelectedIndex()-1).getTitre()));
						c.modelFiles.addElement(receivedfile);
						c.filesList.setModel(c.modelFiles);
						path = System.getProperty("user.home") + File.separator + "Documents/Cowork/"+employeur.getNom()+" "+employeur.getPrenom()+"/"+ALProjet.get(tabpan.getSelectedIndex()-1).getTitre();//tabpan.indexOfTab((sentence.split("/"))[0]);
						//System.out.println(path);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
							//GuiBase.closeStatement();
							//GuiBase.deconnection();
					///
					BufferedOutputStream bos =new BufferedOutputStream(
											      new FileOutputStream(
											          new File(path+"/"+receivedfile)));//a verifier !! 
		 
					byte buf[] =new byte[4096];
					//RéCEPTION DU FICHIER
					while(bis.read(buf) !=-1){
						bos.write(buf);
						bos.flush();
					}
					//FERMETURE STREAM
					bos.close();
					bis.close();
					//ssock.close();
					csock.close();
					//System.out.println("Fichier réceptionné !\n\n");
				}	
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
					
				}

		}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
			if(e.getSource()==quitter){
				System.exit(0);
			}
		else //a faire !!! fenetre
			if(e.getSource()==about){
				GuiAbout fabout=new GuiAbout();
				fabout.setVisible(true);
				}
	    else //a faire !!! fenetre
			if(e.getSource()==sauver && !(((ProjectPanel)tabpan.getSelectedComponent()).chattA.getText().equals(""))){
				try{ProjectPanel c=(ProjectPanel)tabpan.getComponentAt(tabpan.indexOfTab(ALProjet.get(tabpan.getSelectedIndex()-1).getTitre()));
					path = System.getProperty("user.home") + File.separator + "Documents/Cowork/"+employeur.getNom()+" "+employeur.getPrenom()+"/"+ALProjet.get(tabpan.getSelectedIndex()-1).getTitre();//tabpan.indexOfTab((sentence.split("/"))[0]);
				
					File customDir = new File(path);
					if (!customDir.exists())customDir.mkdirs();
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date actuelle = new Date();
					String dat = dateFormat.format(actuelle);
					String NomConv="Conversation "+dat+" num "+(int)((Math.random())*10000)+".jpg";				
	            	File fichier=new File(path+"/"+NomConv+".txt"); 
	            	FileWriter fichierw=new FileWriter(fichier);
	            	String cSautLigne = System.getProperty("line.separator");
	            	fichierw.write(((ProjectPanel)tabpan.getSelectedComponent()).chattA.getText().replaceAll("\n\r|\n|\r", cSautLigne));
	            	fichierw.close(); 
	            	if (Desktop.isDesktopSupported())
	            	{
	            	 Desktop desktop = Desktop.getDesktop();
	            	  desktop.open(fichier);
	            	}
	            	} catch (Exception ec) {}
					}
	}
	public void refreshFileList(){
		new Thread(){
			public void run(){
				while(true){
					if(CoolAsHeck.thereIsNewFiles){
						//filesList
						//GuiEmp.this.filesList.setListData(CoolAsHeck.theSharedFiles);
						GuiEmp.this.repaint();
						CoolAsHeck.thereIsNewFiles=false;
					}
				}
			}
		}.start();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getClickCount()==2 && e.getSource()==projectList){
			int index = projectList.locationToIndex(e.getPoint());
			String pseudo = (String)modelPr.getElementAt(index);	

			boolean test=false;
			int i=0,x=0;
			while(test==false && i<ALProjet.size())
			{
				if(ALProjet.get(i).getTitre().equals(pseudo)){
					test=true;
					x=i;}
				i++;
			}
			Project p =(Project)ALProjet.get(x);

			if(tabpan.indexOfTab(pseudo)==-1)
				tabpan.add(new ProjectPanel(p,employeur),pseudo);
			tabpan.setSelectedIndex(tabpan.indexOfTab(pseudo));
		}	
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void BarreMenu()
	{// Création du menu Fichier
	    fichierMenu = new JMenu("Fichier");
	    sauver = new JMenuItem("Sauver conversation");
	    sauver.addActionListener(this);
	    fichierMenu.add(sauver);
	    fichierMenu.insertSeparator(2);
	    quitter = new JMenuItem("Quitter");
	    quitter.addActionListener(this);
	    fichierMenu.add(quitter);

	    // Création du menu Editer
	    editerMenu = new JMenu("Editer");
	    copier = new JMenuItem("Copier");
	    copier.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit()
	        .getMenuShortcutKeyMask(), false));
	    editerMenu.add(copier);
	    couper = new JMenuItem("Couper");
	    couper.setAccelerator(KeyStroke.getKeyStroke('X', Toolkit.getDefaultToolkit()
	        .getMenuShortcutKeyMask(), false));
	    editerMenu.add(couper);
	    coller = new JMenuItem("Coller");
	    coller.setAccelerator(KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit()
	        .getMenuShortcutKeyMask(), false));
	    editerMenu.add(coller);
	    
	    // Création du menu Help
	    HelpMenu = new JMenu("Help");
	    about = new JMenuItem("About", 'A');
	    about.addActionListener(this);
	    HelpMenu.add(about);
	    Jmenu=new JMenuBar();
	    // ajout des menus à la barre de menus
	    Jmenu.add(fichierMenu);
	    Jmenu .add(editerMenu);
	    Jmenu.add(HelpMenu);
	    this.setJMenuBar(Jmenu);}
}
