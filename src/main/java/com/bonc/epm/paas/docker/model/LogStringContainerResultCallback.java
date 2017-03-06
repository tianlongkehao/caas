/**
 *
 */
package com.bonc.epm.paas.docker.model;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;

/**
 * @author lkx
 *
 */
public class LogStringContainerResultCallback extends LogContainerResultCallback {
    protected final StringBuffer log = new StringBuffer();

    List<Frame> collectedFrames = new ArrayList<Frame>();

    boolean collectFrames = false;

    public LogStringContainerResultCallback() {
        this(false);
    }

    public LogStringContainerResultCallback(boolean collectFrames) {
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
