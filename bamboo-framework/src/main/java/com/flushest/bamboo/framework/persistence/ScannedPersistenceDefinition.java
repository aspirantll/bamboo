package com.flushest.bamboo.framework.persistence;

import com.flushest.bamboo.framework.annotation.Column;
import com.flushest.bamboo.framework.annotation.Id;
import com.flushest.bamboo.framework.annotation.Table;
import com.flushest.bamboo.framework.persistence.definitions.ColumnDefinition;
import com.flushest.bamboo.framework.persistence.definitions.IdDefinition;
import com.flushest.bamboo.framework.persistence.definitions.TableDefinition;
import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.framework.util.ClassUtil;
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
            final IdDefinition idDefinition = new IdDefinition();
            ReflectionUtils.doWithLocalFields(clazz,(Field field)->{
                Column column = field.getAnnotation(Column.class);
                if(column!=null) {
                    ColumnDefinition columnDefinition = new ColumnDefinition(column.name(),column.jdbcType(),field);
                    columnDefinitions.add(columnDefinition);
                    Id id = field.getAnnotation(Id.class);
                    if(id!=null) {
                        if(idDefinition.getField()!=null||idDefinition.getStrategy()!=null) {
                            throw new BambooRuntimeException(String.format("一张表里面只能有一个主键id，请检查[%s]中@Id注解个数",clazz.getName()));
                        }
                        idDefinition.setColumnName(columnDefinition.getName());
                        idDefinition.setField(field);
                        idDefinition.setStrategy(id.strategy());
                    }
                }
            });

            return new TableDefinition(table.tablePrefix(),table.simpleName(),clazz,columnDefinitions,idDefinition);
        }
        return null;
    }
}
