package function;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

public class UpdateTable {
    public static void updateTable(String dbName, String tbName, List<List<String>> tmp) throws DocumentException, IOException {
        //数据库是否合法
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        //表存在则返回表的最后一个文件名
        String this_file=IsLegal.isTable(tbName);
        //find标记是否找到记录
        Boolean find = false;
        //处理where内容和set内容
        String[] tmp1 = new String[tmp.get(0).size()];
        String[] tmp2 = new String[1];
        for (int i = 0; i < tmp.get(0).size(); i++) {
            tmp1 = tmp.get(0).get(i).split("=");
        }
        tmp2 = tmp.get(1).get(0).split("=");

        //扫描所有文件,j记录文件下表,num用来遍历所有文件
        for(int j=Integer.parseInt(this_file);j>=0;j--) {
            String num=""+j;
            File file=new File("./mydatabase/"+dbName+"/"+tbName+"/"+tbName+num+".xml");
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
                        tmp1 = tmp.get(0).get(k).split("=");
                        for (Iterator i = list.iterator(); i.hasNext(); ) {
                            Attribute attribute = (Attribute) i.next();
                            if (attribute.getName().equals(tmp1[0])) {
                                attribute.setText(tmp1[1]);
                            }
                        }
                    }
                    //IO
                    OutputFormat format = OutputFormat.createPrettyPrint();
                    format.setEncoding("UTF-8");
                    XMLWriter writer = new XMLWriter(
                            new OutputStreamWriter(new FileOutputStream("./mydatabase/" + dbName + "/" + tbName + "/"+tbName+num+".xml")), format);
                    writer.write(document);
                    writer.close();
                    System.out.println("更新成功");
                    return;

                }
            }
        }
      System.out.println("更新失败，未找到记录");

    }
}
