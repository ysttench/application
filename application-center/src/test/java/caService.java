
import org.apache.soap.util.xml.*;
import org.apache.soap.*;
import org.apache.soap.rpc.*;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class caService{

    public static String getService(String user) {

    URL url = null;

    try {

        url=new URL("http://192.168.0.100:8080/ca3/services/caSynrochnized");

    } catch (MalformedURLException mue) {

       return mue.getMessage();

      }

          // This is the main SOAP object

    Call soapCall = new Call();

    // Use SOAP encoding

    soapCall.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);

    // This is the remote object we're asking for the price

    soapCall.setTargetObjectURI("urn:xmethods-caSynrochnized");

    // This is the name of the method on the above object

    soapCall.setMethodName("getUser");

    // We need to send the ISBN number as an input parameter to the method

    Vector soapParams = new Vector();



    // name, type, value, encoding style

    Parameter isbnParam = new Parameter("userName", String.class, user, null);

    soapParams.addElement(isbnParam);

    soapCall.setParams(soapParams);

    try {

       // Invoke the remote method on the object

       Response soapResponse = soapCall.invoke(url,"");

       // Check to see if there is an error, return "N/A"

       if (soapResponse.generatedFault()) {

           Fault fault = soapResponse.getFault();

          String f = fault.getFaultString();

          return f;

       } else {

          // read result

          Parameter soapResult = soapResponse.getReturnValue ();

          // get a string from the result

          return soapResult.getValue().toString();

       }

    } catch (SOAPException se) {

       return se.getMessage();

    }

 }
public static void main(String[] args) {
    getService("admin");
}
}