package com.local.common.id.snowflake;


import com.local.common.id.CustomIDGenerator;
import com.local.common.utils.DateTimeHelper;

/**
 * 雪花算法实现分布式id
 * Twitter_Snowflake
 * SnowFlake的结构如下(每部分用-分开):
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */

public class SnowFlakeIDGenerator<Long> extends CustomIDGenerator<java.lang.Long> {

    //开始时间戳
    private final long startTimeStamp = 1591286400000L;

    //机器id所占的位数
    private final long workerIdBits = 5L;

    //数据标识id所占的位数
    private final long dataCenterIdBits = 5L;

    //支持最大的机器id,结果是31.(这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    //支持最大的数据标识id,结果是31
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    //序列在id中所占的位数
    private final long sequenceBits = 12L;

    //机器ID向左移动12位
    private final long workerIdShift = sequenceBits;

    //数据标识id向左移动17位(12+5)
    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    //时间戳向左移动22位(12+5+5)
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    //生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    //工作机器id(0-31)
    private long workerId;

    //数据中心id(0-31)
    private long dataCenterId;

    //毫秒内序列(0-4095)
    private long sequence = 0L;

    //上次生成id的时间戳
    private long lastTimestamp = -1L;

    public SnowFlakeIDGenerator(long workerId, long dataCenterId) {

        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",
                    maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("data center Id can't be greater than %d or less than 0",
                    maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = currentTimeStamp();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimeStamp();
        }
        return timestamp;
    }

    protected long currentTimeStamp() {
        return DateTimeHelper.getSystemTimeStampToMillis();
    }

    @Override
    protected synchronized java.lang.Long createID() {
        long timestamp = currentTimeStamp();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - startTimeStamp) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

}
