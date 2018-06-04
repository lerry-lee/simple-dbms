package function;

import bpulstree.BPlusTree;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class SelectDataFromTable {
    //根据参数不同调用不同的查询方法
    public static void select(String dbName, String tbName, List<String> columns, List<String> condition) throws DocumentException {

        //数据库是否为空
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        //表是否存在
        File config_file = IsLegal.isTable(dbName, tbName);
        if (config_file == null) {
            return;
        }

        //列名称为空，则查询语句为select * from 表名/select * from 表名 where 列名称=列值
        if (columns == null) {
            //条件为空，则查询语句为select * from 表名
            if (condition == null) {
                selectFromTb(dbName, tbName);
            }
            //条件不为空，则查询语句为select * from 表名 where 列名称=列值
            else {
                String[] key_value = condition.get(1).split("=");
                String key = key_value[0];
                //如果表没有建立主键索引或者不是通过主键查询，调用未创建索引的方法
                if (!IsLegal.hasIndex(dbName, tbName) || !IsLegal.isIndex(config_file, key)) {
                    selectAllFromTb(dbName, tbName, condition);
                } else {
                    System.out.println("带索引的查询");
                    selectWithIndex(dbName, tbName, condition);
                }
            }
        }
        //列名称不为空，则查询语句为select 列名称1，列名称2 from 表名 where 列名称=列值/select 列名称1，列名称2 from 表名
        else {
            //条件为空，则查询语句为select 列名称1，列名称2 from 表名
            if (condition.size() == 0) {
                selectFromTb(dbName, tbName, columns);
            }
            //条件不为空，则查询语句为select 列名称1，列名称2 from 表名 where 列名称=列值
            else {
                String[] key_value = condition.get(1).split("=");
                String key = key_value[0];
                //如果表没有建立主键索引或者不是通过主键查询，调用未创建索引的方法
                if (!IsLegal.hasIndex(dbName, tbName) || !IsLegal.isIndex(config_file, key)) {
                    selectFromTb(dbName, tbName, columns, condition);
                } else {
                    System.out.println("带索引的查询");
                    selectWithIndex(dbName, tbName, columns, condition);
                }
            }
        }

    }

    //select * from 表名
    public static void selectFromTb(String dbName, String tbName) throws DocumentException {
        //若表存在，则得到最后一张子表的下标
        String file_num = IsLegal.lastFileName(dbName, tbName);

        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
            //解析xml
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            //获得根节点
            Element rootElement = document.getRootElement();
            //获得节点名为tbName的节点List
            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                Element elementNode = (Element) node;
                List<Attribute> list = elementNode.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                }
                System.out.println();
            }
        }
    }

    //select * from 表名 where 列名称=列值
    public static void selectAllFromTb(String dbName, String tbName, List<String> tmp1) throws DocumentException {
        //若表存在，则得到表的最后一个文件下标
        String file_num = IsLegal.lastFileName(dbName, tbName);
        //标记是否找到记录
        boolean condition_find = false;
        boolean find;
        //存where条件的condition数组
        String[] condition = tmp1.get(1).split("=");

        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");

            //解析xml
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                find=false;
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    if (attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                        condition_find = true;
                        find=true;
                        break;
                    }
                }
                if (find) {
                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                        Attribute attribute = (Attribute) i.next();
                        System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                    }
                    System.out.println();
                }

            }
        }

        if (!condition_find) {
            System.out.println("未找到该记录");
            return;
        }

    }

    //select 列名称1，列名称2 from 表名
    public static void selectFromTb(String dbName, String tbName, List<String> tmp1) throws DocumentException {
        //若表存在，则得到表的最后一个文件下标
        String file_num = IsLegal.lastFileName(dbName, tbName);
        //标记是否找到列
        boolean columns_find = false;

        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
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
                List<Attribute> list = node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    for (int k = 0; k < tmp1.size(); k++) {
                        if (attribute.getName().equals(tmp1.get(k))) {
                            columns_find = true;
                            System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                            break;
                        }
                    }
                }
                System.out.println();
            }

        }
        if (!columns_find) {
            System.out.println("未找到列");
            return;
        }
    }

    //select 列名称1，列名称2 from 表名 where 列名称=列值
    public static void selectFromTb(String dbName, String tbName, List<String> tmp1, List<String> tmp2) throws DocumentException {
        //若表存在，则得到表的最后一个文件下标
        String file_num = IsLegal.lastFileName(dbName, tbName);
        //存where条件的condition数组
        String[] condition = new String[0];
        condition = tmp2.get(1).split("=");
        boolean condition_find = false;
        boolean element_find = false;
        boolean find1;

        for (int j = Integer.parseInt(file_num); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
            //解析XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbName);

            for (Node node : nodes) {
                find1=false;
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    if (attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                        condition_find = true;
                        find1=true;
                        break;
                    }
                }
                if (find1) {
                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                        Attribute attribute = (Attribute) i.next();
                        for (int k = 0; k < tmp1.size(); k++) {
                            if (attribute.getName().equals(tmp1.get(k))) {
                                element_find = true;
                                System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                                break;
                            }
                        }
                    }
                    System.out.println();
                }
            }
        }
        if (!condition_find) {
            System.out.println("未找到记录");
            return;

        }
        if (condition_find && !element_find) {
            System.out.println("未找到列");
        }
    }

    //建立索引后的查询select 列名称1，列名称2 from 表名 where 列名称=列值
    public static void selectWithIndex(String dbName, String tbName, List<String> tmp1, List<String> tmp2) throws DocumentException {
        //存where条件的condition数组
        String[] condition = new String[0];
        condition = tmp2.get(1).split("=");
        int key = Integer.parseInt(condition[1]);
        //找到该表索引对应的B+树
        BPlusTree myTree = CreateIndex.findTree(tbName);
        String filename = myTree.search(key);
        boolean condition_find = false;
        boolean element_find = false;

        File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + filename + ".xml");
        //解析XML
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element rootElement = document.getRootElement();

        List<Node> nodes = rootElement.selectNodes(tbName);

        for (Node node : nodes) {
            Element node1 = (Element) node;
            List<Attribute> list = node1.attributes();
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Attribute attribute = (Attribute) i.next();
                if (attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                    condition_find = true;
                    break;
                }
            }
            //如果找到该条记录
            if (condition_find) {
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    for (int k = 0; k < tmp1.size(); k++) {
                        //如果找到要查询的列
                        if (attribute.getName().equals(tmp1.get(k))) {
                            element_find = true;
                            System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                            break;
                        }
                    }
                }
                System.out.println();
                break;
            }
        }
        if (!condition_find) {
            System.out.println("查询失败，未找到记录");
            return;

        }
        if (condition_find && !element_find) {
            System.out.println("查询失败，未找到列");
        }
    }

    //建立索引后的查询select * from 表名 where 列名称=列值
    public static void selectWithIndex(String dbName, String tbName, List<String> tmp1) throws DocumentException {
        //存where条件的condition数组
        String[] condition = tmp1.get(1).split("=");
        int key = Integer.parseInt(condition[1]);
        //找到索引的B+树
        BPlusTree myTree = CreateIndex.findTree(tbName);
        String filename = myTree.search(key);
        boolean condition_find = false;

        File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + filename + ".xml");
        //解析XML
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element rootElement = document.getRootElement();

        List<Node> nodes = rootElement.selectNodes(tbName);

        for (Node node : nodes) {
            Element node1 = (Element) node;
            List<Attribute> list = node1.attributes();
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Attribute attribute = (Attribute) i.next();
                if (attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                    condition_find = true;
                    break;
                }
            }
            if (condition_find) {
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    System.out.print(attribute.getName() + "=" + attribute.getText() + " ");
                }
                System.out.println();
                break;
            }
        }
        if (!condition_find) {
            System.out.println("未找到记录");
            return;

        }

    }


}
