package projetJava;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ModelEmp extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int nb_lign=0;
	java.sql.ResultSetMetaData rsmd;
	ArrayList<Membre> data=new ArrayList<Membre>(); 
	
	public ModelEmp() throws SQLException {
		// TODO Auto-generated constructor stub
		GuiBase.connect();
		GuiBase.select("select * from emp;");
		rsmd=GuiBase.rs.getMetaData();
		while(GuiBase.rs.next())
		{nb_lign++;
		data.add(new Membre(GuiBase.rs.getInt(1), GuiBase.rs.getString(2), GuiBase.rs.getString(3), GuiBase.rs.getString(5),GuiBase.rs.getString(4)));
		}
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		try {
			return rsmd.getColumnCount();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		//return GuiAdmin.AllMembre.size();
		return nb_lign;
	}

	@Override
	public Object getValueAt(int r	, int c) {
		// TODO Auto-generated method stub

		//Membre e = (Membre) GuiAdmin.AllMembre.get(r);
		Membre e = data.get(r);
		if(c==0)
			return e.getIdM();
		if(c==1)
			return e.getNom();
		if(c==2)
			return e.getPrenom();
		if(c==3)
			return e.getMot_passe();
		else
			return e.getIp();
	}
	@Override
	public String getColumnName(int arg0) {
		// TODO Auto-generated method stub
		try {
			return rsmd.getColumnName(arg0+1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	public void ajouterLigne(String[] ligne) {
		nb_lign++;
		data.add(new Membre(Integer.parseInt(ligne[0]),ligne[1],ligne[2],ligne[3],ligne[4]));
		fireTableDataChanged();
	}
       public void modifierLigne(Membre o, int l) {
		// TODO Auto-generated method stub
		data.set(l,new Membre(o.getIdM(), o.getNom(), o.getPrenom(), o.getIp(), o.getMot_passe()));
		fireTableDataChanged();
		}
}
