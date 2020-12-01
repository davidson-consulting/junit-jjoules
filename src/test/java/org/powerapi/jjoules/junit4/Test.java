package org.powerapi.jjoules.junit4;

import org.powerapi.jjoules.AbstractTest;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * 28/10/2020
 */
public class Test extends AbstractTest {

    @org.junit.jupiter.api.Test
    public void method() {
        EnergyTest.beforeTest("org.powerapi.jjoules.junit4.Test", "method");
        System.out.println("1");
        this.pathToAssert.add("org.powerapi.jjoules.junit4.Test-method.json");
        EnergyTest.afterTest();
    }

    @org.junit.jupiter.api.Test
    public void method2() {
        EnergyTest.beforeTest("org.powerapi.jjoules.junit4.Test", "method2");
        System.out.println("2");
        this.pathToAssert.add("org.powerapi.jjoules.junit4.Test-method2.json");
        EnergyTest.afterTest();
    }

}
