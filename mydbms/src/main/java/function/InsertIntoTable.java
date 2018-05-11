package function;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

public class InsertIntoTable {

    //insert into 表名(列名称1，列名称2) values(列值1，列值2);
    public static void insertIntoTable(String dbName, String tbname, List<String> tmp1, List<String> tmp2) throws DocumentException, IOException {
        //数据库是否合法
        if (!IsLegal.isDatabase()) {
            return;
        }
        //表是否存在
        File file = new File("./mydatabase/" + dbName + "/" + tbname + "");
        if (!file.exists()) {
            System.out.println(tbname + "表不存在");
            return;
        }
        //打开配置文件，获取文件名作为写入文件
        File config_file = new File("./mydatabase/" + dbName + "/" + tbname + "/" + tbname + "-config.xml");
        SAXReader config_file_reader = new SAXReader();
        Document config_file_document = config_file_reader.read(config_file);
        Element write_file_element = (Element) config_file_document.getRootElement().selectSingleNode(tbname);

        //若有可插入文件，则插入数据到该文件
        String write_file_name;
        if (write_file_element.selectNodes("insertable").size() > 0) {
            write_file_name = tbname + write_file_element.selectSingleNode("insertable").getText();
            //创建写入对象，获取记录数量
            File write_file = new File("./mydatabase/" + dbName + "/" + tbname + "/" + write_file_name + ".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(write_file);
            Element rootElement = document.getRootElement();
            Element element = rootElement.addElement(tbname);
            for (int i = 1; i < tmp1.size(); i++) {
                element.addAttribute(tmp1.get(i), tmp2.get(i));
            }
            //若可插入文件记录数>=10,更新配置信息
            List<Node> nodes = document.getRootElement().selectNodes(tbname);
            if (nodes.size() >= 10) {
                write_file_element.remove(write_file_element.selectSingleNode("insertable"));
                OutputFormat outputFormat2 = OutputFormat.createPrettyPrint();
                outputFormat2.setEncoding("UTF-8");
                XMLWriter xmlWriter2 = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/" + dbName + "/" + tbname + "/" + tbname + "-config.xml")), outputFormat2);
                xmlWriter2.write(config_file_document);
                xmlWriter2.close();
            }

            OutputFormat outputFormat1 = OutputFormat.createPrettyPrint();
            outputFormat1.setEncoding("UTF-8");
            XMLWriter xmlWriter1 = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/" + dbName + "/" + tbname + "/" + write_file_name + ".xml")), outputFormat1);
            xmlWriter1.write(document);
            xmlWriter1.close();
            System.out.println("插入成功");


        } else {
            //否则插入数据到最后一个文件
            write_file_name = tbname + write_file_element.getText();
            //创建写入对象，获取记录数量
            File write_file = new File("./mydatabase/" + dbName + "/" + tbname + "/" + write_file_name + ".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(write_file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbname);

            //如果该文件中记录个数>=10，新建文件写入
            if (nodes.size() >= 10) {
                Document newDocument = DocumentHelper.createDocument();
                Element newRoot = newDocument.addElement(tbname + "s");
                Element newChild = newRoot.addElement(tbname);
                //更新文件数量
                int file_amount_int = (Integer.parseInt(write_file_element.getText()) + 1);
                String file_amount = ("" + file_amount_int);
                //设置新文件名
                String newfile = tbname + file_amount;
                for (int i = 1; i < tmp1.size(); i++) {
                    newChild.addAttribute(tmp1.get(i),tmp2.get(i));
                }

                OutputFormat outputFormat = OutputFormat.createPrettyPrint();
                outputFormat.setEncoding("UTF-8");
                XMLWriter xmlWriter;
                xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/" + dbName + "/" + tbname + "/" + newfile + ".xml")), outputFormat);
                xmlWriter.write(newDocument);
                xmlWriter.close();
                System.out.println("插入成功");
                //更新配置文件中表的文件数量
                write_file_element.setText(file_amount);
                OutputFormat outputFormat1 = OutputFormat.createPrettyPrint();
                outputFormat1.setEncoding("UTF-8");
                XMLWriter xmlWriter1;
                xmlWriter1 = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/" + dbName + "/" + tbname + "/" + tbname + "-config.xml")), outputFormat1);
                xmlWriter1.write(config_file_document);
                xmlWriter1.close();

            }
            //否则在最后一条记录后插入
            else {
                //创建新节点
                Element childElement = rootElement.addElement(tbname);
                for (int i = 1; i < tmp1.size(); i++) {
                    childElement.addAttribute(tmp1.get(i),tmp2.get(i));
                }
                OutputFormat outputFormat = OutputFormat.createPrettyPrint();
                outputFormat.setEncoding("UTF-8");
                XMLWriter xmlWriter;
                xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/" + dbName + "/" + tbname + "/" + write_file_name + ".xml")), outputFormat);
                xmlWriter.write(document);
                xmlWriter.close();
                System.out.println("插入成功");
            }

        }
    }

//    //insert into table_name values(id1,name1)
//    public static void inertIntoTable(String dbName,String t1,List<String> tmp) throws DocumentException, IOException {
//        //if database illegal
//        if(!IsLegal.isDatabase()){
//            return;
//        }
//        //create document object
//        SAXReader saxReader=new SAXReader();
//        Document document=saxReader.read("./mydatabase/"+dbName+"/"+t1+".xml");
//        //System.out.println("open table");
//
//        //get root node
//        Element rootElement=document.getRootElement();
//        Element childElement=rootElement.element(t1);
//        Element element;
//        //node name array
//        String[] nodesName=new String[childElement.elements().size()];
//        //get all nodes name
//        int j=0;
//        for(Iterator i=childElement.elementIterator();i.hasNext();j++){
//            element=(Element)i.next();
//            nodesName[j]=element.getName();
//            System.out.println(nodesName[j]);
//        }
//        childElement=rootElement.addElement(t1);
//        for(int i=1;i<tmp.size();i++){
//            childElement.addElement(nodesName[i-1]).setText(tmp.get(i));
//        }
//        //System.out.println("write data");
//        OutputFormat outputFormat=OutputFormat.createPrettyPrint();
//        outputFormat.setEncoding("UTF-8");
//        XMLWriter xmlWriter;
//        xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./mydatabase/"+dbName+"/"+t1+".xml")),outputFormat);
//        xmlWriter.write(document);
//        xmlWriter.close();
//        System.out.println("insert successfully");
//
//    }
//
}
