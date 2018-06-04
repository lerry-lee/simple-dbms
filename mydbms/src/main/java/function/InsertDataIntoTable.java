package function;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.hamcrest.core.Is;

import java.io.*;
import java.util.List;

public class InsertDataIntoTable {

    //insert into 表名(列名称1，列名称2) values(列值1，列值2);
    public static void insertIntoTable(String dbName, String tbname, List<String> tmp1, List<String> tmp2) throws DocumentException, IOException {
        //数据库是否合法
        if (!IsLegal.isDatabase()) {
            return;
        }
        //表存在则打开配置文件
        File config_file= IsLegal.isTable(dbName,tbname);
        //解析配置文件
        SAXReader config_file_reader = new SAXReader();
        Document config_file_document = config_file_reader.read(config_file);
        Element write_file_element = (Element) config_file_document.getRootElement().selectSingleNode(tbname);
        Element insertable_element=(Element)config_file_document.getRootElement().selectSingleNode("insertables");
        //判断是否存在索引，存在则插入数据后更新索引
        boolean need_updateIndex=IsLegal.hasIndex(dbName,tbname);
        //若有物理层可插入子表，则将数据插入到该物理层子表
        String write_file_name;
        if (insertable_element.selectNodes("insertable").size() > 0) {
            write_file_name = tbname + insertable_element.selectSingleNode("insertable").getText();
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
            if (nodes.size() >= CreateTable.entry_num) {
                insertable_element.remove(insertable_element.selectSingleNode("insertable"));
                CreateTable.writeIO(config_file,config_file_document);
            }
            CreateTable.writeIO(write_file,document);

            System.out.println("插入成功");
            //插入数据后更新索引
            if(need_updateIndex) {
                CreateIndex.updateIndex_insert(tbname, tmp2.get(1), write_file_name);
            }


        } else {
            //否则插入数据到物理层最后一张子表
            write_file_name = tbname + write_file_element.getText();
            //创建写入对象，获取记录数量
            File write_file = new File("./mydatabase/" + dbName + "/" + tbname + "/" + write_file_name + ".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(write_file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(tbname);

            //如果该文件中记录个数>=10，新建文件写入
            if (nodes.size() >= CreateTable.entry_num) {
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
                CreateTable.writeIO(new File("./mydatabase/" + dbName + "/" + tbname + "/" + newfile + ".xml"),newDocument);

                System.out.println("插入成功");
                //更新配置文件中表的文件数量
                write_file_element.setText(file_amount);
                CreateTable.writeIO(config_file,config_file_document);
                //插入数据后更新索引
                if(need_updateIndex) {
                    CreateIndex.updateIndex_insert(tbname, tmp2.get(1), write_file_name);
                }
            }
            //否则在最后一条记录后插入
            else {
                //创建新节点
                Element childElement = rootElement.addElement(tbname);
                for (int i = 1; i < tmp1.size(); i++) {
                    childElement.addAttribute(tmp1.get(i),tmp2.get(i));
                }
                CreateTable.writeIO(write_file,document);

                System.out.println("插入成功");
                //插入数据后更新索引
                if(need_updateIndex) {
                    CreateIndex.updateIndex_insert(tbname, tmp2.get(1), write_file_name);
                }
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
