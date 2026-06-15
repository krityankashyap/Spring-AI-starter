package com.example.SpringAI_Starter.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
// import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.MediaType;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.ai.advisor.QuestionAnsweringAdvisor;



@RestController
@RequestMapping("/api/rag")
public class RagController {

   private final ChatClient chatClient;
   private final VectorStore vectorStore;

    public RagController(ChatClient.Builder builder, VectorStore vectorStore){
      this.vectorStore= vectorStore;
      this.chatClient= builder
            .defaultSystem("""
            You are a financial analyst assistant. You answer questions about company
            quarterly earnings reports using only the provided context. Provide
            comprehensive, detailed answers covering all relevant information from the
            context — including revenue, profit, margins, growth, guidance, key highlights,
            risks, and any other details present. If the context does not contain enough
            information, say so clearly. Always cite which company and quarter the data
            comes from.
        """)
        .build();
    }

  

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Accepts multipart/form-data for file uploads
  public Map<String, Object> uploadDocuments(@RequestParam("files") List<MultipartFile> files) throws IOException{

    // Now we have to tokenize the documents and store them in a vector database for retrieval.
    int totalChunks= 0;

    List<String> processes= new ArrayList<>();

    TokenTextSplitter splitter= TokenTextSplitter.builder() // Configure the text splitter with desired parameters
    .withChunkSize(512)
    .withMaxNumChunks(10000)
    .withMinChunkLengthToEmbed(500)
    .withMinChunkSizeChars(500)
    .build();

    for(MultipartFile file: files){
        String fileName= file.getOriginalFilename();  // Get the original file name

        TikaDocumentReader reader= new TikaDocumentReader(new InputStreamResource(file.getInputStream()));   // Configure the TikaDocumentReader to read the uploaded file 

        List<Document> rawDocs= reader.read(); // Read the document and get the raw text content

        rawDocs.forEach(doc-> doc.getMetadata().put("source", fileName)); // Add metadata to each document with the source file name

        List<Document> chunks= splitter.split(rawDocs); // Split the raw documents into smaller chunks using the configured text splitter

        vectorStore.add(chunks);  // Add the processed chunks to the vector store for later retrieval during question answering
        totalChunks+= chunks.size(); // Keep track of the total number of chunks processed
        processes.add(fileName); // Add the file name to the list of processed files for reporting back to the user
    }
    

    return Map.of("totalChunks" , totalChunks, "Processed", processes);  // Return a response containing the total number of chunks processed and the list of processed file names to the client
        
  }
 
  // @GetMapping("/ask")
  // public String askQuestion(@RequestParam String question){
  //   QuestionAnswerAdvisor advisor= QuestionAnswerAdvisor.builder()
  //   .vectorStore(vectorStore)
  //   .searchRequest(SearchRequest.builder().similarityThreshold(0.4).topK(15).build())
  //   .build();

  //   String response= chatClient.prompt()
  //   .advisors(advisor) // Use the QuestionAnsweringAdvisor to retrieve relevant context from the vector store
  //   .user(question)
  //   .call()
  //   .content();

  //   return response;  
  // }
  
}
