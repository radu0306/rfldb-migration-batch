package ro.raft.migrationBatch.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ro.astl.services.rfldbapi.country.dto.CountryIn;
import ro.astl.services.rfldbapi.country.dto.RegionIn;
import ro.astl.services.rfldbapi.league.dto.LeagueIn;
import ro.raft.migrationBatch.mapper.CountryMapper;
import ro.raft.migrationBatch.mapper.LeagueMapper;
import ro.raft.migrationBatch.mapper.RegionMapper;
import ro.raft.migrationBatch.processor.CountryProcessor;
import ro.raft.migrationBatch.processor.LeagueProcessor;
import ro.raft.migrationBatch.processor.RegionProcessor;
import ro.raft.migrationBatch.writer.LeagueWriter;
import ro.raft.migrationBatch.writer.MigrationBatchWriter;
import ro.raft.migrationBatch.writer.RegionWriter;

@Configuration
@EnableTransactionManagement
@EnableBatchProcessing
@EnableJpaRepositories("ro.astl")
public class BatchConfig extends DefaultBatchConfigurer implements BatchConfigurer {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private CountryMapper countryFieldMapper;

	@Autowired
	private RegionMapper regionFieldMapper;
	
	@Autowired
	private LeagueMapper leagueFieldMapper;

	@Value("classPath:/input/countryList.csv")
	private Resource countryResource;

	@Value("classPath:/input/RegionList.csv")
	private Resource regionResource;

	@Value("classPath:/input/LeaguesList.csv")
	private Resource leagueResource;
	
	@Bean
	public Job readCSVFileJob() {
		return jobBuilderFactory.get("readCountryCSVJob").incrementer(new RunIdIncrementer()).start(countryStep())
				.next(regionStep()).build();
	}

	@Bean
	public Step countryStep() {
		return stepBuilderFactory.get("countryStep").<CountryIn, CountryIn>chunk(5).reader(countryReader())
				.processor(countryProcessor()).writer(countryWriter()).transactionManager(getBatchTransactionManager())
				.build();
	}

	@Bean
	public Step regionStep() {
		return stepBuilderFactory.get("regionStep").<RegionIn, RegionIn>chunk(5).reader(regionReader())
				.processor(regionProcessor()).writer(regionWriter()).transactionManager(getBatchTransactionManager())
				.build();

	}

	@Bean
	public Step leagueStep() {
		return stepBuilderFactory
				.get("leagueStep")
				.<LeagueIn, LeagueIn>chunk(5)
				.reader(leagueReader())
				.processor(leagueProcessor())
				.writer(leagueWriter())
				.transactionManager(getBatchTransactionManager())
				.build();
	}

	@Bean
	public FlatFileItemReader<CountryIn> countryReader() {
		FlatFileItemReader<CountryIn> itemReader = new FlatFileItemReader<CountryIn>();
		itemReader.setLineMapper(countryLineMapper());
		itemReader.setLinesToSkip(1);
		itemReader.setResource(countryResource);
		return itemReader;

	}

	@Bean
	public FlatFileItemReader<RegionIn> regionReader() {
		FlatFileItemReader<RegionIn> itemReader = new FlatFileItemReader<RegionIn>();
		itemReader.setLineMapper(regionLineMapper());
		itemReader.setLinesToSkip(1);
		itemReader.setResource(countryResource);
		return itemReader;

	}

	private FlatFileItemReader<LeagueIn> leagueReader() {
		FlatFileItemReader<LeagueIn> itemReader = new FlatFileItemReader<LeagueIn>();
		itemReader.setLineMapper(leagueLineMapper());
		itemReader.setLinesToSkip(1);
		itemReader.setResource(leagueResource);
		return itemReader;
	}

	@Bean
	public ItemProcessor<CountryIn, CountryIn> countryProcessor() {
		return new CountryProcessor();
	}

	@Bean
	public ItemProcessor<RegionIn, RegionIn> regionProcessor() {
		return new RegionProcessor();
	}
	
	@Bean
	public ItemProcessor<LeagueIn, LeagueIn> leagueProcessor() {
		return new LeagueProcessor();
	}

	@Bean
	public ItemWriter<CountryIn> countryWriter() {
		return new MigrationBatchWriter();
	}

	@Bean
	public ItemWriter<RegionIn> regionWriter() {
		return new RegionWriter();
	}
	
	@Bean
	public ItemWriter<LeagueIn> leagueWriter() {
		return new LeagueWriter();
	}

	@Bean
	public FieldSetMapper<CountryIn> getMapper() {
		return new CountryMapper();
	}

	@Bean
	public LineMapper<CountryIn> countryLineMapper() {
		DefaultLineMapper<CountryIn> lineMapper = new DefaultLineMapper<CountryIn>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(",");
		lineTokenizer.setNames(new String[] { "CountryName", "CountryShortName" });
		lineTokenizer.setIncludedFields(new int[] { 0, 1 });

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(countryFieldMapper);

		return lineMapper;
	}

	@Bean
	public LineMapper<RegionIn> regionLineMapper() {
		DefaultLineMapper<RegionIn> lineMapper = new DefaultLineMapper<RegionIn>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(",");
		lineTokenizer.setNames(new String[] { "RegionName", "RegionShortName" });
		lineTokenizer.setIncludedFields(new int[] { 0, 1 });

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(regionFieldMapper);

		return lineMapper;
	}
	
	@Bean
	public LineMapper<LeagueIn> leagueLineMapper() {
		DefaultLineMapper<LeagueIn> lineMapper = new DefaultLineMapper<LeagueIn>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(",");
		lineTokenizer.setNames(new String[] { "LeagueName", "LeageNode", "RegionName", "RegionShortName" });
		lineTokenizer.setIncludedFields(new int[] { 0, 1, 2, 3});

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(leagueFieldMapper);

		return lineMapper;
	}

	public PlatformTransactionManager getBatchTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;

	}
}
