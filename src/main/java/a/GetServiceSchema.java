package a;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ofer on 26/09/17.
 */
public class GetServiceSchema {



    public String getSchema(String xml) throws Exception{

        String test = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:c=\"http://c.b.a\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <c:add>\n" +
                "         <!--Optional:-->\n" +
                "         <c:n1>?</c:n1>\n" +
                "         <!--Optional:-->\n" +
                "         <c:n2>?</c:n2>\n" +
                "      </c:add>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        String fixed = "";
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "//*[not(*)]";
        Document doc = GetXMLFromWsdl.buildXML(test);
        List<String> list = new ArrayList<String>();
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodeList.getLength(); i++) {

//                   String s = getXPath(nodeList.item(i));
//                   if(s.endsWith("xs:element")){
//                         System.out.println(getXPath(nodeList.item(i)));
//                         Node node = (nodeList.item(i).getAttributes().getNamedItem("name"));
//                         if (node != null) {
//                                s = node.getNodeValue();
//                                System.out.println(s);
//                         }
////                       String y = getXPath(nodeList.item(i).getAttributes().getNamedItem("name"));
////                       System.out.println(y);
//                   }
            System.out.println(getXPath(nodeList.item(i)));
            list.add(getXPath(nodeList.item(i)));
        }
      /*  int counter = 1;
        for (String current : list) {
            if(current.contains("RequestChannel.Channel")){
                String newString = current.substring(current.lastIndexOf("RequestChannel.Channel") );
                System.out.println(newString);
                counter++;
            }  if(current.contains("ResponseChannel.Channel")){
                String newString = current.substring(current.lastIndexOf("RequestChannel.Channel") );
                System.out.println(newString);
            }
        }*/
        return null;

    }

    public String getSchema2(String xml) throws Exception{
        GetXMLFromWsdl xmlFromWsdl = new GetXMLFromWsdl();
        Document doc = GetXMLFromWsdl.buildXML(xml);
        NodeList list = doc.getElementsByTagName("xs:element");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = (list.item(i).getAttributes().getNamedItem("name"));
            if (node != null) {
                String s = node.getNodeValue();
                System.out.println(s);
            }
        }
        return null;

    }
    public String getXPath(Node node){
        Node parent = node.getParentNode();
        if(parent ==null){
            return node.getNodeName();
        }
//            if(node instanceof Element){
//                   String data = ((Element) node).getAttribute("name");
//                   System.out.println("AAAA" +data);
//            }
        return getXPath(parent) + "." +node.getNodeName() ;
//            return null;
    }

    public static void main(String[] args) {

        GetServiceSchema schema = new GetServiceSchema();

        try {
//            String data = new String(Files.readAllBytes(Paths
//                    .get("C:/Users/ofers/Desktop/consumerDreditxXmlReq.txt")));
//                   schema1
//                   consumerDreditxXmlReq
//                   .get("C:/Users/ofers/Desktop/LDP related/test6.txt")));
//                   String out = xml.hideContent(data);
//                   System.out.println(data);
            String out = schema.getSchema("sdf");
//            System.out.println(out);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

}
