import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TestSubstring {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        doc.appendChild(doc.createElement("root"));
        
        // Test case: substring('12345', -42, 1 div 0)
        XPath xpath = new DOMXPath("substring('12345', -42, 1 div 0)");
        String result = (String) xpath.evaluate(doc);
        
        System.out.println("substring('12345', -42, 1 div 0)");
        System.out.println("Expected: '12345'");
        System.out.println("Got:      '" + result + "'");
        System.out.println("Match: " + "12345".equals(result));
    }
}
