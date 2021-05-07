package com.coder.lee.drift.client.config;


import com.facebook.drift.annotations.ThriftService;
import com.facebook.drift.client.DriftClientFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/5 0:55
 *
 * @author coderLee23
 */
@Log4j2
@Component
public class DriftClientBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware, ResourceLoaderAware {

    private ApplicationContext applicationContext;

    private ResourcePatternResolver resourcePatternResolver;

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private MetadataReaderFactory metadataReaderFactory;


    private static final String SCAN_BASE_PATH = "com.coder.lee.drift.client";


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        DriftClientFactory driftClientFactory = applicationContext.getBean(DriftClientFactory.class);
        log.info("driftClientFactory ==> {}", driftClientFactory);
        Set<Class<?>> classes = scannerPackages();
        log.info("classes ==> {}", classes);
        classes.forEach(clazz -> {
            Object bean = driftClientFactory.createDriftClient(clazz).get();
            configurableListableBeanFactory.registerSingleton(clazz.getSimpleName(), bean);
            log.info("ThriftService register {} ==> {}", clazz.getSimpleName(), bean);
        });
    }

    /**
     * 根据包路径获取包及子包下的所有类
     *
     * @return Set<Class < ?>> Set<Class<?>>
     */
    private Set<Class<?>> scannerPackages() {
        Set<Class<?>> set = new LinkedHashSet<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage() + '/' + DEFAULT_RESOURCE_PATTERN;
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    if (!metadataReader.getAnnotationMetadata().hasAnnotation(ThriftService.class.getName())) {
                        continue;
                    }
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className);
                        set.add(clazz);
                    } catch (ClassNotFoundException e) {
                        log.error("ClassNotFoundException error", e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("IOException error", e);
        }
        return set;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    private Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

    protected String resolveBasePackage() {
        return ClassUtils.convertClassNameToResourcePath(this.getEnvironment().resolveRequiredPlaceholders(SCAN_BASE_PATH));
    }
}