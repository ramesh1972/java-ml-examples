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
 * This project demonstrates a simple multi prompts usage
 ***/
class MultipleChatCompletion {
	public static void main(String... args) {
		
		// Set the Open AI Token & Model
        String token = "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";
        String model = "gpt-3.5-turbo";
		
        // service handle for calling OpenAI APIs
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

        // multiple prompts example
        // Change these and run again and again
		String[] prompts = new String[3];

		prompts[0] = "Capital of India?";
		prompts[1] = "No. of States in India?";
		prompts[2] = "Prime Minister of India?";

		// Loop through each prompt
		for (int i = 0; i < 3; i++) {
			String prompt = prompts[i];
			
			// create the chat message
			final List<ChatMessage> messages = new ArrayList<>();
			final ChatMessage assistantMessage = new ChatMessage(ChatMessageRole.ASSISTANT.value(), prompt);
			messages.add(assistantMessage);

			// create the request object
			ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
														  .model(model)
														  .messages(messages)
														  .n(1)
														  .temperature(.1)
														  .maxTokens(50)
														  .logitBias(new HashMap<>())
														  .build();

			// get the response from chat gpt
			System.out.println("Prompt=" + prompt);
			System.out.print("ChatGPT response=");
			
			service.createChatCompletion(chatCompletionRequest).getChoices().forEach((c) -> {
				System.out.println(c.getMessage().getContent());
			});
			
			System.out.println("--------------------------------");
		}
		
		service.shutdownExecutor();
	}
}
