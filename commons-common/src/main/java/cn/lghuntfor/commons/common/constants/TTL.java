package cn.lghuntfor.commons.common.constants;

/**
 * 秒数的时间数常量, 以秒为单位
 * @author liaogang
 * @date 2020/8/29 14:29
 */
public interface TTL {

    long S1 = 1;

    long S10 = 10;

    long S30 = 30;

    long M1 = 60;

    long M2 = 120;

    long M5 = 300;

    long M10 = 600;

    long M30 = 1800;

    long H1 = 3600;

    long H2 = 7200;

    long H6 = H1 * 6;

    long H12 = H1 * 12;

    long D1 = H1 * 24;

    long W1 = D1 * 7;

}
