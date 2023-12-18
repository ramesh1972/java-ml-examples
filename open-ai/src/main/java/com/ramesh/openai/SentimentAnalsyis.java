package com.ramesh.openai;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

/***
 * This project demonstrates the use of Chat GPT as a classifier.
 * In this case the sentiment for the movie review si classified 
 * as positive, negative or neutral 
 ***/
class SentimentAnalsyis {
	public static void main(String... args) {
		
		// Set the Open AI Token & Model
        String token = "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";
        String model = "gpt-3.5-turbo";

        // service handle for calling OpenAI APIs
		OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

		// dataset of reviews on movies
		System.out.println("\n-----------------------------------------------------------");	
		String[] reviews = new String[10];
		reviews[0]="Inception: The plot was captivating, and the characters were well-developed. I enjoyed every minute of it.";
		reviews[1]="Disillusionment: Unfortunately, the movie didn't live up to my expectations. The storyline was confusing, and the acting felt forced.";
		reviews[2]="Reflections: It had its moments. The cinematography was good, but the pacing felt a bit off. An average film overall.";
		reviews[3]="Sunshine: Uplifting and heartwarming. The characters were endearing, and the message was powerful.";
		reviews[4]="Fading Shadows: This film fell flat for me. The plot was cliché, and the dialogue was uninspired.";
		reviews[5]="Parallel Worlds: A middle-of-the-road movie. The special effects were impressive, but the storyline lacked originality.";
		reviews[6]="Twisted Minds: Incredible storytelling! The twists and turns kept me on the edge of my seat.";
		reviews[7]="High Expectations: I had high hopes, but this movie let me down. The plot was predictable, and the characters were one-dimensional.";
		reviews[8]="Lost Horizons: A decent effort, but it didn't quite hit the mark. Some scenes were memorable, but overall, it didn't leave a lasting impression.";
		reviews[9]="Eternal Echoes: A masterpiece! The director's vision truly came to life on screen. The performances were outstanding, and the emotional depth of the story resonated with me.";

		System.out.println("-----------------------------------------------------------");	

		// let chat gpt know that it is a sentiment analyzer and has to respond the review as positive, negative or neutral
		final ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a sentiment analsyer based on review");
		final ChatMessage assistantMessage = new ChatMessage(ChatMessageRole.ASSISTANT.value(), "sentiment can be positive, negative, neutral");

		// loop through each review
		for (int i = 0; i < 10; i++) {
			String review = reviews[i];
			final List<ChatMessage> messages = new ArrayList<>();

			messages.add(sysMessage);
			messages.add(assistantMessage);

			final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), review);
			final ChatMessage userMessage1 = new ChatMessage(ChatMessageRole.USER.value(), "What is the sentiment?");
			messages.add(userMessage);
			messages.add(userMessage1);

			// create the chat gpt chat completion request
			ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
														  .model(model)
														  .messages(messages)
														  .n(1)
														  .temperature(.1)
														  .maxTokens(50)
														  .logitBias(new HashMap<>())
														  .build();

			System.out.println("review=" + review);
			System.out.print("ChatGPT Sentiment response=");
			
			// send the chat gpt request and get response
			service.createChatCompletion(chatCompletionRequest).getChoices().forEach((c) -> {
				System.out.println(c.getMessage().getContent());
			});
			
			System.out.println("--------------------------------");
		}

		service.shutdownExecutor();
	}
}
