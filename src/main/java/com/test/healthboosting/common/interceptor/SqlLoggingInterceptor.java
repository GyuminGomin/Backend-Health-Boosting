package com.test.healthboosting.common.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
        }),
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class
        })
})
public class SqlLoggingInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger("SQL_LOGGER");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis(); // SQL 실행 시점을 계산하기 위해

        MappedStatement ms = (MappedStatement) invocation.getArgs()[0]; // 실행할 SQL ID 및 XML 정의 정보
        Object param = invocation.getArgs()[1]; // 실제 실행시 전달되는 파라미터
        BoundSql boundSql = ms.getBoundSql(param); // 포함된 SQL + 파라미터 정보 보유 객체

        String sql = boundSql.getSql();
        String formattedSql = buildSqlWithParams(ms, boundSql, param); // ? 매핑을 파라미터로 변경
        String paramStr = String.valueOf(param);

        Object result = null;
        try {
            result = invocation.proceed(); // 실제 SQL문이 DB에 전달됨
            return result;
        } finally {
            long elapsed = System.currentTimeMillis() - start;

            log.info("""
                    ==========[MyBatis SQL Execution]==========
                    📍 호출: {}
                    📍 서비스: {}
                    📍 쿼리: {}
                    🕒 실행 시간: {}ms
                    📄 SQL: 
                    {}
                    🔧 파라미터: {}
                    📤 결과: {}
                    ============================================
                    """,
                    MDC.get("caller"),
                    MDC.get("service"),
                    MDC.get("mapper"),
                    elapsed,
                    formattedSql,
                    paramStr,
                    summrizeResult(result));
        }
    }

    private Object summrizeResult(Object result) {
        if (result instanceof List<?>) {
            List<?> list = (List<?>) result;
            return "List(size=" + list.size() + ")";
        }
        if (result instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) result;
            return "Map(size=" + map.size() + ")";
        }
        return String.valueOf(result);
    }

    private String buildSqlWithParams(MappedStatement ms, BoundSql boundSql, Object param) {
        String sql = boundSql.getSql();
        List<ParameterMapping> paramMappings = boundSql.getParameterMappings();
        if (paramMappings == null || paramMappings.isEmpty()) return sql;

        TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();
        MetaObject metaObject = ms.getConfiguration().newMetaObject(param);

        for (ParameterMapping mapping : paramMappings) {
            String propName = mapping.getProperty();
            Object value;

            if (boundSql.hasAdditionalParameter(propName)) {
                value = boundSql.getAdditionalParameter(propName);
            } else if (metaObject.hasGetter(propName)) {
                value = metaObject.getValue(propName);
            } else {
                value = null;
            }

            String formattedValue = formatValue(value) + " /**p**/";
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(formattedValue));
        }

        return sql;
    }

    private String formatValue(Object val) {
        if (val == null) return "NULL";
        if (val instanceof String || val instanceof java.sql.Date || val instanceof Date) {
            return "'" + val.toString() + "'";
        }
        return val.toString();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}
}
