package one.hust.edu.cn.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: fabricTest
 * @description: 链上记录(Record)结构体
 * @author: zwh
 * @create: 2021-01-07 15:59
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    /**
     * 数据密文hash
     */
    private String hashData;

    /**
     * 源链标识
     */
    private String srcChain;

    /**
     * 用户标识
     */
    private String user;

    /**
     * 目标链标识
     */
    private String dstChain;

    /**
     * 目标数据标识
     */
    private String dataId;

    /**
     * 事务的操作类型
     */
    private String typeTx;

    /**
     * 本次事务号
     */
    private String thisTxId;

    /**
     * 上次事务号
     */
    private String lastTxId;
}
