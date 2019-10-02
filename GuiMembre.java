package projetJava;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GuiMembre  extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton valider,quitter;
	JLabel nomL,prenomL,idL,mpL;
	JTextField nomT,preT,idT,mpT;
	
	Membre my;
	public GuiMembre(Membre m){
		my=m;
		this.setTitle(my.getNom()+" "+my.getPrenom());
		this.setIconImage((new ImageIcon(this.getClass().getResource("/images/Icon.png"))).getImage());
		this.setSize(300, 200);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		valider=new JButton("valider");
		valider.setOpaque(false);
		quitter=new JButton("quitter");
		quitter.setOpaque(false);
		nomL = new JLabel("   Nom :");
		prenomL = new JLabel("   Prenom :");
		idL= new JLabel("   ID :");
		mpL=new JLabel("   Mot de passe :");
		nomT= new JTextField(my.getNom(),15);
		preT= new JTextField(my.getPrenom(),15);
		mpT= new JTextField(my.getMot_passe(),15);
		idT = new JTextField(10);
		idT.setText(my.getIdM()+"");
		idT.setEditable(false);
		idT.setOpaque(false);
		JLabel background=new JLabel((new ImageIcon(this.getClass().getResource("/images/backGround1.jpg"))));
		background.setLayout(new GridLayout(5,2));
		background.add(idL);
		background.add(idT);
		background.add(nomL);
		background.add(nomT);
		background.add(prenomL);
		background.add(preT);
		background.add(mpL);
		background.add(mpT);
		background.add(valider);
		background.add(quitter);
		this.getContentPane().add(background);
		valider.addActionListener(this);
		quitter.addActionListener(this);
		
		
		this.setVisible(true);
		
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==quitter)
		{this.setVisible(false);}
		
		if(e.getSource()==valider)
		{my.setNom(nomT.getText());
		my.setPrenom(preT.getText());
		my.setMot_passe(mpT.getText());
		GuiAdmin.AllMembre.set(GuiAdmin.x,my);
/*		for(int i=0;i<GuiAdmin.AllMembre.size();i++)
			System.out.println(((Membre)GuiAdmin.AllMembre.get(i)).getNom());*/
		//GuiBase.connect();
		try {
			GuiBase.miseAjour("update emp set nom='"+my.getNom()+"'  where id="+my.getIdM()+";");
			GuiBase.miseAjour("update emp set prenom='"+my.getPrenom()+"'  where id="+my.getIdM()+";");
			GuiBase.miseAjour("update emp set mot_de_passe='"+my.getMot_passe()+"'  where id="+my.getIdM()+";");
			 
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//GuiBase.closeStatement();
		//GuiBase.deconnection();
			try {
				GuiAdmin.addMember(GuiAdmin.modelUser);
				GuiAdmin.modelEmp.modifierLigne(my,GuiAdmin.x+1);
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		
	
		this.setVisible(false);
		}
	}

}
