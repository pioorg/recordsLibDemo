/*
 *
 *  Copyright (C) 2021 Piotr Przybył
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package dev.softwaregarden.recordsLibDemo;

import dev.softwaregarden.recordsLibDemo.dto.NameAndEmail;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@EnableBatchProcessing
@Configuration
public class CsvToJsonConvertingJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public CsvToJsonConvertingJob(JobBuilderFactory jobBuilderFactory,
                                  StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job flatFilesReaderJob() throws Exception {
        return jobBuilderFactory.get("csvToJsonConvertingJob")
            .start(csvReaderStep())
            .build();
    }

    @Bean
    public Step csvReaderStep() throws Exception {
        return stepBuilderFactory.get("csvReader")
            .<NameAndEmail, NameAndEmail>chunk(5)
            .reader(csvFileReader(null))
            .writer(jsonFileItemWriter(null))
            .build();
    }


    @Bean
    public FlatFileItemReader<NameAndEmail> csvFileReader(@Value("classpath:namesAndEmails.csv") Resource namesAndEmailsCsv) {
        return new FlatFileItemReaderBuilder<NameAndEmail>()
            .name("csvReader")
            .resource(namesAndEmailsCsv)
            .delimited()
            .delimiter(";")
            .names("firstName", "lastName", "email")
            .targetType(NameAndEmail.class)
            .build();
    }

    @Bean
    public JsonFileItemWriter<NameAndEmail> jsonFileItemWriter(@Value("file:#{systemProperties['java.io.tmpdir']}/namesAndEmails.json") Resource exportResource) {
        System.out.println("Will export to: [" + exportResource+"]");
        return new JsonFileItemWriterBuilder<NameAndEmail>()
            .name("jsonWriter")
            .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
            .resource(exportResource)
            .build();
    }

}


