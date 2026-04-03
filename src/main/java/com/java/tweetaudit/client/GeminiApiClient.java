package com.java.tweetaudit.client;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class GeminiApiClient {

    private final Client client;

    public GeminiApiClient() {
        this.client = new Client();
    }

    public String analyzeTweet(String tweetText){

        String prompt = "Analyze the sentiment of this tweet (Positive/Negative/Neutral). Respond ONLY with the single word sentiment: " + tweetText;

        try{
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null
            );

            return response.text().trim();

        } catch (Exception e){
            System.err.println("API Call failed: " + e.getMessage());
            return "API_ERROR";
        }
    }

    public String analyzeTweetBatch(List<String> tweets) {
        StringBuilder prompt = new StringBuilder(
                "Analyze the sentiment of the following tweets. Respond ONLY with a comma-separated list of sentiments (Positive, Negative, or Neutral) in the exact order of the tweets provided:\n\n"
        );

        for (int i = 0; i < tweets.size(); i++) {
            prompt.append(i + 1).append(". ").append(tweets.get(i)).append("\n");
        }

        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt.toString(),
                    null
            );
            return response.text().trim();
        } catch (Exception e) {
            System.err.println("SDK Call failed: " + e.getMessage());
            return "API_ERROR";
        }
    }


}
