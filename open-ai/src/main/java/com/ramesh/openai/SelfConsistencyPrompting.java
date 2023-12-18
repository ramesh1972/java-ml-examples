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
 * This project demonstrates the Self Consistency prompting technique which is useful when there is need 
 * to get consistent responses from chat gpt, for e.g. solving a puzzle correctly 
 ***/
class SelfConsistencyPrompting {
	public static void main(String... args) {
		
		// Set the Open AI Token & Model
        String token = "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";
        String model = "gpt-3.5-turbo";

        // service handle for calling OpenAI APIs
		OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));
		
		// self consistent messages are part of the prompt which tell chat gpt how to think and respond thus 
		// giving consistent messages
		System.out.println("\n-----------------------------------------------------------");	
		String[] prompts = new String[15];
		prompts[0]="Q: There are 15 trees in the grove. Grove workers will plant trees in the grove today. After they are done,";
		prompts[1]="there will be 21 trees. How many trees did the grove workers plant today?";
		prompts[2]="A: We start with 15 trees. Later we have 21 trees. The difference must be the number of trees they planted.";
		prompts[3]="So, they must have planted 21 - 15 = 6 trees. The answer is 6.";
		prompts[4]="Q: If there are 3 cars in the parking lot and 2 more cars arrive, how many cars are in the parking lot?";
		prompts[5]="A: There are 3 cars in the parking lot already. 2 more arrive. Now there are 3 + 2 = 5 cars. The answer is 5.";
		prompts[6]="Q: Leah had 32 chocolates and her sister had 42. If they ate 35, how many pieces do they have left in total?";
		prompts[7]="A: Leah had 32 chocolates and Leah’s sister had 42. That means there were originally 32 + 42 = 74";
		prompts[8]="chocolates. 35 have been eaten. So in total they still have 74 - 35 = 39 chocolates. The answer is 39.";
		prompts[9]="Q: Jason had 20 lollipops. He gave Denny some lollipops. Now Jason has 12 lollipops. How many lollipops";
		prompts[10]="did Jason give to Denny?";
		prompts[11]="A: Jason had 20 lollipops. Since he only has 12 now, he must have given the rest to Denny. The number of";
		prompts[12]="lollipops he has given to Denny must have been 20 - 12 = 8 lollipops. The answer is 8.";
		prompts[13]="Q: When I was 6 my sister was half my age. Now I’m 70 how old is my sister?";
		prompts[14]="A:";

		final List<ChatMessage> messages = new ArrayList<>();

		for (int i = 0; i < 15; i++) {
			System.out.println(prompts[i]);
			final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), prompts[i]);
			messages.add(userMessage);
		}
		
		// create the chat gpt chat completion request
		ChatCompletionRequest chatCompletionRequest2 = ChatCompletionRequest.builder()
													   .model(model)
													   .messages(messages)
													   .n(1)
													   .temperature(.1)
													   .maxTokens(100)
													   .logitBias(new HashMap<>())
													   .build();

		System.out.println("------------");
		System.out.print("ChatGPT response=");

		// send the request to chat gpt and print the response
		service.createChatCompletion(chatCompletionRequest2).getChoices().forEach((c) -> {
			System.out.println(c.getMessage().getContent());
		});

		service.shutdownExecutor();
	}
}
