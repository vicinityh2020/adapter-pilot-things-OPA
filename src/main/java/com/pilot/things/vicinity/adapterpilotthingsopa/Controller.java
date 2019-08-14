package com.pilot.things.vicinity.adapterpilotthingsopa;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import com.pilot.things.vicinity.adapterpilotthingsopa.exception.AdreamAPIException;
import com.pilot.things.vicinity.adapterpilotthingsopa.service.AdapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adapter")
public class Controller {

    public static final String TEST = "/test";

    public static final String OBJECTS = "/objects";
    public static final String DISCO = "/disco";

    public static final String GET_PROPERTY = "/objects/{oid}/properties/{pid}";
    public static final String GET_CUSTOM_PROPERTY = "/custom/{oid}/x";

    public static final String SET_CUSTOM_PROPERTY = "/custom-set/{oid}/y";

    public static final String POST_ACTION = "/objects/{oid}/actions/{aid}";
    public static final String POST_CUSTOM_ACTION = "/custom-post/{oid}/z";

    public static final String EVENT_LISTENER = "/objects/{iid}/publishers/{oid}/events/{eid}";
    public static final String EVENT_PUBLISHER = "/objects/{iid}/events/{eid}/publish";

    public static final String SLEEP = "/sleep";

    private final static Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private final AdapterService adapterService;

    public Controller(AdapterService adapterService) {this.adapterService = adapterService;}

    @GetMapping(TEST)
    public ResponseEntity getTest(){
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = DISCO,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity discoverThings(){
        LOGGER.debug("discovery");

        return ResponseEntity.ok(this.adapterService.discoverThings());
    }

    @GetMapping(value = OBJECTS,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getObjects(){
        LOGGER.debug("get objects");

        return ResponseEntity.ok(this.adapterService.getAdapter());
    }

    @GetMapping(value = GET_PROPERTY,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCustomProperty(
            @PathVariable(value ="oid") String thingId,
            @PathVariable(value = "pid") String propertyId
    ) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, AdreamAPIException {

        if(thingId.equals("adream-building") &&
            propertyId.equals("ADREAM-Production")
        ){
            return ResponseEntity.ok(this.adapterService.getData().getValue());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}
