package cn.lghuntfor.commons.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请添加描述
 * @author liaogang
 * @date 2020/11/4 17:41
 */
@Data
@AllArgsConstructor
public class Sort {

    private String column;

    private boolean isAsc;

}
