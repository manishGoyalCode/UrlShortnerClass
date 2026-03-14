package org.coding.hld.urlshortnerclass.util;

import java.util.UUID;

public class ShortCodeGenerator {
    public static String generate() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6);
    }
}
