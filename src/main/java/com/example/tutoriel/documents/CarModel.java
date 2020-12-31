package com.example.tutoriel.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
public class CarModel {

    @Field(type= FieldType.Text)
    private String carModelName;

    public  CarModel(String carModelName){
        this.carModelName = carModelName;
    }

}
