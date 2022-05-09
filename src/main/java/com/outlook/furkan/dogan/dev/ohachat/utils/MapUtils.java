package com.outlook.furkan.dogan.dev.ohachat.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Furkan Doğan
 */
public class MapUtils {

  public static <K, V> Map<K, V> map(K k1, V v1) {
    Map<K, V> map = new HashMap<K, V>() {{
      this.put(k1, v1);
    }};

    return Collections.unmodifiableMap(map);
  }

  public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
    Map<K, V> map = new HashMap<K, V>() {{
      this.put(k1, v1);
      this.put(k2, v2);
    }};

    return Collections.unmodifiableMap(map);
  }

  public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3) {
    Map<K, V> map = new HashMap<K, V>() {{
      this.put(k1, v1);
      this.put(k2, v2);
      this.put(k3, v3);
    }};

    return Collections.unmodifiableMap(map);
  }
}
