package projetJava;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class ProjectPanel extends JPanel implements ActionListener ,KeyListener {
	protected static final boolean StopListening = false;
	String NomIm;
	 Project monProjet;
	 Membre thisMembre;
	static DefaultListModel modelMem,modelFiles;
	JList memList,filesList;
	JTextArea chattA;
	JPanel entete,pied,corps,disc,envp;
	JTextField env;
	//JCheckBox mic,enplacement;
	JScrollPane memPr,memFiles,chatt;
	JButton capEcran,shareB,send;
	JFileChooser file;
	ArrayList<Membre> ALMem;
	private sendFile  sendf;
	static Socket conn;
	static String newFileName;
	public static String[] theSharedFiles;
	public static Boolean thereIsNewFiles = false;

	public ProjectPanel(Project p, Membre m)
	{	this.setOpaque(false);
		this.setLayout(new BorderLayout());
		monProjet=p;
		thisMembre=m;

		ALMem= new ArrayList<Membre>();
		//mic = new JCheckBox("Activer micro");
		//enplacement = new JCheckBox("toujour au dessus");
		chattA= new JTextArea();
		chattA.setBackground(new Color(245,245,245));
		chattA.setEditable(false);

		env= new JTextField();
		send = new JButton("Envoyer");
		env.addKeyListener(this);
		send.addActionListener(this);
		envp= new JPanel(new BorderLayout());
		envp.add(env);
		envp.add(send,BorderLayout.EAST);

		modelMem = new DefaultListModel<>();
		modelFiles= new DefaultListModel<>();
		memList= new JList();
		memList.setBackground(Color.lightGray);
		filesList=new JList();
		filesList.setBackground(Color.lightGray);
		
		memPr = new JScrollPane(memList);
		memPr.setBackground(Color.lightGray);
		memFiles=new JScrollPane(filesList);
		memFiles.setBackground(Color.lightGray);
		JSplitPane memPrFiles=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,memPr,memFiles);
		memPrFiles.setBackground(Color.lightGray);
		chatt = new JScrollPane(chattA);

		disc= new JPanel(new BorderLayout());
		disc.add(chatt,BorderLayout.CENTER);
		disc.add(envp,BorderLayout.SOUTH);

		entete = new JPanel();
		//entete.add(enplacement);
		//entete.add(mic);
		capEcran= new JButton("Capture ecran");
		capEcran.addActionListener(this);
		shareB = new JButton("partager un fichier");
		shareB.addActionListener(this);
		pied = new JPanel();
		pied.add(capEcran);
		pied.add(shareB);
		
		corps = new JPanel(new BorderLayout());
		corps.add(disc,BorderLayout.CENTER);
		memList.setModel(modelMem);
		try {
			this.addMember(modelMem);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		filesList.setModel(modelFiles);
		try {
			this.addFiles(modelFiles);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		corps.add(memPrFiles,BorderLayout.SOUTH);
		this.add(entete,BorderLayout.NORTH);
		this.add(corps, BorderLayout.CENTER);
		this.add(pied,BorderLayout.SOUTH);
	}

	public void addMember(DefaultListModel listM) throws SQLException
	{
		//GuiBase.connect();
		if(!listM.isEmpty())listM.clear();
		GuiBase.select("SELECT id, nom, prenom, Ip, mot_de_passe FROM emp, emp_pro WHERE IdPro ="+monProjet.getId()+" AND id = IdEmp");
		//System.out.println("*********************************");
		while(GuiBase.rs.next())
		{
			//System.out.println("resultset : "+ GuiBase.rs.getInt(1));
			if(thisMembre.getIdM()!=GuiBase.rs.getInt(1))
			{Membre m= new Membre(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getString(3),GuiBase.rs.getString(4),GuiBase.rs.getString(5));
			if(!ALMem.contains(m))
			ALMem.add(m);
			
			listM.addElement(GuiBase.rs.getString(2)+" "+GuiBase.rs.getString(3));	
			}

		}
		//System.out.println("*********************************");
		//System.out.println("nombre mates : "+ALMem.size());

	}
	public void addFiles(DefaultListModel listM) throws SQLException
	{ int i=0;
		//GuiBase.connect();
		GuiBase.select("SELECT * FROM sharedfile ");
		//System.out.println("*********************************");
		if(!listM.isEmpty())listM.clear();
		while(GuiBase.rs.next())
		{//System.out.println(monProjet.getTitre());
			//System.out.println("resultset : "+ GuiBase.rs.getInt(1));
			//if(thisMembre.getIdM()!=GuiBase.rs.getInt(1))
			//{Membre m= new Membre(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getString(3),GuiBase.rs.getString(4),GuiBase.rs.getString(5));
			//ALMem.add(m);
			if(monProjet.getTitre().equals(GuiBase.rs.getString(2)));	
			listM.addElement(GuiBase.rs.getString(7));	

		}
		//System.out.println("*********************************");
		//System.out.println("nombre Files : "+i);

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if ((e.getSource() == send) && (!env.getText().equals(""))) {
			this.chattA.append( '\n' + "Moi: "+ env.getText());
			String[] ips=new String[ALMem.size()];
			int[] ids=new int[ALMem.size()];
			//System.out.println("taille membres"+ALMem.size());
			for(int i=0;i<ALMem.size();i++)
			{
				if(!ALMem.get(i).getIp().equals(""))
				{	ips[i]=ALMem.get(i).getIp();
					ids[i]=ALMem.get(i).getIdM()+5000;
					//System.out.println("ip["+i+"]"+ips[i]);
					//System.out.println("id["+i+"]"+ids[i]);
				}
			}
			send(monProjet.getTitre()+"/"+thisMembre.getNom()+": "+env.getText(),ips,ids);
			env.setText("");
		}
		if(e.getSource()==capEcran)
		{	takeScreenShot();
		//Boolean v=false;
		//int dialogButton = JOptionPane.YES_NO_OPTION;
       // JOptionPane.showConfirmDialog (null, "Voulez-vous envoyer cette capture ?","Partage capture : ", dialogButton);
       // if(dialogButton == JOptionPane.NO_OPTION) {
         //     remove(dialogButton);
         //     sendf.interrupt();
          //  }
        	String project = monProjet.getTitre();
			String host = thisMembre.getPrenom()+" "+thisMembre.getNom();//a fixer
			String path =System.getProperty("user.home") + File.separator + "Documents/Cowork/"+thisMembre.getNom()+" "+thisMembre.getPrenom()+"/"+monProjet.getTitre()+"/"+NomIm;
			String query = "INSERT INTO `sharedfile` (`Id`, `project`, `owner`, `path`,`port`,`ownerIp` ,`fileName` ,`fileSize`) VALUES ("+genererId("sharedfile")+", '"
					+ project
					+ "', '"
					+ host
					+ "', '"
					+ path
					+ "', "
					+ (thisMembre.getIdM()+5000)//a fixer
					+ ", '"
					+ thisMembre.getIp()//a fixer
					+ "', '"
					+ NomIm+ "', " + 120 + ");";
			
			executeUpdateQuery(query);
			modelFiles.addElement(NomIm);
				try {
					addFiles(modelFiles);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//GuiBase.closeStatement();
			//GuiBase.deconnection();
			String[] ips=new String[ALMem.size()];
			int[] ids=new int[ALMem.size()];
			//	System.out.println("taille membres"+ALMem.size());
			for(int j=0;j<ALMem.size();j++)
			{
				if(!ALMem.get(j).getIp().equals(""))
				{	ips[j]=ALMem.get(j).getIp();
					ids[j]=ALMem.get(j).getIdM()+22000;
				//	System.out.println("ip["+j+"]"+ips[j]);
				//	System.out.println("id["+j+"]"+ids[j]);
				}
			}
			// if(dialogButton == JOptionPane.YES_OPTION) {
				 sendf=new sendFile(ips,ids,path, NomIm);
				 sendf.start();
			informEveryBodyOfNewSharedFiles(monProjet.getTitre()+"/"+thisMembre.getNom()+" a paratgé un nouveau fichier : "+NomIm);
			 		//}
			 }
		if(e.getSource()==shareB)
		{   shareSomeFiles();}
	}

	public static void send(String notifStr,String[] IP,int[] ID) {
		for(int i=0;i<IP.length;i++)
		{
			String ipi=IP[i];
			int idi=ID[i];

			new Thread() {
				public void run() {
					//System.out.println("sending(" + notifStr + ")==>" + ipi + " ...");
					String[] s = ipi.split("\\.");

					byte[] ipAddr = new byte[] { (byte) Integer.parseInt(s[0]),
							(byte) Integer.parseInt(s[1]),
							(byte) Integer.parseInt(s[2]),
							(byte) Integer.parseInt(s[3]) };

					try {
						DatagramSocket clientSocket = new DatagramSocket();
						InetAddress IPAddress = InetAddress.getByAddress(ipAddr);
						byte[] sendData = new byte[1024];

						sendData = (notifStr).getBytes();
						
						DatagramPacket sendPacket = new DatagramPacket(sendData,
								sendData.length, IPAddress, idi);
						clientSocket.send(sendPacket);
						clientSocket.close();
					} catch (Exception e) {
						e.printStackTrace();
					//	System.out.println(e.getMessage());
					}
					return;
				}
			}.start();
		}

	}

	public class sendFile extends Thread {
	   // InetAddress IPAddress1 = null;
		String[] sIP;
		int[] sID;
		String pathfich;
		String nomFichier;
		JFrame f;
		boolean end=false;
		public sendFile(String[] sIP, int[] sID, String pathfich,String nomFichier) {
			super();
			this.sIP = sIP;
			this.sID = sID;
			this.pathfich = pathfich;
			this.nomFichier = nomFichier;
		}
		JProgressBar load;
		//JButton launch;
		JLabel dl =new JLabel();
		public void run() {
			
		    f=new JFrame();
			f.setSize(400, 70);
			f.setResizable(false);
			f.setTitle(" envoi :  "+this.nomFichier);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setLocationRelativeTo(null);
			 load =new JProgressBar();
			 load.setForeground(Color.green);
			 load.setBackground(Color.black);
			load.setMaximum(100);
			load.setMinimum(0);
			load.setStringPainted(true);
			f.getContentPane().add(load, BorderLayout.CENTER);
			for(int i=0;i<sIP.length;i++)
			{
				String ipi=sIP[i];
				int idi=sID[i];
				String[] s = ipi.split("\\.");

				/*byte[] ipAddr = new byte[] { (byte) Integer.parseInt(s[0]),
						(byte) Integer.parseInt(s[1]),
						(byte) Integer.parseInt(s[2]),
						(byte) Integer.parseInt(s[3]) };
				try {
					InetAddress IPAddress1 = InetAddress.getByAddress(ipAddr);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//System.out.println("ip["+i+"]"+ipi);
				//System.out.println("id["+i+"]"+idi);
				//String[] IPI={ipi};
				//int[] IDI={idi};
				//send(monProjet.getTitre()+"/"+thisMembre.getNom(),IPI ,IDI);
				new Thread(new Traitement(ipi,idi)).start();
				
				
			}
				
			dl.setText(" Téléversé: ");
			f.getContentPane().add(dl, BorderLayout.NORTH);
			f.setVisible(true);
		}

		public class Traitement implements Runnable{
			String sIp;
			int sPort;
			
			public Traitement(String sIp, int sPort) {
				super();
				this.sIp = sIp;
				this.sPort = sPort;
			}

			public void run(){
				//launch.setEnabled(false);
				BufferedInputStream bis =null;
				try{
					
					bis =new BufferedInputStream(
							new FileInputStream(
								new File(pathfich)));
				
				}catch(FileNotFoundException e){}
				Socket sock;
				try{
					sock = new Socket(sIp, sPort);//a fixer : port !! 
					
					
					BufferedOutputStream bos =new BufferedOutputStream(sock.getOutputStream());
	 
					DecimalFormat formater =new DecimalFormat("0.##");
					byte buffer[] =new byte[4096];
					float s=100, kodl;
					int i =0;
					//if(!bis.equals(null))
					load.setMaximum((int)(s =bis.available()));
					s /=1000000;
					long start =System.currentTimeMillis();
	 
					while(bis.read(buffer) !=-1){
						bos.write(buffer);
						bos.flush();
						i +=4096;
						
						load.setString(formater.format((float)i /1000000) +"/" +formater.format(s) +" Mo   vitesse de dl: " +formater.format(kodl =((float)i /1000 /((System.currentTimeMillis() -start) /1000))) +" Ko/s   Tps estimé: " +(int)((s *1000) /kodl) +" sec");
						dl.setText(" a téléverser: " +formater.format((float)bis.available() /1000000) +" Mo           " +" En téléversement depuis: " +(System.currentTimeMillis() -start) /1000 +" sec");
						if(((float)i /1000000)<0)
						load.setValue(load.getMaximum());
						else 
						load.setValue(i);
					}
					//AFFICHAGE GUI
					dl.setText(" Téléverrsé en: " +(System.currentTimeMillis() -start) /1000 +" sec");
					load.setValue(load.getMaximum());
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					load.setString("Téléversement Terminé!");
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					end=true;
					f.setVisible(false);
					bis.close();
					bos.close();
					sock.close();
				//EXCEPTION NON GéRéeS
				}catch (UnknownHostException e){
				}catch (IOException e){}
			//	System.out.println("FICHIER TRANSFERé");
				//launch.setEnabled(true);
			}
					
		}

	}
	//s'il ya signale de nouv fichier
	public void refreshFileList(){
		new Thread(){
			public void run(){
				while(true){
					if(thereIsNewFiles){
						//filesList
						filesList.setListData(theSharedFiles);
						repaint();
						thereIsNewFiles=false;
					}
				}
			}
		}.start();
	}

	public static void executeUpdateQuery(final String query) {
		new Thread() {
			public void run() {
				ConnectionDataBase.loadConnecction();
				ConnectionDataBase.executeUpdate(query);
				//ConnectionDataBase.deconnection();
			}
		}.start();
	}
	public  void takeScreenShot() {

		new Thread() {
			long picSize;

			public void run() {
				try {
					String path = System.getProperty("user.home") + File.separator + "Documents/Cowork/"+thisMembre.getNom()+" "+thisMembre.getPrenom()+"/"+monProjet.getTitre();
					File customDir = new File(path);
					if (!customDir.exists())customDir.mkdirs();
					Thread.sleep(10);
					Rectangle screenRect = new Rectangle(Toolkit
							.getDefaultToolkit().getScreenSize());
					BufferedImage capture = new Robot().createScreenCapture(screenRect);
					//String current = new java.io.File( "." ).getCanonicalPath();
					//System.out.println(current);
					//System.out.println(path);
					NomIm="capture"+(int)((Math.random())*10000)+".jpg";
					ImageIO.write(capture, "bmp", new File(path+"/"+NomIm));
					
					//System.out.println("screenshot saved !!");
					//File f = new File("capture.jpg");
					//picSize = f.length();
				} catch (Exception e1) { // TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}.start();

	}

	public void shareSomeFiles() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		int option = chooser.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			File[] sf = chooser.getSelectedFiles();
			String filelist = "nothing";
			if (sf.length > 0) {
				filelist = sf[0].getName();
				}
				for (int i = 0; i < sf.length; i++) {
					//GuiBase.connect();
					String project = monProjet.getTitre();
					String host = thisMembre.getPrenom()+" "+thisMembre.getNom();//a fixer
					String path = sf[i].getAbsolutePath();
					//System.out.println(sf[i].getAbsolutePath());
					//System.out.println(path);
					String query = "INSERT INTO `sharedfile` (`Id`, `project`, `owner`, `path`,`port`,`ownerIp` ,`fileName` ,`fileSize`) VALUES ("+genererId("sharedfile")+", '"
							+ project
							+ "', '"
							+ host
							+ "', '"
							+ path
							+ "', "
							+ (thisMembre.getIdM()+5000)//a fixer
							+ ", '"
							+ thisMembre.getIp()//a fixer
							+ "', '"
							+ sf[i].getName() + "', " + sf[i].length() + ");";
					
					executeUpdateQuery(query);
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					modelFiles.addElement(sf[i].getName());
					filesList.setModel(modelFiles);
						
					//GuiBase.closeStatement();
					//GuiBase.deconnection();
					String[] ips=new String[ALMem.size()];
					int[] ids=new int[ALMem.size()];
					//	System.out.println("taille membres"+ALMem.size());
					for(int j=0;j<ALMem.size();j++)
					{
						if(!ALMem.get(j).getIp().equals(""))
						{	ips[j]=ALMem.get(j).getIp();
							ids[j]=ALMem.get(j).getIdM()+22000;
						//	System.out.println("ip["+j+"]"+ips[j]);
						//	System.out.println("id["+j+"]"+ids[j]);
						}
					}
					new sendFile(ips,ids,path, sf[i].getName()).start();
					informEveryBodyOfNewSharedFiles(monProjet.getTitre()+"/"+thisMembre.getNom()+" a paratgé un nouveau fichier : "+sf[i].getName());
					
				}

			} else {
			//System.out.println("You canceled.");
		}
	}


	

	// sends UpdateSharedFiles to MateIp ips array
	public  void informEveryBodyOfNewSharedFiles(String msg) {
		// TODO Auto-generated method stub
		String[] ips=new String[ALMem.size()];
		int[] ids=new int[ALMem.size()];
		for(int i=0;i<ALMem.size();i++)
		{
			if(!ALMem.get(i).getIp().equals(""))
			{	ips[i]=ALMem.get(i).getIp();
				ids[i]=ALMem.get(i).getIdM()+5000;
				//System.out.println("ip["+i+"]"+ips[i]);
				//System.out.println("id["+i+"]"+ids[i]);
			}
		}
				//send("UpdateSharedFiles",ips,ids);
				send(msg, ips,ids);

			
		}
	public int genererId(String noTable){
		ArrayList ids=new ArrayList<Integer>();
		String req="select * from "+noTable+";";
		try {
			GuiBase.select(req);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while (GuiBase.rs.next())
				ids.add(GuiBase.rs.getInt(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(ids.size());		
		return (int) (ids.get(ids.size()-1))+1;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if((e.getKeyCode()==e.VK_ENTER)&&(!env.getText().equals(""))) {
			this.chattA.append( '\n' + "Moi: "+ env.getText());
			String[] ips=new String[ALMem.size()];
			int[] ids=new int[ALMem.size()];
			//System.out.println("taille membres"+ALMem.size());
			for(int i=0;i<ALMem.size();i++)
			{
				if(!ALMem.get(i).getIp().equals(""))
				{	ips[i]=ALMem.get(i).getIp();
					ids[i]=ALMem.get(i).getIdM()+5000;
					//System.out.println("ip["+i+"]"+ips[i]);
					//System.out.println("id["+i+"]"+ids[i]);
				}
			}
			send(monProjet.getTitre()+"/"+thisMembre.getNom()+": "+env.getText(),ips,ids);
			env.setText("");
			
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


