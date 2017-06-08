package com.bonc.epm.paas.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 单位转换工具类 2017年6月8日14:54:37
 *
 * @author daien
 *
 */
public class ConvertUtil {

	public static String computeCpuOut(Map<String, String> val) {
		String cpuVal = val.get("cpu");
		if (cpuVal.contains("m")) {
			Float a1 = Float.valueOf(cpuVal.replace("m", "")) / 1000;
			return a1.toString();
		} else {
			return cpuVal;
		}
	}

	public static String computeMemoryOut(Map<String, String> val) {
		String memVal = val.get("memory");
		memVal = memVal.replaceAll("i", "");
		if (memVal.contains("K")) {
			Float a1 = Float.valueOf(memVal.replace("K", "")) / 1000 / 1000;
			return a1.toString();
		} else if (memVal.contains("M")) {
			Float a1 = Float.valueOf(memVal.replace("M", "")) / 1000;
			return a1.toString();
		} else {
			return memVal.replace("G", "");
		}
	}

	public static double convertCpu(String cpu) {
		if (StringUtils.isEmpty(cpu)) {
			return 0d;
		}
		double ret;
		if (cpu.contains("m")) {
			ret = Double.parseDouble(cpu.replace("m", "")) / 1000d;
		} else {
			ret = Double.parseDouble(cpu);
		}

		return ret;
	}

	/**
	 * k8s内的进制为1000
	 * @param memory
	 * @return double
	 */
	public static double convertMemory(String memory) {
		if (StringUtils.isEmpty(memory)) {
			return 0d;
		}
		memory = memory.replaceAll("i", "");
		double ret;
		if (memory.contains("K")) {
			ret = Double.parseDouble(memory.replace("K", "")) / 1000d / 1000d;
		} else if (memory.contains("M")) {
			ret = Double.parseDouble(memory.replace("M", "")) / 1000d;
		} else {
			ret = Double.parseDouble(memory.replace("G", ""));
		}

		return ret;
	}
}
