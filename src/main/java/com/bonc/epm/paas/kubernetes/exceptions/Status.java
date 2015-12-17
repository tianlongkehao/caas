package com.bonc.epm.paas.kubernetes.exceptions;

import com.bonc.epm.paas.kubernetes.model.AbstractKubernetesModel;
import com.bonc.epm.paas.kubernetes.model.Kind;
import com.google.common.base.MoreObjects;

public class Status extends AbstractKubernetesModel {

    private String status, message, reason;
    private int code;
    private StatusDetails details;

    public Status() {
        super(Kind.STATUS);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public StatusDetails getDetails() {
        return details;
    }

    public void setDetails(StatusDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("status", status).add("message", message).add("reason", reason)
                .add("code", code).add("details", details).toString();
    }

}
