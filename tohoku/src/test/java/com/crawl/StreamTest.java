package com.crawl;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamTest {
    public static void main(String[] args) {
        Map<String, Integer> keyMaps = new HashMap<>();
        keyMaps.put("a", 1133);
        keyMaps.put("b", 874);
        keyMaps.put("c", 360);
        keyMaps.put("d", 462);
        keyMaps.put("e", 942);
        keyMaps.put("f", 308);
        keyMaps.put("g", 646);
        keyMaps.put("h", 663);
        keyMaps.put("i", 1278);
        keyMaps.put("j", 330);
        keyMaps.put("k", 514);
        keyMaps.put("l", 607);
        keyMaps.put("m", 864);
        keyMaps.put("n", 1018);
        keyMaps.put("o", 451);
        keyMaps.put("p", 18);
        keyMaps.put("q", 0);
        keyMaps.put("r", 677);
        keyMaps.put("s", 665);
        keyMaps.put("t", 447);
        keyMaps.put("u", 944);
        keyMaps.put("v", 0);
        keyMaps.put("w", 179);
        keyMaps.put("x", 0);
        keyMaps.put("y", 373);
        keyMaps.put("z", 6);
        keyMaps.put("@", 257);
        System.out.println(keyMaps.values().stream().collect(Collectors.summarizingInt(x -> x.intValue())).getSum());
    }
}
