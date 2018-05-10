package function;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectFromTable {

    //select * from tableName where property=value
    public static void selectAllFromTb(String dbName,String tableName,List<String> tmp1) throws DocumentException {
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
        //condition string array
        String[] condition = new String[2];
        condition = tmp1.get(1).split("=");
//        System.out.println(tmp1.get(0)+" "+tmp1.get(1));
//        System.out.println(condition[0]+" "+condition[1]);

        SAXReader reader = new SAXReader();
        //read xml to document
        Document document = reader.read(file);
        //get xml root node
        Element rootElement = document.getRootElement();
        //set an element to traverse
        Element element;
//        Element fooElement=rootElement.element(tableName);

        //traverse all node
        //Element targert=fooElement.element(condition[0]);
        boolean condition_find = false;
        List<Node> nodes = rootElement.selectNodes(tableName);

        for (Node node : nodes) {
            boolean find1=false;
            Element node1 = (Element) node;
            for (Iterator i = node1.elementIterator(); i.hasNext(); ) {
                element = (Element) i.next();

                if (element.getName().equals(condition[0]) && element.getText().equals(condition[1])) {
                    find1 = true;
                    condition_find=true;
                    break;
                }
            }
            if (find1) {
                for (Iterator i = node1.elementIterator(); i.hasNext(); ) {
                    element = (Element) i.next();
                    System.out.println(element.getName() + " " + element.getText());
                }
            }

        }

        if(!condition_find)
        {
            System.out.println("not find");
            return;
        }

    }
    //select property1,property2 from tableName
    public static void selectFromTb(String dbName, String tableName, List<String> tmp1) throws DocumentException {
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
        //condition string array
        String[] condition = new String[0];
        //System.out.println(condition[0]+" "+condition[1]);

        SAXReader reader = new SAXReader();
        //read xml to document
        Document document = reader.read(file);
        //get xml root node
        Element rootElement = document.getRootElement();
        //set an element to traverse
        Element element;
        boolean find2 = false;
        //traverse all node
        List<Node> nodes = rootElement.selectNodes(tableName);

        for (Node node : nodes) {
            Element node1 = (Element) node;
                for (Iterator i = node1.elementIterator(); i.hasNext(); ) {
                    element = (Element) i.next();
                    for (int j = 0; j < tmp1.size(); j++) {
                        if (element.getName().equals(tmp1.get(j))) {
                            find2 = true;
                            System.out.println(element.getName() + " " + element.getText());
                        }
                    }
                }
            }
            if (!find2) {
                System.out.println("not find");
                return;
            }
        }

    //select property1,property2 from tableName where propertyI=valueI
    public static void selectFromTb(String dbName, String tableName, List<String> tmp1, List<String> tmp2) throws DocumentException {
        //if database illegal
        if (!IsDatabase.idDatabase()) {
            return;
        }
        //if table exist
        File file = new File("./mydatabase/" + dbName + "/" + tableName + ".xml");

        if(!file.exists()){
            System.out.println(tableName+" does not exist");
            return;
        }
        //condition string array
        String[] condition = new String[0];
        condition = tmp2.get(1).split("=");
        //System.out.println(condition[0]+" "+condition[1]);

        SAXReader reader = new SAXReader();
        //read xml to document
        Document document = reader.read(file);
        //get xml root node
        Element rootElement = document.getRootElement();
        //set an element to traverse
        Element element;
//        Element fooElement=rootElement.element(tableName);

        //traverse all node
        //Element targert=fooElement.element(condition[0]);
        boolean condition_find = false;
        boolean element_find=false;
        List<Node> nodes = rootElement.selectNodes(tableName);

        for (Node node : nodes) {
            boolean find1=false;
            boolean find2=false;
            Element node1 = (Element) node;
            for (Iterator i = node1.elementIterator(); i.hasNext(); ) {
                element = (Element) i.next();
                //System.out.println(element.getName()+" "+element.getText());
                if (element.getName().equals(condition[0]) && element.getText().equals(condition[1])) {

                    find1 = true;
                    condition_find=true;
                    break;
                }
            }
            if (find1&&!find2) {
                for (Iterator i = node1.elementIterator(); i.hasNext(); ) {
                    element = (Element) i.next();
                    for (int j = 0; j < tmp1.size(); j++) {
                        if (element.getName().equals(tmp1.get(j))) {
                            find2=true;
                            element_find=true;
                            System.out.println(element.getName() + " " + element.getText());
                        }
                    }
                }
            }

        }
        if (!element_find) {
            System.out.println("condition not find");
            return;

        }
        else if(condition_find&&!element_find)
        {
            System.out.println("property columns not exist");
        }
    }

    //select * from tableName
    public static void selectFromTb(String dbName, String tableName) throws DocumentException {
//        if database illegal
        if (!IsDatabase.idDatabase()) {
            return;
        }
        //if table exist
        File file = new File("./mydatabase/" + dbName + "/" + tableName + ".xml");

        if(!file.exists()){
            System.out.println(tableName+" does not exist");
            return;
        }

        SAXReader reader = new SAXReader();
        //read xml to document
        Document document = reader.read(file);
        //get xml root node
        Element rootElement = document.getRootElement();
        //get nodes name=tableName
        List<Node> nodes=rootElement.selectNodes(tableName);

        for(Node node:nodes){
            Element elementNode=(Element)node;
            Element element;
            for(Iterator i=elementNode.elementIterator();i.hasNext();){
                element=(Element)i.next();
                System.out.println(element.getName()+" "+element.getText());
            }
            System.out.println();
        }
    }
}
