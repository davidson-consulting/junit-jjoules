all:
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux org_powerapi_jjoules_jni_Perf.cpp -o org_powerapi_jjoules_jni_Perf.o
	g++ -shared -fPIC -o lib/libperf.so org_powerapi_jjoules_jni_Perf.o -lc