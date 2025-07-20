package com.test.healthboosting.common.config;

import com.test.healthboosting.common.interceptor.SqlLoggingInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = {
                "com.test.healthboosting.common.mapper",
                "com.test.healthboosting.loginapi.mapper"
        },
        sqlSessionFactoryRef = "sqlSessionFactory",
        nameGenerator = com.test.healthboosting.common.config.FullyQualifiedMapperNameGenerator.class
)
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // 인터셉터 명시 등록
        factoryBean.setPlugins(new Interceptor[] {
                new SqlLoggingInterceptor()
        });

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
