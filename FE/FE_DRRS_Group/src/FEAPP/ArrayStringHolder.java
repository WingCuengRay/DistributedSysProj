package FEAPP;


/**
* FEAPP/ArrayStringHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/Ling/Desktop/eclipse-workspace/FE_DRRS_Group/src/FE.idl
* Tuesday, November 28, 2017 5:21:42 PM EST
*/

public final class ArrayStringHolder implements org.omg.CORBA.portable.Streamable
{
  public String value[] = null;

  public ArrayStringHolder ()
  {
  }

  public ArrayStringHolder (String[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FEAPP.ArrayStringHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FEAPP.ArrayStringHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FEAPP.ArrayStringHelper.type ();
  }

}