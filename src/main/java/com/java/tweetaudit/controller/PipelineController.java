package com.java.tweetaudit.controller;

import com.java.tweetaudit.client.GeminiApiClient;
import com.java.tweetaudit.service.OrchestrationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PipelineController {

    private final GeminiApiClient geminiApiClient;
    private final OrchestrationService orchestrationService;

    public PipelineController(GeminiApiClient geminiApiClient, OrchestrationService orchestrationService) {
        this.geminiApiClient = geminiApiClient;
        this.orchestrationService = orchestrationService;
    }

    @GetMapping("/test-ai")
    public String testAi(@RequestParam(defaultValue = "My flight was delayed 3 hours and I lost my luggage. Terrible experience.") String tweet) {
        String sentiment = geminiApiClient.analyzeTweet(tweet);
        return "Tweet: '" + tweet + "' <br> Sentiment: <b>" + sentiment + "</b>";
    }

    @GetMapping("/start-pipeline")
    public String startPipeline() {
        orchestrationService.runPipeline();
        return "Pipeline started! Check your IDE console for the streaming logs.";
    }
}
