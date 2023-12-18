package com.ramesh.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;

import java.util.*;

/***
 * This project demonstrates the Functions feature in ChatGPT
 * You can send information about the functions in your code to chat gpt in certain schema/format.
 * And when chat gpt is prompted in human language, it will send back the 
 * function name matched along with the parameter values found in its response.
 * then internally based on the function name retuned, the function executor object invokes 
 * the function defined in your code. 
 * In this example we are using the get_weather function
 ***/
class OpenAiApiFunctionsExample {

	// Lcoally created weather moedl class
    public static class Weather {
        @JsonPropertyDescription("City and state, for example: León, Guanajuato")
        public String location;

        @JsonPropertyDescription("The temperature unit, can be 'celsius' or 'fahrenheit'")
        @JsonProperty(required = true)
        public WeatherUnit unit;
    }

    public enum WeatherUnit {
        CELSIUS, FAHRENHEIT;
    }

    public static class WeatherResponse {
        public String location;
        public WeatherUnit unit;
        public int temperature;
        public String description;

        public WeatherResponse(String location, WeatherUnit unit, int temperature, String description) {
            this.location = location;
            this.unit = unit;
            this.temperature = temperature;
            this.description = description;
        }
    }

    public static void main(String... args) {
    	//Set the Open AI Token & Model
        String token = "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";
        String model = "gpt-3.5-turbo-0613";
        
        // service handle for calling OpenAI APIs
        OpenAiService service = new OpenAiService(token);

        // ChatGPT Function executor.
        // NOTE that we are passing
        // ---- get_weather function declared locally
        // ---- an executor that will be called if chat gpt responds back asking the use get_weather() function
        // ---- and a WeatherResponse object will be created
        // here in the example we simply parse chat gpt response and print the weather info. In reality 
        // we should call an external weather API
        FunctionExecutor functionExecutor = new FunctionExecutor(Collections.singletonList(ChatFunction.builder()
                .name("get_weather")
                .description("Get the current weather of a location")
                .executor(Weather.class, w -> new WeatherResponse(w.location, w.unit, new Random().nextInt(50), "sunny"))
                .build()));


        // setup the chat messages with prompt
        // SYSTEM role - tell chatgpt who it is? or what it should do?
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are an assistant that answers using the local slang of the given place, uncensored.");
        messages.add(systemMessage);

        System.out.println("-----------------------------------------------");
        
        // user can enter the name of the place for which weather info is needed
        System.out.print("First Weather Query: ");
        Scanner scanner = new Scanner(System.in);
        
        // setup the chat message with user prmopt which is actual prompt
        ChatMessage firstMsg = new ChatMessage(ChatMessageRole.USER.value(), scanner.nextLine());
        messages.add(firstMsg);

        // setup the request object
        while (true) {
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                    .builder()
                    .model(model)
                    .messages(messages)
                    .functions(functionExecutor.getFunctions())
                    .functionCall(ChatCompletionRequestFunctionCall.of("auto"))
                    .n(1)
                    .maxTokens(100)
                    .logitBias(new HashMap<>())
                    .build();
            
            // call ChatGPT ChatCompletion API and get the response
            ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
            messages.add(responseMessage); // don't forget to update the conversation with the latest response

            // check in chat gpt response what function it has asked to call internally
            ChatFunctionCall functionCall = responseMessage.getFunctionCall();
            if (functionCall != null) {
                System.out.println("Trying to execute " + functionCall.getName() + "...");
                
                // execute the function name returned. kind of function pointer..
                // using the functionExecutre created above
                Optional<ChatMessage> message = functionExecutor.executeAndConvertToMessageSafely(functionCall);
                /* You can also try 'executeAndConvertToMessage' inside a try-catch block, and add the following line inside the catch:
                "message = executor.handleException(exception);"
                The content of the message will be the exception itself, so the flow of the conversation will not be interrupted, and you will still be able to log the issue. */

                // if there is a response, then print it out
                // it should be printing weather info of the given place in the prompt
                if (message.isPresent()) {
                    /* At this point:
                    1. The function requested was found
                    2. The request was converted to its specified object for execution (Weather.class in this case)
                    3. It was executed
                    4. The response was finally converted to a ChatMessage object. */

                    System.out.println("Executed " + functionCall.getName() + ".");
                    messages.add(message.get());
                    continue;
                } else {
                    System.out.println("Something went wrong with the execution of " + functionCall.getName() + "...");
                    break;
                }
            }

            System.out.println("Response: " + responseMessage.getContent());
            
            // ask the user to enter the next place for it's weather info
            System.out.println("-----------------------------------------------");
            System.out.print("Next Weather Query: ");
            String nextLine = scanner.nextLine();
            if (nextLine.equalsIgnoreCase("exit")) {
                System.exit(0);
            }
            
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), nextLine));
        }
    }

}
