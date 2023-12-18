package com.ramesh.langchain;

import java.util.Scanner;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

/***
 * This project demostrates the use of LangCHain Services which uses custom tools to generate the final output
 */
public class ServiceWithToolsLive {

	// Open AI Key and Chat GPT Model to use
	public static String OPENAI_API_KEY = "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";
	public static String OPENAI_MODEL = "gpt-3.5-turbo";

	public static void main(String[] args) {
		
		System.out.println("Using a custom Calculator as LangChain \"tool\"");

		// Building a Custom LangChain Assistant using LangChain AiServices
		System.out.println("Building a Custom Assistant using LangChain AiServices");
		Assistant assistant = AiServices.builder(Assistant.class)
				.chatLanguageModel(OpenAiChatModel.withApiKey(OPENAI_API_KEY)).tools(new Calculator())
				.chatMemory(MessageWindowChatMemory.withMaxMessages(10)).build();

		while (true) {
			// get 2 words for which the total characters count is calculated
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter Word 1:");
			String word1 = scanner.nextLine();
			System.out.print("Enter Word 2:");
			String word2 = scanner.nextLine();
			
			String question = "What is the sum of the numbers of letters in the words \"" + word1 + "\" and \"" + word2 + "\"?";
			System.out.println("Prompting ChatGPT :" + question);

			// when a prompt having 2 words are sent LLM via LAngChain Assistant
			// the Calcualtor functions are called to get the final answers
			System.out.println("Invoking Custom Assistant Class chat() and getting response from ChatGPT...");
			String answer = assistant.chat(question);

			System.out.println("ChatGPT Response...\n");
			System.out.println(answer);
		}
	}

	// a custom tool
	static class Calculator {
		@Tool("Calculates the length of a string")
		int stringLength(String s) {
			return s.length();
		}

		@Tool("Calculates the sum of two numbers")
		int add(int a, int b) {
			return a + b;
		}
	}

	interface Assistant {
		String chat(String userMessage);
	}
}
