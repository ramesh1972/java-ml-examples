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
 * This project demonstrates the use of Prompt elements which are prompt roles -
 * SYSTEM, ASSISTANT, USER prompt roles 
 ***/
class PromptElements {
	public static void main(String... args) {
		
		// Set the Open AI Token & Model
        String token = "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";
        String model = "gpt-3.5-turbo";

        // service handle for calling OpenAI APIs
		OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));
		
		System.out.println("-----------------------------------------------------------");

		// put in the 3 different prompt role messages - SYSTEM, ASSISTANT and USER prompts
		final ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a science teacher");
		final ChatMessage assistantMessage = new ChatMessage(ChatMessageRole.ASSISTANT.value(), "you teach 6th grade students");
		final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(),
				"explain einstein's theory of relativity");

		final List<ChatMessage> messages = new ArrayList<>();

		messages.add(sysMessage);
		messages.add(assistantMessage);
		messages.add(userMessage);

		// create the chat gpt chat completion request
		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
													  .model("gpt-3.5-turbo")
													  .messages(messages)
													  .n(1)
													  .temperature(.1)
													  .maxTokens(300)
													  .logitBias(new HashMap<>())
													  .build();

		// send the request to chat gpt and print the response
		service.createChatCompletion(chatCompletionRequest).getChoices().forEach((c) -> {
			System.out.println(c.getMessage().getContent());
		});

		service.shutdownExecutor();
	}
}
