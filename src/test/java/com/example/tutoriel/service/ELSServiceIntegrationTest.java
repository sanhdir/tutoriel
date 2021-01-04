package com.example.tutoriel.service;

import com.example.tutoriel.elasticsearch.documents.CarBrand;
import com.example.tutoriel.elasticsearch.documents.CarModel;
import com.example.tutoriel.elasticsearch.repository.CarBrandRepository;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@SpringBootTest
@TestConfiguration("ElasticSearchConfig.class")
@RunWith(SpringRunner.class)
@Slf4j
public class ELSServiceIntegrationTest {

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
        if (!elsRestTemplate.indexOps(IndexCoordinates.of(indexName)).exists()) {
            log.info("creating index " + indexName);
            elsRestTemplate.indexOps(IndexCoordinates.of(indexName)).create();
        }
        carBrandRepository.save(carBrand);
    }

    @After
    public void tearDown() {
        if (elsRestTemplate.indexOps(IndexCoordinates.of(indexName)).exists()) {
            log.info("deleting index " + indexName);
            elsRestTemplate.indexOps(IndexCoordinates.of(indexName)).delete();
        }
    }

    @Test
    public void givenCarBrandName_whenRunFindByCarBrandName_thenCorrespondingDocsWereFound() {
        Page<CarBrand> carBrand = carBrandRepository.findByCarBrandName("Audi", Pageable.unpaged());
        Assert.assertEquals("Audi", carBrand.getContent().stream().findFirst().get().getCarBrandName());
    }
    @Test
    public void givenCarBrandName_whenRunFindByCarModelName_thenCorrespondingDocsWereFound() {
        Page<CarBrand> carBrand = carBrandRepository.findByCarModelName("a1", Pageable.unpaged());
        Assert.assertEquals("Audi", carBrand.getContent().stream().findFirst().get().getCarBrandName());
    }

    @Test
    public void givenCarBrandName_whenRunSearchByCarBrandName_thenCorrespondingDocsWereFound() {
        Query query = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("carBrandName", "Audi"))
                .build();
        SearchHits<CarBrand> carBrandSearchHits = elsRestTemplate.search(query, CarBrand.class, IndexCoordinates.of(indexName));
        Assert.assertEquals("Audi", carBrandSearchHits.getSearchHits().stream().findFirst().get().getContent().getCarBrandName());
    }

    @Test
    public void givenCarBrandName_whenRunSearchByCarModelName_thenCorrespondingDocsWereFound() {
        Query query = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("carModelList.carModelName", "a3"))
                .build();
        SearchHits<CarBrand> carBrandSearchHits = elsRestTemplate.search(query, CarBrand.class, IndexCoordinates.of(indexName));
        Assert.assertEquals("Audi", carBrandSearchHits.getSearchHits().stream().findFirst().get().getContent().getCarBrandName());
    }

}
