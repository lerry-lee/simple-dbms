package sqlparser;

public class DropIndexSqlParser extends BaseSingleSqlParser{
    public DropIndexSqlParser(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new SqlSegment("(drop index on)(.+)( ENDOFSQL)","[,]"));

    }
}
