package com.productpool.backend.util;

import java.util.Random;

/**
 * ID 生成工具类
 * 用于生成随机 Long 类型的 ID
 */
public class IdGenerator {

    private static final Random RANDOM = new Random();

    /**
     * 生成随机 Long 类型的 ID
     * 使用雪花算法的思想，结合时间戳和随机数，确保唯一性
     *
     * @return 随机的 Long ID
     */
    public static Long generateId() {
        // 使用当前时间戳（毫秒）左移20位，再加上20位随机数
        // 时间戳范围：约到2285年不会溢出
        // 随机数范围：0 ~ 1048575 (2^20 - 1)
        long timestamp = System.currentTimeMillis();
        int randomValue = RANDOM.nextInt(1 << 20);
        return (timestamp << 20) | randomValue;
    }

    /**
     * 生成指定长度的数字字符串 ID
     *
     * @param length 长度
     * @return 数字字符串 ID
     */
    public static String generateNumericId(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 生成指定前缀和长度的字符串 ID
     *
     * @param prefix 前缀
     * @param length 数字部分长度
     * @return 带前缀的字符串 ID
     */
    public static String generateIdWithPrefix(String prefix, int length) {
        return prefix + generateNumericId(length);
    }
}
