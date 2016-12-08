package com.bonc.epm.paas.cluster.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Kind {
    CLUSTER("cluster"),MEMORY("memory"),OVERALLCLUSTERMEMORYUSAGE("OVERALL CLUSTER MEMORY USAGE"),
    LIMITCURRENT("LimitCurrent"),USAGECURRENT("UsageCurrent"),WORKINGSETCURRENT("WorkingSetCurrent"),
    MEMORYUSAGEGROUPBYNODE("MEMORY USAGE GROUP BY NODE"),CPU("CPU"),CPUUSAGEGROUPBYNODE("CPU USAGE GROUP BY NODE"),
    DISK("DISK"),OVERALLCLUSTERDISKUSAGE("OVERALL CLUSTER DISK USAGE"),DISKUSAGEGROUPBYNODE("DISK USAGE GROUP BY NODE"),
    NETWORK("NETWORK"),NETWORKUSAGEGROUPBYNODE("NETWORK USAGE GROUP BY NODE"),MINMON("minmon"),
    GETMEMLIMITOVERALL("getMemLimitOverAll"),GETMEMUSEOVERALL("getMemUseOverAll"),GETMEMSETOVERALL("getMemSetOverAll"),
    GETDISKLIMITOVERALL("getDiskLimitOverAll"),GETDISKUSEOVERALL("getDiskUseOverAll"),
    GETMEMLIMITMINION("getMemLimitMinion"),GETMEMUSEMINION("getMemUseMinion"),;

    private final String text;

    private Kind(final String text) {
        this.text = text;
    }

    @JsonCreator
    public static Kind forValue(String value) {
        return Kind.valueOf(value.toUpperCase());
    }

    @Override
    @JsonValue
    public String toString() {
        return text;
    }
}
