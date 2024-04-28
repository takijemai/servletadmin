package core;
import java.io.File;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class DecoderXML {

    private String ruta_archivo;

    public DecoderXML(String ruta_archivo) {
        this.ruta_archivo = ruta_archivo;
    }

    public Pair<Boolean, ArrayList<Mensaje>> desempaquetar() {
        ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new File(this.ruta_archivo));
            Element rootNode = doc.getRootElement();
            for (Element mensaje : rootNode.getChildren("mensaje")) {
                String mail = mensaje.getChildText("email");
                String elemento = mensaje.getChildText("elemento");
                Mensaje mensajeObj = new Mensaje(mail, elemento);
                mensajes.add(mensajeObj);
            }
            return new Pair<Boolean, ArrayList<Mensaje>>(true, mensajes);
        } catch (IOException | org.jdom2.JDOMException e) {
            e.printStackTrace();
            return new Pair<Boolean, ArrayList<Mensaje>>(false, null);
        }
    }

    public String getRutaArchivoXML() {
        return ruta_archivo;
    }

    public String obtenerXMLComoString() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(getRutaArchivoXML()))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine);
            }
        } catch (FileNotFoundException e1) {
            System.err.println(e1.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return sb.toString();
    }
}
