package projetJava;


import java.sql.Date;

public class Project {
	private int id;
	private String titre;
	private Date datedeb;
	
	public Project(int id, String titre, Date datedeb) {
		
		this.id = id;
		this.titre = titre;
		this.datedeb = datedeb;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Date getDatedeb() {
		return datedeb;
	}

	public void setDatedeb(Date datedeb) {
		this.datedeb = datedeb;
	}
	

}
