package org.kframework.main;

import org.kframework.main.lib.JavaFrontParserMain;
import org.kframework.main.lib.safecallcmd1_0_0;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.StrategoExit;

public class KParser {
	private static Context context = null;

	private static void init() {
		synchronized (KParser.class) {
			if (context == null) {
				context = JavaFrontParserMain.init();
			}
		}
	}

	public static String ParseKString(String kDefinition) {
		init();
		String rez = "";
		context.setStandAlone(true);
		IStrategoTerm result = null;
		try {
			try {
				result = context.invokeStrategyCLI(safecallcmd1_0_0.instance,
						"a.exe", kDefinition);

			} finally {
				context.getIOAgent().closeAllFiles();
			}
			if (result == null) {
				System.err.println("rewriting failed, trace:");
				context.printStackTrace();
				context.setStandAlone(false);
				System.exit(1);
			} else {
				context.setStandAlone(false);
			}
		} catch (StrategoExit exit) {
			context.setStandAlone(false);
			System.exit(exit.getValue());
		}

		if (result.getTermType() == IStrategoTerm.STRING) {
			rez = (((IStrategoString) result).stringValue());
		} else {
			rez = result.toString();
		}
		return rez;
	}
}
