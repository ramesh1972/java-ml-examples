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
 * This project demonstrates the Chain of Thought (CoT) prompting technique which is useful when there is need 
 * for analytical, reasoning, deriving etc. kind of problems 
 ***/
class ChainOfThoughtPrompting {
	public static void main(String... args) {
		// Set the Open AI Token & Model
		String token = "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";
		String model = "gpt-3.5-turbo";
		
		// service handle for calling OpenAI APIs
		OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

        System.out.println("-----------------------------------------------------------");
        
        // prompt - change this and run again and again. Mostly ChatGPT will not give the right response for complex prompt like puzzle.
        // that's where Chain of thought comes to help (next prompt with COT is given below)
		String prompt="I went to the market and bought 10 apples. I gave 2 apples to the neighbor and 2 to the repairman. I then went and bought 5 more apples and ate 1. How many apples did I remain with?";
        System.out.println(prompt);	

        // create the Chat message object
        final List<ChatMessage> messages = new ArrayList<>();
		final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
		messages.add(userMessage);

		// call ChatGPT ChatCompletion API and get the response
		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
													  .builder()
													  .model(model)
													  .messages(messages)
													  .n(1)
													  .temperature(.1)
													  .maxTokens(200)
													  .logitBias(new HashMap<>())
													  .build();

		System.out.println("------------");
		System.out.print("ChatGPT response=");

		service.createChatCompletion(chatCompletionRequest).getChoices().forEach((c) -> {
			System.out.println(c.getMessage().getContent());
		});

		System.out.println("\n-----------------------------------------------------------");
		
		// Call ChatGPT Chat Completion with a CoT (Chain of THought) prompting technique
		// You will see that ChatGPT most likely will give the right answer. This is because in the prompt
		// the thinking process is given in the form of examples
		String[] prompts = new String[10];
		prompts[0] = "The odd numbers in this group add up to an even number: 4, 8, 9, 15, 12, 2, 1.";
		prompts[1] = "A: The answer is False.";
		prompts[2] = "The odd numbers in this group add up to an even number: 17,  10, 19, 4, 8, 12, 24.";
		prompts[3] = "A: The answer is True.";
		prompts[4] = "The odd numbers in this group add up to an even number: 16,  11, 14, 4, 8, 13, 24.";
		prompts[5] = "A: The answer is True.";
		prompts[6] = "The odd numbers in this group add up to an even number: 17,  9, 10, 12, 13, 4, 2.";
		prompts[7] = "A: The answer is False.";
		prompts[8] = "The odd numbers in this group add up to an even number: 15, 32, 5, 13, 82, 7, 1. ";
		prompts[9] = "A: ";

		final List<ChatMessage> messages_cot = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			System.out.println(prompts[i]);
			final ChatMessage assistantMessage = new ChatMessage(ChatMessageRole.ASSISTANT.value(), prompts[i]);
			messages_cot.add(assistantMessage);
		}
		
		ChatCompletionRequest chatCompletionRequest2 = ChatCompletionRequest
													   .builder()
													   .model(model)
													   .messages(messages_cot)
													   .n(1)
													   .temperature(.1)
													   .maxTokens(50)
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
