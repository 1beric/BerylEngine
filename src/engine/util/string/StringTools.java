package engine.util.string;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import engine.util.lambdas.Lambda;

public class StringTools {

	private static final String TAB = "  ";

	public static String smartToString(Object obj) {

		String out = obj.getClass().getSimpleName() + " {\n";

		try {
			for (Method method : obj.getClass().getMethods())
				if (method.getAnnotation(Getter.class) != null) {
					if (method.getReturnType().isAssignableFrom(Iterable.class)) {
						out += TAB + method.getName() + " {\n";
						int i = 0;
						for (Object elem : (Iterable<?>) method.invoke(obj)) {
							out += TAB + TAB + "[" + i + "]: " + elem + "\n";
							i++;
						}
						out += TAB + "}\n";
					} else if (method.getReturnType().isAssignableFrom(Iterator.class)) {
						out += TAB + method.getName();
						int i = 0;
						Iterator<?> iter = (Iterator<?>) method.invoke(obj);
						if (iter.hasNext()) {
							out += " {\n";
							while (iter.hasNext()) {
								out += TAB + TAB + "[" + i + "]: " + iter.next() + "\n";
								i++;
							}
							out += TAB + "}\n";
						} else
							out += ": EMPTY\n";
					} else {
						out += TAB + method.getName() + ": " + method.invoke(obj) + "\n";
					}
				}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		out += "}";

		return out;
	}

	public static String indent(int indentAmt) {
		if (indentAmt == 0)
			return "";
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < indentAmt; i++)
			out.append(TAB);
		return out.toString();
	}

	public static String indentl(int indentAmt) {
		if (indentAmt == 0)
			return "\n";
		StringBuilder out = new StringBuilder();
		out.append("\n");
		for (int i = 0; i < indentAmt; i++)
			out.append(TAB);
		return out.toString();
	}

	public static String buildString(String... strings) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < strings.length; i++)
			out.append(strings[i]);
		return out.toString();
	}

	public static String buildString(int indentAmt, List<?> list) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			out.append(((Stringable) list.get(i)).string(indentAmt));
			if (i < list.size() - 1)
				out.append("\n");
		}
		return out.toString();
	}

	public static String buildString(int indentAmt, Map<?, ?> map) {
		StringBuilder out = new StringBuilder();
		int i = 0;
		for (Entry<?, ?> e : map.entrySet()) {
			out.append(((Stringable) e.getValue()).string(indentAmt));
			if (i < map.size() - 1)
				out.append("\n");
			i++;
		}
		return out.toString();
	}

	public static String buildString(int indentAmt, Lambda m_Hover) {
		return indent(indentAmt) + "LAMBDA";
	}

}
