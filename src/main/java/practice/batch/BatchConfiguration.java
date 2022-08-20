package practice.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import practice.batch.person.PersonDTO;
import practice.batch.person.PersonItems;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private PersonItems personItems;
    @Autowired
    private BatchJobListener listener;

    @Bean
    public Job importJob() {
    return jobBuilderFactory.get("importJob")
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .flow(step1())
        .end()
        .build();
    }

    @Bean
    public Step step1() {
    return stepBuilderFactory.get("step1")
        .<PersonDTO, PersonDTO> chunk(10)
        .reader(personItems.reader())
        .processor(personItems.processor())
        .writer(personItems.writer())
        .build();
    }

}
