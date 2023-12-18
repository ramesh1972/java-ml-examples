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
 * This project demonstrates the General Knowledge prompting technique which is useful when there is need 
 * to cover a large areas of knowledge that the chat gpt can use to provide a good legible response 
 ***/
class GeneralKnowledgePrompting {
	public static void main(String... args) {
	
		String token = "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";
        String model = "gpt-3.5-turbo-0613";

        // service handle for calling OpenAI APIs
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

		System.out.println("\n-----------------------------------------------------------");	
		String[] prompts = new String[3];
		prompts[0]="Question: Part of golf is trying to get a higher point total than others. Yes or No?";
		prompts[1]="Knowledge: The objective of golf is to play a set of holes in the least number of strokes. A round of golf typically consists of 18 holes. Each hole is played once in the round on a standard golf course. Each stroke is counted as one point, and the total number of strokes is used to determine the winner of the game.";
		prompts[2]="Explain and Answer: ";

		final List<ChatMessage> messages1 = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			System.out.println(prompts[i]);
			final ChatMessage assistantMessage1 = new ChatMessage(ChatMessageRole.USER.value(), prompts[i]);
			messages1.add(assistantMessage1);
		}
		
		ChatCompletionRequest chatCompletionRequest2 = ChatCompletionRequest.builder()
													   .model("gpt-3.5-turbo")
													   .messages(messages1)
													   .n(1)
													   .temperature(.1)
													   .maxTokens(100)
													   .logitBias(new HashMap<>())
													   .build();

		System.out.println("------------");
		System.out.print("ChatGPT response=");

		service.createChatCompletion(chatCompletionRequest2).getChoices().forEach((c) -> {
			System.out.println(c.getMessage().getContent());
		});

		service.shutdownExecutor();
	}
}
