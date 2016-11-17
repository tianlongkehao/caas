package com.bonc.epm.paas.shera.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Deprecated
public enum Kind {
    JOBVIEW("JobView"),STATUS("Status"),
    JOBEXEC("JobExec"),JOBEXECLIST("JobExecList"),
    JOBEXECUTION("JobExecution"),JOBEXECUTIONLIST("JobExecutionList"),
    JDK("Jdk"),JDKLIST("JdkList"),JOB("Job"),JOBLIST("JobList");

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
