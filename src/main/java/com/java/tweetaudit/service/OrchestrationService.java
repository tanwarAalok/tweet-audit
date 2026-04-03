package com.java.tweetaudit.service;

import com.java.tweetaudit.client.GeminiApiClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrchestrationService {

    private final TweetReaderService tweetReaderService;
    private final GeminiApiClient geminiApiClient;

    public OrchestrationService(TweetReaderService tweetReaderService, GeminiApiClient geminiApiClient) {
        this.tweetReaderService = tweetReaderService;
        this.geminiApiClient = geminiApiClient;
    }

    public void runPipeline(){
        System.out.println("Starting Tweet Sentiment Analysis Pipeline...");
        String filePath = "src/main/resources/dummy_tweets_short.csv";

        int batchSize = 5;

        tweetReaderService.streamTweetsInBatches(filePath, batchSize, (List<String> batch) -> {
            System.out.println("\n--- Processing new batch of " + batch.size() + " tweets ---");
            String aiResult = geminiApiClient.analyzeTweetBatch(batch);
            System.out.println("AI Response: " + aiResult);
        });

        System.out.println("\nPipeline execution completed.");
    }
}
