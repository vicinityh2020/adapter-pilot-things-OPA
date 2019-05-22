package com.pilot.things.vicinity.adapterpilotthingsopa.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.*;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.pilot.things.vicinity.adapterpilotthingsopa.config.AdapterConfig;
import com.pilot.things.vicinity.adapterpilotthingsopa.config.ApiConfig;
import com.pilot.things.vicinity.adapterpilotthingsopa.data.BuildingConsumption;
import com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity.*;
import com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity.schema.BooleanSchema;
import com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity.schema.StringSchema;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class AdapterService {

    private static final Logger LOGGER = getLogger(AdapterService.class);

    private final ApiConfig config;
    private final AdapterConfig aConfig;

    private final List<Thing> things;

    public AdapterService(ApiConfig config,AdapterConfig aConfig) {
        this.config = config;
        this.aConfig = aConfig;

        this.things = new ArrayList<>();

        this.initThings();
    }

    void initThings(){
        List<Field> fields = Collections.singletonList(
                Field.builder()
                     .name("production")
                     .schema(StringSchema.builder().build())
                     .build()
        );

        List<Field> eventFields = Collections.singletonList(
                Field.builder()
                     .name("state")
                     .schema(BooleanSchema.builder().build())
                     .build()
        );

        ObjectOutput objectOutput = ObjectOutput.ObjectOutputBuilder()
                                                .field(fields)
                                                .build();

        ObjectOutput eventOutput = ObjectOutput.ObjectOutputBuilder()
                                               .description("ON = true OFF = false")
                                               .field(eventFields)
                                               .build();

        ReadLink readLink = ReadLink.builder()
                                    .output(objectOutput)
                                    .build();



        Thing opa = Thing.builder()
                         .oid("adream-building")
                         .name("adream building")
                         .type("adapters:PowerMeter")
                         .events(Collections.singletonList(
                                 Event.builder()
                                      .eid("E02-State")
                                      .monitors("adapters:OnOff")
                                      .output(eventOutput)
                                      .build()
                                 ))
                         .actions(new ArrayList<>())
                         .properties(Collections.singletonList(
                                 Property.builder()
                                         .pid("ADREAM-Production")
                                         .monitors("adapters:ActualEnergyConsumedChargingSession")
                                         .readLink(readLink)
                                         .build()
                         ))
                         .build();

        this.things.add(opa);
    }

    public List<Thing> discoverThings(){
        return this.things;
    }

    public Adapter getAdapter(){
        return Adapter.builder()
                      .adapterId(aConfig.getId())
                      .thingDescriptions(this.things)
                      .build();
    }

    public BuildingConsumption getData() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpGet getRequest = new HttpGet(this.config.getUrl());
        HttpClient client = this.createHttpClient_AcceptsUntrustedCerts(config);
        BuildingConsumption result = new BuildingConsumption();

        HttpResponse response = client.execute(this.setHeaders(getRequest, config));

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                                    .setUseHeader(true)
                                    .setStrictHeaders(false)
                                    .setColumnSeparator(',')
                                    .build();

        LOGGER.debug("{}",response.getStatusLine().getStatusCode());

        try (InputStream inputStream = response.getEntity().getContent()) {
            //            String line;
            //            BufferedReader bs = new BufferedReader(new InputStreamReader(inputStream));
            //
            //            while( !(line = bs.readLine()).isEmpty()){
            //                LOGGER.debug(line);
            //            }

            MappingIterator<BuildingConsumption> iterator = mapper.readerFor(BuildingConsumption.class)
                                                                  .with(schema)
                                                                  .readValues(inputStream);

            while(iterator.hasNext()){
                BuildingConsumption data = iterator.next();

                if(data.getName().equals("PHV.SYNT.TGBT.P_ACT")){
                    result.setValue(data.getValue());
                    result.setChrono(data.getChrono());
                }
            }
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return result;
    }

    private HttpGet setHeaders(HttpGet request, ApiConfig config){
        byte[] token = (config.getUsername() + ":" + config.getPassword()).getBytes(StandardCharsets.UTF_8);
        String encoding = Base64.getEncoder()
                                .encodeToString(token);

        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);

        return request;
    }

    public HttpClient createHttpClient_AcceptsUntrustedCerts(ApiConfig config) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder b = HttpClientBuilder.create();
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(config.getUsername(), config.getPassword());
        SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(null, new TrustManager[] {new AlwaysTrustManager()}, null);

        provider.setCredentials(AuthScope.ANY, credentials);

        HostnameVerifier hostnameVerifier = new TrustingHostnameVerifier();

        SSLContext sslContext1 = new SSLContextBuilder()
                .loadTrustMaterial(null, (x509Certificates, s) -> true)
                .build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext1, (X509HostnameVerifier) hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslsf)
                .build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        return b.setConnectionManager(connMgr)
                .setSslcontext(sslContext)
                //         .setDefaultCredentialsProvider(provider)
                .build();
    }

    private static class AlwaysTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException { }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException { }

        public X509Certificate[] getAcceptedIssuers() { return null; }
    }

    private static final class TrustingHostnameVerifier implements X509HostnameVerifier {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

        @Override
        public void verify(String s, SSLSocket sslSocket) throws IOException {

        }

        @Override
        public void verify(String s, X509Certificate x509Certificate) throws SSLException {

        }

        @Override
        public void verify(String s, String[] strings, String[] strings1) throws SSLException {

        }
    }
}
