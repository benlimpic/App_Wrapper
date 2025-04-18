package com.authentication.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.ListUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Model.ItemModel;
import com.authentication.demo.Model.UserModel;
import com.authentication.demo.Repository.UserRepository;
import com.authentication.demo.Service.CollectionService;
import com.authentication.demo.Service.ItemService;
import com.authentication.demo.Service.UserService;

@Controller
public class ContentController {

    private final UserRepository repository;
    private final UserService userService;
    private final CollectionService collectionService;
    private final ItemService itemService;

    public ContentController(UserRepository repository, UserService userService, CollectionService collectionService,
            ItemService itemService) {
        this.repository = repository;
        this.userService = userService;
        this.collectionService = collectionService;
        this.itemService = itemService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/index")
    public String index(Model model) {

        // Fetch Users
        Map<Long, UserModel> users = userService.getUsersMappedById();
        model.addAttribute("users", users);

        // Fetch all collections
        List<CollectionModel> collections = collectionService.getAllCollections();

        // Partition items for each collection
        Map<Long, List<List<ItemModel>>> partitionedItemsByCollection = new HashMap<>();
        for (CollectionModel collection : collections) {
            List<ItemModel> items = itemService.getAllItemsByCollectionId(collection.getId());
            List<List<ItemModel>> partitionedItems = items != null ? ListUtils.partition(items, 3) : List.of();
            partitionedItemsByCollection.put(collection.getId(), partitionedItems);
        }

        // Add data to the model
        model.addAttribute("collections", collections);
        model.addAttribute("partitionedItemsByCollection", partitionedItemsByCollection);
        model.addAttribute("users", users);

        return handleAuthentication(model, "index");
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        // FETCH SORTED COLLECTIONS BY USER ID
        List<CollectionModel> collections = collectionService.getCollectionsForCurrentUser();

        // BREAK COLLECTIONS INTO GROUPS OF 3
        List<List<CollectionModel>> partitionedCollections = ListUtils.partition(collections, 3);

        // COUNT COLLECTIONS
        int collectionCount = collections.size();

        // ADD TO MODEL
        model.addAttribute("collectionCount", collectionCount);
        model.addAttribute("partitionedCollections", partitionedCollections);
        return handleAuthentication(model, "profile");
    }

    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable("id") Long userId, Model model) {
        // Fetch the user profile
        UserModel userProfile = userService.getUserById(userId);
        if (userProfile == null) {
            return "redirect:/index"; // Handle missing user profile
        } else if (userProfile.getId().equals(userService.getCurrentUserId())) {
            return "redirect:/profile"; // Redirect to own profile
        }

        // Fetch collections for the user
        List<CollectionModel> collections = collectionService.getCollectionsByUserId(userId);
        // Partition collections into rows of 3 for display
        List<List<CollectionModel>> partitionedCollections = ListUtils.partition(collections, 3);
        // Count Collections
        int collectionCount = collections.size();
        // Add the attributes to model
        model.addAttribute("collectionCount", collectionCount);
        model.addAttribute("partitionedCollections", partitionedCollections);
        model.addAttribute("userProfile", userProfile);

        return handleAuthentication(model, "userProfile");
    }

    @GetMapping("/update-profile")
    public String updateProfile(Model model) {
        return handleAuthentication(model, "updateProfile");
    }

    @GetMapping("/update-user-details")
    public String updateUserDetails(Model model) {
        return handleAuthentication(model, "updateUserDetails");
    }

    @GetMapping("/create-collection")
    public String createCollection(Model model) {
        return handleAuthentication(model, "createCollection");
    }

    @GetMapping("/collection/{id}")
    public String collection(@PathVariable("id") Long collectionId, Model model) {
        // Fetch the collection by ID
        CollectionModel collection = collectionService.getCollectionById(collectionId);
        if (collection == null) {
            return "redirect:/profile"; // Redirect if the collection is not found
        }

        // Fetch items for the collection
        List<ItemModel> items = itemService.getAllItemsByCollectionId(collectionId);

        // Partition items into rows of 3 for display
        List<List<ItemModel>> partitionedItems = items != null ? ListUtils.partition(items, 3) : List.of();

        // Add data to the model
        model.addAttribute("collection", collection);
        model.addAttribute("partitionedItems", partitionedItems);

        return handleAuthentication(model, "collection");
    }

    @GetMapping("/create-item/{collectionId}")
    public String createItem(@PathVariable("collectionId") Long collectionId, Model model) {
        // Add the collectionId to the model
        model.addAttribute("collectionId", collectionId);
        return handleAuthentication(model, "createItem");
    }

    @GetMapping("/item/{id}")
    public String getItem(@PathVariable("id") Long itemId, Model model) {
        // Fetch the item by ID
        ItemModel item = itemService.getItemById(itemId);
        if (item == null) {
            return "redirect:/profile"; // Redirect if the item is not found
        }

        // Fetch the collection associated with the item
        CollectionModel collection = item.getCollection();
        if (collection == null) {
            return "redirect:/profile"; // Redirect if the collection is not found
        }

        // Add the item and collection to the model
        model.addAttribute("item", item);
        model.addAttribute("collection", collection);

        return handleAuthentication(model, "item");
    }

    @GetMapping("/update-item/{id}")
    public String updateItem(@PathVariable("id") Long itemId, Model model) {
        ItemModel item = itemService.getItemById(itemId);
        if (item == null) {
            return "redirect:/profile"; // Handle missing item
        }
        model.addAttribute("item", item);
        return handleAuthentication(model, "updateItem");
    }

    @GetMapping("/update-collection/{id}")
    public String updateCollection(@PathVariable("id") Long collectionId, Model model) {
        // Fetch the collection by ID
        CollectionModel collection = collectionService.getCollectionById(collectionId);
        if (collection == null) {
            return "redirect:/profile"; // Redirect if the collection is not found
        }

        // Add the collection to the model
        model.addAttribute("collection", collection);

        return handleAuthentication(model, "updateCollection");
    }

    private String handleAuthentication(Model model, String viewName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            Optional<UserModel> user = repository.findByUsername(authentication.getName());
            if (user.isPresent()) {
                UserModel userModel = user.get();
                model.addAttribute("user", userModel);
                return viewName;
            }
        }
        return "redirect:/login";
    }

}
