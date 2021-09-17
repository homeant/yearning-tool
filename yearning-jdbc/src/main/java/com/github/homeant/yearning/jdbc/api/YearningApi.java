package com.github.homeant.yearning.jdbc.api;

import com.github.homeant.yearning.jdbc.api.domain.*;
import com.github.homeant.yearning.jdbc.api.domain.Query;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface YearningApi {
    @POST("/ldap")
    @Headers("content-type: application/json;charset=UTF-8")
    Call<CommonResp<LoginResp>> login(@Body Login login);

    /**
     * 获取 Schemas
     * @param fetchBase
     * @return
     */
    @PUT("/api/v2/query/fetch_base")
    @Headers("content-type: application/json;charset=UTF-8")
    Call<CommonResp<SchemaResp>> fetchBase(@Body FetchBase fetchBase);

    /**
     * 获取区域
     * @return
     */
    @GET("/api/v2/fetch/idc")
    Call<CommonResp<List<String>>> idc();

    /**
     * 获取审批人
     * @param idc
     * @return
     */
    @GET("/api/v2/fetch/source")
    Call<CommonResp<ApprovalUserResp>> fetchApprovalUser(@retrofit2.http.Query("idc")String idc, @retrofit2.http.Query("tp")String tp);

    /**
     * 提交工单
     * @param queryTicket
     * @return
     */
    @POST("/api/v2/query/refer")
    @Headers("content-type: application/json;charset=UTF-8")
    Call<CommonResp<String>> createTicket(@Body QueryTicket queryTicket);
    /**
     * 查询
     * @param query
     * @return
     */
    @POST("/api/v2/query/results")
    @Headers("content-type: application/json;charset=UTF-8")
    Call<CommonResp<QueryResp>> query(@Body Query query);
}
