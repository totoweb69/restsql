/* Copyright (c) restSQL Project Contributors. Licensed under MIT. */
package org.restsql.core.impl.oracle;

import java.sql.Timestamp;
import java.util.List;
import org.restsql.core.ColumnMetaData;
import org.restsql.core.InvalidRequestException;
import org.restsql.core.Request;
import org.restsql.core.SqlResourceMetaData;
import org.restsql.core.impl.AbstractSqlBuilder;

/**
 * Adds limit clause and special handling for type casting parameters in prepared statements (apparently only needed for
 * IN operator).
 * 
 * @author mamrani
 */
public class OracleSqlBuilder extends AbstractSqlBuilder {

	@Override
	public String buildSelectLimitSql(final int limit, final int offset) {
		String retour =
				String.format(" OFFSET %d rows fetch next %d ROWS ONLY ", offset, limit);				
		return retour;
	}

    @Override
    public SqlStruct buildSelectSql(SqlResourceMetaData metaData, String mainSql, Request request) throws InvalidRequestException {
        SqlStruct retour = super.buildSelectSql(metaData, mainSql, request); 
        List<Object> values = retour.getPreparedValues();
        
        return retour;
    }

    @Override
    protected void appendValue(StringBuilder part, StringBuilder preparedPart, List<Object> preparedValues, Object value, boolean charOrDateTimeType, ColumnMetaData column) {
        super.appendValue(part, preparedPart, preparedValues, value, charOrDateTimeType, column); 
        
        //Traitement des date
        if(!preparedValues.isEmpty()){            
            if(column.isDateTimeType()){
                Object obj = preparedValues.get(preparedValues.size() - 1);
                try{
                    Timestamp ts = Timestamp.valueOf(obj.toString());
                    preparedValues.set(preparedValues.size() - 1, ts);
                }catch(Exception e){
                    
                }
            }
        }
        
    }
        
        
        
        

}
