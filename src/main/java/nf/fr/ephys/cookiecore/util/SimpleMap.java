package nf.fr.ephys.cookiecore.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SimpleMap<K, V> implements Map<K, V> {
	private SimpleSet<Entry<K, V>> data;

	public SimpleMap() {
		this(10);
	}

	public SimpleMap(int size) {
		data = new SimpleSet<>(size);
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		for (Entry<K, V> entry : data) {
			if (entry.getKey().equals(key)) return true;
		}

		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		for (Entry<K, V> entry : data) {
			if (entry.getValue().equals(value)) return true;
		}

		return false;
	}

	@Override
	public V get(Object key) {
		for (Entry<K, V> entry : data) {
			if (entry.getKey().equals(key)) return entry.getValue();
		}

		return null;
	}

	@Override
	public V put(K key, V value) {
		if (key == null || value == null) throw new NullPointerException();

		for (Entry<K, V> entry : data) {
			if (entry.getKey().equals(key)) {
				V oldValue = entry.getValue();

				entry.setValue(value);

				return oldValue;
			}
		}

		data.add(new SimpleEntry(key, value));

		return null;
	}

	@Override
	public V remove(Object key) {
		int i = 0;
		for (Entry<K, V> entry : data) {
			if (entry.getKey().equals(key)) {
				V oldValue = entry.getValue();

				data.remove(i);

				return oldValue;
			}

			i++;
		}

		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		Set<? extends Entry<? extends K, ? extends V>> entrySet = m.entrySet();

		for (Entry<? extends K, ? extends V> set : entrySet) {
			this.put(set.getKey(), set.getValue());
		}
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public Set<K> keySet() {
		SimpleSet<K> set = new SimpleSet<>(data.size());

		for (Entry<K, V> entry : data) {
			set.add(entry.getKey());
		}

		return set;
	}

	@Override
	public Collection<V> values() {
		SimpleSet<V> values = new SimpleSet<>(data.size());

		for (Entry<K, V> entry : data) {
			values.add(entry.getValue());
		}

		return values;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return data;
	}

	private class SimpleEntry implements Entry<K, V> {
		private K key;
		private V value;

		public SimpleEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V oldValue = this.value;

			this.value = value;

			return oldValue;
		}
	}
}