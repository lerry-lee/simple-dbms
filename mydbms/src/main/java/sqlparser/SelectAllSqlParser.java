package sqlparser;

/**
 * 不带where的查询语句解析
 */
public class SelectAllSqlParser extends BaseSingleSqlParser{
    public SelectAllSqlParser(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new SqlSegment("(select \\* from)(.+)( ENDOFSQL)","[,]"));
    }
}
