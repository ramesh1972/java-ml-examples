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
 * This project demonstrates the Few Shot prompting technique which is useful when needs to be a classification and
 * when providing similar example prompts makes chat gpt responses more precise
 ***/
class FewShotPrompting {
	public static void main(String... args) {
		
		// Set the Open AI Token & Model
        String token = "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";
        String model = "gpt-3.5-turbo";

        // service handle for calling OpenAI APIs
		OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

		// Few Shot Prompting. provide examples for the actual prompt. ChatGPT will respond more accurately
		String[] prompts = new String[5];
		prompts[0]="Positive This is awesome! ";
		prompts[1]="This is bad! Negative";
		prompts[2]="Wow that movie was rad!";
		prompts[3]="Positive";
		prompts[4]="What an interensting movie! --";

		// create the Chat message with prompt in assistant role as we are assisting chatGPT to respond better
		final List<ChatMessage> messages = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			System.out.println(prompts[i]);
			final ChatMessage assistantMessage = new ChatMessage(ChatMessageRole.USER.value(), prompts[i]);
			messages.add(assistantMessage);
		}
		
		// create the Chat Completion request
		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
													  .builder().model(model)
													  .messages(messages)
													  .n(1)
													  .temperature(.1)
													  .maxTokens(100)
													  .logitBias(new HashMap<>())
													  .build();

		System.out.print("\nChatGPT response=");

		// call ChatGPT ChatCompletion API and get the response
		service.createChatCompletion(chatCompletionRequest).getChoices().forEach((c) -> {
			System.out.println(c.getMessage().getContent());
		});

		service.shutdownExecutor();
	}
}
