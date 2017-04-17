package org.xcylite.fitnesse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;


public class SoapFitnesseAdapter {
	private String inputURL = null;
	private String SoapAction = null;
	private String trustAll = null;
	private static SSLSocketFactory sslSocketFactory = null;

	public SoapFitnesseAdapter(String url, String SOAPAction, String trustAll)
			throws Exception {
		inputURL = url;
		SoapAction = SOAPAction;
		this.trustAll = trustAll;
	}

	public String makeRequest(String xmlString)
			throws Exception {
		/*String soapXmlString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ><SOAP-ENV:Body>\n" +
			                       xmlString +
								   "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
		 */
		System.out.println("SoapFitnesseAdapter : makeRequest ");
		String soapXmlString = xmlString;
		StringBuffer input = new StringBuffer();
		String returnString;
		HttpURLConnection httpConn = null;       	        

		try {
			System.out.println("Input XML:\n" + soapXmlString);
	//		TrustManager[] trustAllCerts = disableSSLCertificateChecking();

			// Create the connection where we're going to send the file.
			URL url = new URL(inputURL);
			URLConnection connection = url.openConnection();
			httpConn = (HttpURLConnection) connection;

			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty("Content-Length",
					String.valueOf(soapXmlString.length()));
			httpConn.setRequestProperty("Content-Type",
					"text/xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction", SoapAction);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			
			  
			
			//Certs
			
			if (trustAll.equalsIgnoreCase("Y")) {
				setAcceptAllVerifier((HttpsURLConnection)httpConn);
			}

			// Everything's set up; send the XML that was read in to b.
			OutputStream out = httpConn.getOutputStream();
			out.write(soapXmlString.getBytes());
			out.close();

			// Read the response and write it to standard out.
			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());

			BufferedReader in = new BufferedReader(isr);

			String inputLine;

			while ((inputLine = in.readLine()) != null)
				input.append(inputLine + "\n");

			in.close();
		} catch (IOException ie) {
			System.out.println(" io exception " + ie + " httpConn " + httpConn);
			try {
				if (httpConn != null && httpConn.getResponseCode() == 500) {

					// Read the response and write it to standard out.
					InputStreamReader isr = new InputStreamReader(httpConn.getErrorStream());
					BufferedReader in = new BufferedReader(isr);

					String inputLine;

					while ((inputLine = in.readLine()) != null)
						input.append(inputLine + "\n");

					in.close();
				}
			} catch (Exception e2) {
				System.out.println("Problem calling SOAP Provider");
				e2.printStackTrace();
			}        	


		} catch (Exception e) {
			System.out.println("Problem calling SOAP Provider");
			e.printStackTrace();
		}

		returnString = input.toString();
		//int beginIndex = returnString.indexOf("<iaa:Message");
		//int endIndex = returnString.indexOf("</iaa:Message>");
		System.out.println("Return XML: \n" +returnString);

		//return returnString.substring(beginIndex, endIndex+14);


		return returnString;
	}
	
    private TrustManager[] disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return trustAllCerts;
    }

    /**
     * Overrides the SSL TrustManager and HostnameVerifier to allow
     * all certs and hostnames.
     * WARNING: This should only be used for testing, or in a "safe" (i.e. firewalled)
     * environment.
     * 
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    protected static void setAcceptAllVerifier(HttpsURLConnection connection) throws NoSuchAlgorithmException, KeyManagementException {

        // Create the socket factory.
        // Reusing the same socket factory allows sockets to be
        // reused, supporting persistent connections.
        if( null == sslSocketFactory) {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, ALL_TRUSTING_TRUST_MANAGER, new java.security.SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
        }

        connection.setSSLSocketFactory(sslSocketFactory);

        // Since we may be using a cert with a different name, we need to ignore
        // the hostname as well.
        connection.setHostnameVerifier(ALL_TRUSTING_HOSTNAME_VERIFIER);
    }

    private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[] {
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }
    };

    private static final HostnameVerifier ALL_TRUSTING_HOSTNAME_VERIFIER  = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

}
