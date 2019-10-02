package projetJava;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ModelPro extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int nb_lign=0;
	java.sql.ResultSetMetaData rsmd;
	ArrayList<Project> data=new ArrayList<Project>();
	public ModelPro() throws SQLException {
		// TODO Auto-generated constructor stub
		GuiBase.connect();
		GuiBase.select("select * from projet;");
		rsmd=GuiBase.rs.getMetaData();
		while(GuiBase.rs.next())
		{nb_lign++;
		data.add(new Project(GuiBase.rs.getInt(1), GuiBase.rs.getString(2),(java.sql.Date)GuiBase.rs.getDate(3)));
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
		//System.out.println(GuiAdmin.AllProjects.size());
		//return GuiAdmin.AllProjects.size();
		return nb_lign;
	}

	@Override
	public Object getValueAt(int r	, int c) {
		// TODO Auto-generated method stub
		//Project e =(Project) GuiAdmin.AllProjects.get(r);
		Project e = data.get(r);
		if(c==0)
			return e.getId();
		if(c==1)
			return e.getTitre();
		if(c==2)
			return e.getDatedeb();
		else
			return "x";
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
	
	public void ajouterLigne(Object[] ligne) {
		nb_lign++;
		data.add(new Project(Integer.parseInt((String)ligne[0]),(String)ligne[1],java.sql.Date.valueOf((String) ligne[2])));
		fireTableDataChanged();
		
		
	}

}



