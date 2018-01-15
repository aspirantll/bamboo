package com.flushest.bamboo.framework.persistence;

import com.alibaba.druid.pool.DruidDataSource;
import com.flushest.bamboo.common.Constant;
import com.flushest.bamboo.framework.persistence.definitions.TableDefinition;
import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.framework.resource.ResourceResolverUtil;
import com.flushest.bamboo.framework.util.Assert;
import com.flushest.bamboo.framework.util.ClassUtil;
import com.flushest.bamboo.framework.util.StringUtil;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/10/21 0021.
 */
public  class DBConfig {
    private static final Logger logger = LoggerFactory.getLogger(DBConfig.class);

    /**
     * 常量
     */
    private static final String PREFIX_PROPERTIES = "bamboo.jdbc";
    private static final String PROPERTIES_FILENAME = PREFIX_PROPERTIES +".properties";
    private static final String DB_NAMES_ITEM = PREFIX_PROPERTIES + ".names";
    private static final String DB_TYPE_ITEM = PREFIX_PROPERTIES + ".dbType";
    private static final String SUFFIX_TABLE_PREFIX_ITEM = ".tablePrefix";
    private static final String SUFFIX_URL_ITEM = ".url";
    private static final String SUFFIX_USER_ITEM = ".user";
    private static final String SUFFIX_PASSWORD_ITEM = ".password";
    private static final String DB_NAMES_SEPARATOR = ",";

    private static final Map<String,String> dbTypeAndUrlPrefixMap = new HashMap<>();
    static {
        dbTypeAndUrlPrefixMap.put("oracle","jdbc:oracle:thin:@");
    }

    private Properties properties;

    private List<TableDefinition> tableDefinitions;

    private Map<String,DataSource> nameAndDataSourceMap = new ConcurrentHashMap<>();
    private Map<String,Configuration> tablePrefixesAndConfigurationMap = new ConcurrentHashMap<>();


    public DBConfig() {
        prepare();
        loadProperties();
        createConfigurations();
        scanAnnotations();
        parseMapper();
        createSqlSessionFactories();
    }

    private void prepare() {

    }

    private void loadProperties() {
        properties = new Properties();
        ClassPathResource classPathResource = new ClassPathResource(PROPERTIES_FILENAME,this.getClass().getClassLoader());
        try {
            properties.load(classPathResource.getInputStream());
        }catch (FileNotFoundException e) {
            logger.warn("no datasource properties");
        }catch (IOException e) {
            logger.warn(String.format("occurred exception at loading file [%s] ",PROPERTIES_FILENAME));
        }
    }

    private DataSource createDataSource(String name) {
        String dbType = properties.getProperty(DB_TYPE_ITEM);
        Assert.notHasText(dbType,String.format("数据库类型dbType不能为空,请检查配置项[%s]",DB_TYPE_ITEM));
        String dbUrlPrefix = dbTypeAndUrlPrefixMap.get(dbType.toLowerCase());
        Assert.notNull(dbUrlPrefix,String.format("数据库类型dbType必须时%s中一个,请检查配置项[%s]",dbTypeAndUrlPrefixMap.keySet(),DB_TYPE_ITEM));

        String prefix = PREFIX_PROPERTIES+"."+name;

        String url = properties.getProperty(prefix+SUFFIX_URL_ITEM);
        Assert.notHasText(url,String.format("数据库URL不能为空，请检查[%s]配置项",prefix+SUFFIX_URL_ITEM));

        String user = properties.getProperty(prefix+SUFFIX_USER_ITEM);
        Assert.notHasText(user,String.format("数据库User不能为空，请检查[%s]配置项",prefix+SUFFIX_USER_ITEM));

        String password = properties.getProperty(prefix+SUFFIX_PASSWORD_ITEM);
        Assert.notHasText(password,String.format("数据库password不能为空，请检查[%s]配置项",prefix+SUFFIX_PASSWORD_ITEM));

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbUrlPrefix+url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        try {
            dataSource.init();
        } catch (SQLException e) {
            logger.error(String.format("初始化数据源[%s]失败",name),e);
            throw new BambooRuntimeException(String.format("初始化数据源[%s]失败",name),e);
        }

        nameAndDataSourceMap.put(name,dataSource);

        return dataSource;
    }

    private void createConfigurations() {
        if(!properties.isEmpty()) {
            String names = properties.getProperty(DB_NAMES_ITEM);
            if(StringUtil.hasText(names)) {
                String[] namesArray = names.trim().split(DB_NAMES_SEPARATOR);
                for(String name : namesArray) {
                    String tablePrefixes = properties.getProperty(PREFIX_PROPERTIES+"."+name+SUFFIX_TABLE_PREFIX_ITEM);
                    Assert.notHasText(tablePrefixes,String.format("数据库tablePrefix为空，请检查配置项[%s]",PREFIX_PROPERTIES+"."+name+SUFFIX_TABLE_PREFIX_ITEM));

                    DataSource dataSource = createDataSource(name);

                    Configuration configuration = new Configuration();

                    configuration.setEnvironment(new Environment(DBConfig.class.getSimpleName(),new SpringManagedTransactionFactory(),dataSource));

                    tablePrefixesAndConfigurationMap.put(tablePrefixes,configuration);
                }
            }
        }
        logger.warn("no datasource loaded");
    }

    private void scanAnnotations() {
        tableDefinitions = new ArrayList<>();

        try {
            Resource[] resources = ClassUtil.scanPackage(Constant.BASE_PACKAGE);
            for(Resource resource : resources) {
                ScannedPersistenceDefinition scanner = new ScannedPersistenceDefinition(resource);
                TableDefinition tableDefinition = scanner.scan();
                if(tableDefinition!=null) {
                    tableDefinitions.add(tableDefinition);
                }
            }
        } catch (IOException e) {
            logger.error("occurred IOException at scanAnnotation",e);
        } catch (ClassNotFoundException e) {
            logger.error("occurred ClassNotFoundException at scanAnnotation",e);
        }
    }

    private void parseMapper() {
        try {
            Resource[] resources = ResourceResolverUtil.getResource("classpath*:configs/mappers/*.xml");
            for(Configuration configuration : tablePrefixesAndConfigurationMap.values()) {
                parseMapperForConfiguration(configuration,resources);
            }
        } catch (IOException e) {
            logger.error("loading mapper error",e);
        }
    }


    private void parseMapperForConfiguration(Configuration configuration,Resource...  resources) throws IOException {
        parseAnnotationMapper(configuration);

        for(Resource resource : resources) {
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration, resource.toString(), configuration.getSqlFragments());
            xmlMapperBuilder.parse();
        }
    }

    private void parseAnnotationMapper(Configuration configuration) {
        for(TableDefinition tableDefinition : tableDefinitions) {
            TableDefinitionParser tableDefinitionParser = new TableDefinitionParser(configuration,tableDefinition);
            tableDefinitionParser.parse();
        }
    }

    private void createSqlSessionFactories() {
        for(Map.Entry<String,Configuration> entry : tablePrefixesAndConfigurationMap.entrySet()) {
            String[] tablePrefixes = entry.getKey().split(",");
            DataSource dataSource = entry.getValue().getEnvironment().getDataSource();
            SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(entry.getValue());
            for(String tablePrefix : tablePrefixes) {
                SqlSessionFactoryProxy.registerDataSource(tablePrefix,dataSource);
                SqlSessionFactoryProxy.registerSqlSessionFactory(tablePrefix.trim(),sqlSessionFactory);
            }
        }
    }
}
