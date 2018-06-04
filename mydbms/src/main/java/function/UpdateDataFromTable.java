package function;

import bpulstree.BPlusTree;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class UpdateDataFromTable {
    public static void updateTable(String dbName, String tbName, List<List<String>> tmp) throws DocumentException, IOException {
        //数据库是否为空
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        //表存在则返回表的物理层最后一张子表的下标,得到配置文件
        File config_file = IsLegal.isTable(dbName, tbName);
        //find标记是否找到记录
        Boolean find = false;
        //tmp2表示where的列名称和列值
        String[] tmp2 = tmp.get(1).get(0).split("=");
        //key为where的列名称
        String key=tmp2[0];
        //是否是主键查询
        if(IsLegal.isIndex(config_file,key)) {
            BPlusTree tree=CreateIndex.findTree(tbName);
            String file_name=tree.search(Integer.parseInt(key));
            File file=new File("./mydatabase/"+dbName+"/"+tbName+"/"+file_name+".xml");
            find=update(tbName,file,tmp,tmp2);
            if(!find){
                System.out.println("更新失败，未找到记录");
            }
            //更新索引文件
//            CreateIndex.updateIndex_update(tbName,key,tmp2[2]);
        }
        else {
            //扫描所有文件,j记录文件下表,num用来遍历所有文件
            String this_file = IsLegal.lastFileName(dbName, tbName);
            for (int j = Integer.parseInt(this_file); j >= 0; j--) {
                String num = "" + j;
                File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
                find=update(tbName,file,tmp,tmp2);
                if(find){
                    return;
                }
            }
            System.out.println("更新失败，未找到记录");
        }
    }

    public static boolean update(String tbName,File file,List<List<String>> tmp,String[] tmp2) throws DocumentException, IOException {
        boolean find=false;
        //创建解析器，document对象，获得根节点
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();

        List<Node> nodes = root.selectNodes(tbName);
        for (Node node : nodes) {
            Element currentNode = (Element) node;
            List<Attribute> list = currentNode.attributes();
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Attribute attribute = (Attribute) i.next();
                if (attribute.getName().equals(tmp2[0]) && attribute.getText().equals(tmp2[1])) {
                    find = true;
                    break;
                }
            }
            if (find) {
                for (int k = 0; k < tmp.get(0).size(); k++) {
                    String[] tmp1 = tmp.get(0).get(k).split("=");
                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                        Attribute attribute = (Attribute) i.next();
                        if (attribute.getName().equals(tmp1[0])) {
                            attribute.setText(tmp1[1]);
                        }
                    }
                }
                //写入IO
                CreateTable.writeIO(file,document);
                System.out.println("更新成功");
                return true;
            }
        }
        return false;

    }
}
