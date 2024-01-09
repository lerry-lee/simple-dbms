package sqlparser;
/**
 *
 * 删除表语句解析器
 */
public class DropTableSqlParser extends BaseSingleSqlParser{
    public DropTableSqlParser(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new SqlSegment("(drop table)(.+)( ENDOFSQL)","[,]"));

    }
}
