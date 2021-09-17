package com.github.homeant.yearning.api;

import com.github.homeant.yearning.api.domain.*;
import retrofit2.Call;
import retrofit2.http.*;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

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
     * 获取数据源
     * @param idc
     * @return
     */
    @GET("/api/v2/fetch/source")
    Call<CommonResp<DataSourceResp>> fetchDataSource(@Query("idc")String idc, @Query("tp")String tp);


    /**
     * 获取数据库和审批人
     * @param source
     * @return
     */
    @GET("/api/v2/fetch/base")
    Call<CommonResp<DataBaseResp>> fetchDataBase(@Query("source")String source);

    /**
     * 提交工单
     * @param queryTicket
     * @return
     */
    @POST("/api/v2/query/refer")
    @Headers("content-type: application/json;charset=UTF-8")
    Call<CommonResp<String>> createTicket(@Body QueryTicket queryTicket);


    @PUT("/api/v2/common/list")
    @Headers("content-type: application/json;charset=UTF-8")
    Call<CommonResp<PageResp<WorkOrderResp>>> fetchWorkOrder(@Body PageQuery<WorkOrderQuery> query);

    /**
     * sql 测试
     * @param testWorkOrder
     * @return
     */
    @PUT("/api/v2/fetch/test")
    @Headers("content-type: application/json;charset=UTF-8")
    Call<CommonListResp<TestWorkOrderResp>> testWorkOrder(@Body TestWorkOrder testWorkOrder);

    @POST("/api/v2/common/post")
    @Headers("content-type: application/json;charset=UTF-8")
    Call<CommonResp<Map<String, Object>>> submitWorkOrder(@Body SubmitWorkOrder workOrder);


}
