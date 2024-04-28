package client;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import BufferApp.Buffer;
import BufferApp.BufferHelper;
import core.CoderXML;
import core.DecoderXML;
import core.Validador;
import java.util.ArrayList;
import core.Mensaje;
import core.Pair;
import core.Parser;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private String[] args = {"-ORBInitialPort","1050","ORBInitialHost","localhost"};
	private ORB orb;
	private org.omg.CORBA.Object objRef;
	private NamingContextExt ncRef;
	private String name;
	private Buffer bufferImpl;
	private StringHolder elem;
	private CoderXML empaquetador;
	private DecoderXML desempaquetador;
	private Validador validador;
	private String nombre_archivo;
	private String ruta_archivo;
    private Boolean firstRead;
   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet() {
        super();
        try{
			this.orb = ORB.init(args, null);
			System.out.println("orb: " + this.orb);
			this.objRef = orb.resolve_initial_references("NameService");
			String[] initialReferences = orb.list_initial_services();
			for (String ref : initialReferences) {
			    System.out.println("Initial Reference: " + ref);
			}

			System.out.println("objref: " + this.objRef);
			this.ncRef = NamingContextExtHelper.narrow(objRef);
			this.name = "Buffer";
			this.bufferImpl = BufferHelper.narrow(ncRef.resolve_str(name));
		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}
        this.nombre_archivo = "documentoProductorConsumidor";
		this.ruta_archivo = System.getProperty("user.home")+File.separator+"productor_consumidor_files"+File.separator+nombre_archivo+".xml";
        this.firstRead = false;
    }

    /**
     * Sobrecargamos el metodo get para que muestre un mensaje
     * de error indicando que los parametros no pueden ser
     * enviados al servlet por este metodo
     * @param request contiene la peticion del usuario sobre la servlet
     * @param response representa la respuesta de la servlet a la peticion
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		  out.println("<html>");
		  out.println("<head>");
		  out.println("</head>");
	      out.println("<body>");
	      out.println("Error! Los parametros no han sido enviados por el metodo POST");
	      out.println("</body>");
	      out.println("</html>");
	}

	/**
	 * Este metodo es llamado cuando la informacion le hes enviada a
	 * la servlet por el metodo POST a traves del protocolo HTTP.
	 * Imprime el formulario a traves del cual
	 * el usuario podra enviar datos y por el cual
	 * se le mostrara la informacion al usuario.
	 * @param request contiene la peticion del usuario sobre la servlet
	 * @param response representa la respuesta de la servlet a la peticion
	 *        actual.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
		out.println("<meta name=\"Author\" content=\"Manuel Martin Gonzalez\">");
		out.println("<meta name=\"Description\" content=\"University of Almeria (Spain)\">");
		out.println("<title>Productor-Consumidor</title>");
		out.println("</head>");
		out.println("<body style=\"color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);\" alink=\"#990000\" link=\"#043a66\" vlink=\"#999900\">");
		out.println("<center>");
		out.println("<br>");
		out.println("<b> <font face=\"Arial,Helvetica\"> <font color=\"#003366\"><font size=\"+1\"> Cliente Productor-Consumidor</font></font></font></b>");
		out.println("</center> <br>");
		out.println("<form action=\"http://localhost:8080/Productor-Consumidor/Servlet\" method=\"post\">");
		out.println("<center>");
		out.println("<font face=\"Arial,Helvetica\"><font size=\"-1\">E-mail:</font></font>");
		out.println("<input name=\"email\" size=\"20\">");
		out.println("<br><br>");
		out.println("<font face=\"Arial,Helvetica\"><font size=\"-1\">Mensaje:</font></font>");
		out.println("<p>");
		out.println("<textarea name=\"mensaje\" rows=\"10\" cols=\"40\"></textarea>");
		out.println("</p>");
		out.println("<input name=\"action\" value=\"Enviar\" alt=\"Pulsar boton para enviar elemento al buffer\" type=\"submit\"/>");
		out.println("<input value=\"Reset\" alt=\"Pulsar boton para reset info.\" type=\"reset\"/>");
		out.println("<a>|</a>");
		out.println("<input name=\"action\" value=\"Recibir\" alt=\"Pulsar boton para recibir ultimo elemento del buffer\" type=\"submit\">");
		out.println("<input name=\"action\" value=\"Leer\" alt=\"Pulsar boton para leer ultimo elemento del buffer\" type=\"submit\"> ");
		out.println("<a>|</a>");
		out.println("<input name=\"action\" value=\"Ver buffer\" alt=\"Pulsar boton para ver el contenido del buffer\" type=\"submit\">");
		out.println("</center>");
		out.println("</form>");
		
		if (request.getParameter("action").compareTo("Enviar") == 0)
			if (request.getParameter("email").compareTo("") == 0) {
				out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: La dirección de correo no puede estar vacía.</font></font></font></center>");
			} else if (request.getParameter("mensaje").compareTo("") == 0) {
				out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: El contenido del mensaje no puede estar vacío.</font></font></font></center>");
			} else {
					ArrayList<String> emails = new ArrayList<String>(); // lista de emails
					ArrayList<String> elementos = new ArrayList<String>(); // lista de elementos
					emails.add(request.getParameter("email")); //para obtener valor del email
					elementos.add(request.getParameter("mensaje")); //para obtener valor del dato 
					empaquetador = new CoderXML(nombre_archivo);
					//crear archivo XML (empaquetar)
					if(empaquetador.empaquetar(emails, elementos)){
						out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"green\"><font size=\"-1\">[OK]: El documento XML se ha creado correctamente.</font></font></font></center>");
						String xsd = System.getProperty("user.home")+File.separator+"productor_consumidor_files"+File.separator+"documentoProductorConsumidor.xsd";
						Parser parser = new Parser(ruta_archivo);
						//validar sintacticamente el mensaje XML (antes de ser enviado)
						if(parser.validar()){
							out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"green\"><font size=\"-1\">[OK]: El documento XML ha sido validado sintacticamente de forma correcta.</font></font></font></center>");
							File xsd_file = new File(xsd);
							if (!xsd_file.exists()){
								out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: No se ha encontrado el esquema XSD para validar mensajes XML de Productor-Consumidor.</font></font></font></center>");
								out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: El documento XML no ha sido almacenado en el buffer del servidor.</font></font></font></center>");
							}else{
								validador = new Validador(empaquetador.getRutaArchivoXML(), xsd);
								//validar archivo XML semanticamente (antes de ser enviado)
								if(validador.validar()){
									out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"green\"><font size=\"-1\">[OK]: El documento XML ha sido validado semanticamente de forma correcta.</font></font></font></center>");
									String mensaje = new String(empaquetador.obtenerXMLComoString());
									//enviar archivo XML en forma de String
									if(bufferImpl.put(mensaje)){
										firstRead = true;
										out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"green\"><font size=\"-1\">[OK]: El documento XML ha sido almacenado correctamente en el buffer del servidor.</font></font></font></center>");
									}else{
										out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: El documento XML no ha sido almacenado en el buffer del servidor [Pila llena].</font></font></font></center>");
									}
								}else{
									out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: El documento XML no es valido.</font></font></font></center>");
									out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: El documento XML no ha sido almacenado en el buffer del servidor.</font></font></font></center>");
								}
							}
						}else{
							
						}
					}else{
						out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: No se ha podido almacenar el documento XML.</font></font></font></center>");
					}
			}
		else if (request.getParameter("action").compareTo("Recibir") == 0) {
			elem = new StringHolder();
			//obtener XML del servidor en forma de String
			if(bufferImpl.get(elem)){
				out.println("<br><center><font face=\"Arial,Helvetica\"><font size=\"-1\">Recibiendo elemento.</font></font></center>");
				empaquetador = new CoderXML(nombre_archivo);
				//crear arcivo XML recibido a partir del String
				if(empaquetador.empaquetar(elem.value)){
					desempaquetador = new DecoderXML(empaquetador.getRutaArchivoXML());
					//desempaquetar datos del archivo XML
					Pair<Boolean, ArrayList<Mensaje>> nuevo = new Pair<Boolean, ArrayList<Mensaje>>(desempaquetador.desempaquetar());
					if(nuevo.first()){
						for (Mensaje mensaje : nuevo.second()) {
							out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">Mensaje recibido:</font></font></font></center>");
							out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">E-mail: "+mensaje.getMail()+"</font></font></font></center>");
							out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">Elemento: "+mensaje.getElemento()+"</font></font></font></center>");
						}
					}else{
						out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: El mensaje no se ha desempaquetado correctamente.</font></font></font></center>");
					}
				}else{
					out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: El mensaje no se ha desempaquetado correctamente.</font></font></font></center>");
				}
			}else{
				out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: Elemento no recibido ["+elem.value+"].</font></font></font></center>");
			}
		} else if (request.getParameter("action").compareTo("Leer") == 0) {
			if(!firstRead){
				out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: Elemento no leido [Buffer vacio].</font></font></font></center>");
			}else{
				elem = new StringHolder();
				//leer XML del servidor en forma de String
				if(bufferImpl.read(elem)){
					out.println("<br><center><font face=\"Arial,Helvetica\"><font size=\"-1\">Leyendo elemento.</font></font></center>");
					empaquetador = new CoderXML(nombre_archivo);
					//crear archivo XML leido a partir del String
					if(empaquetador.empaquetar(elem.value)){
						desempaquetador = new DecoderXML(empaquetador.getRutaArchivoXML());
						//desempaquetar datos del archivo XML
						Pair<Boolean, ArrayList<Mensaje>> nuevo = new Pair<Boolean, ArrayList<Mensaje>>(desempaquetador.desempaquetar());
						if(nuevo.first()){
							for (Mensaje mensaje : nuevo.second()) {
								out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"blue\"><font size=\"-1\">Mensaje recibido:</font></font></font></center>");
								out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"blue\"><font size=\"-1\">E-mail: "+mensaje.getMail()+"</font></font></font></center>");
								out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"blue\"><font size=\"-1\">Elemento: "+mensaje.getElemento()+"</font></font></font></center>");
							}
						}else{
							out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: El mensaje no se ha desempaquetado correctamente.</font></font></font></center>");
						}
					}else{
						out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: El mensaje no se ha desempaquetado correctamente.</font></font></font></center>");
					}
				}else{
					out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: Elemento no leido [Buffer vacio].</font></font></font></center>");
				}
			}
		} else if (request.getParameter("action").compareTo("Ver buffer") == 0) {
			if(!firstRead){
				out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: Elementos no leidos [Buffer vacio].</font></font></font></center>");
			}else{
				elem = new StringHolder();
				//leer XML del servidor en forma de String (el XML contiene varios mensajes)
				if(bufferImpl.read(elem)){
					out.println("<br><center><font face=\"Arial,Helvetica\"><font size=\"-1\">Leyendo elementos.</font></font></center>");
					empaquetador = new CoderXML(nombre_archivo);
					//crear archivo XML (con todos los mensajes) leido a partir del String
					if(empaquetador.empaquetarTodos(elem.value)){
						desempaquetador = new DecoderXML(empaquetador.getRutaArchivoXML());
						//desempaquetar datos (todos los mensajes) del archivo XML
						Pair<Boolean, ArrayList<Mensaje>> nuevo = new Pair<Boolean, ArrayList<Mensaje>>(desempaquetador.desempaquetar());
						if(nuevo.first()){
							out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"green\"><font size=\"-1\">El buffer contiene "+nuevo.second().size()+" elementos.</font></font></font></center>");
							for (Mensaje mensaje : nuevo.second()) {
								out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"blue\"><font size=\"-1\">Mensaje recibido:</font></font></font></center>");
								out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"blue\"><font size=\"-1\">E-mail: "+mensaje.getMail()+"</font></font></font></center>");
								out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"blue\"><font size=\"-1\">Elemento: "+mensaje.getElemento()+"</font></font></font></center>");
							}
						}else{
							out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: Los mensajes no se han desempaquetado correctamente.</font></font></font></center>");
						}
					}else{
						out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: Los mensajes no se han desempaquetado correctamente.</font></font></font></center>");
					}
				}else{
					out.println("<br><center><font face=\"Arial,Helvetica\"><font color=\"red\"><font size=\"-1\">[ERROR]: Elementos no leidos [Buffer vacio].</font></font></font></center>");
				}
			}
		}
		else {
			out.println("<br><center><font face=\"Arial,Helvetica\"><font size=\"-1\">Accion '" + request.getParameter("action")
					+ "' no reconocida.</font></font></center>");
		}
		out.println("</body></html>");
	}

}
