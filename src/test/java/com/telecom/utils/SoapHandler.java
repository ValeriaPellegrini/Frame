package com.telecom.utils;
/** It's main use is the handling of a SOAP protocol test case in a multithread safe manner.
 * @author Marcelo Luna.
 * @version 1.0
 * @since 1.0
 */
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

public final class SoapHandler {
    private static ThreadLocal <SoapValues> soapValues = new ThreadLocal <SoapValues>();

    /** Sets the web service address in a new object that will hold the required values to do a Soap request.
     *
     * @param webServiceAdress The web service address.
     */
    public static void setSoapWebServiceAdress (String webServiceAdress) {
        soapValues.set(new SoapValues());
        soapValues.get().setSoapWebServiceAdress(webServiceAdress);
    }
    
	/** Clears ThreadLocal stored values
	 * 
	 */
	public static void clearSoapValues() {
		if (soapValues.get()!=null) {
			soapValues.remove();
		}
	}

    /** Loads the XML file that will be send to the web service as part of a request in an object that will hold the required values to do a Soap request. The file should be located inside the folder SoapRequest in the project.
     *
     * @param xmlPath The XML file name. Could have an inner sub folder name with double backslash.
     * Ex: SoapCRM\\Add.xml
     */
    public static void loadXml (String xmlPath) {
        try {
            InputStream soapRequestfile = new FileInputStream(".\\SoapRequest\\"+xmlPath);

            soapValues.get().setXmlContentAsString(new String(soapRequestfile.readAllBytes()));

            soapRequestfile.close();
        } catch (Exception exp) {
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    /** Changes the requested XML file values as needed.
     *
     * @param data A Key-Value Map representing the values as they are called in the XML as 'keys' of the Map, and the values that should end up in the XML as 'values' of the Map.
     */
    public static void changeXmlValues(Map<String, String> data) {
        data.forEach((key,value)->{
            final String actualValue = soapValues.get().getXmlContentAsString();
            soapValues.get().setXmlContentAsString(actualValue.replaceFirst(key, value));
        });
    }

    /** Makes the actual request to the SOAP service and stores the response in a related object that stores all the information about the request.
     *
     */
    public static void doPostRequest() {
        try {
            String xmlContentAsString = soapValues.get().getXmlContentAsString();

            InputStream soapRequestfile = new ByteArrayInputStream(xmlContentAsString.getBytes());

            CloseableHttpClient client = HttpClients
                    .custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            HttpPost request = new HttpPost(soapValues.get().getSoapWebServiceAdress());

            request.addHeader("Content-Type","text/xml");

            request.addHeader("SOAPAction","'#POST'");

            request.setEntity(new InputStreamEntity(soapRequestfile));

            soapRequestfile.close();

            CloseableHttpResponse response = client.execute(request);

            soapValues.get().setResponseCode(response.getStatusLine().getStatusCode());

            soapValues.get().setResponseMSG(response.getStatusLine().getReasonPhrase());

            soapValues.get().setResponseAsString(EntityUtils.toString(response.getEntity()));

            soapValues.get().setXmlPath();
            
            response.close();

            client.close();

        } catch (Exception exp) {
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    /** Returns the numeric code that the SOAP response yields.
     *
     * @return An 'Int' representing the HTTP response status code as listed here https://developer.mozilla.org/es/docs/Web/HTTP/Status.
     * Ex: 200, 301, 500, etc.
     */
    public static int getResponseCode() {
        return soapValues.get().getResponseCode();
    }

    /** Returns a message that clarifies the status of the SOAP response if the services have one defined.
     *
     * @return A word or phrase representing the HTTP response status possibly similar to the ones listed here https://developer.mozilla.org/es/docs/Web/HTTP/Status.
     * Ex: OK, Moved Permanently, Internal Server Error, etc.
     */
    public static String getResponseMSG() {
        return soapValues.get().getResponseMSG();
    }

    /** Returns a specific tag from the response XML.
     *
     * @param tag The tag name depicted as a sequence of nodes separated with dot signs.
     * Ex: 'Envelope.Body.SubtractResponse.SubtractResult'
     * @return A String object with the requested tag contents.
     */
    public static String getTagFromResponse(String tag) {
        return soapValues.get().getTagFromResponse(tag);
    }

    public static String getRequest() {
        return soapValues.get().getXmlContentAsString();
    }

    public static List<String> getListFromResponse(String tag) {
        return soapValues.get().getListFromResponse(tag);
    }

    /** Returns the xml Response as a String.
     *
     * @return A String object with the response of the service request.
     */
    public static String getResponseRequest() {
        return soapValues.get().getResponseAsString();
    }
}