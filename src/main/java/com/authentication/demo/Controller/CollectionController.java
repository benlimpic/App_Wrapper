package com.authentication.demo.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.authentication.demo.Exceptions.CollectionCreationException;
import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Service.CollectionService;

@Controller
public class CollectionController {

    private final CollectionService collectionService;
    private final CollectionRepository collectionRepository;

    public CollectionController(@Lazy CollectionService collectionService, CollectionRepository collectionRepository) {
        this.collectionService = collectionService;
        this.collectionRepository = collectionRepository;
    }

    @PostMapping("/create_collection")
    public String postCollection(
        @RequestParam("collectionTitle") String title,
        @RequestParam(value = "collectionCaption", required = false) String caption,
        @RequestParam("collectionImage") MultipartFile collectionImage,
        RedirectAttributes redirectAttributes) throws IOException {

        System.out.println("▶️ POST /create_collection hit");
        System.out.println("Title: " + title);
        System.out.println("Caption: " + caption);
        System.out.println("File name: " + (collectionImage != null ? collectionImage.getOriginalFilename() : "null"));
        System.out.println("File empty? " + (collectionImage == null || collectionImage.isEmpty()));

        if (title == null || title.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Title is required");
            System.out.println("❌ Title is missing");
            return "redirect:/create-collection";
        }

        if (collectionImage == null || collectionImage.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Image is required");
            System.out.println("❌ Image is missing");
            return "redirect:/create-collection";
        }

        try {
            Map<String, String> collectionDetails = Map.of(
                "collectionTitle", title,
                "collectionCaption", caption != null ? caption : ""
            );

            collectionService.createCollection(collectionDetails, collectionImage);
            redirectAttributes.addFlashAttribute("message", "Collection created successfully");
            System.out.println("✅ Collection created. Redirecting to /profile");
            return "redirect:/profile";
        } catch (Exception e) {
            System.err.println("❌ Error creating collection: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/create-collection";
        }
    }




    // UPDATE COLLECTION
    @PostMapping("/update-collection/{id}")
    public String updateCollection(
            @PathVariable("id") Long collectionId,
            @RequestParam Map<String, String> params,
            @RequestParam(required = false) MultipartFile collectionImage,
            RedirectAttributes redirectAttributes) throws IOException {
        try {
            params.put("id", String.valueOf(collectionId));
            collectionService.updateCollection(params, collectionImage);
            redirectAttributes.addFlashAttribute("message", "Collection updated successfully");
        } catch (CollectionCreationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/update-collection/" + collectionId;
        }
        redirectAttributes.addFlashAttribute("message", "Collection updated successfully");
        return "redirect:/collection/" + collectionId;
    }

    
    @PostMapping("/delete-collection/{id}")
    public String deleteCollection(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        collectionService.deleteCollection(id);

        redirectAttributes.addFlashAttribute("message", "Collection deleted successfully");
        return "redirect:/profile";
    }


    @GetMapping("/search-collections")
    public ResponseEntity<Map<String, Object>> searchCollections(@RequestParam("query") String query) {
        List<CollectionModel> collections = collectionRepository.findByTitleContainingIgnoreCase(query);
        Map<String, Object> response = new HashMap<>();
        response.put("collections", collections);
        return ResponseEntity.ok(response);
    }

}
