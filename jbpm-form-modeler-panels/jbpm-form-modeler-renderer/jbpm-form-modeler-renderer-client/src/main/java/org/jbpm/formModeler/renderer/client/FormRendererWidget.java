/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formModeler.renderer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.jbpm.formModeler.api.client.FormRenderContextTO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class FormRendererWidget extends Composite {

    boolean canSubmit = false;
    private String ctxUID;

    @Inject
    private Frame frame;

    @PostConstruct
    public void initRenderer() {
        VerticalPanel widget = new VerticalPanel();
        initWidget(widget);
        widget.add(frame);
        frame.setWidth("100%");
        frame.setHeight("600px");
        frame.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
    }

    public void submitForm() {
        if (canSubmit) submitForm(ctxUID, false);
    }

    public void submitFormAndPersist() {
        if (canSubmit) submitForm(ctxUID, true);
    }

    private native void submitForm(String uid, boolean persist) /*-{
        var frame = $doc.getElementById('frame_' + uid)

        if (frame) {
            var frameDoc = frame.contentWindow.document;
            frameDoc.getElementById('persist_' + uid).value = persist;
            frameDoc.getElementById('formRendering' + uid).submit();
        }
    }-*/;

    public void loadContext(FormRenderContextTO ctx) {
       loadContext(ctx.getCtxUID());
    }

    public void loadContext(String ctxUID) {
        this.ctxUID = ctxUID;

        frame.getElement().setId("frame_" + ctxUID);
        frame.setUrl(UriUtils.fromString(GWT.getModuleBaseURL() + "Controller?_fb=frc&_fp=Start&ctxUID=" + ctxUID).asString());
        canSubmit = true;
    }

    public void endContext() {
        canSubmit = false;
        frame.setUrl("");
        ctxUID = null;
    }
}

