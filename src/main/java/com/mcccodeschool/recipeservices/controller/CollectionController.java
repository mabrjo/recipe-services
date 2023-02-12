package com.mcccodeschool.recipeservices.controller;


import com.mcccodeschool.recipeservices.dto.CollectionDTO;
import com.mcccodeschool.recipeservices.dto.Recipe2DTO;
import com.mcccodeschool.recipeservices.model.Recipe;
import com.mcccodeschool.recipeservices.repository.RecipeRepository;
import com.mcccodeschool.recipeservices.service.CollectionService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

@Controller
@RestController
@RequestMapping("/api/profile/collections")
@CrossOrigin
public class CollectionController {

    private final CollectionService collectionService;
    private Recipe2DTO recipe2DTO;

    public CollectionController(CollectionService collectionService) { this.collectionService = collectionService; }

    @GetMapping("/{id}")
    public CollectionDTO getCollectionById(@PathVariable Long id) {return collectionService.getCollectionById(id);}

    @GetMapping("/user/{userId}")
    public List<CollectionDTO> getCollectionByUser(@PathVariable Long userId){return collectionService.getCollectionByUser(userId);}

    @PostMapping("/user/{userId}")
    public CollectionDTO postCollectionById(@RequestBody CollectionDTO collectionDTO) {return collectionService.createCollection(collectionDTO);}

    @PostMapping("/{collectionId}")
    public CollectionDTO postRecipeToCollection(@RequestBody Recipe2DTO recipe2DTO, @PathVariable Long collectionId) {
        return collectionService.addRecipe2Collection(recipe2DTO, collectionId);
    }

    @GetMapping("/deploy")
    public String getDeployInfo() {
        String result="DEFAULT STUFF\n";
        try {
            String fn = "deploy.txt";
            InputStream is = (new ClassPathResource(fn)).getInputStream();

            String line;
            LineNumberReader in = new LineNumberReader(
                    new InputStreamReader(is));
            while (null != (line = in.readLine())) {
                result += line + "\n"; // slow  ya know
            }
            in.close();
        } catch (Exception bland) {
            bland.printStackTrace();
        }
        return String.format("<pre>%s</pre>", result);
    }

}
