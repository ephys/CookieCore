package nf.fr.ephys.cookiecore.util;

import java.util.HashMap;

/**
 * String -> V HashMap that does not care about the case of the key.
 * @param <V> The type of the elements inserted in the Map.
 */
@Deprecated
public class CaseInsensitiveMap<V> extends HashMap<String, V> {
	@Override
	public V put(String key, V value) {
		return super.put(key.toLowerCase(), value);
	}

	public V get(String key) {
		return super.get(key.toLowerCase());
	}

	@Override
	public V get(Object key) {
		return get((String) key);
	}
}