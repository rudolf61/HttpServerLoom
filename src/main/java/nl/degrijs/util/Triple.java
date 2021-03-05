package nl.degrijs.util;

public class Triple<S, T, U> extends Pair<S, T> {

	private U _u;

	Triple(S _s, T _t, U _u) {
		super(_s, _t);
		this._u = _u;
	}

	public U get_3() {
		return _u;
	}

}
