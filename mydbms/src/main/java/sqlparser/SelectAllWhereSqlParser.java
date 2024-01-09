package sqlparser;

/**
 * 带where的查询语句解析
 */
public class SelectAllWhereSqlParser extends BaseSingleSqlParser{
    public SelectAllWhereSqlParser(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new SqlSegment("(select \\* from)(.+)( where)","[,]"));
        segments.add(new SqlSegment("(where)(.+)( ENDOFSQL)","[,]"));
    }
}
