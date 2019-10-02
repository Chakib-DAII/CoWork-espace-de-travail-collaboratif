package projetJava;

public class Membre {

	private int idM;
	private String nom,prenom,mot_passe,ip;
	public Membre(int idM, String nom, String prenom, String Ip,String mot_passe) {
		
		this.idM = idM;
		this.nom = nom;
		this.prenom = prenom;
		this.mot_passe = mot_passe;
		this.ip = Ip;
	}
	public int getIdM() {
		return idM;
	}
	public void setIdM(int idM) {
		this.idM = idM;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getMot_passe() {
		return mot_passe;
	}
	public void setMot_passe(String mot_passe) {
		this.mot_passe = mot_passe;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
