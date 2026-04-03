package com.java.tweetaudit.client;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Component;


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
}
