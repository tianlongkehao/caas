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

	/**
	 * convertMemory:转换内存值字符串为无单位数值. <br/>
	 *
	 * @param memory
	 * @return double
	 */
	public static double parseMemory(String memory) {
		if (StringUtils.isEmpty(memory)) {
			return 0d;
		}
		memory = memory.replaceAll("i", "");
		double ret;
		if (memory.endsWith("n")) {
			ret = Double.parseDouble(memory.replace("n", "")) * Math.pow(10, -9);
		} else if (memory.endsWith("u")) {
			ret = Double.parseDouble(memory.replace("u", "")) * Math.pow(10, -6);
		} else if (memory.endsWith("m")) {
			ret = Double.parseDouble(memory.replace("m", "")) * Math.pow(10, -3);
		} else if (memory.endsWith("k")) {
			ret = Double.parseDouble(memory.replace("k", "")) * Math.pow(10, 3);
		} else if (memory.endsWith("K")) {
			ret = Double.parseDouble(memory.replace("K", "")) * Math.pow(10, 3);
		} else if (memory.endsWith("M")) {
			ret = Double.parseDouble(memory.replace("M", "")) * Math.pow(10, 6);
		} else if (memory.endsWith("G")) {
			ret = Double.parseDouble(memory.replace("G", "")) * Math.pow(10, 9);
		} else if (memory.endsWith("T")) {
			ret = Double.parseDouble(memory.replace("T", "")) * Math.pow(10, 12);
		} else if (memory.endsWith("P")) {
			ret = Double.parseDouble(memory.replace("P", "")) * Math.pow(10, 15);
		} else if (memory.endsWith("E")) {
			ret = Double.parseDouble(memory.replace("E", "")) * Math.pow(10, 18);
		} else if (memory.endsWith("Ki")) {
			ret = Double.parseDouble(memory.replace("Ki", "")) * Math.pow(2, 10);
		} else if (memory.endsWith("Mi")) {
			ret = Double.parseDouble(memory.replace("Mi", "")) * Math.pow(2, 20);
		} else if (memory.endsWith("Gi")) {
			ret = Double.parseDouble(memory.replace("Gi", "")) * Math.pow(2, 30);
		} else if (memory.endsWith("Ti")) {
			ret = Double.parseDouble(memory.replace("Ti", "")) * Math.pow(2, 40);
		} else if (memory.endsWith("Pi")) {
			ret = Double.parseDouble(memory.replace("Pi", "")) * Math.pow(2, 50);
		} else if (memory.endsWith("Ei")) {
			ret = Double.parseDouble(memory.replace("Ei", "")) * Math.pow(2, 60);
		} else {
			ret = Double.parseDouble(memory);
		}
		return ret;
	}
}
