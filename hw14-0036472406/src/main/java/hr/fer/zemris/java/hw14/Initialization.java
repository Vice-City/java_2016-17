package hr.fer.zemris.java.hw14;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * A ServletContextListener which does necessary initialization work when
 * a webapp is first run. This listener expects at least one voting
 * definition file to be present in the directory defined in dbsettings.properties.
 * 
 * @author Vice Ivušić
 *
 */
@WebListener
public class Initialization implements ServletContextListener {

	/** list of paths to the voting definition files */
	private static List<Path> votingDefinitionPaths = new ArrayList<>();
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String fileName = sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties");
		Properties prop = loadAndInitProperties(fileName);
		
		String dbHost = prop.getProperty("host");
		String dbPort = prop.getProperty("port");
		String dbName= prop.getProperty("name");
		String dbUser = prop.getProperty("user");
		String dbPassword = prop.getProperty("password");
		
		if (dbHost==null || dbPort==null || dbName==null || dbUser==null || dbPassword==null) {
			throw new NullPointerException("One or more properties has an undefined value!");
		}
		
		String connectionURL = String.format(
			"jdbc:derby://%s:%s/%s;user=%s;password=%s", 
			dbHost,
			dbPort,
			dbName,
			dbUser,
			dbPassword
		);
		
		FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				votingDefinitionPaths.add(file.toAbsolutePath());
				return FileVisitResult.CONTINUE;
			}
		};
		
		String votingFilesString = prop.getProperty("votingFilesPath", "/WEB-INF/voting/");
		Path votingFilesPath = Paths.get(sce.getServletContext().getRealPath(votingFilesString));
		
		try { 
			Files.walkFileTree(votingFilesPath, visitor);
		} catch (IOException ex) {
			throw new RuntimeException("Could not load voting definition files!", ex);
		}
		
		if (votingDefinitionPaths.size() == 0) {
			throw new RuntimeException("No voting definition files were found in "+votingFilesPath);
		}
		 
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException ex) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", ex);
		}
		cpds.setJdbcUrl(connectionURL);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
		
		Connection connection = null;
		try {
			connection = cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		initializeTables(connection);
		initializeValues(connection);
		
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Helper method for loading and initializing a Properties object
	 * from the specified configuration file.
	 * 
	 * @param configFileName configuration file to read properties from
	 * @return a Properties object with properties from the specified
	 * 		   configuration file
	 */
	private static Properties loadAndInitProperties(String configFileName) {
		Path propPath = Paths.get(configFileName);
		
		Properties prop = new Properties();
		try {
			prop.load(Files.newBufferedReader(propPath, StandardCharsets.ISO_8859_1));
		} catch (IOException e) {
			throw new RuntimeException("Could not load: "+propPath.toAbsolutePath(), e);
		}
		
		return prop;
	}
	
	/**
	 * Helper method for initializing Polls and PollOptions tables.
	 * 
	 * @param con established connection to the underlying database management system
	 */
	public void initializeTables(Connection con) {
		
		final String POLLS_PDST = 
			"CREATE TABLE Polls"+
				"(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"+
				"title VARCHAR(150) NOT NULL,"+
				"message CLOB(2048) NOT NULL)";
		
		final String POLL_OPTIONS_PDST = 
			"CREATE TABLE PollOptions"+
				"(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"+
				"optionTitle VARCHAR(100) NOT NULL,"+
				"optionLink VARCHAR(150) NOT NULL,"+
				"pollID BIGINT,"+
				"votesCount BIGINT,"+
				"FOREIGN KEY (pollID) REFERENCES Polls(id))";
		
		/*
		 * The table PollOptions is tied by FOREIGN KEY reference to
		 * the table Polls. The DBMS should ensure integrity so there
		 * won't ever be a situation where PollOptions exists, but
		 * Polls does. So, if Polls doesn't exist, then I can be
		 * sure that PollOptions doesn't either.
		 */
		if (!checkTableExists("POLLS", con)) {
			createTable(POLLS_PDST, con);
			createTable(POLL_OPTIONS_PDST, con);
		}
	}
	
	/**
	 * Helper method for checking whether the table with the specified
	 * table ID exists within the database.
	 * 
	 * @param tableId ID of the table
	 * @param con established connection to the underlying database management system
	 * @return true if the table exists; false otherwise
	 */
	private static boolean checkTableExists(String tableId, Connection con) {
		try  {
			DatabaseMetaData metaData = con.getMetaData();
			
			try (ResultSet tables = metaData.getTables(null, null, tableId, null)) {
				if (tables.next()) {
					return true;
				}
			} catch (SQLException ex) {
				throw new RuntimeException("Could not check if table"+tableId+"exists!", ex);
			}
			
		} catch (SQLException ex) {
			throw new RuntimeException("Could not access database metadata!", ex);
		}
		
		return false;
	}
	
	/**
	 * Helper method for creating a table with the specified prepared statement
	 * string, i.e. SQL query for creating a table.
	 * 
	 * @param pstString SQL query for creating a table
	 * @param con established connection to the underlying database management system
	 */
	private static void createTable(String pstString, Connection con) {
		try (PreparedStatement pst = con.prepareStatement(pstString)) {
			pst.executeUpdate();
			
		} catch(SQLException ex) {
			throw new RuntimeException("Could not create table!", ex);
		} 
	}
	
	/**
	 * Helper method for initializing values within the tables. Will 
	 * initialize values only if there are no values in table Polls.
	 * 
	 * @param con established connection to the underlying database management system
	 */
	private static void initializeValues(Connection con) {
		/*
		 * If no values exist in Polls, PollOptions will be filled regardless
		 * of its current state and content. 
		 */
		if (!checkValuesExist("POLLS", con)) {
			fillTables(con);
		}
	}
	
	/**
	 * Helper method for filling the Polls and PollOptions tables with
	 * values defined in voting definition files.
	 * 
	 * @param con established connection to the underlying database management system
	 */
	private static void fillTables(Connection con) {
		Map<Poll, List<PollOption>> map = createPollToPollOptionMap();
		
		for (Map.Entry<Poll, List<PollOption>> entry : map.entrySet()) {
			long key = fillPolls(entry.getKey(), con);
			fillPollOptions(entry.getValue(), key, con);
		}
		
	}

	/**
	 * Helper method for inserting a value into Polls.
	 * 
	 * @param poll the poll to be inserted
	 * @param con established connection to the underlying database management system
	 * @return the key under which the poll was inserted; to be used by its associated 
	 * 	       poll options so they can remember it as their foreign key
	 */
	private static long fillPolls(Poll poll, Connection con) {
		Long votingKey = null;
		
		try (PreparedStatement pst = con.prepareStatement(
				"INSERT INTO Polls (title, message) values (?,?)", 
				Statement.RETURN_GENERATED_KEYS)) {
			
			pst.setString(1, poll.getTitle());
			pst.setString(2, poll.getMessage());
			
			pst.executeUpdate();
	
			try (ResultSet rset = pst.getGeneratedKeys()) {
				if(rset != null && rset.next()) {
					votingKey = rset.getLong(1);
				}
			}
			
		} catch(SQLException ex) {
			throw new RuntimeException("Could not insert value into Polls!", ex);
		} 
		
		return votingKey;
	}

	/**
	 * Helper method for filling the PollOptions table with values
	 * defined in voting definition files.
	 * 
	 * @param pollOptions list of poll options to fill table with
	 * @param pollId ID of the poll whose options are being added
	 * @param con established connection to the underlying database management system
	 */
	private static void fillPollOptions(List<PollOption> pollOptions, long pollId, Connection con) {
		
		try (PreparedStatement pst = con.prepareStatement(
				"INSERT INTO PollOptions (optionTitle, optionLink, pollId, votesCount) values (?,?,?,?)")) {
			
			for (PollOption opt : pollOptions) {
				pst.setString(1, opt.getOptionTitle());
				pst.setString(2, opt.getOptionLink());
				pst.setLong(3, pollId);
				pst.setLong(4, 0);

				pst.executeUpdate();
			}
			
		} catch(SQLException ex) {
			throw new RuntimeException("Could not insert values into PollOptions!", ex);
		} 		
	}

	
	/**
	 * Helper method for checking whether at least one row exists in the 
	 * table with the specified table ID.
	 * 
	 * @param tableId table to check for values
	 * @param con established connection to the underlying database management system
	 * @return true if at least one value exists; false otherwise
	 */
	private static boolean checkValuesExist(String tableId, Connection con) {
		try (PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM "+tableId)) {
			
			try (ResultSet rset = pst.executeQuery()) {
				if (rset.next()) {
					return rset.getLong(1) != 0;
				}
			}
			
		} catch (SQLException ex) {
			throw new RuntimeException("Could not determine if values exist in table "+tableId, ex);
		}
		 
		return true;
	}
	
	/**
	 * Helper method for creating a map containing the whole structure
	 * of the voting defintion files, with each Poll being mapped to a
	 * list of its corresponding PollOptions.
	 * 
	 * @return a map mapping Polls to a list of their corresponding PollOptions
	 */
	private static Map<Poll, List<PollOption>> createPollToPollOptionMap() {
		Map<Poll, List<PollOption>> map = new LinkedHashMap<>();
		
		for (Path path : votingDefinitionPaths) {
			List<String> lines;
			try {
				lines = Files.readAllLines(path);
			} catch (IOException ex) {
				throw new RuntimeException("Could not open file "+path, ex);
			}
			
			Poll poll = null;
			List<PollOption> pollOptions = new ArrayList<>();
			
			for (int i = 0, n = lines.size(); i < n; i++) {
				String[] tokens = lines.get(i).split("\t");
				
				if (tokens.length < 2) {
					continue;
				}
				
				if (i == 0) {
					poll = new Poll(null, tokens[0], tokens[1]);
					continue;
				}
				
				pollOptions.add(new PollOption(null, tokens[1], tokens[2], null, null));
			}
			
			map.put(poll, pollOptions);
		}
		
		return map;
	}
	
}
