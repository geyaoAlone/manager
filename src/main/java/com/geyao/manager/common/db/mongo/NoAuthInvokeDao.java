package com.geyao.manager.common.db.mongo;

import com.geyao.manager.common.dataobject.mongo.NoAuthInvokeRecord;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NoAuthInvokeDao {

    @Resource
    private MongoTemplate template;


    public boolean saveInvokeRecord(NoAuthInvokeRecord record){
        return template.save(record,"noAuthInvokeRecord") != null;
    }
}
