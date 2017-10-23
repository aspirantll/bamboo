package com.flushest.bamboo.runtime.common.persistence;

import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.flushest.bamboo.runtime.annotation.Column;
import com.flushest.bamboo.runtime.annotation.Table;
import com.flushest.bamboo.runtime.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/22 0022.
 */
public class ScannedPersistenceDefinition {
    private static Logger  logger = LoggerFactory.getLogger(ScannedPersistenceDefinition.class);

    private static MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();

    private Resource resource;

    public ScannedPersistenceDefinition(Resource resource) {
        this.resource = resource;
    }

    public TableDefinition scan() throws IOException, ClassNotFoundException {
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        if(annotationMetadata.isAnnotated(Table.class.getName())) {
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            Class<?> clazz = ClassUtil.forName(classMetadata.getClassName(),this.getClass().getClassLoader());

            Table table = clazz.getAnnotation(Table.class);

            final List<ColumnDefinition> columnDefinitions = new ArrayList<>();
            ReflectionUtils.doWithLocalFields(clazz,(Field field)->{
                Column column = field.getAnnotation(Column.class);
                if(column!=null) {
                    columnDefinitions.add(new ColumnDefinition(column.name(),column.jdbcType(),field));
                }
            });

            return new TableDefinition(table.tablePrefix(),table.simpleName(),clazz,columnDefinitions);
        }
        return null;
    }
}
