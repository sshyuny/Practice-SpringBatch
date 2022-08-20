package practice.batch.person;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PersonItems {

    @Autowired
    public DataSource dataSource;
    
    @Bean
    @StepScope
    public JsonItemReader<PersonDTO> reader() {

        JacksonJsonObjectReader<PersonDTO> jsonObjectReader = new JacksonJsonObjectReader<>(PersonDTO.class);

        return new JsonItemReaderBuilder<PersonDTO>()
            .name("reader")
            .resource(new ClassPathResource("./files/jsondata.json"))
            .jsonObjectReader(jsonObjectReader)
            .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<PersonDTO> writer() {

        return new JdbcBatchItemWriterBuilder<PersonDTO>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
            .dataSource(dataSource)
            .build();
            
    }

}
