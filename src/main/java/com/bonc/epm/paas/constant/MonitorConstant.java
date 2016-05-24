package com.bonc.epm.paas.constant;

/**
 * Created by chiwenguang on 16-3-28.
 */
public class MonitorConstant {

    public static final String SUN_VALUE = " sum(\"value\") ";
    public static final String MAX_VALUE = " max(\"value\") ";
    public static final String LAST_VALUE = " last(\"value\") ";
    public static final String NEGATIVE_VALUE_U = " non_negative_derivative(max(value),1u) ";
    public static final String NEGATIVE_VALUE_S = " non_negative_derivative(max(value),1s) ";

    public static final String MEMORY_LIMIT = " \"memory/limit_bytes_gauge\" ";
    public static final String MEMORY_USAGE = " \"memory/usage_bytes_gauge\" ";
    public static final String MEMORY_WORKING_SET = " \"memory/working_set_bytes_gauge\" ";

    public static final String CPU_LIMIT = " \"cpu/limit_gauge\" ";
    public static final String CPU_USAGE = " \"cpu/usage_ns_cumulative\" ";

    public static final String FILE_LIMIT = " \"filesystem/limit_bytes_gauge\" ";
    public static final String FILE_USAGE = " \"filesystem/usage_bytes_gauge\" ";

    public static final String NET_TX = " \"network/tx_bytes_cumulative\" ";
    public static final String NET_RX = " \"network/rx_bytes_cumulative\" ";
}
