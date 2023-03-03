package com.nicorego.nhs.pocapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nicorego.nhs.pocapi.model.Hospital;
import org.apache.commons.lang3.StringUtils;

public class JsonMapper {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectNode getHospitalJson(Hospital hospital) {
        ObjectNode hospitalJson = mapper.createObjectNode();

        try {
            hospitalJson.put("id", hospital.getId() != null ? hospital.getId() : 0);
            hospitalJson.put("name", StringUtils.defaultString(hospital.getName()));
            hospitalJson.put("latitude",  hospital.getLatitude() != null ? hospital.getLatitude() : 0);
            hospitalJson.put("longitude", hospital.getLongitude() != null ? hospital.getLongitude() : 0);
            hospitalJson.put("free_beds", hospital.getFreeBeds() != null ? hospital.getFreeBeds() : 0);
            hospitalJson.put("context_message", StringUtils.defaultString(hospital.getContextMessage()));
        } catch (NullPointerException e) {
            hospitalJson.put("error", "Hospital object contains null values");
            // log the exception
        } catch (Exception e) {
            hospitalJson.put("error", e.getMessage());
            // log the exception
        }
        return hospitalJson;
    }
}
