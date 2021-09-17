package com.github.homeant.yearning.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.github.homeant.yearning.api.domain.*;
import com.github.homeant.yearning.domain.YearningSettings;
import com.github.homeant.yearning.exception.YearningException;
import com.google.common.collect.Lists;
import lombok.extern.apachecommons.CommonsLog;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@CommonsLog
public class YearningService {

    private final YearningApi yearningApi;

    private static final String AUTHORIZATION_HEADER = "authorization";

    private static final ThreadLocal<String> tokenLocal = new ThreadLocal<>();

    public static String getToken() {
        return tokenLocal.get();
    }

    public static void setToken(String token) {
        tokenLocal.set(token);
    }

    public YearningService(YearningSettings settings) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        String url = settings.getUrl();
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120L, TimeUnit.SECONDS)
                .readTimeout(120L, TimeUnit.SECONDS)
                .authenticator(new RefreshAuthenticator(settings))
                .addInterceptor(new AuthenticatorInterceptor(settings))
                //.addInterceptor(new AgentInterceptor())
                .addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).baseUrl(url).build();
        this.yearningApi = retrofit.create(YearningApi.class);
    }

    public LoginResp login(String username, String password) {
        Login login = new Login(username, password);
        return fetchBody(() -> yearningApi.login(login));
    }

    public List<String> fetchIdc(){
        return fetchBody(yearningApi::idc);
    }


    public List<String> getSchemas(String database) {
        SchemaResp schemaResp = fetchBody(() -> yearningApi.fetchBase(FetchBase.builder().source(database).build()));
        List<String> schemaList = Lists.newArrayList();
        for (SchemaResp.Info info : schemaResp.getInfo()) {
            schemaList.addAll(info.getChildren().stream().map(SchemaResp.Children::getTitle).collect(Collectors.toList()));
        }
        return schemaList;
    }

    /**
     * 创建工单
     */
    public void createATicket() {
        List<String> idcs = fetchBody(yearningApi::idc);
        DataSourceResp dataSourceResp = fetchBody(() -> yearningApi.fetchDataSource(idcs.get(0), "query"));
        String assigned = dataSourceResp.getAssigned().get(0);
        QueryTicket queryTicket = new QueryTicket();
        queryTicket.setAssigned(assigned);
        queryTicket.setExport(0);
        queryTicket.setIdc(idcs.get(0));
        queryTicket.setText("query");
        fetchBody(() -> yearningApi.createTicket(queryTicket));
        log.info("创建工单");
    }

    public DataSourceResp fetchQueryApprovalUser() {
        List<String> idcs = fetchBody(yearningApi::idc);
        return fetchBody(() -> yearningApi.fetchDataSource(idcs.get(0), "query"));
    }

    public PageResp<WorkOrderResp> fetchWorkOrder(PageQuery<WorkOrderQuery> query) {
        return fetchBody(()->yearningApi.fetchWorkOrder(query));
    }

    public DataSourceResp fetchDataSource(String idc,String sqlType) {
        return fetchBody(() -> yearningApi.fetchDataSource(idc, sqlType));
    }

    public DataBaseResp fetchDataBase(String source) {
        return fetchBody(() -> yearningApi.fetchDataBase(source));
    }

    public List<TestWorkOrderResp> testWorkOrder(TestWorkOrder workOrder){
        return fetchBodyOfList(() -> yearningApi.testWorkOrder(workOrder));
    }

    public Map<String, Object> submitWorkOrder(SubmitWorkOrder workOrder){
        return fetchBody(() -> yearningApi.submitWorkOrder(workOrder));
    }


    @FunctionalInterface
    public interface Action<T> {
        Call<T> call();
    }

    private <T> Response<CommonResp<T>> fetchResp(Action<CommonResp<T>> action) {
        try {
            return action.call().execute();
        } catch (IOException e) {
            throw new YearningException(e);
        }
    }

    private <T> T fetchBody(Action<CommonResp<T>> action) {
        Response<CommonResp<T>> response = fetchResp(action);
        if (response.isSuccessful()) {
            CommonResp<T> body = response.body();
            assert body != null;
            if (body.isSuccessful()) {
                return body.getData();
            } else {
                throw new YearningException(body.getText());
            }
        } else {
            try (ResponseBody responseBody = response.errorBody()) {
                assert responseBody != null;
                throw new YearningException(responseBody.string());
            } catch (IOException e) {
                throw new YearningException(e);
            }

        }
    }

    private <T> Response<CommonListResp<T>> fetchRespOfList(Action<CommonListResp<T>> action) {
        try {
            return action.call().execute();
        } catch (IOException e) {
            throw new YearningException(e);
        }
    }

    private <T> List<T> fetchBodyOfList(Action<CommonListResp<T>> action) {
        Response<CommonListResp<T>> response = fetchRespOfList(action);
        if (response.isSuccessful()) {
            CommonListResp<T> body = response.body();
            assert body != null;
            if (body.isSuccessful()) {
                return body.getData();
            } else {
                throw new YearningException(body.getText());
            }
        } else {
            try (ResponseBody responseBody = response.errorBody()) {
                assert responseBody != null;
                throw new YearningException(responseBody.string());
            } catch (IOException e) {
                throw new YearningException(e);
            }

        }
    }



    class RefreshAuthenticator implements Authenticator {

        private final YearningSettings settings;

        RefreshAuthenticator(YearningSettings settings) {
            this.settings = settings;
        }

        @Nullable
        @Override
        public Request authenticate(@Nullable Route route, @NotNull okhttp3.Response response) throws IOException {
            LoginResp loginResp = login(settings.getUsername(), settings.getPassword());
            setToken(loginResp.getToken());
            Request request = response.request();
            return request.newBuilder().removeHeader(AUTHORIZATION_HEADER)
                    .addHeader(AUTHORIZATION_HEADER, "Bearer " + loginResp.getToken()).build();
        }
    }

    static class AgentInterceptor implements Interceptor {

        @NotNull
        @Override
        public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request().newBuilder().removeHeader("User-Agent")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36").build();
            return chain.proceed(request);
        }
    }

    static class AuthenticatorInterceptor implements Interceptor {

        private final YearningSettings settings;

        AuthenticatorInterceptor(YearningSettings settings) {
            this.settings = settings;
        }

        @NotNull
        @Override
        public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
            String token = getToken();
            Request request = chain.request().newBuilder().removeHeader(AUTHORIZATION_HEADER)
                    .addHeader(AUTHORIZATION_HEADER, "Bearer " + token).build();
            return chain.proceed(request);
        }
    }
}
