package hr.fer.zemris.java.hw06.shell;

/**
 * Enum representing directives for the Shell.
 * 
 * @author Vice Ivušić
 *
 */
public enum ShellStatus {

	/** shell should stop current activity and move on to next input **/
	CONTINUE,
	/** shell should stop current activity and terminate itself **/
	TERMINATE
}
