package a;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ofer on 26/09/17.
 */
public class GetXMLFromWsdl {

    static Set<String> schemaList = new HashSet<String>();


    public String getUrlContent(String sourceUrl) {

        String xml = null;
        try {

            // get URL content
            URL url = new URL(sourceUrl);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            // build the xml as a string
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            xml = builder.toString();
            br.close();

            schemaList.add(xml);
//            System.out.println(xml);
            this.getXmlDocument(xml);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }


    public void getXmlDocument(String xml) throws Exception {

        //build Xml doc from string
        Document doc = this.buildXML(xml);
        doc.getDocumentElement().normalize();

        //check if the xml is WSDL or xsd:

        //for wsdl
        NodeList list = doc.getElementsByTagName("xsd:import");
        if (list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {

                Element el = (Element) list.item(i);
                //get the url for the XSDs
                String s = el.getAttribute("schemaLocation");
                if (s != null) {
                    //get the xml content
                    this.getUrlContent(s);
                }
            }
        }
        //for xsd
        else {
            list = doc.getElementsByTagName("xs:import");
            for (int i = 0; i < list.getLength(); i++) {

                Element el = (Element) list.item(i);

                String s = el.getAttribute("schemaLocation");
                if (s != null) {
                    this.getUrlContent(s);
                }
            }

        }
    }


    public static Document buildXML(String xmlString) throws Exception {

        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(new InputSource(new StringReader(xmlString)));
        return doc;
    }

    public static void main(String[] args) throws Exception {
//        GetXMLFromWsdl get = new GetXMLFromWsdl();
//        GetServiceSchema getSchema = new GetServiceSchema();
        final String wsdl1 ="http://www.webservicex.com/globalweather.asmx?wsdl";

//        String url = "http://192.22.10.18:7001/JerusalemBank/ADA_Services/proxy_services/ArchiveData?wsdl";
//        String url1 = "http://192.22.10.18:7001/JerusalemBank/InternetServices/proxy_services/ConsumerCredit?wsdl";
//     String url ="http://192.22.10.18:7001/JerusalemBank/ADA_Services/proxy_services/ArchiveData?SCHEMA%2FJerusalemBank%2FADA+Services%2FSchemas%2FArchiveData_XSD";
//        String output = get.getUrlContent(wsdl1);
//        int count = 1;
//        for (String xml : schemaList) {
//            System.out.println(count);
//            System.out.println(xml);
////            getSchema.getSchema(xml);
//            count++;
//        }
//        WSDLParser parser = new WSDLParser();
//
//        Definitions wsdl = parser.parse(wsdl1);
//
//        StringWriter writer = new StringWriter();
//
//        //SOAPRequestCreator constructor: SOARequestCreator(Definitions, Creator, MarkupBuilder)
//        SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestTemplateCreator(), new MarkupBuilder(writer));
//
//        //creator.createRequest(PortType name, Operation name, Binding name);
//        creator.createRequest("textMatching", "create", "textMatchingBinding");
//        System.out.println(writer);

        WsdlProject project = new WsdlProject();
        WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, "https://svn.apache.org/repos/asf/airavata/sandbox/xbaya-web/test/Calculator.wsdl");
        WsdlInterface wsdl = wsdls[0];
        for (Operation operation : wsdl.getOperationList()) {
            WsdlOperation wsdlOperation = (WsdlOperation) operation;
            System.out.println("OP:"+wsdlOperation.getName());
            System.out.println("Request:");
            System.out.println(wsdlOperation.createRequest(true));
            System.out.println("Response:");
            System.out.println(wsdlOperation.createResponse(true));
        }


    }
}