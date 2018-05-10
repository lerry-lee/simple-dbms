package function;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class UpdateTable {
    public static void updateTable(String dbName, String tableName, List<List<String>> tmp) throws DocumentException, IOException {
        //if database illegal
        if (!IsDatabase.idDatabase()) {
            return;
        }
        //if table exist
        File file = new File("./mydatabase/" + dbName + "/" + tableName + ".xml");

        if (!file.exists()) {
            System.out.println(tableName + " does not exist");
            return;
        }
        //create document
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        //get root
        Element root = document.getRootElement();

        //List<List<String>> tmp3=new ArrayList<List<String>>();
        //String[][] tmp4=new String[tmp.get(0).size()][2];
        String[] tmp1 = new String[tmp.get(0).size()];
        String[] tmp2 = new String[1];
        for (int i = 0; i < tmp.get(0).size(); i++) {
            //tmp1[i] = tmp.get(0).get(i);
            //tmp3.get(i)= Arrays.asList(tmp.get(0).get(i).split("="));
            tmp1 = tmp.get(0).get(i).split("=");
        }
        tmp2 = tmp.get(1).get(0).split("=");
        //set node to traverse
        Element element;
        Boolean find = false;

        List<Node> nodes = root.selectNodes(tableName);
        for (Node node : nodes) {
            Element currentNode = (Element) node;
            for (Iterator i = currentNode.elementIterator(); i.hasNext(); ) {
                element = (Element) i.next();
                if (element.getName().equals(tmp2[0]) && element.getText().equals(tmp2[1])) {
                    find = true;
                    break;
                }
            }
            if (find) {
                for (int j = 0; j < tmp.get(0).size(); j++) {
                    tmp1 = tmp.get(0).get(j).split("=");
                    for (Iterator i = currentNode.elementIterator(); i.hasNext(); ) {
                        element = (Element) i.next();
                        if (element.getName().equals(tmp1[0])) {
                            element.setText(tmp1[1]);
                        }
                    }
                }
                break;
            }
        }


        //write IO
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(
                new OutputStreamWriter(new FileOutputStream("./mydatabase/" + dbName + "/" + tableName + ".xml")), format);
        writer.write(document);
        writer.close();
        System.out.println("update successfully");
    }
}
