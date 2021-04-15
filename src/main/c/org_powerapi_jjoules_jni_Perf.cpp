#include "org_powerapi_jjoules_jni_Perf.h"

#include <asm/unistd.h>
#include <linux/perf_event.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ioctl.h>
#include <unistd.h>
#include <inttypes.h>
#include <iostream>
#include <ostream>

static long
perf_event_open(struct perf_event_attr *hw_event, pid_t pid,
                int cpu, int group_fd, unsigned long flags)
{
    int ret;
    ret = syscall(__NR_perf_event_open, hw_event, pid, cpu,
                  group_fd, flags);
    return ret;
}

#define nb_configs 2

// cache-references,cache-misses,cycles,instructions,branches,branch-misses
int fd;
struct perf_event_attr pe;
long long instructions;

JNIEXPORT void JNICALL Java_org_powerapi_jjoules_jni_Perf_start(JNIEnv *env, jobject thisObject)
{
    instructions = 0;
    memset(&pe, 0, sizeof(struct perf_event_attr));
    pe.type = PERF_TYPE_HARDWARE;
    pe.size = sizeof(struct perf_event_attr);
    pe.config = PERF_COUNT_HW_INSTRUCTIONS;
    pe.disabled = 0;
    pe.exclude_kernel = 1;
    pe.exclude_hv = 1;
    fd = perf_event_open(&pe, 0, -1, -1, 0);
    if (fd == -1)
    {
        exit(EXIT_FAILURE);
    }
    ioctl(fd, PERF_EVENT_IOC_RESET, 0);
    ioctl(fd, PERF_EVENT_IOC_ENABLE, 0);
}

JNIEXPORT jlong JNICALL Java_org_powerapi_jjoules_jni_Perf_stop(JNIEnv *env, jobject thisObject)
{

    ioctl(fd, PERF_EVENT_IOC_DISABLE, 0);
    read(fd, &instructions, sizeof(jlong));
    close(fd);
    return instructions;
}
