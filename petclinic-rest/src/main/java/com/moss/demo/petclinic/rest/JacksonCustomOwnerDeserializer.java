package com.moss.demo.petclinic.rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.moss.demo.petclinic.model.Owner;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JacksonCustomOwnerDeserializer extends StdDeserializer<Owner> {

    public JacksonCustomOwnerDeserializer(){
        this(null);
    }

    public JacksonCustomOwnerDeserializer(Class<Owner> t) {
        super(t);
    }


    @Override
    public Owner deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Owner owner = new Owner();
        int id = node.get("id").asInt();
        String firstName = node.get("firstName").asText(null);
        String lastName = node.get("lastName").asText(null);
        String address = node.get("address").asText(null);
        String city = node.get("city").asText(null);
        String telephone = node.get("telephone").asText(null);

        if (!(id == 0)) {
            owner.setId(id);
        }
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setAddress(address);
        owner.setCity(city);
        owner.setTelephone(telephone);
        return owner;
    }
}
