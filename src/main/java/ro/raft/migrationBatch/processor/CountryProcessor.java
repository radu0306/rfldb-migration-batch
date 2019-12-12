package ro.raft.migrationBatch.processor;

import org.springframework.batch.item.ItemProcessor;

import ro.astl.services.rfldbapi.country.dto.CountryIn;

public class CountryProcessor implements ItemProcessor<CountryIn, CountryIn> {

	@Override
	public CountryIn process(CountryIn country) throws Exception {
		System.out.println("Country read: " + country);
        return country;
	}

	
	

}
