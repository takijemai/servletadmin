package test;

import core.Validador;

/**
 * TestParserYValidador para pruebas de validacion de archivos XML
 * @author Manuel Martin Gonzalez
 * 
 */
public class TestParserYValidador {

	public static void main(String args[]) throws Exception {
		
		String nombre_xmlFile = "documentoProductorConsumidor.xml";
		
		/*
		//test validador sintactico
		Parser validador_sintactico = new Parser(nombre_xmlFile);
		
		boolean valido1 = validador_sintactico.validar();
		
		if(valido1) System.out.println("Documento XML valido sintacticamente");
		else System.out.println("Documento XML no valido sintacticamente");
		*/
		
		// test validador semantico
		String nombre_schemaFile = "documentoProductorConsumidor.xsd";
		
		Validador validador_semantico = new Validador(nombre_xmlFile, nombre_schemaFile);
		
		boolean valido2 = validador_semantico.validar();
		
		if(valido2) System.out.println("Documento XML valido semanticamente");
		else System.out.println("Documento XML no valido semanticamente");
		
	}
}