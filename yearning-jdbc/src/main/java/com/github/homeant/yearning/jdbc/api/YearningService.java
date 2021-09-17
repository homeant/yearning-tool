package com.github.homeant.yearning.jdbc.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.github.homeant.yearning.jdbc.api.domain.*;
import com.github.homeant.yearning.jdbc.core.Session;
import com.github.homeant.yearning.jdbc.exception.JdbcException;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@CommonsLog
public class YearningService {


    private final YearningApi yearningApi;

    private static final String AUTHORIZATION_HEADER = "authorization";

    public YearningService(Session session) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        String url = session.getUrl().replace("jdbc:yearning://", "https://");
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120L, TimeUnit.SECONDS)
                .readTimeout(120L, TimeUnit.SECONDS)
                .authenticator(new RefreshAuthenticator(session))
                .addInterceptor(new AuthenticatorInterceptor(session))
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
        ApprovalUserResp approvalUserResp = fetchBody(() -> yearningApi.fetchApprovalUser(idcs.get(0), "query"));
        String assigned = approvalUserResp.getAssigned().get(0);
        QueryTicket queryTicket = new QueryTicket();
        queryTicket.setAssigned(assigned);
        queryTicket.setExport(0);
        queryTicket.setIdc(idcs.get(0));
        queryTicket.setText("query");
        fetchBody(() -> yearningApi.createTicket(queryTicket));
        log.info("创建工单");
    }


    public QueryResp query(String source, String catalog, String sql) {
        Query query = new Query();
        query.setDataBase(catalog);
        query.setSource(source);
        query.setSql(sql);
        QueryResp queryResp = fetchBody(() -> yearningApi.query(query));
        // 工单过期了/或者没有创建过工单
        if (queryResp.isStatus()) {
            createATicket();
            return fetchBody(() -> yearningApi.query(query));
        }
        return queryResp;
    }

    @FunctionalInterface
    public interface Action<T> {
        Call<T> call();
    }

    private <T> Response<CommonResp<T>> fetchResp(Action<CommonResp<T>> action) {
        try {
            return action.call().execute();
        } catch (IOException e) {
            throw new JdbcException(e);
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
                throw new JdbcException(body.getText());
            }
        } else {
            try (ResponseBody responseBody = response.errorBody()) {
                assert responseBody != null;
                throw new JdbcException(responseBody.string());
            } catch (IOException e) {
                throw new JdbcException(e);
            }

        }
    }

    class RefreshAuthenticator implements Authenticator {

        private final Session session;

        RefreshAuthenticator(Session session) {
            this.session = session;
        }

        @Nullable
        @Override
        public Request authenticate(@Nullable Route route, @NotNull okhttp3.Response response) throws IOException {
            LoginResp loginResp = login(session.getUserName(), session.getPassword());
            session.setToken(loginResp.getToken());
            Request request = response.request();
            return request.newBuilder().removeHeader(AUTHORIZATION_HEADER)
                    .addHeader(AUTHORIZATION_HEADER, "Bearer " + loginResp.getToken()).build();
        }
    }

    class AgentInterceptor implements Interceptor {

        @NotNull
        @Override
        public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request().newBuilder().removeHeader("User-Agent")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36").build();
            return chain.proceed(request);
        }
    }

    class AuthenticatorInterceptor implements Interceptor {

        private final Session session;

        AuthenticatorInterceptor(Session session) {
            this.session = session;
        }

        @NotNull
        @Override
        public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
            String token = session.getToken();
            Request request = chain.request().newBuilder().removeHeader(AUTHORIZATION_HEADER)
                    .addHeader(AUTHORIZATION_HEADER, "Bearer " + token).build();
            return chain.proceed(request);
        }
    }
}
