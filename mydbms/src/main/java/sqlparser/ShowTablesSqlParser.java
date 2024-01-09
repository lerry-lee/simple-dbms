package sqlparser;

/**
 * 展示所有表语句的解析器
 */
public class ShowTablesSqlParser extends BaseSingleSqlParser {
    public ShowTablesSqlParser(String originalSql)
    {
        super(originalSql);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void initializeSegments(){
        segments.add(new SqlSegment("(show tables)(.+)(ENDOFSQL)","[,]"));
    }
}
