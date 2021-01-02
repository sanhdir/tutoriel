package com.example.tutoriel.elasticsearch.repository;

import com.example.tutoriel.elasticsearch.documents.CarBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarBrandRepository extends ElasticsearchRepository<CarBrand,String> {

    Page<CarBrand> findByCarBrandName(String name, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"carModelList.carModelName\": \"?0\"}}]}}")
    Page<CarBrand> findByCarModelName(String name, Pageable pageable);



}
