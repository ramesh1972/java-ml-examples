package com.ramesh.langchain;

import static dev.langchain4j.data.document.FileSystemDocumentLoader.loadDocument;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.joining;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

/***
 * This project demonstrates how to use LangChain embeddings and receive LLM response based 
 * on the relevant embeddings of the input prompt
 * Note that no LangChain chain is created here. So the response comes in one single shot
 */
public class ChatWithDocumentLive {
	
	// Open AI Key and Chat GPT Model to use
	public static String OPENAI_API_KEY = "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";
	public static String OPENAI_MODEL = "gpt-3.5-turbo";

    public static void main(String[] args) {
  
    	System.out.println("Loading sample document and splitting into words...");
    	
    	// loading input document and splitting into segments
    	Document document = loadDocument(".\\simpsons_adventures.txt");
        DocumentSplitter splitter = DocumentSplitters.recursive(100, 0, new OpenAiTokenizer(OPENAI_MODEL));
        List<TextSegment> segments = splitter.split(document);

        // Generating embeddings for the words in document and storing in memory
        System.out.println("Generating embeddings for the words in document and storing in memory...");
        EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();
        List<Embedding> embeddings = embeddingModel.embedAll(segments)
            .content();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);

        // Generating embeddings for prompt \"Who is Simpson?\
        System.out.println("\nGenerating embeddings for prompt \"Who is Simpson?\"");
        String question = "Who is Simpson?";
        Embedding questionEmbedding = embeddingModel.embed(question)
            .content();
        int maxResults = 3;
        double minScore = 0.7;
        
        // Getting relavant embeddings or words for prompt \"Who is Simpson?\" from the embeddings stored in memory")
        System.out.println("Getting relavant embeddings or words for prompt \"Who is Simpson?\" from the embeddings stored in memory");
        List<EmbeddingMatch<TextSegment>> relevantEmbeddings = embeddingStore.findRelevant(questionEmbedding, maxResults, minScore);

        // Sending relevant embeddings and prompt \"Who is Simpson?\" to chat gpt
        System.out.println("Sending relevant embeddings and prompt \"Who is Simpson?\" to chat gpt");

        // creating a LangChain PromptTemplate
        PromptTemplate promptTemplate = PromptTemplate.from("Answer the following question to the best of your ability:\n" + "\n" + "Question:\n" + "{{question}}\n" + "\n" + "Base your answer on the following information:\n" + "{{information}}");

        // streaming the responses from chatGPT and joining in the end
        String information = relevantEmbeddings.stream()
            .map(match -> match.embedded()
                .text())
            .collect(joining("\n\n"));

        Map<String, Object> variables = new HashMap<>();
        variables.put("question", question);
        variables.put("information", information);

        Prompt prompt = promptTemplate.apply(variables);
        
        // creating the chatmodel
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
            .apiKey(OPENAI_API_KEY)
            .timeout(ofSeconds(60))
            .build();
        
        // calling chatgpt and generating the response
        AiMessage aiMessage = chatModel.generate(prompt.toUserMessage())
            .content();

        System.out.println("response from ChatGPT for prompt \"Who is Simpson?\"\n");

        System.out.println(aiMessage.text());
    }

}
