package BufferApp;


/**
* BufferApp/BufferOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Buffer.idl
* dimanche 3 mars 2024 17 h 53 CET
*/

public interface BufferOperations 
{
  boolean put (String elemento);
  boolean get (org.omg.CORBA.StringHolder elemento);
  boolean read (org.omg.CORBA.StringHolder elemento);
  void shutdown ();
} // interface BufferOperations
