package com.ramesh.langchain;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;

/***
 * THis project demostrates hwo to use LangChain's prompt template
 */
public class PromptTemplatesLive {

	// Open AI Key and Chat GPT Model to use
	public static String OPENAI_API_KEY = "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";
	public static String OPENAI_MODEL = "gpt-3.5-turbo";

	public static void main(String[] args) {
		
		// create a LangChain PromptTEmplate with 2 placeholders
		PromptTemplate promptTemplate = PromptTemplate.from("Tell me a {{adjective}} joke about {{content}}..");

		Map<String, Object> variables = new HashMap<>();

		// get use input for the 2 placeholders and apply to the PromptTemplate
		while (true) {
			System.out.println("-----------------------------------------");
			System.out.print("Enter an adjective for a joke: ");
			Scanner scanner = new Scanner(System.in);
			String adj = scanner.nextLine();

			System.out.print("Enter topic of a joke : ");
			String content = scanner.nextLine();

			variables.put("adjective", adj);
			variables.put("content", content);

			Prompt prompt = promptTemplate.apply(variables);

			// use the Prompt object created from PromtpTemplate and send to OpenAI via LangChain
			ChatLanguageModel model = OpenAiChatModel.builder().apiKey(OPENAI_API_KEY).modelName(OPENAI_MODEL)
					.temperature(0.3).build();

			// get the response from LLM - chatgpt via LangChain
			String response = model.generate(prompt.text());
			
			System.out.println("\nChatGPT responded Joke...");
			System.out.println(response);
		}
	}
}
