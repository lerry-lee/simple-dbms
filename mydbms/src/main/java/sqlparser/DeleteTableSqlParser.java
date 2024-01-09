package sqlparser;

/**
 * 没用到
 */
public class DeleteTableSqlParser extends BaseSingleSqlParser{
    public DeleteTableSqlParser(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new SqlSegment("(delete table)(.+)( ENDOFSQL)","[,]"));

    }
}
