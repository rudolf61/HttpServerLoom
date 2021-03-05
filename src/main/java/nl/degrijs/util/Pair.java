package nl.degrijs.util;

public class Pair<S, T> {
	private S _s;
	private T _t;

	Pair(S _s, T _t) {
		super();
		this._s = _s;
		this._t = _t;
	}

	public S get_1() {
		return _s;
	}

	public T get_2() {
		return _t;
	}

	public static <S, T> Pair<S, T> of(S s, T t) {
		return new Pair<>(s, t);
	}

}
