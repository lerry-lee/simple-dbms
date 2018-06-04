package function;

import bpulstree.BPlusTree;
import bpulstree.Main;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CreateIndex {
    static public Map<String,BPlusTree> tmpMap=new HashMap<String, BPlusTree>();
    //存储所有索引B+树的list，每次进入系统都会把索引文件以B+树形式加载到内存
    static public List<Map<String,BPlusTree>> myTree_list=new ArrayList<Map<String, BPlusTree>>();

    public static void createIndex(String dbName, String tbName, String index) throws DocumentException, IOException {
        //数据库是否为空
        if(IsLegal.isDatabaseEmpty()){
            return;
        }
        //表是否存在，存在则返回表的配置文件
        File config_file=IsLegal.isTable(dbName,tbName);
        //是否存在索引
        if(IsLegal.hasIndex(dbName,tbName)){
            System.out.println(tbName+"表已经存在主键索引");
            return;
        }

        //声明一个索引id的list
        List<List<String>> index_file=new ArrayList<List<String>>();

        SAXReader config_file_reader=new SAXReader();
        Document config_file_document=config_file_reader.read(config_file);
        Element file_name_element=(Element)config_file_document.getRootElement().selectSingleNode(tbName);
        //从最后一个表文件开始遍历
        for(int j=Integer.parseInt(file_name_element.getText());j>=0;j--) {
            String file_name=tbName+Integer.toString(j);

            //打开逻辑表的物理层文件，解析该文件，获取并列的所有记录的父节点
            File file = new File("./mydatabase/" + dbName + "/"+tbName+"/" + file_name + ".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(file);
            List<Node> nodes = document.getRootElement().selectNodes(tbName);

            //遍历所有节点，取出索引列的值和对应的文件名
            for (Node node : nodes) {

                Element element = (Element) node;
                List<Attribute> list=element.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                   Attribute attribute=(Attribute)i.next();
                    if (attribute.getName().equals(index)) {
                        //暂存每一个id对应filename
                        List<String> tmp=new ArrayList<String>();
                        tmp.add(attribute.getText());
                        tmp.add(file_name);
                        index_file.add(tmp);
                        break;
                    }
                }
            }
        }
        //构建B+树,存入B+树List
        BPlusTree myTree=Main.createBPlusTree(index_file);
        Map<String,BPlusTree> tmpMap=new HashMap<String, BPlusTree>();
        tmpMap.put(tbName,myTree);
        myTree_list.add(tmpMap);

        System.out.println("索引创建成功");
        //更新配置文件，记录表是否建立索引为true，主键名称为index
        Element index_element=(Element)config_file_document.getRootElement().selectSingleNode("index");
        index_element.setText("1");
        Element index_name=(Element)config_file_document.getRootElement().selectSingleNode("index_name");
        index_name.setText(index);
        //写入操作
        CreateTable.writeIO(config_file,config_file_document);
        //写入索引文件
        File indexFile=new File("./mydatabase/index.xml");
        SAXReader indexSAXReader=new SAXReader();
        Document indexDocument=indexSAXReader.read(indexFile);
        Element indexElement=indexDocument.getRootElement().addElement(tbName);

        for(int k=0;k<index_file.size();k++){
            indexElement.addAttribute("k"+index_file.get(k).get(0),index_file.get(k).get(1));
        }
        //写入操作
        CreateTable.writeIO(indexFile,indexDocument);
        
        return;
    }
    //找到对应表的B+树
    public static BPlusTree findTree(String tbName){
        for(int i=0;i<myTree_list.size();i++){
            if(myTree_list.get(i).containsKey(tbName)){
                return myTree_list.get(i).get(tbName);
            }
        }
        return null;
    }
    //插入新数据后修改索引文件和B+树
    public  static void updateIndex_insert(String tbName,String key,String value) throws DocumentException, IOException {
        for(int i=0;i<myTree_list.size();i++){
            if(myTree_list.get(i).containsKey(tbName)){
                myTree_list.get(i).get(tbName).insert(Integer.parseInt(key),value);
                break;
            }
        }
        File file=new File("./mydatabase/index.xml");
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(file);
        Element root=document.getRootElement();
        for(Iterator i=root.elementIterator();i.hasNext();){
            Element element=(Element)i.next();
            if(element.getName().equals(tbName)){
                element.addAttribute("k"+key,value);
                break;
            }
        }
        CreateTable.writeIO(file,document);
        System.out.println("索引更新成功");
    }
//    更新数据后修改索引文件和B+树
    public static void updateIndex_update(String tbName,String key,String value) throws DocumentException, IOException {
//        for(int i=0;i<myTree_list.size();i++){
//            if(myTree_list.get(i).containsKey(tbName)){
//                myTree_list.get(i).get(tbName).update(Integer.parseInt(key),value);
//                break;
//            }
//        }
//        //更新索引文件
//        File file=new File("./mydatabase/index.xml");
//        SAXReader saxReader=new SAXReader();
//        Document document=saxReader.read(file);
//        Element root=document.getRootElement();
//        for(Iterator i=root.elementIterator();i.hasNext();){
//            Element element=(Element)i.next();
//            if(element.getName().equals(tbName)){
//                List<Attribute> list=element.attributes();
//                for(Iterator j=list.iterator();j.hasNext();){
//                    Attribute attribute=(Attribute)j.next();
//                    if(attribute.getName().equals("k"+key)){
//                        attribute.setValue(value);
//                        break;
//                    }
//                }
//                break;
//            }
//        }
//        CreateTable.writeIO(file,document);
//        System.out.println("索引更新成功");
    }
    //删除数据后修改索引文件和B+树
    public static void updateIndex_delete(String tbName,String key) throws DocumentException, IOException {
        //删除B+树节点
        for(int i=0;i<myTree_list.size();i++){
            if(myTree_list.get(i).containsKey(tbName)){
               myTree_list.get(i).get(tbName).delete(Integer.parseInt(key));
            }
        }
        //更新索引文件
        File file=new File("./mydatabase/index.xml");
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(file);
        Element root=document.getRootElement();
        for(Iterator i=root.elementIterator();i.hasNext();){
            Element element=(Element)i.next();
            if(element.getName().equals(tbName)){
                List<Attribute> list=element.attributes();
                for(Iterator j=list.iterator();j.hasNext();){
                    Attribute attribute=(Attribute)j.next();
                    if(attribute.getName().equals("k"+key)){
                        element.remove(attribute);
                        break;
                    }
                }
                break;
            }
        }
        CreateTable.writeIO(file,document);
        System.out.println("索引更新成功");
    }
    //每次登陆加载索引
    public static void loadIndex() throws DocumentException {
        File file=new File("./mydatabase/index.xml");
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(file);
        Element element=document.getRootElement();
        //遍历每个建有主键索引的表节点
        for(Iterator i=element.elementIterator();i.hasNext();) {
            Element tableElement=(Element)i.next();
            List<List<String>> list = new ArrayList<List<String>>();
            List<Attribute> listAttribute = tableElement.attributes();
            //遍历该表的所有索引关系
            for (Iterator j = listAttribute.iterator(); j.hasNext(); ) {
                Attribute attribute = (Attribute) j.next();
                List<String> tmp=new ArrayList<String>();
                tmp.add(attribute.getName());
                tmp.add(attribute.getText());
                list.add(tmp);
            }
            //构建该表对应的B+树
            BPlusTree myTree=Main.loadIndex(list);
            Map<String,BPlusTree> tmpMap=new HashMap<String, BPlusTree>();
            tmpMap.put(tableElement.getName(),myTree);
            myTree_list.add(tmpMap);
        }

    }

}
