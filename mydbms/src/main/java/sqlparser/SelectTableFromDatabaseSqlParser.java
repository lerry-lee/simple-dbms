package sqlparser;
//没用
public class SelectTableFromDatabaseSqlParser extends BaseSingleSqlParser{

    public SelectTableFromDatabaseSqlParser(String originalSql)
    {
        super(originalSql);
        // TODO Auto-generated constructor stub
    }
    //show databases;

    @Override
    protected void initializeSegments()
    {

        //System.out.println("调用了ShowDatabaseSqlParser的initializeSegments方法");
        segments.add(new SqlSegment("(select table)(.+)(from)","[,]"));
        segments.add(new SqlSegment("(from)(.+)(ENDOFSQL)","[,]"));
    }
}
