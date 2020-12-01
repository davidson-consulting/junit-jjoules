package org.powerapi.jjoules.junit4;

import org.powerapi.jjoules.EnergySample;
import org.powerapi.jjoules.rapl.RaplDevice;
import org.powerapi.jjoules.utils.ReportRegister;

import java.util.Map;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * 29/11/2020
 */
public class EnergyTest {

    private static EnergyTest _instance;

    private EnergySample currentEnergySample;

    private String currentTestClassName;

    private String currentTestMethodName;

    private ReportRegister register = new ReportRegister();

    private static EnergyTest _get() {
        if (_instance == null) {
            _instance = new EnergyTest();
        }
        return _instance;
    }

    public static void beforeTest(String className, String testMethodName) {
        final EnergyTest instance = _get();
        instance.currentTestClassName = className;
        instance.currentTestMethodName = testMethodName;
        instance.currentEnergySample = RaplDevice.RAPL.recordEnergy();
    }

    public static void afterTest() {
        final EnergyTest instance = _get();
        final Map<String, Long> report = instance.currentEnergySample.stop();
        instance.register.save(instance.currentTestClassName, instance.currentTestMethodName, report);
        instance.reset();
    }

    private void reset() {
        this.currentTestClassName = null;
        this.currentTestMethodName = null;
        this.currentEnergySample = null;
    }

}
