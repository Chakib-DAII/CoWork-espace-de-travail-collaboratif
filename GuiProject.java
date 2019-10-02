package projetJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class GuiProject extends JFrame implements ActionListener, MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton ajouter,ecouter,retirer;
	JList userList;
	JComboBox<String> memBox;
	Container cont;
	DefaultListModel userModel;
	JPanel but,baj,bec,header;
	int projetId,i=0;
	public GuiProject(int prId){
		projetId= prId;
		this.setTitle("projet num "+projetId);
		this.setSize(300, 200);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setIconImage((new ImageIcon(this.getClass().getResource("/images/Icon.png"))).getImage());
		cont = new Container();
		cont=this.getContentPane();
		cont.setLayout(new BorderLayout());
		JLabel background=new JLabel((new ImageIcon(this.getClass().getResource("/images/backGround1.jpg"))));
		background.setLayout(new BorderLayout());
		ajouter = new JButton("AJOUTER");
		ajouter.setOpaque(false);
		ajouter.addActionListener(this);
		ajouter.setEnabled(false);
		//ecouter = new JButton("ECOUTER");
		//ecouter.addActionListener(this);
		retirer= new JButton("Retirer tout");
		retirer.setOpaque(false);
		retirer.addActionListener(this);
		but = new JPanel(new GridLayout(2,1));
		but.setOpaque(false);
		baj = new JPanel();
		baj.setOpaque(false);
		bec = new JPanel();
		bec.setOpaque(false);
		header= new JPanel();
		header.setOpaque(false);
		memBox=new JComboBox<String>();
		memBox.setOpaque(false);
		memBox.addActionListener(this);
		memBox.addItem("	");

		//bec.add(ecouter);
		baj.add(retirer);
		userModel = new DefaultListModel();
		try {
			addMember(userModel);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userList = new JList(userModel);
		userList.setBackground(new Color(245,245,245));
		userList.addMouseListener(this);
		while(i<GuiAdmin.AllMembre.size())
		{
			if(!userModel.contains((((Membre)GuiAdmin.AllMembre.get(i)).getNom()+" "+((Membre)GuiAdmin.AllMembre.get(i)).getPrenom())))
			{
				memBox.addItem(((Membre)GuiAdmin.AllMembre.get(i)).getNom()+" "+((Membre)GuiAdmin.AllMembre.get(i)).getPrenom());
			}
			i++;
		}
		header.add(ajouter);
		header.add(memBox);
		but.add(bec);
		but.add(baj);
		background.add(header,BorderLayout.NORTH);
		background.add(but,BorderLayout.WEST);
		background.add(userList,BorderLayout.CENTER);
		cont.add(background);

		this.setVisible(true);
	}
	public void addMember(DefaultListModel listM) throws SQLException
	{
		//GuiBase.connect();
		GuiBase.select("select nom,prenom from emp,emp_pro where "+projetId+"= IdPro and IdEmp=id;");
		while(GuiBase.rs.next())
		{	
			listM.addElement(GuiBase.rs.getString(1)+" "+GuiBase.rs.getString(2));

		}
		//GuiBase.closeStatement();
		//GuiBase.deconnection();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(memBox.getSelectedItem()!="	"){
			ajouter.setEnabled(true);	
			if(e.getSource()==ajouter)
			{
				int res=0;
				//GuiBase.connect();
				try {
					GuiBase.select("select id from emp where nom like '"+(((String)memBox.getSelectedItem()).split(" "))[0]+"' and prenom like '"+(((String)memBox.getSelectedItem()).split(" "))[1]+"';");
					while(GuiBase.rs.next())
						res=GuiBase.rs.getInt(1);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					GuiBase.miseAjour("insert into emp_pro values("+res+","+projetId+");");
					if(!userModel.contains(memBox.getSelectedItem()))
					userModel.addElement(memBox.getSelectedItem());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//GuiBase.closeStatement();
				//GuiBase.deconnection();
			}
		}
		else if(e.getSource()==retirer){
			int rep = JOptionPane.showConfirmDialog(null, "Voulez vous supprimer tous les pseudos?", "Supprimer pseudo", 2);
			switch(rep)
			{
			case 0: // '\0'
				userModel.removeAllElements();
				//GuiBase.connect();
				try {
					GuiBase.miseAjour("delete from emp_pro where idPro="+projetId+";");
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
		this.revalidate();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int bd=e.getButton();
		
		if(bd==MouseEvent.BUTTON3)
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
					userModel.removeElement(oldName);
					//GuiBase.connect();
					try {
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
					try {
						GuiBase.miseAjour("delete from emp_pro where idEmp="+res+" and idPro="+projetId+";");
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
		this.revalidate();
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
}
