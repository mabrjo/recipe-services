package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.CollectionDTO;
import com.mcccodeschool.recipeservices.dto.Recipe2DTO;
import com.mcccodeschool.recipeservices.repository.Recipe2Repository;
import com.mcccodeschool.recipeservices.model.Collection;
import com.mcccodeschool.recipeservices.model.Recipe2;
import com.mcccodeschool.recipeservices.repository.CollectionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final ModelMapper modelMapper;
    private final Recipe2Repository recipe2Repository;


    public CollectionService(CollectionRepository collectionRepository, ModelMapper modelMapper, Recipe2Repository recipe2Repository){
        this.collectionRepository=collectionRepository;
        this.modelMapper=modelMapper;
        this.recipe2Repository=recipe2Repository;
    }

    public CollectionDTO createCollection(CollectionDTO collectionDTO){
        Collection c = modelMapper.map(collectionDTO,Collection.class);

        c=collectionRepository.save(c);
        return modelMapper.map(c,CollectionDTO.class);
    }

    public CollectionDTO getCollectionById(Long id) {
        Optional<Collection> r = collectionRepository.findById(id);
        Collection c = r.get();
        CollectionDTO dto= modelMapper.map(c, CollectionDTO.class);
        return dto;
    }

    public List<CollectionDTO> getCollectionByUser(Long userId) {
        Optional<List <Collection>> r = collectionRepository.findByUserId(userId);
        List<Collection> c = r.get();
        List<CollectionDTO> cDTO = new ArrayList<>();
        for(Collection collection:c){
            CollectionDTO dto= modelMapper.map(collection, CollectionDTO.class);
            cDTO.add(dto);
        }
        return cDTO;
    }

    public CollectionDTO addRecipe2Collection(Recipe2DTO recipe2DTO, Long collectionId) {
        Optional<Recipe2> recipe2 = recipe2Repository.findById(recipe2DTO.getId());
        Recipe2 r2 = recipe2.get();
        Optional<Collection> r = collectionRepository.findById(collectionId);
        Collection c = r.get();
        List<Recipe2> recipeList= c.getRecipeList();
        recipeList.add(r2);
        c.setRecipeList(recipeList);
        collectionRepository.save(c);
        CollectionDTO dto= modelMapper.map(c, CollectionDTO.class);
        return dto;
    }

    public void makeDefaultCollection(Long userId) {
        CollectionDTO favorites = new CollectionDTO();
        favorites.setCollectionName("Favorites");
        favorites.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/6/67/Favorite_Icon.png");
        System.out.println(favorites.getImageUrl());
        favorites.setUserId(userId);
        Collection coll= modelMapper.map(favorites, Collection.class);
        collectionRepository.save(coll);
    }
}
