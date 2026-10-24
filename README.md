# Tweet Audit

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?logo=springboot&logoColor=white) ![Gemini](https://img.shields.io/badge/Gemini-2.5_Flash-8E75B2?logo=googlegemini&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)

A Spring Boot pipeline that audits tweets for sentiment with Google Gemini. It streams a CSV of tweets line by line, groups them into batches, and sends each batch to Gemini in a single prompt, so thousands of tweets can be classified as **Positive**, **Negative** or **Neutral** with few API calls and flat memory use.

## How it works

```
tweets.csv ──▶ TweetReaderService ──▶ batches of N ──▶ GeminiApiClient ──▶ "Positive, Neutral, Negative, ..."
               (streams with           (Consumer          (one prompt per
                BufferedReader)          callback)          batch, gemini-2.5-flash)
                                  ▲
                    OrchestrationService runs the pipeline
                    PipelineController exposes it over HTTP
```

- **`TweetReaderService`** reads the CSV with a `BufferedReader`, never loading the whole file, and hands each full batch to a callback.
- **`GeminiApiClient`** wraps the official `google-genai` Java SDK. It can classify one tweet, or a numbered batch with the answers returned in order.
- **`OrchestrationService`** wires the two together (batch size 5 by default) and logs each batch result.
- **`PipelineController`** exposes the endpoints below.

## Endpoints

| Method | Path | What it does |
| --- | --- | --- |
| `GET` | `/test-ai?tweet=...` | Classifies a single tweet and returns the sentiment |
| `GET` | `/start-pipeline` | Runs the batch pipeline over the sample CSV; results stream to the console |

Ready-made requests are in [`api-requests/`](api-requests) for the IntelliJ / VS Code HTTP client.

## Getting started

Requires Java 21 and a [Gemini API key](https://aistudio.google.com/app/apikey).

```bash
git clone https://github.com/tanwarAalok/tweet-audit.git
cd tweet-audit
export GOOGLE_API_KEY=your-gemini-key      # Windows: set GOOGLE_API_KEY=your-gemini-key
./mvnw spring-boot:run                     # Windows: mvnw.cmd spring-boot:run
```

Then try:

```bash
curl "http://localhost:8080/test-ai?tweet=I%20am%20so%20happy%20with%20my%20new%20promotion!"
curl "http://localhost:8080/start-pipeline"
```

## Sample data

- `src/main/resources/dummy_tweets_short.csv`: 9 tweets, used by the pipeline by default
- `src/main/resources/dummy_tweets.csv`: 10,000 tweets for load testing (switch the path in `OrchestrationService`)

Both use the format `id,tweet_text`.

## Tech stack

Java 21 · Spring Boot 4 (Web MVC, RestClient) · Google GenAI Java SDK · Lombok · Maven
