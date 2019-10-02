
package projetJava;

import java.sql.Statement;

import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuiBase {
	static Connection conn=null;
	static Statement req1;
	static PreparedStatement prest;
	static ResultSet rs;
	private static Class<?> c=null;

	public static void connect()
	{
		String driverName="com.mysql.jdbc.Driver";
		String dataBaseType="jdbc:mysql://localhost:3306/base_projet"/*"jdbc:mysql://mysql-coworkjava.alwaysdata.net/coworkjava_cowork"*/;
		String user = "root"/*"137884"*/;
		String passwd = "chakib123";
		try {
			c=Class.forName(driverName);
			System.out.println("Chargement de drivers"+c);
		} catch (ClassNotFoundException e) {
			System.out.println("Erreur chargement de drivers"+c);
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Erreur de chargement de drivrer "+driverName,"Erreur",JOptionPane.ERROR_MESSAGE);
		}
		try {
			conn=DriverManager.getConnection(dataBaseType,user,passwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			req1=conn.createStatement();
			System.out.println("Connexion etablie");
		} catch (SQLException e) {
			System.out.println("Erreur de Connexion"+e);
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Erreur de connexion avec la base :\n"+dataBaseType,"Erreur",JOptionPane.ERROR_MESSAGE);

		}


	}


	public static void miseAjour(String ch) throws SQLException{

		if(conn==null)
			JOptionPane.showMessageDialog(null,"Erreur de connexion avec la base","Erreur",JOptionPane.ERROR_MESSAGE);

		try
		{
			int a=req1.executeUpdate(ch);
		}
		catch(SQLException e)
		{
			System.out.println("Erreur de mise a jour "+e);
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Erreur de mise a jour!","Erreur",JOptionPane.ERROR_MESSAGE);
		}
	}


	public static void select(String ch) throws SQLException	{
		
		if(conn==null)
		{
			JOptionPane.showMessageDialog(null,"Erreur de connexion avec la base","Erreur",JOptionPane.ERROR_MESSAGE);
		}
		try
		{
			rs= req1.executeQuery(ch);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Erreur de selection!"+e,"Erreur",JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void closeStatement()
	{
		try
		{
			if(req1!=null)
			req1.close();
		}
		catch(SQLException e)
		{
			System.out.println("Erreur "+e);
			JOptionPane.showMessageDialog(null,"Erreur de fermeture!","Erreur",JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void deconnection()
	{
		try
		{
			if(conn!=null)
			conn.close();
		}
		catch(SQLException e)
		{
			System.out.println("Erreur "+e);
			JOptionPane.showMessageDialog(null,"Erreur de déconnexion!","Erreur",JOptionPane.ERROR_MESSAGE);
		}
	}

}

