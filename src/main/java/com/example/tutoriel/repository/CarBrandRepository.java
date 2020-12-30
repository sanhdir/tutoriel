package com.example.tutoriel.repository;

import com.example.tutoriel.documents.CarBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarBrandRepository extends ElasticsearchRepository<CarBrand,String> {

    Page<CarBrand> findByCarBrandName(String name, Pageable pageable);

}
