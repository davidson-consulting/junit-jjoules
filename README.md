# JUnit extension for measuring energy consumption

This project is an extension of Junit5 based on the project  [j-joules](https://github.com/Mamadou59/j-joules) to extract the energy consumption of RAPL domains (Package and DRAM),.

## Install

First of all you must install the project [j-joules](https://github.com/Mamadou59/j-joules) with

	git clone https://github.com/Mamadou59/j-joule
	
	cd j-joules
	
	mvn clean install
	
Then, clone and install `junit-jjoules` with

	git clone https://github.com/Mamadou59/junit-jjoule
	
	cd junit-jjoules
	
	mvn clean installl

## CONFIGURATIONS

Allow reading perf events:

```sh
sudo -i
echo -1 > /proc/sys/kernel/perf_event_paranoid
```

Add this dependency to your *pom.xml* which will allow you to use this project in your own project

	<dependency>
		<groupId>org.powerapi.jjoules</groupId>
		<artifactId>junit-jjoules</artifactId>
		<version>1.0-SNAPSHOT</version>
    </dependency>

Now, add these dependencies too if you get errors for your tests execution in JUnit 55

	<dependency>
		<groupId>org.junit.jupiter</groupId>
		<artifactId>junit-jupiter-api</artifactId>
		<version>${junit.jupiter.version}</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>org.junit.jupiter</groupId>
		<artifactId>junit-jupiter-engine</artifactId>
		<version>${junit.jupiter.version}</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>org.junit.platform</groupId>
		<artifactId>junit-platform-runner</artifactId>
		<version>${junit.platform.version}</version>
		<scope>test</scope>
	</dependency>

You also need plugins configurations for creating the tests classes call graph 

Add this one which create a jar file for all test classes

	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-jar-plugin</artifactId>
		<version>3.2.0</version>
		<executions>
			<execution>
				<phase>test</phase>
				<goals>
					<goal>test-jar</goal>
				</goals>
				<!-- if you want exclude some classes use this config -->
				<!-- <configuration> 
					<excludes> 
						<exclude>org/powerapi/jjoules/Fibonacci.class</exclude> 
					</excludes> 
				</configuration> -->
			</execution>
		</executions>
	</plugin>
	
Copy the jar file (call graph tool) from this directory `src/main/java/org/powerapi/jjoules/ressources/javacg-0.1-SNAPSHOT-static.jar` to your project root directory.

Then add these plugins

	<plugin>
		<groupId>org.codehaus.mojo</groupId>
		<artifactId>exec-maven-plugin</artifactId>
		<version>3.0.0</version>
		<executions>
			<execution>
				<id>call-graph</id>
				<phase>test</phase>
				<goals>
					<goal>exec</goal>
				</goals>
				<configuration>
					<executable>java</executable>
					<arguments>
						<argument>-jar</argument>
						<argument>javacg-0.1-SNAPSHOT-static.jar</argument>
						<argument>${project.build.directory}/${project.build.finalName}-tests.jar</argument>
					</arguments>
					<outputFile>${project.build.directory}/call-graph/tests-callgraph.txt</outputFile>
					<!-- to put your own output file name -->
					<!-- <outputFile>${project.build.directory}/tests-call-graph/callgraph.txt</outputFile> -->
				</configuration>
			</execution>
		</executions>
	</plugin>
	
	<plugin>
		<groupId>org.powerapi.jjoules</groupId>
		<artifactId>junit-jjoules</artifactId>
		<version>1.0-SNAPSHOT</version>
		<executions>
			<execution>
				<id>callgraph-matrix</id>
				<phase>test</phase>
				<goals>
					<goal>generate-matrix</goal>
				</goals>
				<!-- example configuration -->
				<!-- <configuration> 
					<outputDir>${project.build.directory}/tests-call-graph</outputDir> 
					<outputFilename>matrix.txt</outputFilename> 
					<callgraphFilename>callgraph.txt</callgraphFilename> 
				</configuration> -->
			</execution>
		</executions>
	</plugin>
	
The first one execute java command with call graph tool to create à call graph file and put the results in this directory *target/call-graph/tests-callgraph.txt* you can also change it by changing the value in `<outputFile>` but be careful you will have to report it in the second plugin like did in commented configurations. The second plugin configuration execute goal *generate-matrix* to create a matrix of call graph for only energy tests. 

#### How to write tests 

You have to add the annotation `@EnergyTest` on each energy, instead of classical `@Test` annotation.
Following an example:  

	//...
	
	public class MyClassTest{
		
		@EnergyTest
		public void MyFirstTest(){
			// Test something
		}
		
	//...

## RUN

In your project root execute this command to run all tests
	
	mvn clean test

Now, you will get the energy consumption reports of all energy tests in directory `/target/jjoules-reports` only one test in one json file.

You can also see the call graph files in this directory `/target/call-graph` if you did not change the directory path before in plugins configuration.

## Developing the lib for JNI and retrieve instructions

```sh
javac -h javac -h . src/main/java/org/powerapi/jjoules/jni/Perf.java
mv org_powerapi_jjoules_jni_Perf.h src/main/c/org_powerapi_jjoules_jni_Perf.h
```

Modify the corresponding `.cpp` file. 

You must set your JAVA_HOME correctly.
Then run the commande `make` from the folder `src/main/c/` to compile and build the shared library.

Run the following commands to add the new shared lib in your paths:

```shell
LD_LIBRARY_PATH=<absolute-path>/junit-jjoules/src/main/c/lib/
export LD_LIBRARY_PATH
```
