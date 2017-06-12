/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:DnsExecStartResultCallback.java
 * Package Name:com.bonc.epm.paas.docker.model
 * Date:2017年6月12日上午9:44:14
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/
/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:DnsExecStartResultCallback.java
 * Package Name:com.bonc.epm.paas.docker.model
 * Date:2017年6月12日上午9:44:14
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
 */

package com.bonc.epm.paas.docker.model;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.ExecStartResultCallback;

public class ExecStartStringResultCallback extends ExecStartResultCallback {
    protected final StringBuffer log = new StringBuffer();

    List<Frame> collectedFrames = new ArrayList<Frame>();

    boolean collectFrames = false;

    public ExecStartStringResultCallback() {
        this(false);
    }

    public ExecStartStringResultCallback(boolean collectFrames) {
        this.collectFrames = collectFrames;
    }

    @Override
    public void onNext(Frame frame) {
        if(collectFrames) collectedFrames.add(frame);
        log.append(new String(frame.getPayload()));
    }

    @Override
    public String toString() {
        return log.toString();
    }


    public List<Frame> getCollectedFrames() {
        return collectedFrames;
    }
}
