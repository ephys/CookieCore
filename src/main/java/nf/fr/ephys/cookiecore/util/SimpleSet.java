package nf.fr.ephys.cookiecore.util;

import java.util.ArrayList;
import java.util.Set;

public class SimpleSet<E> extends ArrayList<E> implements Set<E> {
	public SimpleSet() {
		super();
	}

	public SimpleSet(int size) {
		super(size);
	}
}
