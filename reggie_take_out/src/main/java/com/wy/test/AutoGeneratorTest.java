package com.wy.test;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @Author: wangyu
 * @Date: 2022/09/14/16:20
 */
public class AutoGeneratorTest {

    public static void main(String[] args) {

        AutoGenerator autoGenerator = new AutoGenerator();

        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setAuthor("wangyu")
                .setOutputDir("E:\\IDEA项目\\reggie\\reggie_take_out\\src\\main\\java")//设置输出路径
                .setFileOverride(true)//设置文件覆盖
                .setIdType(IdType.AUTO)//设置主键生成策略
                .setServiceName("%sService")//service接口的名称
                .setBaseResultMap(true)//基本结果集合
                .setBaseColumnList(true)//设置基本的列
                .setControllerName("%sController");

        //配置数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver").setUrl("jdbc:mysql://localhost:3306/reggie?serverTimezone=UTC")
                .setUsername("root").setPassword("root");

        //策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true)//设置全局大写命名
                .setNaming(NamingStrategy.underline_to_camel)//数据库表映射到实体的命名策略
                //.setTablePrefix("tbl_")//设置表名前缀
                .setInclude();

        //包名配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.wy").setMapper("mapper")
                .setService("service").setController("controller")
                .setEntity("entity").setXml("mapper");

        autoGenerator.setGlobalConfig(globalConfig).setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig).setPackageInfo(packageConfig);

        autoGenerator.execute();

    }

}
