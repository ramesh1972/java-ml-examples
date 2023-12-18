package com.ramesh.openai;

import java.time.Duration;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

/***
 * This project demonstrates a very basic Chat Completion. How the OpenAIService can be used to 
 * generate multiple responses for one prompt
 * IT also shows the responses with temperature value set to 0 to 1, which implies
 * zero hallucination and hence precise to bug hallucinations and less precise
 ***/
class Completion {
    public static void main(String... args) {
    	// Set the Open AI Token & Model
        String token = "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";
        String model = "babbage-002";
        
        // service handle for calling OpenAI APIs
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

        System.out.println("-----------------------------------------------------------");      
        System.out.println("Creating 3 completions with NO Hallunications...\n");
        
        // prompt - change this and run again and again. Mostly ChatGPT will hallucinate as the temperature (precision) is set to .2
        // ChatGPT will respond with 3 messages as n=3
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model(model)
                .prompt("The earth goes around the sun")
                .echo(true)
                .user("testing")
                .temperature(0.2)
                .n(3)
                .maxTokens(20)
                .build();
        
        // call ChatGPT Completion API and get the response
        service.createCompletion(completionRequest).getChoices().forEach( (c) -> { 
        	System.out.println(c.getText());
			System.out.println("++++++++++++++");	
        });
        
        // prompt - Same prompt again and ChatGPT will NOT hallucinate as the temperature (precision) is set to .2
        // ChatGPT will respond with 3 messages as n=3
        System.out.println("-----------------------------------------------------------");	
        System.out.println("Creating 3 completion WITH Hallunications...\n");
        completionRequest = CompletionRequest.builder()
                .model(model)
                .prompt("The earth goes around the sun")
                .echo(true)
                .user("testing")
                .temperature(0.9)
                .n(3)
                .maxTokens(20)
                .build();
        
        // call ChatGPT Completion API and get the response
        service.createCompletion(completionRequest).getChoices().forEach( (c) -> { 
        	System.out.println(c.getText());
        	System.out.println("++++++++++++++");	
        });

        service.shutdownExecutor();
    }
}
