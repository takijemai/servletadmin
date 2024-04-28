package core;

import java.util.Objects;


public final class Pair<A, B> {
	
	private final A a;
	private final B b;

	/**
	 * Constructor para instanciar objeto de tipo Pair a partir de un par de valores
	 * @param a elemento first
	 * @param b elemento second
	 */
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	/**
	 * Constructor copia
	 * @param other objeto fuente
	 */
	public Pair(Pair<A, B> other) {
		this.a = other.a;
		this.b = other.b;
	}

	/**
	 * Obtener elemento first
	 * @return first
	 */
	public A first() {
		return a;
	}

	/**
	 * Obtener elemento second
	 * @return second
	 */
	public B second() {
		return b;
	}

	/**
	 * Obtener representacion del objeto en una cadena
	 */
	@Override
	public String toString() {
		return "[" + Objects.toString(a) + ", " + Objects.toString(b) + "]";
	}

	/**
	 * Comparar igualdad con otro objeto
	 */
	@Override
	public boolean equals(Object x) {
		if (!(x instanceof Pair))
			return false;
		else {
			Pair<?, ?> that = (Pair<?, ?>) x;
			return Objects.equals(this.a, that.a)
					&& Objects.equals(this.b, that.b);
		}
	}

	/**
	 * Obtener hashCode del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}
}