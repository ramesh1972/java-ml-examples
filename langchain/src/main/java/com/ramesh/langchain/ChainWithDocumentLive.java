package com.ramesh.langchain;

import static dev.langchain4j.data.document.FileSystemDocumentLoader.loadDocument;
import static java.time.Duration.ofSeconds;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

/***
 * This project demonstrates how to use LangChain to ingest data from a document and 
 * get responses for prompts from the same, by creating a LangChain Chain
 */
public class ChainWithDocumentLive {

	// Open AI Key and Chat GPT Model to use
	public static String OPENAI_API_KEY = "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";
	public static String OPENAI_MODEL = "gpt-3.5-turbo";

    public static void main(String[] args) {
    	
    	// embedding model to yse
        EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

        // embeddings will be stored in memory
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        //Creating instance of EmbeddingStoreIngestor
        System.out.println("Creating instance of EmbeddingStoreIngestor...");
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
            .documentSplitter(DocumentSplitters.recursive(500, 0))
            .embeddingModel(embeddingModel)
            .embeddingStore(embeddingStore)
            .build();

        // ingesting input data
        System.out.println("Loading content from simpsons_adventures.txt and ingesting...");
        Document document = loadDocument(".\\simpsons_adventures.txt");
        ingestor.ingest(document);

        // building the chat model
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
            .apiKey(OPENAI_API_KEY)
            .timeout(ofSeconds(60))
            .build();

        // Building LangChain with Embeddings Retriever
        System.out.println("Building LangChain with Embeddings Retriever...");
        ConversationalRetrievalChain chain = ConversationalRetrievalChain.builder()
            .chatLanguageModel(chatModel)
            .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .promptTemplate(PromptTemplate.from("Answer the following question to the best of your ability: {{question}}\n\nBase your answer on the following information:\n{{information}}"))
            .build();

        // prompting ChatGPT
        System.out.println("Prompting ChatGPT \"Who is Simpson?\"...");
        System.out.println("\nFetching response from ChatGPT via the created LangChain...\n");

        // executing the LangChain chain
        String answer = chain.execute("Who is Simpson?");

        System.out.println(answer);

    }

}
