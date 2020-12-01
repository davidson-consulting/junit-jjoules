package org.powerapi.jjoules.junit5;

import org.powerapi.jjoules.AbstractTest;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * 28/10/2020
 */
public class Test extends AbstractTest {

    @EnergyTest
    public void method() {
        System.out.println("1");
        this.pathToAssert.add("org.powerapi.jjoules.junit.Test-method1.json");
    }

    @EnergyTest
    public void method2() {
        System.out.println("2");
        this.pathToAssert.add("org.powerapi.jjoules.junit.Test-method2.json");
    }

}
