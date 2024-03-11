package com.harvey.oj.util;

import org.apache.commons.lang3.StringUtils;

public class SqlUtil {
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
