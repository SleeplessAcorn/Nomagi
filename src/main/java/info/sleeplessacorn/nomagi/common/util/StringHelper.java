package info.sleeplessacorn.nomagi.common.util;

import com.google.common.base.CaseFormat;

public final class StringHelper {

    public static String toLangKey(String registryName) {
        return CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL).convert(registryName);
    }

    public static String getOreName(String registryName) {
        return CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL).convert(registryName);
    }

}

