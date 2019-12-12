package ro.raft.migrationBatch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import ro.astl.services.rfldbapi.league.dto.LeagueNodeIn;
import ro.astl.services.rfldbapi.league.manager.LeagueNodeManager;

public class LeagueNodeWriter implements ItemWriter<LeagueNodeIn> {

	@Autowired
	private LeagueNodeManager leagueNodeManager;
	
	@Override
	public void write(List<? extends LeagueNodeIn> items) throws Exception {
		
		for(LeagueNodeIn leagueNode : items) {
			
			leagueNodeManager.createLeagueNode(leagueNode);
			
		}

	}

}
