/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import griffon.core.GriffonApplication;
import griffon.swing.factory.ApplicationFactory;
import griffon.util.ApplicationHolder;
import griffon.util.CallableWithArgs;
import griffon.util.CallableWithArgsClosure;
import org.codehaus.griffon.runtime.core.AbstractGriffonAddon;
import org.codehaus.groovy.runtime.StackTraceUtils;
import org.jdesktop.swinghelper.debug.CheckThreadViolationRepaintManager;
import org.jdesktop.swinghelper.debug.EventDispatchThreadHangMonitor;
import sun.awt.AppContext;

import javax.swing.*;

/**
 * @author Andres Almiray
 */
public class SwingGriffonAddon extends AbstractGriffonAddon {
    private static final String SWING_EDT_VIOLATIONS_KEY = "griffon.swing.edt.violations.check";
    private static final String SWING_EDT_HANG_MONITOR_KEY = "griffon.swing.edt.hang.monitor";
    private static final String SWING_EDT_HANG_MONITOR_TIMEOUT_KEY = "griffon.swing.edt.hang.monitor.timeout";

    private static final String[] GROOVY_PACKAGES =
            System.getProperty("groovy.sanitized.stacktraces",
                            "groovy.," +
                            "org.codehaus.groovy.," +
                            "java.," +
                            "javax.," +
                            "sun.," +
                            "gjdk.groovy.," +
                            CheckThreadViolationRepaintManager.class.getPackage().getName()
            ).split("(\\s|,)+");

    public SwingGriffonAddon() {
        super(ApplicationHolder.getApplication());
        factories.put("application", new ApplicationFactory());
    }

    @Override
    public void addonPostInit(GriffonApplication app) {
        String value = System.getProperty(SWING_EDT_VIOLATIONS_KEY);
        if (value != null && Boolean.parseBoolean(value)) {
            if (getLog().isInfoEnabled()) {
                getLog().info("EDT violations check enabled.");
            }
            RepaintManager currentRepaintManager = getCurrentRepaintManager();
            if (null == currentRepaintManager) currentRepaintManager = new RepaintManager();
            RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager(currentRepaintManager));

            StackTraceUtils.addClassTest(new CallableWithArgsClosure<Boolean>(new CallableWithArgs<Boolean>() {
                @Override
                public Boolean call(Object[] args) {
                    String className = (String) args[0];
                    for (String groovyPackage : GROOVY_PACKAGES) {
                        if (className.startsWith(groovyPackage)) {
                            return false;
                        }
                    }
                    return true;
                }
            }));
        }

        value = System.getProperty(SWING_EDT_HANG_MONITOR_KEY);
        if (value != null && Boolean.parseBoolean(value)) {
            if (getLog().isInfoEnabled()) {
                getLog().info("EDT hang monitor enabled.");
            }
            EventDispatchThreadHangMonitor.initMonitoring();
            value = System.getProperty(SWING_EDT_HANG_MONITOR_TIMEOUT_KEY);
            if (value != null) {
                try {
                    EventDispatchThreadHangMonitor.getInstance().setTimeout(Long.parseLong(value));
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
    }

    private RepaintManager getCurrentRepaintManager() {
        return (RepaintManager) AppContext.getAppContext().get(RepaintManager.class);
    }
}
