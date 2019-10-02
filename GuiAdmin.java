package projetJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


public class GuiAdmin extends JFrame implements MouseListener, FocusListener, ActionListener {

	Container container;
	static JList projectList, userList;
	JLabel proLabel, userLabel, help,ajPr,ajEmp;
	JPanel pied,label,proPanel,empPanel;
	JTextField tNom,tPrenom,tMp,tTitre,tDatedep;
	static DefaultListModel modelUser;
	DefaultListModel modelPr;
	JSplitPane jsp;
	JButton bAjEmp,bAjPr;
	JTable ProjectTable,EmpTable;
	JLabel idMem,pswMem;
	JButton connect;
	JTextField tId;
	JPasswordField tPsw;
	static Membre admin;
	static ModelEmp modelEmp;
	private ModelPro modelPro;
	static int x;
	private JMenu fichierMenu, HelpMenu,Emp,pro;
	private JMenuItem ModE, SuppE, quitter,editerMenu,copier, couper, coller,about,Modp,Suppp;
	private JMenuBar Jmenu;
	static ArrayList AllProjects,AllMembre;

	public GuiAdmin(Membre a) throws SQLException {
		this.admin=new Membre(a.getIdM(),a.getNom(), a.getPrenom(), a.getIp(), a.getMot_passe());
		this.setSize(900, 620);
		//this.setTitle("Admin interface");
		this.setLocationRelativeTo(null);
		this.setTitle("CoWork : "+admin.getNom()+" "+admin.getPrenom());
		this.setIconImage((new ImageIcon(this.getClass().getResource("/images/Icon.png"))).getImage());
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		
		//MenuPanel barreMenus=new MenuPanel();
		//this.setJMenuBar(barreMenus);
		BarreMenu();
		AllProjects = new ArrayList<Project>();
		AllMembre = new ArrayList<Membre>();
		
		container = new Container();
		modelPr = new DefaultListModel();
		modelUser = new DefaultListModel();

		proLabel = new JLabel("Projets");
		proLabel.setOpaque(false);
		userLabel = new JLabel("Employés");
		userLabel.setOpaque(false);
		userLabel.setHorizontalAlignment(JLabel.CENTER);


		label = new JPanel();
		label.setOpaque(false);
		label.setLayout(new GridLayout(1, 2));
		proLabel.setHorizontalAlignment(JLabel.CENTER);
		label.add(proLabel);
		label.add(userLabel);


		projectList = new JList(modelPr);
		projectList.setBackground(Color.lightGray);
		projectList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),BorderFactory.createTitledBorder("Liste des projets")));
		projectList.addMouseListener(this);
		this.addProject(modelPr);
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) projectList.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		
		userList = new JList(modelUser);
		userList.setBackground(Color.lightGray);
		userList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),BorderFactory.createTitledBorder("Liste des employés")));
		userList.addMouseListener(this);
		this.addMember(modelUser);
		DefaultListCellRenderer renderer1 = (DefaultListCellRenderer) userList.getCellRenderer();
		renderer1.setHorizontalAlignment(JLabel.CENTER);

		bAjEmp= new JButton(" Ajouter emp ");
		bAjEmp.setOpaque(false);
		bAjEmp.addActionListener(this);
		bAjPr= new JButton(" Ajouter pro ");  
		bAjPr.setOpaque(false);
		bAjPr.addActionListener(this);
		ajPr = new JLabel("    Nouveaux projet ");
		ajEmp = new JLabel("    Nouveaux employé ");
		tNom=new JTextField(" Entrez le nom de l'emp",15);
		tNom.addFocusListener(this);
		tPrenom=new JTextField(" Entrez le prenom de l'emp",15);
		tPrenom.addFocusListener(this);
		tMp=new JTextField(" accordez un mot de passe",15);
		tMp.addFocusListener(this);
		tTitre=new JTextField(" Entrez le titre du projet",15);
		tTitre.addFocusListener(this);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date actuelle = new Date();
		String dat = dateFormat.format(actuelle);
		tDatedep=new JTextField(dat,15);
		tDatedep.setEditable(false);
		help = new JLabel("help");

		pied=new JPanel(new GridLayout(2,5));
		pied.add(ajEmp);
		pied.add(tNom);
		pied.add(tPrenom);
		pied.add(tMp);
		pied.add(bAjEmp);
		pied.add(ajPr);
		pied.add(tTitre);
		pied.add(tDatedep);
		pied.add(new JLabel(""));
		pied.add(bAjPr);
		modelEmp=new ModelEmp();
		EmpTable= new JTable(modelEmp);
		EmpTable.setOpaque(false);
		modelPro=new ModelPro();
		ProjectTable = new JTable(modelPro);
		ProjectTable.setOpaque(false);
		proPanel= new JPanel(new GridLayout(1, 2));
		empPanel = new JPanel(new GridLayout(1, 2));
		empPanel.setOpaque(false);
		proPanel.setOpaque(false);
		proPanel.add(new JScrollPane(projectList));
		proPanel.add(new JScrollPane(ProjectTable));
		empPanel.add(new JScrollPane(userList));
		empPanel.add(new JScrollPane(EmpTable));
		
		jsp = new JSplitPane();
		jsp.setOpaque(false);
		jsp.setRightComponent(empPanel);
		jsp.setLeftComponent(proPanel);
		jsp.setDividerLocation(450);
		container = this.getContentPane();
		JLabel background=new JLabel((new ImageIcon(this.getClass().getResource("/images/backGround.jpg"))));
		background.setLayout(new BorderLayout());
		JPanel pied1=new JPanel(new BorderLayout());
		pied.setOpaque(false);
		pied1.add(label, BorderLayout.SOUTH);
		pied1.add(new PhotosAcueilPanel());
		pied1.setOpaque(false);
		background.add(pied1, BorderLayout.NORTH);
		background.add(jsp, BorderLayout.CENTER);
		background.add(pied, BorderLayout.SOUTH);
		container.add(background);
	}

	public static void addMember(DefaultListModel listM) throws SQLException
	{
		if(!AllMembre.isEmpty())AllMembre.clear();
		if(!listM.isEmpty())listM.clear();
		//GuiBase.connect();
		GuiBase.select("select * from emp where id <> "+admin.getIdM()+";");
		while(GuiBase.rs.next())
		{
			Membre m= new Membre(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getString(3),GuiBase.rs.getString(5),GuiBase.rs.getString(4));
			AllMembre.add(m);
			listM.addElement(GuiBase.rs.getString(2)+" "+GuiBase.rs.getString(3));	//nom prenom	

		}
		//GuiBase.closeStatement();
		//GuiBase.deconnection();
	}
	
	public void addProject(DefaultListModel listM) throws SQLException
	{
		//GuiBase.connect();
		if(!AllProjects.isEmpty())AllProjects.clear();
		if(!listM.isEmpty())listM.clear();
		GuiBase.select("select * from projet;");
		while(GuiBase.rs.next())
		{
			Project p = new Project(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getDate(3));
			AllProjects.add(p);
			listM.addElement(GuiBase.rs.getString(2));	//titre projet	
		}
		//GuiBase.closeStatement();
		//GuiBase.deconnection();
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

		return (int) (ids.get(ids.size()-1))+1;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getClickCount()==2 && e.getSource()==projectList){
			int index = projectList.locationToIndex(e.getPoint());
			String pseudo = (String)modelPr.getElementAt(index);	

			boolean test=false;
			int i=0,x=0;
			while(test==false && i<AllProjects.size())
			{
				if(((Project)AllProjects.get(i)).getTitre().equals(pseudo)){
					test=true;
					x=i;}
				i++;
			}
			GuiProject pr =new GuiProject(((Project)AllProjects.get(x)).getId());//ajout dans l'interface projets
		}
		else
			if(e.getClickCount()==2 && e.getSource()==userList){
				int index = userList.locationToIndex(e.getPoint());
				String pseudo = (String)modelUser.getElementAt(index);	

				boolean test=false;
				int i=0;
				x=0;
				while(test==false && i<AllMembre.size())
				{
					if((((Membre)AllMembre.get(i)).getNom()+" "+((Membre)AllMembre.get(i)).getPrenom()).equals(pseudo)){
						test=true;
						x=i;}
					i++;
				}
				GuiMembre pr =new GuiMembre(((Membre)AllMembre.get(x)));//ajout dans  l'interface membres 
			}	
		int bd=e.getButton();
		//supprission user
		if(bd==MouseEvent.BUTTON3 && e.getSource()==userList)
		{
			JPopupMenu pop=new JPopupMenu();
			JMenuItem sup= new JMenuItem("supprimer");
			sup.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int res=0;
					int rep = JOptionPane.showConfirmDialog(null, "Voulez vous supprimer ce pseudo?", "Supprimer pseudo", 2);
					switch(rep)
					{
					case 0: // '\0'
						String oldName = (String)userList.getSelectedValue();
						
						boolean test=false;
						int i=0,x=0;
						while(test==false && i<AllMembre.size())
						{
							if((((Membre)AllMembre.get(i)).getNom()+" "+((Membre)AllMembre.get(i)).getPrenom()).equals(oldName)){
								test=true;
								x=i;}
							i++;
						}
						System.out.println(((Membre)AllMembre.get(x)).getNom()+" "+((Membre)AllMembre.get(x)).getPrenom());
						Membre mem =new Membre(((Membre)AllMembre.get(x)).getIdM(),((Membre)AllMembre.get(x)).getNom(),((Membre)AllMembre.get(x)).getPrenom(),((Membre)AllMembre.get(x)).getNom()+" "+((Membre)AllMembre.get(x)).getIp(),((Membre)AllMembre.get(x)).getNom()+" "+((Membre)AllMembre.get(x)).getMot_passe());
						
						AllMembre.remove(mem);
						modelUser.removeElement(oldName);
						
						//GuiBase.connect();
						try {//selection de user suprrimer
							GuiBase.select("select id from emp where nom like '"+(oldName.split(" "))[0]+"' and prenom like '"+(oldName.split(" "))[1]+"';");
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							while(GuiBase.rs.next())
								res=GuiBase.rs.getInt(1);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {//delete from tables emp_pro et emp
							GuiBase.miseAjour("delete from emp_pro where idEmp="+res+";");
							GuiBase.miseAjour("delete from emp where id="+res+";");
							EmpTable.setModel(new ModelEmp());
							try {
								addMember(modelUser);
							} catch (SQLException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//GuiBase.closeStatement();
						//GuiBase.deconnection();
						break;
					case 1: // '\001'
						break;
					case 2: // '\002'
						break;
					}
					

				}
			});
			pop.add(sup);
			pop.show(userList, e.getX(), e.getY());
		}
		//supprission project
		if(bd==MouseEvent.BUTTON3 && e.getSource()==projectList)
		{
			JPopupMenu pop=new JPopupMenu();
			JMenuItem sup= new JMenuItem("supprimer");
			sup.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int res=0;
					int rep = JOptionPane.showConfirmDialog(null, "Voulez vous supprimer ce projet ?", "Supprimer pseudo", 2);
					switch(rep)
					{
					case 0: // '\0'
						String oldName = (String)projectList.getSelectedValue();
						modelPr.removeElement(oldName);
						boolean test=false;
						int i=0,x=0;
						while(test==false && i<AllProjects.size())
						{
							if(((Project)AllProjects.get(i)).getTitre().equals(oldName)){
								test=true;
								x=i;}
							i++;
						}
						Project pr =new Project(((Project)AllProjects.get(x)).getId(),((Project)AllProjects.get(x)).getTitre(),((Project)AllProjects.get(x)).getDatedeb());

						AllProjects.remove(pr);
						//GuiBase.connect();
						try {//selection de projet a supprimer
							GuiBase.select("select Id from projet where titre like '"+oldName+"';");
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							while(GuiBase.rs.next())
								res=GuiBase.rs.getInt(1);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {//delete from tables emp_pro et projet
							GuiBase.miseAjour("delete from emp_pro where idPro="+res+";");
							GuiBase.miseAjour("delete from projet where id="+res+";");
							ProjectTable.setModel(new ModelPro());

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//GuiBase.closeStatement();
						//GuiBase.deconnection();
						break;
					case 1: // '\001'
						break;
					case 2: // '\002'
						break;
					}
					

				}
			});
			pop.add(sup);
			pop.show(projectList, e.getX(), e.getY());
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

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		if ( e.getSource()==tNom)
			if(tNom.getText().equals(" Entrez le nom de l'emp"))
				tNom.setText("");

		if( e.getSource()==tPrenom)
			if(tPrenom.getText().equals(" Entrez le prenom de l'emp"))
				tPrenom.setText("");

		if( e.getSource()==tMp)
			if(tMp.getText().equals(" accordez un mot de passe"))
				tMp.setText("");

		if ( e.getSource()==tTitre)
			if(tTitre.getText().equals(" Entrez le titre du projet"))
				tTitre.setText("");




	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

		if ( e.getSource()==tNom)
			if(tNom.getText().equals(""))
				tNom.setText(" Entrez le nom de l'emp");

		if( e.getSource()==tPrenom)
			if( tPrenom.getText().equals(""))
				tPrenom.setText(" Entrez le prenom de l'emp");

		if( e.getSource()==tMp)
			if(tMp.getText().equals(""))
				tMp.setText(" accordez un mot de passe");

		if ( e.getSource()==tTitre)
			if(tTitre.getText().equals(""))
				tTitre.setText(" Entrez le titre du projet");



	}
	public void BarreMenu()
	{// Création du menu Fichier
	    fichierMenu = new JMenu("Fichier");
	    Emp = new JMenu("Employé");
	    ModE= new JMenuItem("Modifier");
	    ModE.addActionListener(this);
	    SuppE= new JMenuItem("Supprimer");
	    SuppE.addActionListener(this);
	    Emp.add(ModE);
	    Emp.add(SuppE);
	    fichierMenu.add(Emp);
	    pro = new JMenu("Projet");
	    Modp= new JMenuItem("Modifier");
	    Modp.addActionListener(this);
	    Suppp= new JMenuItem("Supprimer");
	    Suppp.addActionListener(this);
	    pro.add(Modp);
	    pro.add(Suppp);
	    fichierMenu.add(pro);
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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		  //System.out.println("Item clicked: "+e.getActionCommand());
		if(e.getSource()==bAjEmp)
		{
			if((!tNom.getText().equals("")&&!tNom.getText().equals(" Entrez le nom de l'emp") )
			&&(!tPrenom.getText().equals("")&&!tPrenom.getText().equals(" Entrez le prenom de l'emp"))
			&& (!tMp.getText().equals("")&& !tMp.getText().equals(" accordez un mot de passe")))
			{
				//GuiBase.connect();
				try {
					//GuiBase.miseAjour("ALTER TABLE emp AUTO_INCREMENT =1;");
					int i=genererId("emp");
					GuiBase.miseAjour("insert into emp values("+ genererId("emp") +",'"+tNom.getText()+"','"+tPrenom.getText()+"','"+tMp.getText()+"','');");
					String ligne[]={i+"",tNom.getText(),tPrenom.getText(),"",tMp.getText()};
					modelEmp.ajouterLigne(ligne);
					//					Membre m= new Membre(GuiBase.rs.getInt(1),GuiBase.rs.getString(2),GuiBase.rs.getString(3),GuiBase.rs.getString(4),GuiBase.rs.getString(5));
					//					AllMembre.add(m);
					
					modelUser.addElement(tNom.getText()+" "+tPrenom.getText());
					EmpTable.setModel(new ModelEmp());
					this.addMember(modelUser);
					tNom.setText(" Entrez le nom de l'emp");
					tPrenom.setText(" Entrez le prenom de l'emp");
					tMp.setText(" accordez un mot de passe");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("erreur d'enregistrement d'emp");
				}
				//GuiBase.closeStatement();
				//GuiBase.deconnection();
			}
		}
		else 
			if(e.getSource()==bAjPr)
			{
				if(!tTitre.getText().equals("") && !tTitre.getText().equals(" Entrez le titre du projet") )
				{
					//GuiBase.connect();
					try {
						int i=genererId("projet");
						GuiBase.miseAjour("insert into projet values("+genererId("projet")+",'"+tTitre.getText()+"','"+tDatedep.getText()+"');");
						Object ligne[]={i+"",tTitre.getText(),tDatedep.getText()};
						modelPro.ajouterLigne(ligne);
						modelPr.addElement(tTitre.getText());
						this.addProject(modelPr);
						tTitre.setText(" Entrez le titre du projet");
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("erreur d'enregistrement du projet");
					}
					//GuiBase.closeStatement();
					//GuiBase.deconnection();
				}
			}
			else 
				if(e.getSource()==ModE)
				{int index = userList.getSelectedIndex();
				String pseudo = (String)modelUser.getElementAt(index);	

				boolean test=false;
				int i=0;
				x=0;
				while(test==false && i<AllMembre.size())
				{
					if((((Membre)AllMembre.get(i)).getNom()+" "+((Membre)AllMembre.get(i)).getPrenom()).equals(pseudo)){
						test=true;
						x=i;}
					i++;
				}
				GuiMembre pr =new GuiMembre(((Membre)AllMembre.get(x)));//ajout dans  l'interface membres 
				}
			else 
				if(e.getSource()==SuppE)
				{
					int res=0;
					int rep = JOptionPane.showConfirmDialog(null, "Voulez vous supprimer ce pseudo?", "Supprimer pseudo", 2);
					switch(rep)
					{
					case 0: // '\0'
						String oldName = (String)userList.getSelectedValue();
						modelUser.removeElement(oldName);
						//GuiBase.connect();
						try {//selection de user suprrimer
							GuiBase.select("select id from emp where nom like '"+(oldName.split(" "))[0]+"' and prenom like '"+(oldName.split(" "))[1]+"';");
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							while(GuiBase.rs.next())
								res=GuiBase.rs.getInt(1);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {//delete from tables emp_pro et emp
							GuiBase.miseAjour("delete from emp_pro where idEmp="+res+";");
							GuiBase.miseAjour("delete from emp where id="+res+";");
							EmpTable.setModel(new ModelEmp());
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//GuiBase.closeStatement();
						//GuiBase.deconnection();
						break;
					case 1: // '\001'
						break;
					case 2: // '\002'
						break;
					}
			}	
			else 
				if(e.getSource()==Modp)
				{
					int index = projectList.getSelectedIndex();
					String pseudo = (String)modelPr.getElementAt(index);	

					boolean test=false;
					int i=0,x=0;
					while(test==false && i<AllProjects.size())
					{
						if(((Project)AllProjects.get(i)).getTitre().equals(pseudo)){
							test=true;
							x=i;}
						i++;
					}
					GuiProject pr =new GuiProject(((Project)AllProjects.get(x)).getId());//ajout dans l'interface projets		
				}
			else 
				if(e.getSource()==Suppp)
				{
					int res=0;
					int rep = JOptionPane.showConfirmDialog(null, "Voulez vous supprimer ce projet ?", "Supprimer pseudo", 2);
					switch(rep)
					{
					case 0: // '\0'
						String oldName = (String)projectList.getSelectedValue();
						modelPr.removeElement(oldName);
						boolean test=false;
						int i=0,x=0;
						while(test==false && i<AllProjects.size())
						{
							if(((Project)AllProjects.get(i)).getTitre().equals(oldName)){
								test=true;
								x=i;}
							i++;
						}
						Project pr =new Project(((Project)AllProjects.get(x)).getId(),((Project)AllProjects.get(x)).getTitre(),((Project)AllProjects.get(x)).getDatedeb());

						AllProjects.remove(pr);
						//GuiBase.connect();
						try {//selection de projet a supprimer
							GuiBase.select("select Id from projet where titre like '"+oldName+"';");
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							while(GuiBase.rs.next())
								res=GuiBase.rs.getInt(1);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {//delete from tables emp_pro et projet
							GuiBase.miseAjour("delete from emp_pro where idPro="+res+";");
							GuiBase.miseAjour("delete from projet where id="+res+";");
							ProjectTable.setModel(new ModelPro());

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//GuiBase.closeStatement();
						//GuiBase.deconnection();
						break;
					case 1: // '\001'
						break;
					case 2: // '\002'
						break;
					}			
				}else 
					if(e.getSource()==quitter){
						System.exit(0);
					}
				else //a faire !!! fenetre
					if(e.getSource()==about){
						GuiAbout fabout=new GuiAbout();
						fabout.setVisible(true);
						}
		


	}

}
