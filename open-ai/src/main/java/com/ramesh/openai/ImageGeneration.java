package com.ramesh.openai;

import java.time.Duration;

import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;

/***
 * This project demonstrates how to generate images based on prompt
 * Chat gpt uses it DALL-E model to create the image
 * the image is create and automatically stored in Azure Blob storage, 
 * the URL for which is returned by the Chat GPT API
 ***/
class OpenAiApiExample {
    public static void main(String... args) {
    	
    	//Set the Open AI Token & Model
        String token = "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";
        
        // service handle for calling OpenAI APIs
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

        // Generate an image
        // NOTE: change the prompt below and run again and again
        System.out.println("\nCreating Image of a A cow breakdancing with a turtle...");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt("A cow breakdancing with a turtle")
                .build();

        System.out.println("--------------------------------------------------------");
        System.out.println("\nImage is located at:");
        
        // get the URL of the generated image. It will stored in Azure Blob Storage
        // copy the imag eURL from output window and paste it in the browser to see the image
        System.out.println(service.createImage(request).getData().get(0).getUrl());
        
        service.shutdownExecutor();
    }
}
