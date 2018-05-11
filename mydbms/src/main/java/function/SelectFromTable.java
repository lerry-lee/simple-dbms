package function;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import javax.smartcardio.ATR;
import java.io.File;
import java.util.Iterator;
import java.util.List;

public class SelectFromTable {

    //select * from 表名
    public static void selectFromTb(String dbName, String tbName) throws DocumentException {
        //数据库是否合法
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        //若表存在，则得到表的最后一个文件下标
        String file_num = IsLegal.isTable(tbName);

        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num=""+j;
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/"+tbName+num+".xml");
            //解析xml
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            //获得根节点
            Element rootElement = document.getRootElement();
            //获得节点名为tbName的节点List
            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                Element elementNode = (Element) node;
                List<Attribute> list=elementNode.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute=(Attribute)i.next();
                    System.out.println(attribute.getName() + " " + attribute.getText());
                }
                System.out.println();
            }
        }
    }

    //select * from 表名 where 列名称=列值
    public static void selectAllFromTb(String dbName,String tbName,List<String> tmp1) throws DocumentException {
        //数据库是否合法
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        //若表存在，则得到表的最后一个文件下标
        String file_num=IsLegal.isTable(tbName);
        //标记是否找到记录
        boolean condition_find = false;
        //存where条件的condition数组
        String[] condition = new String[2];
        condition = tmp1.get(1).split("=");

        for(int j = Integer.parseInt(file_num); j >= 0; j--) {
            boolean find=false;
            String num=""+j;
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/"+tbName+num+".xml");

            //解析xml
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                find=false;
                Element node1 = (Element) node;
                List<Attribute> list=node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute=(Attribute)i.next();
                    if (attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                        find = true;
                        condition_find=true;
                        break;
                    }
                }
                if (condition_find&&find) {
                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                        Attribute attribute = (Attribute) i.next();
                        System.out.println(attribute.getName() + " " + attribute.getText());
                    }
                    System.out.println();
                }

            }
        }

        if(!condition_find)
        {
            System.out.println("未找到该记录");
            return;
        }

    }
    //select 列名称1，列名称2 from 表名
    public static void selectFromTb(String dbName, String tbName, List<String> tmp1) throws DocumentException {
        //数据库是否合法
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        //若表存在，则得到表的最后一个文件下标
        String file_num=IsLegal.isTable(tbName);
        //标记是否找到列
        boolean find2 = false;

        for(int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
            //解析XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            //遍历所有节点
            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                Element node1 = (Element) node;
                List<Attribute> list=node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    for (int k = 0; k < tmp1.size(); k++) {
                        if (attribute.getName().equals(tmp1.get(k))) {
                            find2 = true;
                            System.out.println(attribute.getName() + " " + attribute.getText());
                        }
                    }
                }
            }
        }
            if (!find2) {
                System.out.println("未找到列");
                return;
            }
        }

    //select 列名称1，列名称2 from tbName where 列名称=列值
    public static void selectFromTb(String dbName, String tbName, List<String> tmp1, List<String> tmp2) throws DocumentException {
        //if database illegal
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        //若表存在，则得到表的最后一个文件下标
        String file_num=IsLegal.isTable(tbName);
        //存where条件的condition数组
        String[] condition = new String[0];
        condition = tmp2.get(1).split("=");
        boolean condition_find = false;
        boolean element_find = false;
        boolean find1 = false;
        boolean find2 = false;

        for(int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
            //解析XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            condition_find = false;
            element_find = false;
            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                find1 = false;
                find2 = false;
                Element node1 = (Element) node;
                List<Attribute> list=node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    if (attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                        find1 = true;
                        condition_find = true;
                        break;
                    }
                }
                if (find1 && !find2) {
                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                        Attribute attribute = (Attribute) i.next();
                        for (int k = 0; k < tmp1.size(); k++) {
                            if (attribute.getName().equals(tmp1.get(k))) {
                                find2 = true;
                                element_find = true;
                                System.out.println(attribute.getName() + " " + attribute.getText());
                            }
                        }
                    }
                }

            }
        }
        if (!element_find) {
            System.out.println("未找到记录");
            return;

        }
        else if(condition_find&&!element_find)
        {
            System.out.println("未找到列");
        }
    }


}
