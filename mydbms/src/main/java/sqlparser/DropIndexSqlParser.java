package sqlparser;
/**
 *
 * 删除索引语句解析器
 */
public class DropIndexSqlParser extends BaseSingleSqlParser{
    public DropIndexSqlParser(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new SqlSegment("(drop index on)(.+)( ENDOFSQL)","[,]"));

    }
}
