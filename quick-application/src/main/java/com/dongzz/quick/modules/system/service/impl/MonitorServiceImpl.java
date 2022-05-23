package com.dongzz.quick.modules.system.service.impl;

import cn.hutool.core.date.BetweenFormater;
import com.dongzz.quick.common.utils.Constants;
import com.dongzz.quick.common.utils.DateUtil;
import com.dongzz.quick.common.utils.FileUtil;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.modules.system.service.MonitorService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class MonitorServiceImpl implements MonitorService {

    private final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public Map<String, Object> getServerInfo() throws Exception {
        Map<String, Object> data = new HashMap<>();
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        HardwareAbstractionLayer hw = si.getHardware();
        // 系统信息
        data.put("sys", getSystemInfo(os));
        // cpu 信息
        data.put("cpu", getCpuInfo(hw.getProcessor()));
        // 内存信息
        data.put("memory", getMemoryInfo(hw.getMemory()));
        // 交换区信息
        data.put("swap", getSwapInfo(hw.getMemory()));
        // 磁盘
        data.put("disk", getDiskInfo(os));
        // 本次统计时间
        data.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        return data;
    }

    /**
     * 获取磁盘信息
     *
     * @return /
     */
    private Map<String, Object> getDiskInfo(OperatingSystem os) {
        Map<String, Object> data = new LinkedHashMap<>();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        String osName = System.getProperty("os.name");
        long available = 0, total = 0;
        for (OSFileStore fs : fsArray) {
            // windows 需要将所有磁盘分区累加，linux 和 mac 直接累加会出现磁盘重复的问题，暂未解决
            if (osName.toLowerCase().startsWith(Constants.WIN)) {
                available += fs.getUsableSpace();
                total += fs.getTotalSpace();
            } else {
                available = fs.getUsableSpace();
                total = fs.getTotalSpace();
                break;
            }
        }
        long used = total - available;
        data.put("total", total > 0 ? FileUtil.getSize(total) : "未知大小");
        data.put("available", FileUtil.getSize(available));
        data.put("used", FileUtil.getSize(used));
        if (total != 0) {
            data.put("usageRate", df.format(used / (double) total * 100));
        } else {
            data.put("usageRate", 0);
        }
        return data;
    }

    /**
     * 获取交换区信息
     *
     * @param memory /
     * @return /
     */
    private Map<String, Object> getSwapInfo(GlobalMemory memory) {
        Map<String, Object> data = new LinkedHashMap<>();
        VirtualMemory virtualMemory = memory.getVirtualMemory(); // 虚拟内存
        long total = virtualMemory.getSwapTotal();
        long used = virtualMemory.getSwapUsed();
        data.put("total", FormatUtil.formatBytes(total));
        data.put("used", FormatUtil.formatBytes(used));
        data.put("available", FormatUtil.formatBytes(total - used));
        if (used == 0) {
            data.put("usageRate", 0);
        } else {
            data.put("usageRate", df.format(used / (double) total * 100));
        }
        return data;
    }

    /**
     * 获取内存信息
     *
     * @param memory /
     * @return /
     */
    private Map<String, Object> getMemoryInfo(GlobalMemory memory) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("total", FormatUtil.formatBytes(memory.getTotal()));
        data.put("available", FormatUtil.formatBytes(memory.getAvailable()));
        data.put("used", FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()));
        data.put("usageRate", df.format((memory.getTotal() - memory.getAvailable()) / (double) memory.getTotal() * 100));
        return data;
    }

    /**
     * 获取CPU相关信息
     *
     * @param processor 处理器
     * @return
     */
    private Map<String, Object> getCpuInfo(CentralProcessor processor) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", processor.getProcessorIdentifier().getName());
        data.put("package", processor.getPhysicalPackageCount() + "个物理CPU");
        data.put("core", processor.getPhysicalProcessorCount() + "个物理核心");
        data.put("coreNumber", processor.getPhysicalProcessorCount());
        data.put("logic", processor.getLogicalProcessorCount() + "个逻辑CPU");
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 等待1秒
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()]; // CPU执行高优先级用户程序的时间
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()]; // CPU执行低优先级用户程序的时间
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()]; // CPU处于核心态的时间
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()]; // CPU处于空闲的时间
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()]; // IO等待时间
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()]; // 硬中断消耗时间
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()]; // 软中断消耗时间
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()]; // 虚拟机偷取时间
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        data.put("used", df.format(100d * user / totalCpu + 100d * sys / totalCpu));
        data.put("idle", df.format(100d * idle / totalCpu));
        return data;
    }

    /**
     * 获取系统相关信息
     * 系统，运行天数，服务器IP
     *
     * @param os 系统
     * @return
     */
    private Map<String, Object> getSystemInfo(OperatingSystem os) {
        Map<String, Object> data = new LinkedHashMap<>();
        // jvm 运行时间
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        // 计算项目运行时间
        String duration = DateUtil.formatBetween(date, new Date(), BetweenFormater.Level.HOUR);
        // 系统信息
        data.put("os", os.toString());
        data.put("day", duration);
        data.put("ip", StringUtil.getLocalIp());
        return data;
    }

}
