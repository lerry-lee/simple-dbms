package function;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class InsertIntoTable {

    public static void insertIntoTable(String dbName, String t1, List<String> tmp1, List<String> tmp2) throws DocumentException, IOException {
        //if database illegal
        if(!IsDatabase.idDatabase()){
            return;
        }
        //if table exist
        File file=new File("./mydatabase/"+dbName+"/"+t1+".xml");

        if(!file.exists()){
            System.out.println(t1+" does not exist");
            return;
        }
        //create document object
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(file);
        //System.out.println("open table");

        //get root node
        Element rootElement=document.getRootElement();
        //create child node
        Element childElement=rootElement.addElement(t1);
        for(int i=1;i<tmp1.size();i++){
            childElement.addElement(tmp1.get(i)).setText(tmp2.get(i));
        }
        //System.out.println("write data");
        OutputFormat outputFormat=OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        XMLWriter xmlWriter;
        xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/"+dbName+"/"+t1+".xml")),outputFormat);
        xmlWriter.write(document);
        xmlWriter.close();
        System.out.println("insert successfully");

    }
    public static void inertIntoTable(String dbName,String t1,List<String> tmp) throws DocumentException, IOException {
        //if database illegal
        if(!IsDatabase.idDatabase()){
            return;
        }
        //create document object
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read("./mydatabase/"+dbName+"/"+t1+".xml");
        //System.out.println("open table");

        //get root node
        Element rootElement=document.getRootElement();
        Element childElement=rootElement.element(t1);
        Element element;
        //node name array
        String[] nodesName=new String[childElement.elements().size()];
        //get all nodes name
        int j=0;
        for(Iterator i=childElement.elementIterator();i.hasNext();j++){
            element=(Element)i.next();
            nodesName[j]=element.getName();
            System.out.println(nodesName[j]);
        }
        childElement=rootElement.addElement(t1);
        for(int i=1;i<tmp.size();i++){
            childElement.addElement(nodesName[i-1]).setText(tmp.get(i));
        }
        //System.out.println("write data");
        OutputFormat outputFormat=OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        XMLWriter xmlWriter;
        xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/"+dbName+"/"+t1+".xml")),outputFormat);
        xmlWriter.write(document);
        xmlWriter.close();
        System.out.println("insert successfully");

    }

}
