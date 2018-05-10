package sqlparser;

public class InsertAllSqlParser extends BaseSingleSqlParser {
    public InsertAllSqlParser(String originalSql) {
        super(originalSql);

        }
        @Override
        protected void initializeSegments()
        {
            segments.add(new SqlSegment("(insert into)(.+?)( values)","[,]"));
            segments.add(new SqlSegment("(values[(])(.+)([)] ENDOFSQL)","[,]"));

        }
}
