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
import java.util.Iterator;
import java.util.List;

public class DeleteFromTable {
    public static void deleteFromTable(String dbName,String tableName,List<String> tmp) throws DocumentException, IOException {
        //if database illegal
        if(!IsDatabase.idDatabase()){
            return;
        }
        //if table exist
        File file = new File("./mydatabase/" + dbName + "/" + tableName + ".xml");

        if(!file.exists()){
            System.out.println(tableName+" does not exist");
            return;
        }
        //create document
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        //get root
        Element root=document.getRootElement();

        String[] tmp1=new String[2];
        //String[] tmp2=new String[1];
            tmp1=tmp.get(0).split("=");

        //set node to traverse
        Element element;
        Boolean find=false;

        List<Node> nodes=root.selectNodes(tableName);
        for(Node node:nodes){
            Element currentNode=(Element)node;
            for(Iterator i = currentNode.elementIterator(); i.hasNext();){
                element=(Element)i.next();
                if(element.getName().equals(tmp1[0])&&element.getText().equals(tmp1[1])){
                    find=true;
                    break;
                }
            }
            if(find){
                root.remove(currentNode);
                break;
        }

    }
    if(!find){
            System.out.println("not find");
            return;
        }


        //write IO
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(
                new OutputStreamWriter(new FileOutputStream("./mydatabase/"+dbName+"/"+tableName+".xml")), format);
        writer.write(document);
        writer.close();
        System.out.println("delete successfully");
    }
}

