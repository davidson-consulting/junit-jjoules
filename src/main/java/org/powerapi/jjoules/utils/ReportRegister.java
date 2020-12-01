/**
 *
 */
package org.powerapi.jjoules.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author spirals
 *
 */
public class ReportRegister {

    public static final String JSON_EXTENSION = ".json";

    private final File reportsDir;

    public ReportRegister() {
        this.reportsDir = new File("target", "jjoules-reports");
        if (!reportsDir.exists()) {
            if (!reportsDir.mkdir()) {
                throw new RuntimeException("Could not create " + reportsDir.getAbsolutePath());
            }
        }
    }

    public void save(String className, String testMethodName, Map<String, Long> report) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final File file = new File(reportsDir, className + "-" + testMethodName + JSON_EXTENSION);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Could not create " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (FileWriter writer = new FileWriter(file.getAbsoluteFile())) {
            writer.write(gson.toJson(report));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
