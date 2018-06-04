package sqlparser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SelectSqlParserTest {

    @Test
    public void initializeSegments() throws Exception{
        SelectSqlParser parser = new SelectSqlParser("select * from table;");
        String sql = parser.getParsedSql();
        System.out.println(sql);
        Assert.assertEquals(sql, "|n|n|n||n|n");
    }
    @Test
    public  void init() {

    }
}