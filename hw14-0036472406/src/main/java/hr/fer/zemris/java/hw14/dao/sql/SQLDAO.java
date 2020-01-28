package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author marcupic
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getPolls() {
		Connection con = SQLConnectionProvider.getConnection();
		
		List<Poll> list = new ArrayList<>();
		try (PreparedStatement pst = 
				con.prepareStatement("SELECT id, title, message from POLLS order by id")) {
			
			try (ResultSet rset = pst.executeQuery()) {
				while(rset.next()) {
					long id = rset.getLong(1);
					String title = rset.getString(2);
					String message = rset.getString(3);
					
					list.add(new Poll(id, title, message));
				}
				
			} 
		} catch(SQLException ex) {
			throw new DAOException("Could not load polls from Polls!", ex);
		} 
		
		return list;
	}
	
	@Override
	public Poll getPollWithId(long requestedPollId) {
		Connection con = SQLConnectionProvider.getConnection();
		
		Poll poll = null;
		try (PreparedStatement pst = 
				con.prepareStatement("SELECT id, title, message from POLLS WHERE id="+requestedPollId)) {
			
			try (ResultSet rset = pst.executeQuery()) {
				if (rset.next()) {
					long id = rset.getLong(1);
					String title = rset.getString(2);
					String message = rset.getString(3);

					poll = new Poll(id, title, message);
				}
			} 
			
		} catch(SQLException ex) {
			throw new DAOException("Could not load poll with ID "+requestedPollId, ex);
		} 
		
		return poll;
	}
	
	@Override
	public List<PollOption> getPollOptions(long requestedPollId) {
		Connection con = SQLConnectionProvider.getConnection();
		
		List<PollOption> list = new ArrayList<>();
		try (PreparedStatement pst = con.prepareStatement(
				"SELECT id, optionTitle, optionLink, pollId, votesCount "+
				"FROM POLLOPTIONS WHERE pollId="+requestedPollId+" ORDER BY id")) {
			
			try (ResultSet rset = pst.executeQuery()) {
				while(rset.next()) {
					long id = rset.getLong(1);
					String optionTitle = rset.getString(2);
					String optionLink = rset.getString(3);
					long pollId = rset.getLong(4);
					long votesCount = rset.getInt(5);
					
					list.add(new PollOption(id, optionTitle, optionLink, pollId, votesCount));
				}
			} 
			
		} catch(SQLException ex) {
			throw new DAOException("Could not load poll options with ID "+requestedPollId, ex);
		}
		
		return list;
	}


	@Override
	public long addVote(long requestedId) {
		Connection con = SQLConnectionProvider.getConnection();
		
		Long pollIdOfIncrementedPollOption = null;
		try (PreparedStatement pst =
				con.prepareStatement("SELECT pollId FROM POLLOPTIONS WHERE id="+requestedId)) {
			
			try (ResultSet rset = pst.executeQuery()) {
				if (rset.next()) {
					pollIdOfIncrementedPollOption = rset.getLong(1);
				}
			}
			
			
		} catch(SQLException ex) {
			throw new DAOException("Could not determine pollID of poll option with ID "+requestedId, ex);
		} 

		try (PreparedStatement pst = con.prepareStatement(
				"UPDATE POLLOPTIONS SET votesCount=votesCount+1 WHERE id="+requestedId)) {
			pst.executeUpdate();
			
		} catch(SQLException ex) {
			throw new DAOException("Could not add vote to poll option with ID "+requestedId, ex);
		} 		
		
		return pollIdOfIncrementedPollOption == null ? 0 : pollIdOfIncrementedPollOption;
	}

}
