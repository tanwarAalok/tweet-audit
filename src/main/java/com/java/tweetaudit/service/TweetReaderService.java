package com.java.tweetaudit.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class TweetReaderService {

    public void streamTweetsInBatches(String filePath, int batchSize, Consumer<List<String>> batchProcessor){

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            List<String> batch = new ArrayList<>();

            br.readLine();

            while((line = br.readLine()) != null){
                String[] columns = line.split(",", 2);
                if(columns.length == 2){
                    batch.add(columns[1]);
                }

                if(batch.size() == batchSize){
                    batchProcessor.accept(batch);
                    batch.clear();
                }
            }

            if(!batch.isEmpty()){
                batchProcessor.accept(batch);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage());
        }
    }
}
