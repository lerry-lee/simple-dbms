package sqlparser;

public class CreateIndexSqlParser extends BaseSingleSqlParser {
    public CreateIndexSqlParser(String originalSql) {
        super(originalSql);

    }

    //create index on 表名(列名称);
    @Override
    protected void initializeSegments()
    {
        //System.out.println("调用了CreateTableSqlParser的initializeSegments方法");
        segments.add(new SqlSegment("(create index on)(.+?)([(])","[,]"));
        segments.add(new SqlSegment("([(])(.+?)([)] ENDOFSQL)","[,]"));

    }

}
