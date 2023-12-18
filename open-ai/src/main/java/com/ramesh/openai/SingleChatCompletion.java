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
 * This project demonstrates a simple single promt
 * a good starting point to know Open AI APIs
 ***/
class SingleChatCompletion {
	public static void main(String... args) {
		
		// Set the Open AI Token & Model
        String token = "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";
        String model = "gpt-3.5-turbo";

        // service handle for calling OpenAI APIs
		OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

		// set the prompt
		// change the prompt and run again and again
		String prompt = "President of India?";

		// create the chat message with the prompt
		final List<ChatMessage> messages = new ArrayList<>();
		final ChatMessage assistantMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
		messages.add(assistantMessage);

		// create the chat gpt chat completion request
		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
													  .model(model)
													  .messages(messages)
													  .n(1)
													  .temperature(.1)
													  .maxTokens(50)
													  .logitBias(new HashMap<>())
													  .build();

		System.out.println("Prompt=" + prompt);
		System.out.print("ChatGPT response=");

		// send the chat gpt request and get response
		service.createChatCompletion(chatCompletionRequest).getChoices().forEach((c) -> {
			System.out.println(c.getMessage().getContent());
		});

		service.shutdownExecutor();
	}
}
