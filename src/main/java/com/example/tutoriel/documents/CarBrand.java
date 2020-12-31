package com.example.tutoriel.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "car_brands", replicas = 0)
@Getter
@Setter
public class CarBrand {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String carBrandName;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<CarModel> carModelList;
}
