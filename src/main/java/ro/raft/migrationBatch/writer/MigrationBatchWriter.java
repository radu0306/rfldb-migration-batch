package ro.raft.migrationBatch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import ro.astl.services.rfldbapi.country.dto.CountryIn;
import ro.astl.services.rfldbapi.country.manager.CountryManager;
import ro.astl.services.rfldbapi.country.service.CountryService;

public class MigrationBatchWriter implements ItemWriter<CountryIn>{

	@Autowired
	private CountryManager countryManager;
	
	@Override
	public void write(List<? extends CountryIn> items) throws Exception {
		
		for(CountryIn countryIn : items) {
			countryManager.createCountry(countryIn);
//			System.out.println("PULA MEA-----------" + countryManager.findCountryByShortName("stringpl"));
			countryManager.createCountry(countryIn);
		}
		
	}

}
