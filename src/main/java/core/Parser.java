package core;


import java.io.File;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class Parser {
    
    private String ruta_nombre_xmlFile;
    
    public Parser(String nombre_xmlFile){
        this.ruta_nombre_xmlFile = nombre_xmlFile;
    }
    
    public boolean validar() {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new File(this.ruta_nombre_xmlFile));
            Element rootNode = doc.getRootElement();
            // Assuming "mensaje" is the root element
            for (Element mensaje : rootNode.getChildren("mensaje")) {
                String email = mensaje.getChildText("email");
                String elemento = mensaje.getChildText("elemento");
                // Do something with email and elemento
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

