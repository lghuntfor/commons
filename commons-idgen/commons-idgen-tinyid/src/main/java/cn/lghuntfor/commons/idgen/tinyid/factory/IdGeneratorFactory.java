package cn.lghuntfor.commons.idgen.tinyid.factory;


import cn.lghuntfor.commons.idgen.tinyid.generator.IdGenerator;

/**
 * @author du_imba
 */
public interface IdGeneratorFactory {
    /**
     * 根据bizType创建id生成器
     * @param bizType
     * @return
     */
    IdGenerator getIdGenerator(String bizType);
}
