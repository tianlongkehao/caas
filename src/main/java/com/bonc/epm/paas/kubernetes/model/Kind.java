package com.bonc.epm.paas.kubernetes.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Kind {
    STATUS("Status"), STATUSDETAILS("StatusDetails"), POD("Pod"), PODLIST("PodList"), REPLICATIONCONTROLLER(
            "ReplicationController"), REPLICATIONCONTROLLERLIST("ReplicationControllerList"), SERVICE("Service"), SERVICELIST(
            "ServiceList"), NAMESPACE("Namespace"), NAMESPACELIST("NamespaceList"), LIMITRANGE("LimitRange"), LIMITRANGELIST(
            		"LimitRangeList"), RESOURCEQUOTA("ResourceQuota"), RESOURCEQUOTALIST("ResourceQuotaList");

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
