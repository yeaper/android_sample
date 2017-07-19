package com.xh;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class DaoGenerator {

    public static void main(String[] args) throws Exception {
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(1, "com.yyp.sun.entity");
        schema.setDefaultJavaPackageDao("com.yyp.sun.dao");
        addMoodDiary(schema);

        new org.greenrobot.greendao.generator.DaoGenerator().generateAll(schema, args[1]);
    }

    /**
     * 创建 MoodDiary 表
     * @param schema
     */
    public static void addMoodDiary(Schema schema){

        Entity entity = schema.addEntity("MoodDiaryData");
        entity.addIdProperty();
        entity.addStringProperty("authorID");
        entity.addStringProperty("createDate");
        entity.addStringProperty("content");
        entity.addStringProperty("imageUrl1");
        entity.addStringProperty("imageUrl2");
    }
}
