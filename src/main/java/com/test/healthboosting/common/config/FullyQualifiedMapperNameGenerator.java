package com.test.healthboosting.common.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;

public class FullyQualifiedMapperNameGenerator extends DefaultBeanNameGenerator {
    protected String buildDefaultBeanName(BeanDefinition definition) {
        return definition.getBeanClassName(); // ex: pkpork.label.pcm2.mapper.CommonMapper
    }
}
