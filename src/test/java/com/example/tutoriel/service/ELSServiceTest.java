package com.example.tutoriel.service;

import com.example.tutoriel.documents.CarBrand;
import com.example.tutoriel.documents.CarModel;
import com.example.tutoriel.repository.CarBrandRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestConfiguration("ElasticSearchConfig.class")
public class ELSServiceTest {

    @Autowired
    ElasticsearchRestTemplate elsRestTemplate;

    @Autowired
    RestHighLevelClient elasticsearchClient;

    @Autowired
    CarBrandRepository carBrandRepository;

    private final static String indexName = "car_brands";


    @Before
    public void setUp() {
        CarModel carModelAudi1 = new CarModel("a1");
        CarModel carModelAudi2 = new CarModel("a2");
        CarModel carModelAudi3 = new CarModel("a3");
        CarModel carModelAudi4 = new CarModel("a4");
        CarBrand carBrand = new CarBrand();
        carBrand.setCarBrandName("Audi");
        carBrand.setCarModelList(Arrays.asList(carModelAudi1, carModelAudi2, carModelAudi3, carModelAudi4));
        carBrandRepository.save(carBrand);
    }

    @After
    public void tearDown() {
        elsRestTemplate.indexOps(IndexCoordinates.of(indexName)).delete();
    }

    @Test
    public void givenCarBrandName_whenRunFindByCarBrandName_thenCorrespondingDocIsFound() {
        Page<CarBrand> carBrand = carBrandRepository.findByCarBrandName("Audi", Pageable.unpaged());
        Assert.assertEquals("Audi", carBrand.getContent().stream().findFirst().get().getCarBrandName());
    }

}
