package core;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class CoderXML {
    
    private String ruta_archivo;
    private String nombre_archivo;
    
    public CoderXML(String nombre_archivo) {
        this.nombre_archivo = nombre_archivo;
        String dir = System.getProperty("user.home") + File.separator + "productor_consumidor_files";
        File XMLdirectory = new File(dir);
        if (!XMLdirectory.exists())
            XMLdirectory.mkdir();
        this.ruta_archivo = dir + File.separator + nombre_archivo + ".xml";
    }
    
    public boolean empaquetar(ArrayList<String> emails, ArrayList<String> elementos) {
        if (emails.isEmpty() || elementos.isEmpty() || emails.size() != elementos.size()) {
            System.out.println("ERROR empty ArrayList");
            return false;
        } else {
            Element root = new Element(nombre_archivo);
            Document doc = new Document(root);
            for (int i = 0; i < elementos.size(); i++) {
                Element mensaje = new Element("mensaje");
                Element email = new Element("email").setText(emails.get(i));
                Element elemento = new Element("elemento").setText(elementos.get(i));
                mensaje.addContent(email);
                mensaje.addContent(elemento);
                root.addContent(mensaje);
            }
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            try {
                xmlOutput.output(doc, new FileWriter(ruta_archivo));
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return false;
            }
        }
        return true;
    }
    
    public boolean empaquetar(String elemXML) {
        if (ruta_archivo.compareTo("") != 0) {
            try {
                FileWriter writer = new FileWriter(ruta_archivo);
                writer.write(elemXML);
                writer.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
    
    public boolean empaquetarTodos(String elemXML) {
        if (ruta_archivo.compareTo("") != 0) {
            try {
                FileWriter writer = new FileWriter(ruta_archivo);
                writer.write(elemXML);
                writer.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
    
    public String getRutaArchivoXML() {
        return this.ruta_archivo;
    }
    
    public String obtenerXMLComoString() {
        StringBuilder sb = new StringBuilder();
        try {
            org.jdom2.input.SAXBuilder saxBuilder = new org.jdom2.input.SAXBuilder();
            Document doc = saxBuilder.build(new File(ruta_archivo));
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            sb.append(xmlOutput.outputString(doc));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return sb.toString();
    }
}
