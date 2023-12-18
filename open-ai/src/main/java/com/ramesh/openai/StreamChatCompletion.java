package com.ramesh.openai;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

/***
 * This project demonstrates the how to stream respones from chat gpt as it 
 * is getting generated.
 * Useful when the response is expected to be huge
 ***/
class StreamChatCompletion {
    public static void main(String... args) {
    	
		// Set the Open AI Token & Model
        String token = "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";
        String model = "gpt-3.5-turbo";

        // service handle for calling OpenAI APIs
		OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));
		
        System.out.println("--------------------------------------------------------");
        System.out.println("Streaming chat completion...");
        
        // set the chat message
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
        messages.add(systemMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        ArrayList<ChatCompletionChoice> choices3 = new ArrayList<ChatCompletionChoice>();
        
        // call chat gpt and open a stream through which responses will come in chunks and continously just like you see on chatgpt website
        service.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach((c2) -> { c2.getChoices().forEach( (c1) -> { 
                	System.out.println(c1.getMessage().getContent());
                	choices3.add(c1);
                	}); 
                });

        // print the full message in the end
        System.out.println("--------------------------------------------------------");
        System.out.print("Full message=");
        choices3.forEach( (c) -> { 
        	if (c.getMessage().getContent() != null)
                System.out.print( c.getMessage().getContent());
        });
        
        service.shutdownExecutor();
    }
}
