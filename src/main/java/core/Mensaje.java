package core;

public class Mensaje {
	
	private String mail;
	private String elemento;
	
	/**
	 * Constructor de un mensaje a partir de un mail y un elemento (dato)
	 * @param mail cadena que representa el mail
	 * @param elemento cadena que representa el dato
	 */
	public Mensaje(String mail, String elemento) {
		this.mail = mail;
		this.elemento = elemento;
	}
	
	/**
	 * Constructor copia
	 * @param nuevo mensaje fuente
	 */
	public Mensaje(Mensaje nuevo) {
		this.mail = nuevo.mail;
		this.elemento = nuevo.elemento;
	}

	/**
	 * Obtener mail
	 * @return mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * Establecer mail
	 * @param mail cadena que representa el mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * Obtener elemento
	 * @return elemento
	 */
	public String getElemento() {
		return elemento;
	}

	/**
	 * Establecer elemento
	 * @param elemento cadena que representa el elemento
	 */
	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

}
