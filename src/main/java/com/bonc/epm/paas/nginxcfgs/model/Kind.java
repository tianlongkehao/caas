package com.bonc.epm.paas.nginxcfgs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Kind {
    K8S("k8s"),EXTERN("extern"); 

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
