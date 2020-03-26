package sandbox.filegeneration_0_1;

import routines.DataOperation;
import routines.TalendDataGenerator;
import routines.DataQuality;
import routines.Relational;
import routines.Mathematical;
import routines.DataQualityDependencies;
import routines.SQLike;
import routines.Numeric;
import routines.TalendString;
import routines.StringHandling;
import routines.DQTechnical;
import routines.TalendDate;
import routines.DataMasking;
import routines.DqStringHandling;
import routines.system.*;
import routines.system.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Comparator;

@SuppressWarnings("unused")
/**
 * Job: fileGeneration Purpose: <br>
 * Description:  <br>
 * @author jesus.i.hernandez@autozone.com
 * @version 6.2.1.20160704_1411
 * @status 
 */
public class fileGeneration implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "fileGeneration.log");
	}
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(fileGeneration.class);

	public final Object obj = new Object();

	// for transmiting parameters purpose
	private Object valueObject = null;

	public Object getValueObject() {
		return this.valueObject;
	}

	public void setValueObject(Object valueObject) {
		this.valueObject = valueObject;
	}

	private final static String defaultCharset = java.nio.charset.Charset
			.defaultCharset().name();

	private final static String utf8Charset = "UTF-8";

	// create and load default properties
	private java.util.Properties defaultProps = new java.util.Properties();

	// create application properties with default
	public class ContextProperties extends java.util.Properties {

		private static final long serialVersionUID = 1L;

		public ContextProperties(java.util.Properties properties) {
			super(properties);
		}

		public ContextProperties() {
			super();
		}

		public void synchronizeContext() {

		}

	}

	private ContextProperties context = new ContextProperties();

	public ContextProperties getContext() {
		return this.context;
	}

	private final String jobVersion = "0.1";
	private final String jobName = "fileGeneration";
	private final String projectName = "SANDBOX";
	public Integer errorCode = null;
	private String currentComponent = "";

	private final java.util.Map<String, Object> globalMap = new java.util.HashMap<String, Object>();
	private final static java.util.Map<String, Object> junitGlobalMap = new java.util.HashMap<String, Object>();

	private final java.util.Map<String, Long> start_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Long> end_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Boolean> ok_Hash = new java.util.HashMap<String, Boolean>();
	public final java.util.List<String[]> globalBuffer = new java.util.ArrayList<String[]>();

	private RunStat runStat = new RunStat();

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";

	public void setDataSources(
			java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources
				.entrySet()) {
			talendDataSources.put(
					dataSourceEntry.getKey(),
					new routines.system.TalendDataSource(dataSourceEntry
							.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
	}

	LogCatcherUtils tLogCatcher_1 = new LogCatcherUtils();

	private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	private final java.io.PrintStream errorMessagePS = new java.io.PrintStream(
			new java.io.BufferedOutputStream(baos));

	public String getExceptionStackTrace() {
		if ("failure".equals(this.getStatus())) {
			errorMessagePS.flush();
			return baos.toString();
		}
		return null;
	}

	private Exception exception;

	public Exception getException() {
		if ("failure".equals(this.getStatus())) {
			return this.exception;
		}
		return null;
	}

	private class TalendException extends Exception {

		private static final long serialVersionUID = 1L;

		private java.util.Map<String, Object> globalMap = null;
		private Exception e = null;
		private String currentComponent = null;
		private String virtualComponentName = null;

		public void setVirtualComponentName(String virtualComponentName) {
			this.virtualComponentName = virtualComponentName;
		}

		private TalendException(Exception e, String errorComponent,
				final java.util.Map<String, Object> globalMap) {
			this.currentComponent = errorComponent;
			this.globalMap = globalMap;
			this.e = e;
		}

		public Exception getException() {
			return this.e;
		}

		public String getCurrentComponent() {
			return this.currentComponent;
		}

		public String getExceptionCauseMessage(Exception e) {
			Throwable cause = e;
			String message = null;
			int i = 10;
			while (null != cause && 0 < i--) {
				message = cause.getMessage();
				if (null == message) {
					cause = cause.getCause();
				} else {
					break;
				}
			}
			if (null == message) {
				message = e.getClass().getName();
			}
			return message;
		}

		@Override
		public void printStackTrace() {
			if (!(e instanceof TalendException || e instanceof TDieException)) {
				if (virtualComponentName != null
						&& currentComponent.indexOf(virtualComponentName + "_") == 0) {
					globalMap.put(virtualComponentName + "_ERROR_MESSAGE",
							getExceptionCauseMessage(e));
				}
				globalMap.put(currentComponent + "_ERROR_MESSAGE",
						getExceptionCauseMessage(e));
				System.err
						.println("Exception in component " + currentComponent);
			}
			if (!(e instanceof TDieException)) {
				if (e instanceof TalendException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
					e.printStackTrace(errorMessagePS);
					fileGeneration.this.exception = e;
				}
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass()
							.getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(fileGeneration.this, new Object[] { e,
									currentComponent, globalMap });
							break;
						}
					}

					if (!(e instanceof TDieException)) {
						tLogCatcher_1.addMessage("Java Exception",
								currentComponent, 6, e.getClass().getName()
										+ ":" + e.getMessage(), 1);
						tLogCatcher_1Process(globalMap);
					}
				} catch (TalendException e) {
					// do nothing

				} catch (Exception e) {
					this.e.printStackTrace();
				}
			}
		}
	}

	public void tLogCatcher_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tLogCatcher_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tLogCatcher_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tWarn_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tWarn_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRowGenerator_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tReplicate_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileOutputXML_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tWarn_2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tWarn_2_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileOutputDelimited_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tWarn_3_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tWarn_3_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tWarn_4_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tWarn_4_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogCatcher_1_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tWarn_1_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tRowGenerator_1_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tWarn_2_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tWarn_3_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tWarn_4_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public static class row1Struct implements
			routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_SANDBOX_fileGeneration = new byte[0];
		static byte[] commonByteArray_SANDBOX_fileGeneration = new byte[0];

		public java.util.Date moment;

		public java.util.Date getMoment() {
			return this.moment;
		}

		public String pid;

		public String getPid() {
			return this.pid;
		}

		public String root_pid;

		public String getRoot_pid() {
			return this.root_pid;
		}

		public String father_pid;

		public String getFather_pid() {
			return this.father_pid;
		}

		public String project;

		public String getProject() {
			return this.project;
		}

		public String job;

		public String getJob() {
			return this.job;
		}

		public String context;

		public String getContext() {
			return this.context;
		}

		public Integer priority;

		public Integer getPriority() {
			return this.priority;
		}

		public String type;

		public String getType() {
			return this.type;
		}

		public String origin;

		public String getOrigin() {
			return this.origin;
		}

		public String message;

		public String getMessage() {
			return this.message;
		}

		public Integer code;

		public Integer getCode() {
			return this.code;
		}

		private java.util.Date readDate(ObjectInputStream dis)
				throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos)
				throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_SANDBOX_fileGeneration.length) {
					if (length < 1024
							&& commonByteArray_SANDBOX_fileGeneration.length == 0) {
						commonByteArray_SANDBOX_fileGeneration = new byte[1024];
					} else {
						commonByteArray_SANDBOX_fileGeneration = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_SANDBOX_fileGeneration, 0, length);
				strReturn = new String(commonByteArray_SANDBOX_fileGeneration,
						0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private Integer readInteger(ObjectInputStream dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private void writeInteger(Integer intNum, ObjectOutputStream dos)
				throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_SANDBOX_fileGeneration) {

				try {

					int length = 0;

					this.moment = readDate(dis);

					this.pid = readString(dis);

					this.root_pid = readString(dis);

					this.father_pid = readString(dis);

					this.project = readString(dis);

					this.job = readString(dis);

					this.context = readString(dis);

					this.priority = readInteger(dis);

					this.type = readString(dis);

					this.origin = readString(dis);

					this.message = readString(dis);

					this.code = readInteger(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// java.util.Date

				writeDate(this.moment, dos);

				// String

				writeString(this.pid, dos);

				// String

				writeString(this.root_pid, dos);

				// String

				writeString(this.father_pid, dos);

				// String

				writeString(this.project, dos);

				// String

				writeString(this.job, dos);

				// String

				writeString(this.context, dos);

				// Integer

				writeInteger(this.priority, dos);

				// String

				writeString(this.type, dos);

				// String

				writeString(this.origin, dos);

				// String

				writeString(this.message, dos);

				// Integer

				writeInteger(this.code, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("moment=" + String.valueOf(moment));
			sb.append(",pid=" + pid);
			sb.append(",root_pid=" + root_pid);
			sb.append(",father_pid=" + father_pid);
			sb.append(",project=" + project);
			sb.append(",job=" + job);
			sb.append(",context=" + context);
			sb.append(",priority=" + String.valueOf(priority));
			sb.append(",type=" + type);
			sb.append(",origin=" + origin);
			sb.append(",message=" + message);
			sb.append(",code=" + String.valueOf(code));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (moment == null) {
				sb.append("<null>");
			} else {
				sb.append(moment);
			}

			sb.append("|");

			if (pid == null) {
				sb.append("<null>");
			} else {
				sb.append(pid);
			}

			sb.append("|");

			if (root_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(root_pid);
			}

			sb.append("|");

			if (father_pid == null) {
				sb.append("<null>");
			} else {
				sb.append(father_pid);
			}

			sb.append("|");

			if (project == null) {
				sb.append("<null>");
			} else {
				sb.append(project);
			}

			sb.append("|");

			if (job == null) {
				sb.append("<null>");
			} else {
				sb.append(job);
			}

			sb.append("|");

			if (context == null) {
				sb.append("<null>");
			} else {
				sb.append(context);
			}

			sb.append("|");

			if (priority == null) {
				sb.append("<null>");
			} else {
				sb.append(priority);
			}

			sb.append("|");

			if (type == null) {
				sb.append("<null>");
			} else {
				sb.append(type);
			}

			sb.append("|");

			if (origin == null) {
				sb.append("<null>");
			} else {
				sb.append(origin);
			}

			sb.append("|");

			if (message == null) {
				sb.append("<null>");
			} else {
				sb.append(message);
			}

			sb.append("|");

			if (code == null) {
				sb.append("<null>");
			} else {
				sb.append(code);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row1Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tLogCatcher_1Process(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tLogCatcher_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {

			String currentMethodName = new java.lang.Exception()
					.getStackTrace()[0].getMethodName();
			boolean resumeIt = currentMethodName.equals(resumeEntryMethodName);
			if (resumeEntryMethodName == null || resumeIt || globalResumeTicket) {// start
																					// the
																					// resume
				globalResumeTicket = true;

				row1Struct row1 = new row1Struct();

				/**
				 * [tLogRow_1 begin ] start
				 */

				ok_Hash.put("tLogRow_1", false);
				start_Hash.put("tLogRow_1", System.currentTimeMillis());

				currentComponent = "tLogRow_1";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("row1" + iterateId, 0, 0);

					}
				}

				int tos_count_tLogRow_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tLogRow_1 - " + "Start to work.");
				StringBuilder log4jParamters_tLogRow_1 = new StringBuilder();
				log4jParamters_tLogRow_1.append("Parameters:");
				log4jParamters_tLogRow_1.append("BASIC_MODE" + " = " + "true");
				log4jParamters_tLogRow_1.append(" | ");
				log4jParamters_tLogRow_1
						.append("TABLE_PRINT" + " = " + "false");
				log4jParamters_tLogRow_1.append(" | ");
				log4jParamters_tLogRow_1.append("VERTICAL" + " = " + "false");
				log4jParamters_tLogRow_1.append(" | ");
				log4jParamters_tLogRow_1.append("FIELDSEPARATOR" + " = "
						+ "\"|\"");
				log4jParamters_tLogRow_1.append(" | ");
				log4jParamters_tLogRow_1.append("PRINT_HEADER" + " = "
						+ "false");
				log4jParamters_tLogRow_1.append(" | ");
				log4jParamters_tLogRow_1.append("PRINT_UNIQUE_NAME" + " = "
						+ "false");
				log4jParamters_tLogRow_1.append(" | ");
				log4jParamters_tLogRow_1.append("PRINT_COLNAMES" + " = "
						+ "false");
				log4jParamters_tLogRow_1.append(" | ");
				log4jParamters_tLogRow_1.append("USE_FIXED_LENGTH" + " = "
						+ "false");
				log4jParamters_tLogRow_1.append(" | ");
				log4jParamters_tLogRow_1.append("PRINT_CONTENT_WITH_LOG4J"
						+ " = " + "true");
				log4jParamters_tLogRow_1.append(" | ");
				if (log.isDebugEnabled())
					log.debug("tLogRow_1 - " + log4jParamters_tLogRow_1);

				// /////////////////////

				final String OUTPUT_FIELD_SEPARATOR_tLogRow_1 = "|";
				java.io.PrintStream consoleOut_tLogRow_1 = null;

				StringBuilder strBuffer_tLogRow_1 = null;
				int nb_line_tLogRow_1 = 0;
				// /////////////////////

				/**
				 * [tLogRow_1 begin ] stop
				 */

				/**
				 * [tLogCatcher_1 begin ] start
				 */

				ok_Hash.put("tLogCatcher_1", false);
				start_Hash.put("tLogCatcher_1", System.currentTimeMillis());

				currentComponent = "tLogCatcher_1";

				int tos_count_tLogCatcher_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tLogCatcher_1 - " + "Start to work.");
				StringBuilder log4jParamters_tLogCatcher_1 = new StringBuilder();
				log4jParamters_tLogCatcher_1.append("Parameters:");
				log4jParamters_tLogCatcher_1.append("CATCH_JAVA_EXCEPTION"
						+ " = " + "true");
				log4jParamters_tLogCatcher_1.append(" | ");
				log4jParamters_tLogCatcher_1.append("CATCH_TDIE" + " = "
						+ "true");
				log4jParamters_tLogCatcher_1.append(" | ");
				log4jParamters_tLogCatcher_1.append("CATCH_TWARN" + " = "
						+ "true");
				log4jParamters_tLogCatcher_1.append(" | ");
				log4jParamters_tLogCatcher_1.append("CATCH_TACTIONFAILURE"
						+ " = " + "true");
				log4jParamters_tLogCatcher_1.append(" | ");
				if (log.isDebugEnabled())
					log.debug("tLogCatcher_1 - " + log4jParamters_tLogCatcher_1);

				for (LogCatcherUtils.LogCatcherMessage lcm : tLogCatcher_1
						.getMessages()) {
					row1.type = lcm.getType();
					row1.origin = (lcm.getOrigin() == null
							|| lcm.getOrigin().length() < 1 ? null : lcm
							.getOrigin());
					row1.priority = lcm.getPriority();
					row1.message = lcm.getMessage();
					row1.code = lcm.getCode();

					row1.moment = java.util.Calendar.getInstance().getTime();

					row1.pid = pid;
					row1.root_pid = rootPid;
					row1.father_pid = fatherPid;

					row1.project = projectName;
					row1.job = jobName;
					row1.context = contextStr;

					/**
					 * [tLogCatcher_1 begin ] stop
					 */

					/**
					 * [tLogCatcher_1 main ] start
					 */

					currentComponent = "tLogCatcher_1";

					tos_count_tLogCatcher_1++;

					/**
					 * [tLogCatcher_1 main ] stop
					 */

					/**
					 * [tLogRow_1 main ] start
					 */

					currentComponent = "tLogRow_1";

					// row1
					// row1

					if (execStat) {
						runStat.updateStatOnConnection("row1" + iterateId, 1, 1);
					}

					if (log.isTraceEnabled()) {
						log.trace("row1 - "
								+ (row1 == null ? "" : row1.toLogString()));
					}

					// /////////////////////

					strBuffer_tLogRow_1 = new StringBuilder();

					if (row1.moment != null) { //

						strBuffer_tLogRow_1.append(FormatterUtils.format_Date(
								row1.moment, "yyyy-MM-dd HH:mm:ss"));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.pid != null) { //

						strBuffer_tLogRow_1.append(String.valueOf(row1.pid));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.root_pid != null) { //

						strBuffer_tLogRow_1.append(String
								.valueOf(row1.root_pid));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.father_pid != null) { //

						strBuffer_tLogRow_1.append(String
								.valueOf(row1.father_pid));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.project != null) { //

						strBuffer_tLogRow_1
								.append(String.valueOf(row1.project));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.job != null) { //

						strBuffer_tLogRow_1.append(String.valueOf(row1.job));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.context != null) { //

						strBuffer_tLogRow_1
								.append(String.valueOf(row1.context));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.priority != null) { //

						strBuffer_tLogRow_1.append(String
								.valueOf(row1.priority));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.type != null) { //

						strBuffer_tLogRow_1.append(String.valueOf(row1.type));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.origin != null) { //

						strBuffer_tLogRow_1.append(String.valueOf(row1.origin));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.message != null) { //

						strBuffer_tLogRow_1
								.append(String.valueOf(row1.message));

					} //

					strBuffer_tLogRow_1.append("|");

					if (row1.code != null) { //

						strBuffer_tLogRow_1.append(String.valueOf(row1.code));

					} //

					if (globalMap.get("tLogRow_CONSOLE") != null) {
						consoleOut_tLogRow_1 = (java.io.PrintStream) globalMap
								.get("tLogRow_CONSOLE");
					} else {
						consoleOut_tLogRow_1 = new java.io.PrintStream(
								new java.io.BufferedOutputStream(System.out));
						globalMap.put("tLogRow_CONSOLE", consoleOut_tLogRow_1);
					}
					log.info("tLogRow_1 - Content of row "
							+ (nb_line_tLogRow_1 + 1) + ": "
							+ strBuffer_tLogRow_1.toString());
					consoleOut_tLogRow_1
							.println(strBuffer_tLogRow_1.toString());
					consoleOut_tLogRow_1.flush();
					nb_line_tLogRow_1++;
					// ////

					// ////

					// /////////////////////

					tos_count_tLogRow_1++;

					/**
					 * [tLogRow_1 main ] stop
					 */

					/**
					 * [tLogCatcher_1 end ] start
					 */

					currentComponent = "tLogCatcher_1";

				}

				if (log.isDebugEnabled())
					log.debug("tLogCatcher_1 - " + "Done.");

				ok_Hash.put("tLogCatcher_1", true);
				end_Hash.put("tLogCatcher_1", System.currentTimeMillis());

				/**
				 * [tLogCatcher_1 end ] stop
				 */

				/**
				 * [tLogRow_1 end ] start
				 */

				currentComponent = "tLogRow_1";

				// ////
				// ////
				globalMap.put("tLogRow_1_NB_LINE", nb_line_tLogRow_1);
				if (log.isInfoEnabled())
					log.info("tLogRow_1 - " + "Printed row count: "
							+ nb_line_tLogRow_1 + ".");

				// /////////////////////

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("row1" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("tLogRow_1 - " + "Done.");

				ok_Hash.put("tLogRow_1", true);
				end_Hash.put("tLogRow_1", System.currentTimeMillis());

				/**
				 * [tLogRow_1 end ] stop
				 */

			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage());
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tLogCatcher_1 finally ] start
				 */

				currentComponent = "tLogCatcher_1";

				/**
				 * [tLogCatcher_1 finally ] stop
				 */

				/**
				 * [tLogRow_1 finally ] start
				 */

				currentComponent = "tLogRow_1";

				/**
				 * [tLogRow_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tLogCatcher_1_SUBPROCESS_STATE", 1);
	}

	public void tWarn_1Process(final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tWarn_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {

			String currentMethodName = new java.lang.Exception()
					.getStackTrace()[0].getMethodName();
			boolean resumeIt = currentMethodName.equals(resumeEntryMethodName);
			if (resumeEntryMethodName == null || resumeIt || globalResumeTicket) {// start
																					// the
																					// resume
				globalResumeTicket = true;

				/**
				 * [tWarn_1 begin ] start
				 */

				ok_Hash.put("tWarn_1", false);
				start_Hash.put("tWarn_1", System.currentTimeMillis());

				currentComponent = "tWarn_1";

				int tos_count_tWarn_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tWarn_1 - " + "Start to work.");
				StringBuilder log4jParamters_tWarn_1 = new StringBuilder();
				log4jParamters_tWarn_1.append("Parameters:");
				log4jParamters_tWarn_1.append("MESSAGE" + " = "
						+ "\"File generation started\"");
				log4jParamters_tWarn_1.append(" | ");
				log4jParamters_tWarn_1.append("CODE" + " = " + "0");
				log4jParamters_tWarn_1.append(" | ");
				log4jParamters_tWarn_1.append("PRIORITY" + " = " + "3");
				log4jParamters_tWarn_1.append(" | ");
				if (log.isDebugEnabled())
					log.debug("tWarn_1 - " + log4jParamters_tWarn_1);

				/**
				 * [tWarn_1 begin ] stop
				 */

				/**
				 * [tWarn_1 main ] start
				 */

				currentComponent = "tWarn_1";

				resumeUtil.addLog("USER_DEF_LOG", "NODE:tWarn_1", "", Thread
						.currentThread().getId() + "", "INFO", "",
						"File generation started", "", "");
				if (log.isInfoEnabled())
					log.info("tWarn_1 - " + "Message: "
							+ "File generation started" + ". Code: " + 0);
				if (log.isDebugEnabled())
					log.debug("tWarn_1 - "
							+ "Sending message to tLogCatcher_1.");
				tLogCatcher_1.addMessage("tWarn", "tWarn_1", 3,
						"File generation started", 0);
				tLogCatcher_1Process(globalMap);
				globalMap.put("tWarn_1_WARN_MESSAGES",
						"File generation started");
				globalMap.put("tWarn_1_WARN_PRIORITY", 3);
				globalMap.put("tWarn_1_WARN_CODE", 0);

				tos_count_tWarn_1++;

				/**
				 * [tWarn_1 main ] stop
				 */

				/**
				 * [tWarn_1 end ] start
				 */

				currentComponent = "tWarn_1";

				if (log.isDebugEnabled())
					log.debug("tWarn_1 - " + "Done.");

				ok_Hash.put("tWarn_1", true);
				end_Hash.put("tWarn_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk1", 0, "ok");
				}
				tRowGenerator_1Process(globalMap);

				/**
				 * [tWarn_1 end ] stop
				 */
			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage());
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tWarn_1 finally ] start
				 */

				currentComponent = "tWarn_1";

				/**
				 * [tWarn_1 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tWarn_1_SUBPROCESS_STATE", 1);
	}

	public static class row3Struct implements
			routines.system.IPersistableRow<row3Struct> {
		final static byte[] commonByteArrayLock_SANDBOX_fileGeneration = new byte[0];
		static byte[] commonByteArray_SANDBOX_fileGeneration = new byte[0];

		public String id;

		public String getId() {
			return this.id;
		}

		public String name;

		public String getName() {
			return this.name;
		}

		public String email;

		public String getEmail() {
			return this.email;
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_SANDBOX_fileGeneration.length) {
					if (length < 1024
							&& commonByteArray_SANDBOX_fileGeneration.length == 0) {
						commonByteArray_SANDBOX_fileGeneration = new byte[1024];
					} else {
						commonByteArray_SANDBOX_fileGeneration = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_SANDBOX_fileGeneration, 0, length);
				strReturn = new String(commonByteArray_SANDBOX_fileGeneration,
						0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_SANDBOX_fileGeneration) {

				try {

					int length = 0;

					this.id = readString(dis);

					this.name = readString(dis);

					this.email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.id, dos);

				// String

				writeString(this.name, dos);

				// String

				writeString(this.email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("id=" + id);
			sb.append(",name=" + name);
			sb.append(",email=" + email);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (id == null) {
				sb.append("<null>");
			} else {
				sb.append(id);
			}

			sb.append("|");

			if (name == null) {
				sb.append("<null>");
			} else {
				sb.append(name);
			}

			sb.append("|");

			if (email == null) {
				sb.append("<null>");
			} else {
				sb.append(email);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row3Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row4Struct implements
			routines.system.IPersistableRow<row4Struct> {
		final static byte[] commonByteArrayLock_SANDBOX_fileGeneration = new byte[0];
		static byte[] commonByteArray_SANDBOX_fileGeneration = new byte[0];

		public String id;

		public String getId() {
			return this.id;
		}

		public String name;

		public String getName() {
			return this.name;
		}

		public String email;

		public String getEmail() {
			return this.email;
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_SANDBOX_fileGeneration.length) {
					if (length < 1024
							&& commonByteArray_SANDBOX_fileGeneration.length == 0) {
						commonByteArray_SANDBOX_fileGeneration = new byte[1024];
					} else {
						commonByteArray_SANDBOX_fileGeneration = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_SANDBOX_fileGeneration, 0, length);
				strReturn = new String(commonByteArray_SANDBOX_fileGeneration,
						0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_SANDBOX_fileGeneration) {

				try {

					int length = 0;

					this.id = readString(dis);

					this.name = readString(dis);

					this.email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.id, dos);

				// String

				writeString(this.name, dos);

				// String

				writeString(this.email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("id=" + id);
			sb.append(",name=" + name);
			sb.append(",email=" + email);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (id == null) {
				sb.append("<null>");
			} else {
				sb.append(id);
			}

			sb.append("|");

			if (name == null) {
				sb.append("<null>");
			} else {
				sb.append(name);
			}

			sb.append("|");

			if (email == null) {
				sb.append("<null>");
			} else {
				sb.append(email);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row4Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row2Struct implements
			routines.system.IPersistableRow<row2Struct> {
		final static byte[] commonByteArrayLock_SANDBOX_fileGeneration = new byte[0];
		static byte[] commonByteArray_SANDBOX_fileGeneration = new byte[0];

		public String id;

		public String getId() {
			return this.id;
		}

		public String name;

		public String getName() {
			return this.name;
		}

		public String email;

		public String getEmail() {
			return this.email;
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_SANDBOX_fileGeneration.length) {
					if (length < 1024
							&& commonByteArray_SANDBOX_fileGeneration.length == 0) {
						commonByteArray_SANDBOX_fileGeneration = new byte[1024];
					} else {
						commonByteArray_SANDBOX_fileGeneration = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_SANDBOX_fileGeneration, 0, length);
				strReturn = new String(commonByteArray_SANDBOX_fileGeneration,
						0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_SANDBOX_fileGeneration) {

				try {

					int length = 0;

					this.id = readString(dis);

					this.name = readString(dis);

					this.email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.id, dos);

				// String

				writeString(this.name, dos);

				// String

				writeString(this.email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("id=" + id);
			sb.append(",name=" + name);
			sb.append(",email=" + email);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (id == null) {
				sb.append("<null>");
			} else {
				sb.append(id);
			}

			sb.append("|");

			if (name == null) {
				sb.append("<null>");
			} else {
				sb.append(name);
			}

			sb.append("|");

			if (email == null) {
				sb.append("<null>");
			} else {
				sb.append(email);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row2Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tRowGenerator_1Process(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tRowGenerator_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {

			String currentMethodName = new java.lang.Exception()
					.getStackTrace()[0].getMethodName();
			boolean resumeIt = currentMethodName.equals(resumeEntryMethodName);
			if (resumeEntryMethodName == null || resumeIt || globalResumeTicket) {// start
																					// the
																					// resume
				globalResumeTicket = true;

				row2Struct row2 = new row2Struct();
				row3Struct row3 = new row3Struct();
				row4Struct row4 = new row4Struct();

				/**
				 * [tFileOutputXML_1 begin ] start
				 */

				ok_Hash.put("tFileOutputXML_1", false);
				start_Hash.put("tFileOutputXML_1", System.currentTimeMillis());

				currentComponent = "tFileOutputXML_1";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("row3" + iterateId, 0, 0);

					}
				}

				int tos_count_tFileOutputXML_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileOutputXML_1 - " + "Start to work.");
				StringBuilder log4jParamters_tFileOutputXML_1 = new StringBuilder();
				log4jParamters_tFileOutputXML_1.append("Parameters:");
				log4jParamters_tFileOutputXML_1
						.append("FILENAME"
								+ " = "
								+ "\"C:/Users/jhernan2/Documents/LocalTalend/out.xml\"");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("INPUT_IS_DOCUMENT"
						+ " = " + "false");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("ROW_TAG" + " = "
						+ "\"row\"");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("SPLIT" + " = "
						+ "false");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("CREATE" + " = "
						+ "true");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("ROOT_TAGS" + " = "
						+ "[]");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("MAPPING" + " = "
						+ "[{SCHEMA_COLUMN_NAME=" + ("true")
						+ ", SCHEMA_COLUMN=" + ("id") + ", AS_ATTRIBUTE="
						+ ("false") + "}, {SCHEMA_COLUMN_NAME=" + ("true")
						+ ", SCHEMA_COLUMN=" + ("name") + ", AS_ATTRIBUTE="
						+ ("false") + "}, {SCHEMA_COLUMN_NAME=" + ("true")
						+ ", SCHEMA_COLUMN=" + ("email") + ", AS_ATTRIBUTE="
						+ ("false") + "}]");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("USE_DYNAMIC_GROUPING"
						+ " = " + "false");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("FLUSHONROW" + " = "
						+ "false");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("ADVANCED_SEPARATOR"
						+ " = " + "false");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("ENCODING" + " = "
						+ "\"ISO-8859-15\"");
				log4jParamters_tFileOutputXML_1.append(" | ");
				log4jParamters_tFileOutputXML_1.append("DELETE_EMPTYFILE"
						+ " = " + "false");
				log4jParamters_tFileOutputXML_1.append(" | ");
				if (log.isDebugEnabled())
					log.debug("tFileOutputXML_1 - "
							+ log4jParamters_tFileOutputXML_1);

				String originalFileName_tFileOutputXML_1 = "C:/Users/jhernan2/Documents/LocalTalend/out.xml";
				java.io.File originalFile_tFileOutputXML_1 = new java.io.File(
						originalFileName_tFileOutputXML_1);

				String fileName_tFileOutputXML_1 = originalFileName_tFileOutputXML_1;
				java.io.File file_tFileOutputXML_1 = new java.io.File(
						fileName_tFileOutputXML_1);
				if (!file_tFileOutputXML_1.isAbsolute()) {
					file_tFileOutputXML_1 = file_tFileOutputXML_1
							.getCanonicalFile();
				}

				// create directory only if not exists

				log.info("tFileOutputXML_1 - Creating directory '"
						+ file_tFileOutputXML_1.getParentFile() + "'.");

				file_tFileOutputXML_1.getParentFile().mkdirs();

				log.info("tFileOutputXML_1 - Directory '"
						+ file_tFileOutputXML_1.getParentFile()
						+ "' created successfully.");

				String[] headers_tFileOutputXML_1 = new String[2];

				headers_tFileOutputXML_1[0] = "<?xml version=\"1.0\" encoding=\""
						+ "ISO-8859-15" + "\"?>";

				String[] footers_tFileOutputXML_1 = new String[1];

				headers_tFileOutputXML_1[1] = "<" + "root" + ">";

				footers_tFileOutputXML_1[0] = "</" + "root" + ">";

				int nb_line_tFileOutputXML_1 = 0;

				java.io.BufferedWriter out_tFileOutputXML_1 = new java.io.BufferedWriter(
						new java.io.OutputStreamWriter(
								new java.io.FileOutputStream(
										file_tFileOutputXML_1), "ISO-8859-15"));

				out_tFileOutputXML_1.write(headers_tFileOutputXML_1[0]);
				out_tFileOutputXML_1.newLine();
				out_tFileOutputXML_1.write(headers_tFileOutputXML_1[1]);
				out_tFileOutputXML_1.newLine();

				/**
				 * [tFileOutputXML_1 begin ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 begin ] start
				 */

				ok_Hash.put("tFileOutputDelimited_1", false);
				start_Hash.put("tFileOutputDelimited_1",
						System.currentTimeMillis());

				currentComponent = "tFileOutputDelimited_1";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("row4" + iterateId, 0, 0);

					}
				}

				int tos_count_tFileOutputDelimited_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - " + "Start to work.");
				StringBuilder log4jParamters_tFileOutputDelimited_1 = new StringBuilder();
				log4jParamters_tFileOutputDelimited_1.append("Parameters:");
				log4jParamters_tFileOutputDelimited_1.append("USESTREAM"
						+ " = " + "false");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1
						.append("FILENAME"
								+ " = "
								+ "\"C:/Users/jhernan2/Documents/LocalTalend/out.csv\"");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("ROWSEPARATOR"
						+ " = " + "\"\\n\"");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("FIELDSEPARATOR"
						+ " = " + "\";\"");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("APPEND" + " = "
						+ "false");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("INCLUDEHEADER"
						+ " = " + "true");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("COMPRESS" + " = "
						+ "false");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1
						.append("ADVANCED_SEPARATOR" + " = " + "false");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("CSV_OPTION"
						+ " = " + "false");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("CREATE" + " = "
						+ "true");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("SPLIT" + " = "
						+ "false");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("FLUSHONROW"
						+ " = " + "false");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("ROW_MODE" + " = "
						+ "false");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("ENCODING" + " = "
						+ "\"ISO-8859-15\"");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				log4jParamters_tFileOutputDelimited_1.append("DELETE_EMPTYFILE"
						+ " = " + "false");
				log4jParamters_tFileOutputDelimited_1.append(" | ");
				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - "
							+ log4jParamters_tFileOutputDelimited_1);

				String fileName_tFileOutputDelimited_1 = "";
				fileName_tFileOutputDelimited_1 = (new java.io.File(
						"C:/Users/jhernan2/Documents/LocalTalend/out.csv"))
						.getAbsolutePath().replace("\\", "/");
				String fullName_tFileOutputDelimited_1 = null;
				String extension_tFileOutputDelimited_1 = null;
				String directory_tFileOutputDelimited_1 = null;
				if ((fileName_tFileOutputDelimited_1.indexOf("/") != -1)) {
					if (fileName_tFileOutputDelimited_1.lastIndexOf(".") < fileName_tFileOutputDelimited_1
							.lastIndexOf("/")) {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1;
						extension_tFileOutputDelimited_1 = "";
					} else {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(0, fileName_tFileOutputDelimited_1
										.lastIndexOf("."));
						extension_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(fileName_tFileOutputDelimited_1
										.lastIndexOf("."));
					}
					directory_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
							.substring(0, fileName_tFileOutputDelimited_1
									.lastIndexOf("/"));
				} else {
					if (fileName_tFileOutputDelimited_1.lastIndexOf(".") != -1) {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(0, fileName_tFileOutputDelimited_1
										.lastIndexOf("."));
						extension_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(fileName_tFileOutputDelimited_1
										.lastIndexOf("."));
					} else {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1;
						extension_tFileOutputDelimited_1 = "";
					}
					directory_tFileOutputDelimited_1 = "";
				}
				boolean isFileGenerated_tFileOutputDelimited_1 = true;
				java.io.File filetFileOutputDelimited_1 = new java.io.File(
						fileName_tFileOutputDelimited_1);
				globalMap.put("tFileOutputDelimited_1_FILE_NAME",
						fileName_tFileOutputDelimited_1);
				int nb_line_tFileOutputDelimited_1 = 0;
				int splitEvery_tFileOutputDelimited_1 = 1000;
				int splitedFileNo_tFileOutputDelimited_1 = 0;
				int currentRow_tFileOutputDelimited_1 = 0;

				final String OUT_DELIM_tFileOutputDelimited_1 = /**
				 * Start field
				 * tFileOutputDelimited_1:FIELDSEPARATOR
				 */
				";"/** End field tFileOutputDelimited_1:FIELDSEPARATOR */
				;

				final String OUT_DELIM_ROWSEP_tFileOutputDelimited_1 = /**
				 * Start
				 * field tFileOutputDelimited_1:ROWSEPARATOR
				 */
				"\n"/** End field tFileOutputDelimited_1:ROWSEPARATOR */
				;

				// create directory only if not exists
				if (directory_tFileOutputDelimited_1 != null
						&& directory_tFileOutputDelimited_1.trim().length() != 0) {
					java.io.File dir_tFileOutputDelimited_1 = new java.io.File(
							directory_tFileOutputDelimited_1);
					if (!dir_tFileOutputDelimited_1.exists()) {
						log.info("tFileOutputDelimited_1 - Creating directory '"
								+ dir_tFileOutputDelimited_1.getCanonicalPath()
								+ "'.");
						dir_tFileOutputDelimited_1.mkdirs();
						log.info("tFileOutputDelimited_1 - The directory '"
								+ dir_tFileOutputDelimited_1.getCanonicalPath()
								+ "' has been created successfully.");
					}
				}

				// routines.system.Row
				java.io.Writer outtFileOutputDelimited_1 = null;

				java.io.File fileToDelete_tFileOutputDelimited_1 = new java.io.File(
						fileName_tFileOutputDelimited_1);
				if (fileToDelete_tFileOutputDelimited_1.exists()) {
					fileToDelete_tFileOutputDelimited_1.delete();
				}
				outtFileOutputDelimited_1 = new java.io.BufferedWriter(
						new java.io.OutputStreamWriter(
								new java.io.FileOutputStream(
										fileName_tFileOutputDelimited_1, false),
								"ISO-8859-15"));
				if (filetFileOutputDelimited_1.length() == 0) {
					outtFileOutputDelimited_1.write("id");
					outtFileOutputDelimited_1
							.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("name");
					outtFileOutputDelimited_1
							.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("email");
					outtFileOutputDelimited_1
							.write(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.flush();
				}

				resourceMap.put("out_tFileOutputDelimited_1",
						outtFileOutputDelimited_1);
				resourceMap.put("nb_line_tFileOutputDelimited_1",
						nb_line_tFileOutputDelimited_1);

				/**
				 * [tFileOutputDelimited_1 begin ] stop
				 */

				/**
				 * [tReplicate_1 begin ] start
				 */

				ok_Hash.put("tReplicate_1", false);
				start_Hash.put("tReplicate_1", System.currentTimeMillis());

				currentComponent = "tReplicate_1";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("row2" + iterateId, 0, 0);

					}
				}

				int tos_count_tReplicate_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tReplicate_1 - " + "Start to work.");
				StringBuilder log4jParamters_tReplicate_1 = new StringBuilder();
				log4jParamters_tReplicate_1.append("Parameters:");
				if (log.isDebugEnabled())
					log.debug("tReplicate_1 - " + log4jParamters_tReplicate_1);

				/**
				 * [tReplicate_1 begin ] stop
				 */

				/**
				 * [tRowGenerator_1 begin ] start
				 */

				ok_Hash.put("tRowGenerator_1", false);
				start_Hash.put("tRowGenerator_1", System.currentTimeMillis());

				currentComponent = "tRowGenerator_1";

				int tos_count_tRowGenerator_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tRowGenerator_1 - " + "Start to work.");
				StringBuilder log4jParamters_tRowGenerator_1 = new StringBuilder();
				log4jParamters_tRowGenerator_1.append("Parameters:");
				if (log.isDebugEnabled())
					log.debug("tRowGenerator_1 - "
							+ log4jParamters_tRowGenerator_1);

				int nb_line_tRowGenerator_1 = 0;
				int nb_max_row_tRowGenerator_1 = 100;

				class tRowGenerator_1Randomizer {
					public String getRandomid() {

						return TalendString.getAsciiRandomString(6);

					}

					public String getRandomname() {

						return TalendString.getAsciiRandomString(6);

					}

					public String getRandomemail() {

						return TalendString.getAsciiRandomString(6);

					}
				}
				tRowGenerator_1Randomizer randtRowGenerator_1 = new tRowGenerator_1Randomizer();

				log.info("tRowGenerator_1 - Generating records.");
				for (int itRowGenerator_1 = 0; itRowGenerator_1 < nb_max_row_tRowGenerator_1; itRowGenerator_1++) {
					row2.id = randtRowGenerator_1.getRandomid();
					row2.name = randtRowGenerator_1.getRandomname();
					row2.email = randtRowGenerator_1.getRandomemail();
					nb_line_tRowGenerator_1++;

					log.debug("tRowGenerator_1 - Retrieving the record "
							+ nb_line_tRowGenerator_1 + ".");

					/**
					 * [tRowGenerator_1 begin ] stop
					 */

					/**
					 * [tRowGenerator_1 main ] start
					 */

					currentComponent = "tRowGenerator_1";

					tos_count_tRowGenerator_1++;

					/**
					 * [tRowGenerator_1 main ] stop
					 */

					/**
					 * [tReplicate_1 main ] start
					 */

					currentComponent = "tReplicate_1";

					// row2
					// row2

					if (execStat) {
						runStat.updateStatOnConnection("row2" + iterateId, 1, 1);
					}

					if (log.isTraceEnabled()) {
						log.trace("row2 - "
								+ (row2 == null ? "" : row2.toLogString()));
					}

					row3 = new row3Struct();

					row3.id = row2.id;
					row3.name = row2.name;
					row3.email = row2.email;
					row4 = new row4Struct();

					row4.id = row2.id;
					row4.name = row2.name;
					row4.email = row2.email;

					tos_count_tReplicate_1++;

					/**
					 * [tReplicate_1 main ] stop
					 */

					/**
					 * [tFileOutputXML_1 main ] start
					 */

					currentComponent = "tFileOutputXML_1";

					// row3
					// row3

					if (execStat) {
						runStat.updateStatOnConnection("row3" + iterateId, 1, 1);
					}

					if (log.isTraceEnabled()) {
						log.trace("row3 - "
								+ (row3 == null ? "" : row3.toLogString()));
					}

					StringBuilder tempRes_tFileOutputXML_1 = new StringBuilder(
							"<" + "row");
					tempRes_tFileOutputXML_1.append(">");
					out_tFileOutputXML_1.write(tempRes_tFileOutputXML_1
							.toString());

					out_tFileOutputXML_1.newLine();
					out_tFileOutputXML_1.write("<"
							+ "id"
							+ ">"
							+ ((row3.id == null) ? "" : (TalendString
									.checkCDATAForXML(row3.id))) + "</" + "id"
							+ ">");

					out_tFileOutputXML_1.newLine();
					out_tFileOutputXML_1.write("<"
							+ "name"
							+ ">"
							+ ((row3.name == null) ? "" : (TalendString
									.checkCDATAForXML(row3.name))) + "</"
							+ "name" + ">");

					out_tFileOutputXML_1.newLine();
					out_tFileOutputXML_1.write("<"
							+ "email"
							+ ">"
							+ ((row3.email == null) ? "" : (TalendString
									.checkCDATAForXML(row3.email))) + "</"
							+ "email" + ">");

					out_tFileOutputXML_1.newLine();
					out_tFileOutputXML_1.write("</" + "row" + ">");

					out_tFileOutputXML_1.newLine();

					nb_line_tFileOutputXML_1++;
					log.debug("tFileOutputXML_1 - Writing the record "
							+ nb_line_tFileOutputXML_1 + " to the file.");

					tos_count_tFileOutputXML_1++;

					/**
					 * [tFileOutputXML_1 main ] stop
					 */

					/**
					 * [tFileOutputDelimited_1 main ] start
					 */

					currentComponent = "tFileOutputDelimited_1";

					// row4
					// row4

					if (execStat) {
						runStat.updateStatOnConnection("row4" + iterateId, 1, 1);
					}

					if (log.isTraceEnabled()) {
						log.trace("row4 - "
								+ (row4 == null ? "" : row4.toLogString()));
					}

					StringBuilder sb_tFileOutputDelimited_1 = new StringBuilder();
					if (row4.id != null) {
						sb_tFileOutputDelimited_1.append(row4.id);
					}
					sb_tFileOutputDelimited_1
							.append(OUT_DELIM_tFileOutputDelimited_1);
					if (row4.name != null) {
						sb_tFileOutputDelimited_1.append(row4.name);
					}
					sb_tFileOutputDelimited_1
							.append(OUT_DELIM_tFileOutputDelimited_1);
					if (row4.email != null) {
						sb_tFileOutputDelimited_1.append(row4.email);
					}
					sb_tFileOutputDelimited_1
							.append(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);

					nb_line_tFileOutputDelimited_1++;
					resourceMap.put("nb_line_tFileOutputDelimited_1",
							nb_line_tFileOutputDelimited_1);

					outtFileOutputDelimited_1.write(sb_tFileOutputDelimited_1
							.toString());
					log.debug("tFileOutputDelimited_1 - Writing the record "
							+ nb_line_tFileOutputDelimited_1 + ".");

					tos_count_tFileOutputDelimited_1++;

					/**
					 * [tFileOutputDelimited_1 main ] stop
					 */

					/**
					 * [tRowGenerator_1 end ] start
					 */

					currentComponent = "tRowGenerator_1";

				}
				globalMap.put("tRowGenerator_1_NB_LINE",
						nb_line_tRowGenerator_1);
				log.info("tRowGenerator_1 - Generated records count:"
						+ nb_line_tRowGenerator_1 + " .");

				if (log.isDebugEnabled())
					log.debug("tRowGenerator_1 - " + "Done.");

				ok_Hash.put("tRowGenerator_1", true);
				end_Hash.put("tRowGenerator_1", System.currentTimeMillis());

				/**
				 * [tRowGenerator_1 end ] stop
				 */

				/**
				 * [tReplicate_1 end ] start
				 */

				currentComponent = "tReplicate_1";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("row2" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("tReplicate_1 - " + "Done.");

				ok_Hash.put("tReplicate_1", true);
				end_Hash.put("tReplicate_1", System.currentTimeMillis());

				/**
				 * [tReplicate_1 end ] stop
				 */

				/**
				 * [tFileOutputXML_1 end ] start
				 */

				currentComponent = "tFileOutputXML_1";

				out_tFileOutputXML_1.write(footers_tFileOutputXML_1[0]);

				out_tFileOutputXML_1.newLine();
				out_tFileOutputXML_1.close();

				log.debug("tFileOutputXML_1 - Written records count: "
						+ nb_line_tFileOutputXML_1 + " .");
				globalMap.put("tFileOutputXML_1_NB_LINE",
						nb_line_tFileOutputXML_1);

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("row3" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("tFileOutputXML_1 - " + "Done.");

				ok_Hash.put("tFileOutputXML_1", true);
				end_Hash.put("tFileOutputXML_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk2", 0, "ok");
				}
				tWarn_2Process(globalMap);

				/**
				 * [tFileOutputXML_1 end ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 end ] start
				 */

				currentComponent = "tFileOutputDelimited_1";

				if (outtFileOutputDelimited_1 != null) {
					outtFileOutputDelimited_1.flush();
					outtFileOutputDelimited_1.close();
				}

				globalMap.put("tFileOutputDelimited_1_NB_LINE",
						nb_line_tFileOutputDelimited_1);
				globalMap.put("tFileOutputDelimited_1_FILE_NAME",
						fileName_tFileOutputDelimited_1);

				resourceMap.put("finish_tFileOutputDelimited_1", true);

				log.debug("tFileOutputDelimited_1 - Written records count: "
						+ nb_line_tFileOutputDelimited_1 + " .");

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("row4" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - " + "Done.");

				ok_Hash.put("tFileOutputDelimited_1", true);
				end_Hash.put("tFileOutputDelimited_1",
						System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk3", 0, "ok");
				}
				tWarn_3Process(globalMap);

				/**
				 * [tFileOutputDelimited_1 end ] stop
				 */

			}// end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil
						.addLog("CHECKPOINT",
								"CONNECTION:SUBJOB_OK:tRowGenerator_1:OnSubjobOk",
								"", Thread.currentThread().getId() + "", "",
								"", "", "", "");
			}

			if (execStat) {
				runStat.updateStatOnConnection("OnSubjobOk1", 0, "ok");
			}

			tWarn_4Process(globalMap);

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage());
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tRowGenerator_1 finally ] start
				 */

				currentComponent = "tRowGenerator_1";

				/**
				 * [tRowGenerator_1 finally ] stop
				 */

				/**
				 * [tReplicate_1 finally ] start
				 */

				currentComponent = "tReplicate_1";

				/**
				 * [tReplicate_1 finally ] stop
				 */

				/**
				 * [tFileOutputXML_1 finally ] start
				 */

				currentComponent = "tFileOutputXML_1";

				/**
				 * [tFileOutputXML_1 finally ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 finally ] start
				 */

				currentComponent = "tFileOutputDelimited_1";

				if (resourceMap.get("finish_tFileOutputDelimited_1") == null) {

					java.io.Writer outtFileOutputDelimited_1 = (java.io.Writer) resourceMap
							.get("out_tFileOutputDelimited_1");
					if (outtFileOutputDelimited_1 != null) {
						outtFileOutputDelimited_1.flush();
						outtFileOutputDelimited_1.close();
					}

				}

				/**
				 * [tFileOutputDelimited_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRowGenerator_1_SUBPROCESS_STATE", 1);
	}

	public void tWarn_2Process(final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tWarn_2_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {

			String currentMethodName = new java.lang.Exception()
					.getStackTrace()[0].getMethodName();
			boolean resumeIt = currentMethodName.equals(resumeEntryMethodName);
			if (resumeEntryMethodName == null || resumeIt || globalResumeTicket) {// start
																					// the
																					// resume
				globalResumeTicket = true;

				/**
				 * [tWarn_2 begin ] start
				 */

				ok_Hash.put("tWarn_2", false);
				start_Hash.put("tWarn_2", System.currentTimeMillis());

				currentComponent = "tWarn_2";

				int tos_count_tWarn_2 = 0;

				if (log.isDebugEnabled())
					log.debug("tWarn_2 - " + "Start to work.");
				StringBuilder log4jParamters_tWarn_2 = new StringBuilder();
				log4jParamters_tWarn_2.append("Parameters:");
				log4jParamters_tWarn_2.append("MESSAGE" + " = "
						+ "\"txml file generated\"");
				log4jParamters_tWarn_2.append(" | ");
				log4jParamters_tWarn_2.append("CODE" + " = " + "0");
				log4jParamters_tWarn_2.append(" | ");
				log4jParamters_tWarn_2.append("PRIORITY" + " = " + "3");
				log4jParamters_tWarn_2.append(" | ");
				if (log.isDebugEnabled())
					log.debug("tWarn_2 - " + log4jParamters_tWarn_2);

				/**
				 * [tWarn_2 begin ] stop
				 */

				/**
				 * [tWarn_2 main ] start
				 */

				currentComponent = "tWarn_2";

				resumeUtil.addLog("USER_DEF_LOG", "NODE:tWarn_2", "", Thread
						.currentThread().getId() + "", "INFO", "",
						"txml file generated", "", "");
				if (log.isInfoEnabled())
					log.info("tWarn_2 - " + "Message: " + "txml file generated"
							+ ". Code: " + 0);
				if (log.isDebugEnabled())
					log.debug("tWarn_2 - "
							+ "Sending message to tLogCatcher_1.");
				tLogCatcher_1.addMessage("tWarn", "tWarn_2", 3,
						"txml file generated", 0);
				tLogCatcher_1Process(globalMap);
				globalMap.put("tWarn_2_WARN_MESSAGES", "txml file generated");
				globalMap.put("tWarn_2_WARN_PRIORITY", 3);
				globalMap.put("tWarn_2_WARN_CODE", 0);

				tos_count_tWarn_2++;

				/**
				 * [tWarn_2 main ] stop
				 */

				/**
				 * [tWarn_2 end ] start
				 */

				currentComponent = "tWarn_2";

				if (log.isDebugEnabled())
					log.debug("tWarn_2 - " + "Done.");

				ok_Hash.put("tWarn_2", true);
				end_Hash.put("tWarn_2", System.currentTimeMillis());

				/**
				 * [tWarn_2 end ] stop
				 */
			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage());
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tWarn_2 finally ] start
				 */

				currentComponent = "tWarn_2";

				/**
				 * [tWarn_2 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tWarn_2_SUBPROCESS_STATE", 1);
	}

	public void tWarn_3Process(final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tWarn_3_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {

			String currentMethodName = new java.lang.Exception()
					.getStackTrace()[0].getMethodName();
			boolean resumeIt = currentMethodName.equals(resumeEntryMethodName);
			if (resumeEntryMethodName == null || resumeIt || globalResumeTicket) {// start
																					// the
																					// resume
				globalResumeTicket = true;

				/**
				 * [tWarn_3 begin ] start
				 */

				ok_Hash.put("tWarn_3", false);
				start_Hash.put("tWarn_3", System.currentTimeMillis());

				currentComponent = "tWarn_3";

				int tos_count_tWarn_3 = 0;

				if (log.isDebugEnabled())
					log.debug("tWarn_3 - " + "Start to work.");
				StringBuilder log4jParamters_tWarn_3 = new StringBuilder();
				log4jParamters_tWarn_3.append("Parameters:");
				log4jParamters_tWarn_3.append("MESSAGE" + " = "
						+ "\"delimited file generated\"");
				log4jParamters_tWarn_3.append(" | ");
				log4jParamters_tWarn_3.append("CODE" + " = " + "0");
				log4jParamters_tWarn_3.append(" | ");
				log4jParamters_tWarn_3.append("PRIORITY" + " = " + "3");
				log4jParamters_tWarn_3.append(" | ");
				if (log.isDebugEnabled())
					log.debug("tWarn_3 - " + log4jParamters_tWarn_3);

				/**
				 * [tWarn_3 begin ] stop
				 */

				/**
				 * [tWarn_3 main ] start
				 */

				currentComponent = "tWarn_3";

				resumeUtil.addLog("USER_DEF_LOG", "NODE:tWarn_3", "", Thread
						.currentThread().getId() + "", "INFO", "",
						"delimited file generated", "", "");
				if (log.isInfoEnabled())
					log.info("tWarn_3 - " + "Message: "
							+ "delimited file generated" + ". Code: " + 0);
				if (log.isDebugEnabled())
					log.debug("tWarn_3 - "
							+ "Sending message to tLogCatcher_1.");
				tLogCatcher_1.addMessage("tWarn", "tWarn_3", 3,
						"delimited file generated", 0);
				tLogCatcher_1Process(globalMap);
				globalMap.put("tWarn_3_WARN_MESSAGES",
						"delimited file generated");
				globalMap.put("tWarn_3_WARN_PRIORITY", 3);
				globalMap.put("tWarn_3_WARN_CODE", 0);

				tos_count_tWarn_3++;

				/**
				 * [tWarn_3 main ] stop
				 */

				/**
				 * [tWarn_3 end ] start
				 */

				currentComponent = "tWarn_3";

				if (log.isDebugEnabled())
					log.debug("tWarn_3 - " + "Done.");

				ok_Hash.put("tWarn_3", true);
				end_Hash.put("tWarn_3", System.currentTimeMillis());

				/**
				 * [tWarn_3 end ] stop
				 */
			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage());
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tWarn_3 finally ] start
				 */

				currentComponent = "tWarn_3";

				/**
				 * [tWarn_3 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tWarn_3_SUBPROCESS_STATE", 1);
	}

	public void tWarn_4Process(final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tWarn_4_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {

			String currentMethodName = new java.lang.Exception()
					.getStackTrace()[0].getMethodName();
			boolean resumeIt = currentMethodName.equals(resumeEntryMethodName);
			if (resumeEntryMethodName == null || resumeIt || globalResumeTicket) {// start
																					// the
																					// resume
				globalResumeTicket = true;

				/**
				 * [tWarn_4 begin ] start
				 */

				ok_Hash.put("tWarn_4", false);
				start_Hash.put("tWarn_4", System.currentTimeMillis());

				currentComponent = "tWarn_4";

				int tos_count_tWarn_4 = 0;

				if (log.isDebugEnabled())
					log.debug("tWarn_4 - " + "Start to work.");
				StringBuilder log4jParamters_tWarn_4 = new StringBuilder();
				log4jParamters_tWarn_4.append("Parameters:");
				log4jParamters_tWarn_4.append("MESSAGE" + " = "
						+ "\"process ended\"");
				log4jParamters_tWarn_4.append(" | ");
				log4jParamters_tWarn_4.append("CODE" + " = " + "0");
				log4jParamters_tWarn_4.append(" | ");
				log4jParamters_tWarn_4.append("PRIORITY" + " = " + "3");
				log4jParamters_tWarn_4.append(" | ");
				if (log.isDebugEnabled())
					log.debug("tWarn_4 - " + log4jParamters_tWarn_4);

				/**
				 * [tWarn_4 begin ] stop
				 */

				/**
				 * [tWarn_4 main ] start
				 */

				currentComponent = "tWarn_4";

				resumeUtil.addLog("USER_DEF_LOG", "NODE:tWarn_4", "", Thread
						.currentThread().getId() + "", "INFO", "",
						"process ended", "", "");
				if (log.isInfoEnabled())
					log.info("tWarn_4 - " + "Message: " + "process ended"
							+ ". Code: " + 0);
				if (log.isDebugEnabled())
					log.debug("tWarn_4 - "
							+ "Sending message to tLogCatcher_1.");
				tLogCatcher_1.addMessage("tWarn", "tWarn_4", 3,
						"process ended", 0);
				tLogCatcher_1Process(globalMap);
				globalMap.put("tWarn_4_WARN_MESSAGES", "process ended");
				globalMap.put("tWarn_4_WARN_PRIORITY", 3);
				globalMap.put("tWarn_4_WARN_CODE", 0);

				tos_count_tWarn_4++;

				/**
				 * [tWarn_4 main ] stop
				 */

				/**
				 * [tWarn_4 end ] start
				 */

				currentComponent = "tWarn_4";

				if (log.isDebugEnabled())
					log.debug("tWarn_4 - " + "Done.");

				ok_Hash.put("tWarn_4", true);
				end_Hash.put("tWarn_4", System.currentTimeMillis());

				/**
				 * [tWarn_4 end ] stop
				 */
			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage());
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tWarn_4 finally ] start
				 */

				currentComponent = "tWarn_4";

				/**
				 * [tWarn_4 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tWarn_4_SUBPROCESS_STATE", 1);
	}

	public String resuming_logs_dir_path = null;
	public String resuming_checkpoint_path = null;
	public String parent_part_launcher = null;
	private String resumeEntryMethodName = null;
	private boolean globalResumeTicket = false;

	public boolean watch = false;
	// portStats is null, it means don't execute the statistics
	public Integer portStats = null;
	public int portTraces = 4334;
	public String clientHost;
	public String defaultClientHost = "localhost";
	public String contextStr = "Default";
	public boolean isDefaultContext = true;
	public String pid = "0";
	public String rootPid = null;
	public String fatherPid = null;
	public String fatherNode = null;
	public long startTime = 0;
	public boolean isChildJob = false;
	public String log4jLevel = "";

	private boolean execStat = true;

	private ThreadLocal<java.util.Map<String, String>> threadLocal = new ThreadLocal<java.util.Map<String, String>>() {
		protected java.util.Map<String, String> initialValue() {
			java.util.Map<String, String> threadRunResultMap = new java.util.HashMap<String, String>();
			threadRunResultMap.put("errorCode", null);
			threadRunResultMap.put("status", "");
			return threadRunResultMap;
		};
	};

	private java.util.Properties context_param = new java.util.Properties();
	public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

	public String status = "";

	public static void main(String[] args) {
		final fileGeneration fileGenerationClass = new fileGeneration();

		int exitCode = fileGenerationClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'fileGeneration' - Done.");
		}

		System.exit(exitCode);
	}

	public String[][] runJob(String[] args) {

		int exitCode = runJobInTOS(args);
		String[][] bufferValue = new String[][] { { Integer.toString(exitCode) } };

		return bufferValue;
	}

	public boolean hastBufferOutputComponent() {
		boolean hastBufferOutput = false;

		return hastBufferOutput;
	}

	public int runJobInTOS(String[] args) {
		// reset status
		status = "";

		String lastStr = "";
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--context_param")) {
				lastStr = arg;
			} else if (lastStr.equals("")) {
				evalParam(arg);
			} else {
				evalParam(lastStr + " " + arg);
				lastStr = "";
			}
		}

		if (!"".equals(log4jLevel)) {
			if ("trace".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.TRACE);
			} else if ("debug".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.DEBUG);
			} else if ("info".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.INFO);
			} else if ("warn".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.WARN);
			} else if ("error".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.ERROR);
			} else if ("fatal".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.FATAL);
			} else if ("off".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.OFF);
			}
			org.apache.log4j.Logger.getRootLogger().setLevel(log.getLevel());
		}
		log.info("TalendJob: 'fileGeneration' - Start.");

		if (clientHost == null) {
			clientHost = defaultClientHost;
		}

		if (pid == null || "0".equals(pid)) {
			pid = TalendString.getAsciiRandomString(6);
		}

		if (rootPid == null) {
			rootPid = pid;
		}
		if (fatherPid == null) {
			fatherPid = pid;
		} else {
			isChildJob = true;
		}

		if (portStats != null) {
			// portStats = -1; //for testing
			if (portStats < 0 || portStats > 65535) {
				// issue:10869, the portStats is invalid, so this client socket
				// can't open
				System.err.println("The statistics socket port " + portStats
						+ " is invalid.");
				execStat = false;
			}
		} else {
			execStat = false;
		}

		try {
			// call job/subjob with an existing context, like:
			// --context=production. if without this parameter, there will use
			// the default context instead.
			java.io.InputStream inContext = fileGeneration.class
					.getClassLoader().getResourceAsStream(
							"sandbox/filegeneration_0_1/contexts/" + contextStr
									+ ".properties");
			if (isDefaultContext && inContext == null) {

			} else {
				if (inContext != null) {
					// defaultProps is in order to keep the original context
					// value
					defaultProps.load(inContext);
					inContext.close();
					context = new ContextProperties(defaultProps);
				} else {
					// print info and job continue to run, for case:
					// context_param is not empty.
					System.err.println("Could not find the context "
							+ contextStr);
				}
			}

			if (!context_param.isEmpty()) {
				context.putAll(context_param);
			}
		} catch (java.io.IOException ie) {
			System.err.println("Could not load context " + contextStr);
			ie.printStackTrace();
		}

		// get context value from parent directly
		if (parentContextMap != null && !parentContextMap.isEmpty()) {
		}

		// Resume: init the resumeUtil
		resumeEntryMethodName = ResumeUtil
				.getResumeEntryMethodName(resuming_checkpoint_path);
		resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
		resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName,
				jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
		// Resume: jobStart
		resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName,
				parent_part_launcher, Thread.currentThread().getId() + "", "",
				"", "", "",
				resumeUtil.convertToJsonText(context, parametersToEncrypt));

		if (execStat) {
			try {
				runStat.openSocket(!isChildJob);
				runStat.setAllPID(rootPid, fatherPid, pid, jobName);
				runStat.startThreadStat(clientHost, portStats);
				runStat.updateStatOnJob(RunStat.JOBSTART, fatherNode);
			} catch (java.io.IOException ioException) {
				ioException.printStackTrace();
			}
		}

		java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<Object, Object>();
		globalMap.put("concurrentHashMap", concurrentHashMap);

		long startUsedMemory = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		long endUsedMemory = 0;
		long end = 0;

		startTime = System.currentTimeMillis();

		this.globalResumeTicket = true;// to run tPreJob

		this.globalResumeTicket = false;// to run others jobs

		try {
			errorCode = null;
			tWarn_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tWarn_1) {
			globalMap.put("tWarn_1_SUBPROCESS_STATE", -1);

			e_tWarn_1.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println((endUsedMemory - startUsedMemory)
					+ " bytes memory increase when running : fileGeneration");
		}

		if (execStat) {
			runStat.updateStatOnJob(RunStat.JOBEND, fatherNode);
			runStat.stopThreadStat();
		}
		int returnCode = 0;
		if (errorCode == null) {
			returnCode = status != null && status.equals("failure") ? 1 : 0;
		} else {
			returnCode = errorCode.intValue();
		}
		resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher,
				Thread.currentThread().getId() + "", "", "" + returnCode, "",
				"", "");

		return returnCode;

	}

	// only for OSGi env
	public void destroy() {

	}

	private java.util.Map<String, Object> getSharedConnections4REST() {
		java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();

		return connections;
	}

	private void evalParam(String arg) {
		if (arg.startsWith("--resuming_logs_dir_path")) {
			resuming_logs_dir_path = arg.substring(25);
		} else if (arg.startsWith("--resuming_checkpoint_path")) {
			resuming_checkpoint_path = arg.substring(27);
		} else if (arg.startsWith("--parent_part_launcher")) {
			parent_part_launcher = arg.substring(23);
		} else if (arg.startsWith("--watch")) {
			watch = true;
		} else if (arg.startsWith("--stat_port=")) {
			String portStatsStr = arg.substring(12);
			if (portStatsStr != null && !portStatsStr.equals("null")) {
				portStats = Integer.parseInt(portStatsStr);
			}
		} else if (arg.startsWith("--trace_port=")) {
			portTraces = Integer.parseInt(arg.substring(13));
		} else if (arg.startsWith("--client_host=")) {
			clientHost = arg.substring(14);
		} else if (arg.startsWith("--context=")) {
			contextStr = arg.substring(10);
			isDefaultContext = false;
		} else if (arg.startsWith("--father_pid=")) {
			fatherPid = arg.substring(13);
		} else if (arg.startsWith("--root_pid=")) {
			rootPid = arg.substring(11);
		} else if (arg.startsWith("--father_node=")) {
			fatherNode = arg.substring(14);
		} else if (arg.startsWith("--pid=")) {
			pid = arg.substring(6);
		} else if (arg.startsWith("--context_param")) {
			String keyValue = arg.substring(16);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.put(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.put(keyValue.substring(0, index),
							keyValue.substring(index + 1));
				}
			}
		} else if (arg.startsWith("--log4jLevel=")) {
			log4jLevel = arg.substring(13);
		}

	}

	private final String[][] escapeChars = { { "\\\\", "\\" }, { "\\n", "\n" },
			{ "\\'", "\'" }, { "\\r", "\r" }, { "\\f", "\f" }, { "\\b", "\b" },
			{ "\\t", "\t" } };

	private String replaceEscapeChars(String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0], currIndex);
				if (index >= 0) {

					result.append(keyValue.substring(currIndex,
							index + strArray[0].length()).replace(strArray[0],
							strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left
			// into the result
			if (index < 0) {
				result.append(keyValue.substring(currIndex));
				currIndex = currIndex + keyValue.length();
			}
		}

		return result.toString();
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getStatus() {
		return status;
	}

	ResumeUtil resumeUtil = null;
}
/************************************************************************************************
 * 99806 characters generated by Talend Real-time Big Data Platform on the March
 * 25, 2020 10:32:02 PM MST
 ************************************************************************************************/
