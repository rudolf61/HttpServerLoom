package nl.http.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HttpClientTest {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newVirtualThreadExecutor();
        HttpClient httpClient = HttpClient.newBuilder()
                .executor(executor)
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        List<URI> targets = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            targets.add(URI.create("http://localhost:8080/application"));
        }

        Instant start = Instant.now();

        List<CompletableFuture<String>> result = targets.stream()
                .map(url -> httpClient.sendAsync(
                        HttpRequest.newBuilder(url)
                                .GET()
                                .setHeader("User-Agent", "performance-test")
                                .build(),
                        HttpResponse.BodyHandlers.ofString())
                        .thenApply(response -> response.body()))
                .collect(Collectors.toList());

        var endResult = CompletableFuture.allOf(result.toArray(new CompletableFuture[]{}));
        try {
            endResult.get();
            Instant end = Instant.now();
            System.out.printf("Execution took %d milliseconds", Duration.between(start, end)
                    .toMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
