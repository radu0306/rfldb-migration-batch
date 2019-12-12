package ro.raft.migrationBatch.processor;

import org.springframework.batch.item.ItemProcessor;

import ro.astl.services.rfldbapi.league.dto.LeagueNodeIn;

public class LeagueNodeProcessor implements ItemProcessor<LeagueNodeIn, LeagueNodeIn> {

	@Override
	public LeagueNodeIn process(LeagueNodeIn leagueNode) throws Exception {
		
		System.out.println("LeagueNode read: " + leagueNode);
		return leagueNode;
	}

}
