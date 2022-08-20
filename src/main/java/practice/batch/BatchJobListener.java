package practice.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import practice.batch.person.PersonDTO;

@Component
public class BatchJobListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(BatchJobListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BatchJobListener(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("(Spring Batch) JOB STARTED");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("(Spring Batch) JOB FINISHED - Time to verify the results");

            jdbcTemplate.query("SELECT first_name, last_name FROM people",
                (rs, row) -> new PersonDTO(
                    rs.getString(1),
                    rs.getString(2))
            ).forEach(person -> log.info("Found <" + person + "> in the database."));
        }
    }
}