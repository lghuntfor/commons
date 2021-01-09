package cn.lghuntfor.commons.idgen.tinyid.service.impl;

import cn.lghuntfor.commons.idgen.api.IdGen;
import cn.lghuntfor.commons.idgen.common.Const;
import cn.lghuntfor.commons.idgen.tinyid.factory.impl.IdGeneratorFactoryClient;
import cn.lghuntfor.commons.idgen.tinyid.generator.IdGenerator;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * tinyid segment 实现类
 * @author lghuntfor
 * @date 2020/11/28
 */
public class TinyIdGenImpl implements IdGen {

    private IdGeneratorFactoryClient client = null;

    @Value("${tinyid.server:}")
    private String tinyIdServer;
    @Value("${tinyid.token:}")
    private String tinyIdToken;
    @Value("${tinyid.readTimeout:}")
    private String readTimeout;
    @Value("${tinyid.connectTimeout:}")
    private String connectTimeout;

    @PostConstruct
    public void init() {
        Properties properties = new Properties();
        properties.put("tinyid.server", tinyIdServer);
        properties.put("tinyid.token", tinyIdToken);
        properties.put("tinyid.readTimeout", readTimeout);
        properties.put("tinyid.connectTimeout", connectTimeout);
        client = IdGeneratorFactoryClient.getInstance(properties);
    }

    @Override
    public Long nextId() {
        IdGenerator idGenerator = client.getIdGenerator(Const.DEFAULT_KEY);
        return idGenerator.nextId();
    }

    @Override
    public Long nextId(String businessKey) {
        IdGenerator idGenerator = client.getIdGenerator(businessKey);
        return idGenerator.nextId();
    }
}
