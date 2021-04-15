package org.powerapi.jjoules.junit5;

import java.util.Map;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.powerapi.jjoules.EnergySample;
import org.powerapi.jjoules.jni.Perf;
import org.powerapi.jjoules.rapl.RaplDevice;
import org.powerapi.jjoules.utils.ReportRegister;

/**
 * JUnit extension for logging the energy consumption of tests.
 *
 */
public class EnergyTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final Namespace NAMESPACE = Namespace.create(EnergyTestExtension.class);

    private static final ReportRegister REGISTER = new ReportRegister();

    private static final Perf perf = new Perf();

    private Store getStore(final ExtensionContext context) {
        return context.getStore(NAMESPACE);
    }

    @Override
    public void beforeTestExecution(final ExtensionContext context) {
        if (context.getRequiredTestMethod().isAnnotationPresent(EnergyTest.class)) {
            getStore(context).put(context.getRequiredTestMethod(), RaplDevice.RAPL.recordEnergy());
            try {
                perf.start();
            } catch (java.lang.UnsatisfiedLinkError e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterTestExecution(final ExtensionContext context) {
        if (context.getRequiredTestMethod().isAnnotationPresent(EnergyTest.class)) {
            final long instructions = perf.stop();
            final Map<String, Long> report = getStore(context).get(context.getRequiredTestMethod(), EnergySample.class).stop();
            report.put("instructions", instructions);
            REGISTER.save(context.getRequiredTestClass().getName(), context.getRequiredTestMethod().getName(), report);
        }
    }

}
