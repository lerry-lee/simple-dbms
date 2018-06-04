package sqlparser;

public class DropTableSqlParser extends BaseSingleSqlParser{
    public DropTableSqlParser(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new SqlSegment("(drop table)(.+)( ENDOFSQL)","[,]"));

    }
}
