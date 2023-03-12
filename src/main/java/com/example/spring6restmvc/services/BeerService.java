package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<Beer> listBeers();

    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void updateBeer(UUID beerId, Beer beer);

    void deleteBeer(UUID beerId);

    void patchBeer(UUID beerId, Beer beer);
}
