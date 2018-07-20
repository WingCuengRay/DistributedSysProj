package FEAPP;


/**
* FEAPP/_FEStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/Ling/Desktop/eclipse-workspace/FE_DRRS_Group/src/FE.idl
* Tuesday, November 28, 2017 5:21:42 PM EST
*/

public class _FEStub extends org.omg.CORBA.portable.ObjectImpl implements FEAPP.FE
{

  public boolean Login (String id)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("Login", true);
                $out.write_string (id);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return Login (id        );
            } finally {
                _releaseReply ($in);
            }
  } // Login

  public boolean[] createRoom (String id, String room_Number, String date, String[] Time_Slots)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("createRoom", true);
                $out.write_string (id);
                $out.write_string (room_Number);
                $out.write_string (date);
                FEAPP.ArrayStringHelper.write ($out, Time_Slots);
                $in = _invoke ($out);
                boolean $result[] = FEAPP.ArrayBooleanHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return createRoom (id, room_Number, date, Time_Slots        );
            } finally {
                _releaseReply ($in);
            }
  } // createRoom

  public boolean[] deleteRoom (String id, String room_Number, String date, String[] Time_Slots)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("deleteRoom", true);
                $out.write_string (id);
                $out.write_string (room_Number);
                $out.write_string (date);
                FEAPP.ArrayStringHelper.write ($out, Time_Slots);
                $in = _invoke ($out);
                boolean $result[] = FEAPP.ArrayBooleanHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return deleteRoom (id, room_Number, date, Time_Slots        );
            } finally {
                _releaseReply ($in);
            }
  } // deleteRoom

  public String bookRoom (String id, String campusName, String room_Number, String date, String Time_Slots)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("bookRoom", true);
                $out.write_string (id);
                $out.write_string (campusName);
                $out.write_string (room_Number);
                $out.write_string (date);
                $out.write_string (Time_Slots);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return bookRoom (id, campusName, room_Number, date, Time_Slots        );
            } finally {
                _releaseReply ($in);
            }
  } // bookRoom

  public String getAvailableTimeSlot (String id, String date)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getAvailableTimeSlot", true);
                $out.write_string (id);
                $out.write_string (date);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getAvailableTimeSlot (id, date        );
            } finally {
                _releaseReply ($in);
            }
  } // getAvailableTimeSlot

  public boolean cancelBooking (String id, String bookingID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("cancelBooking", true);
                $out.write_string (id);
                $out.write_string (bookingID);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return cancelBooking (id, bookingID        );
            } finally {
                _releaseReply ($in);
            }
  } // cancelBooking

  public String changeReservation (String id, String bookingID, String campusName, String room_Number, String Time_Slots)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("changeReservation", true);
                $out.write_string (id);
                $out.write_string (bookingID);
                $out.write_string (campusName);
                $out.write_string (room_Number);
                $out.write_string (Time_Slots);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return changeReservation (id, bookingID, campusName, room_Number, Time_Slots        );
            } finally {
                _releaseReply ($in);
            }
  } // changeReservation

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:FEAPP/FE:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _FEStub
