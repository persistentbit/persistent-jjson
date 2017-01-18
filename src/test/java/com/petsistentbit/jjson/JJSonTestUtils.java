package com.petsistentbit.jjson;

import com.persistentbit.core.ModuleCore;
import com.persistentbit.core.logging.printing.LogFormatter;
import com.persistentbit.core.logging.printing.LogPrint;
import com.persistentbit.core.logging.printing.LogPrintStream;

/**
 * Logging and other utils for testing
 *
 * @author petermuys
 * @since 12/01/17
 */
public class JJSonTestUtils{

	public static LogFormatter testLogFormatter = ModuleCore.createLogFormatter(true);
	public static LogPrint     testLogPrint     = LogPrintStream.sysOut(testLogFormatter);


}
