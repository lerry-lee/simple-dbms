package function;

import java.io.File;
import java.io.IOException;

import function.DropIndex;
import org.dom4j.DocumentException;

public class DropTable {
    //delete table 表名
    public static void deleteTable(String dbName, String tbName) throws DocumentException, IOException {
        //判断数据库是否为空
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        DropIndex.dropIndex(dbName,tbName);
        //表存在则返回一个对象
        File file = IsLegal.hasDir(dbName, tbName);
        System.out.println(file.toString());
        //删除整张表
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        file.delete();
        System.out.println(tbName+ "表删除成功");

    }
}
