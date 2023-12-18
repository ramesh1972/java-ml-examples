package com.ramesh.langchain;

import static dev.langchain4j.data.message.UserMessage.userMessage;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;

/***
 * Demostrates LangChain's memory capability and getting response back from LLMs based on information in the memory
 */
public class ChatWithMemoryLive {

	// Open AI Key and Chat GPT Model to use
	public static String OPENAI_API_KEY = "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";
	public static String OPENAI_MODEL = "gpt-3.5-turbo";

    public static void main(String[] args) {
    	
    	// tokenizing the input messages/text and storing in chat memory
        ChatLanguageModel model = OpenAiChatModel.withApiKey(OPENAI_API_KEY);
        ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(300, new OpenAiTokenizer(OPENAI_MODEL));

        // sending the prompt to chatgpt
        System.out.println("Adding text \"Hello, my name is Ramesh\" to Chat Memory");
        chatMemory.add(userMessage("Hello, my name is Ramesh"));
        AiMessage answer = model.generate(chatMemory.messages())
        						.content();

        System.out.print("ChatGPT response: ");
        System.out.print(answer.text());
        
        chatMemory.add(answer);

        // calling chat gpt again to respond based on memory created via LangChain
        System.out.println("\n\nRetrieving text from memory for prompt \"What is my name?\"");
        chatMemory.add(userMessage("What is my name?"));
        AiMessage answerWithName = model.generate(chatMemory.messages())
        								.content();
        
        System.out.print("ChatGPT response: ");
        System.out.print(answerWithName.text());
        chatMemory.add(answerWithName);
    }
}
