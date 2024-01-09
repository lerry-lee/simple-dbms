package function;

import bpulstree.BPlusTree;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class DeleteDataFromTable {
    //delete from 表名 where 列名称=列值
    public static void deleteFromTable(String dbName,String tbName,List<String> tmp) throws DocumentException, IOException {
        //数据库是否为空
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        //输出tmp列表
        for (int i = 0; i < tmp.size(); i++) {
            System.out.println(tmp.get(i));
        }
        //表存在则返回物理层最后一张子表的下标，并得到配置文件
        File config_file=IsLegal.isTable(dbName,tbName);
        String write_file_last_num = IsLegal.lastFileName(dbName, tbName);
        //获取where列名称
        String [] key_value=tmp.get(0).split("=");
        String key=key_value[0];
        //find标记是否找到记录
        Boolean find = false;
        //非主键查询删除
        if(!IsLegal.isIndex(config_file,key)) {
            for (int j = Integer.parseInt(write_file_last_num); j >= 0; j--) {
                //设置变量traverse_file用来遍历表的所有文件
                String last_num = "" + j;
                //创建写入对象，创建sax解析器，document对象，获得root节点
                File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + last_num + ".xml");
                find=delete(file,dbName,tbName,key_value,last_num);
                if(find){
                    return;
                }
            }
            System.out.println("没有找到要删除的记录");
        }
        //主键查询删除
        else {
            BPlusTree tree=CreateIndex.findTree(tbName);
            String file_name=tree.search(Integer.parseInt(key_value[1]));
            String num=file_name.substring(file_name.length()-1,file_name.length());
            File file=new File("./mydatabase/"+dbName+"/"+tbName+"/"+file_name+".xml");
            find=delete(file,dbName,tbName,key_value,num);
            //删除数据后更新索引
            if(find) {
                CreateIndex.updateIndex_delete(tbName, key_value[1]);
                return;
            }
            System.out.println("没有找到要删除的记录");
        }
    }
    //主键删除用delete方法
    public static boolean delete(File file,String dbName,String tbName,String[] key_value,String last_num) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        boolean find=false;

        //设置一个节点用来遍历
        Element element;
        List<Node> nodes = root.selectNodes(tbName);
        boolean isFull=nodes.size()==10;
        for (Node node : nodes) {
            Element currentNode=(Element)node;
            List<Attribute> lists=currentNode.attributes();
            for (Iterator i = lists.iterator(); i.hasNext(); ) {
                Attribute attribute = (Attribute) i.next();
                if (attribute.getName().equals(key_value[0]) && attribute.getText().equals(key_value[1])) {
                    find = true;
                    break;
                }
            }
            if (find) {

                root.remove(currentNode);
                //写入IO
                CreateTable.writeIO(file,document);

                //更新配置文件
                if(isFull) {
                    File file1 = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + "-config.xml");
                    SAXReader saxReader = new SAXReader();
                    Document document1 = saxReader.read(file1);
                    Element element1 = (Element) document1.getRootElement().selectSingleNode("insertables");
                    element1.addElement("insertable").setText(last_num);
                    //写入IO
                    CreateTable.writeIO(file1, document1);
                }
                System.out.println("删除记录成功");
                return true;
            }
        }
        return false;
    }
}

