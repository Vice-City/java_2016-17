package hr.fer.zemris.java.hw06.shell;

/**
 * Contains a number of helper static methods used in various
 * ShellCommand classes, mostly for similar error messages.
 * 
 * @author Vice Ivušić
 *
 */
public class ShellUtil {

	/**
	 * Wraps specified command name into an invalid arguments error message.
	 * 
	 * @param command command which received invalid arguments
	 * @return message with error info about specified command
	 */
	public static String invalidArgumentsMessage(String command) {
		return String.format("Invalid number of arguments for command '%s'! "
				+ "Type 'help %s' for additional information.",
				command,
				command
		);
	}
	
	/**
	 * Wraps specified path into a path conversion error message.
	 * 
	 * @param path path for which conversion failed
	 * @return message with error info about specified path
	 */
	public static String pathConversionMessage(String path) {
		return "Cannot convert \""+path+"\" to path!";
	}
	
	/**
	 * Wraps specified path into a path doesn't exist error message.
	 * 
	 * @param path path which doesn't exist
	 * @return message with error about specified path
	 */
	public static String pathDoesntExistMessage(String path) {
		return "\""+path+"\" does not exist!";
	}
	
	/**
	 * Wraps specified path into a path is a directory error message.
	 * 
	 * @param path path which is a directory
	 * @return message with error about specified path
	 */
	public static String pathIsDirectoryMessage(String path) {
		return "\""+path+"\" is a directory! Must be a file!";
	}
	
	/**
	 * Wraps specified path into a path isn't a directory error message.
	 * 
	 * @param path path which isn't a directory
	 * @return message with error about specified path
	 */
	public static String pathIsntDirectoryMessage(String path) {
		return "\""+path+"\" is not a directory! Must be a directory!";
	}
}
