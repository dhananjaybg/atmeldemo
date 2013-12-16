package com.sdl.cd.livecontent.services.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sdl.cd.livecontent.model.ApiResultModel;
import com.sdl.cd.livecontent.model.AuditTrailListModel;
import com.sdl.cd.livecontent.model.AuditTrailModel;
import com.sdl.cd.livecontent.model.EventModel;
import com.sdl.cd.livecontent.dao.exist.AuditTrailDao;
import com.sdl.cd.livecontent.services.rest.RestServiceBase;
import com.sdl.cd.livecontent.services.rest.exceptions.ApiException;

/** 
** 
** @CLASSGUID: GUID-CAB2B784-0000-0140-AC7E-1A93FFFF8000
** 
** @ClassTitle: Audit Trail Management V2
** 
** @ShortDesc: Resources to create, manage, and display audit trails.
** 
**/
@Path("/audit")
public class AuditTrailResource extends RestServiceBase{
	 
	/** 
	** 
	** @APIGUID:  GUID-CAB2B784-0000-0140-AC7E-1A93FFFF8001
	** 
	** @APITitle: Delete an Audit
	** 
	** @ShortDesc: Deletes an audit from the collection of audits.
	** 
	** @Desc: 
	** 
	** @URI: /v2/audit/{session_id}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>Param Name: </b>session_id the session_id of the Audit to delete.
	** 
	** @QueryParam: <b>Param Name: </b>
	** 
	** @RespSuccessXML: {@code <result message="Action = delete_AuditTrail" messageKey="Action = delete_AuditTrail" status="SUCCESS" statusCode="0" />}
	** 
	** @RespSuccessJSON: {@code {"statusCode":0,"messageKey":"Action = delete_AuditTrail","message":"Action = delete_AuditTrail","status":"SUCCESS","info":[]}}
	** 
	** @RespFailedXML: {@code <result message="Not Found" messageKey="Not Found" status="FAIL" statusCode="404" />}
	** 
	** @RespFailedJSON: {@code {"statusCode":404,"messageKey":"Not Found","message":"Not Found","status":"FAIL","info":[]}}
	** 
	** @Permissions: @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>DELETE </b> <codeph>@include_ExampleURL/audit/{session_id}</codeph></p>
	** 
	** @ExplResponse:  {@code <result message="Action = delete_AuditTrail" messageKey="Action = delete_AuditTrail" status="SUCCESS" statusCode="0" />}
	** 
	**/	
	@DELETE 
	@Path("{session_id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel deleteAuditTrail(@PathParam("session_id") String session_id) {
		logger.debug("[SDL LCR] DELETE /v2/audit/{session_id}. \nPath params: N/A. \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		ApiResultModel ar = new ApiResultModel();
		this.assertPermission(request, "Manage app"); 
		AuditTrailDao dao = new AuditTrailDao(request.getSession(true)); 
		if(dao.containsIllegalCharacters(session_id)){
			ar.init(1251, "invalid.characters.alphanumeric", false, user); 
			logger.debug("[SDL LCR] API fail: PUT /v2/audit/{session_id}. ApiResultModel returned to be marshaled.");
			return ar;
		}
		logger.debug("[SDL LCR] API succeed: DELETE /v2/AuditTrail/" + session_id + ". AuditTrail returned as a String.");
		if( dao.deleteAuditTrail(session_id)){
			logger.debug("[SDL LCR] API succeed: DELETE /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			ar.init(0, "Action = delete_AuditTrail", true, user);
			return ar;
		}else{
			ar.init(404, "Not Found", true, user);
		}
		return ar; 
	}
	
	
		/** 
	** 
	** @APIGUID: GUID-CAB2B784-0000-0140-AC7E-1A93FFFF8002
	** 
	** @APITitle: Fetch an audit
	** 
	** @ShortDesc: Retrieves and displays an audit trail.
	** 
	** @Desc: 
	** 
	** @URI: /v2/audit/{session_id}
	** 
	** @HTTPMethod: GET  
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @RespSuccessXML: {@code  <Audit aggregated="no" session_id="rg7y77g6r10g" time="2013-06-04T09:51:32.462-04:00" hostid="283a0990-1d98-4d85-b3f8-63194b7aa9c1" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0" browser="Firefox"><event filterTransId="none" groups="dba" skin="base" time="2013-06-04T09:51:32.462-04:00" type="SetSkin" user="admin"/><event app_version="6.0.1.130526" filterTransId="none" groups="dba" lang="en" browser="Firefox" host="http://localhost:4444" hosturl="http://localhost:4444/LiveContent/" db_version="1.4.1.0" time="2013-06-04T09:51:32.462-04:00" type="startAuditTrail" user="admin" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0" id="Session" last_access="2013-06-03T16:16:16.407-04:00" refer="" session_id="rg7y77g6r10g" start="2013-06-04T09:51:32.462-04:00"/><event filterTransId="none" groups="dba" time="2013-06-04T09:51:32.462-04:00" type="login" user="admin"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-06-04T09:51:32.462-04:00" type="ViewPubs" user="admin"/></Audit>}
	** 
	** @RespSuccessJSON: {"{@code  aggregated":"no","session_id":"rg7y77g6r10g","time":"2013-06-04T09:51:32.462-04:00","hostid":"283a0990-1d98-4d85-b3f8-63194b7aa9c1","user_agent":"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0","browser":"Firefox","event":[{"ShowAll":null,"app_version":null,"filterTransId":"none","groups":"dba","skin":"base","lang":null,"pub":[],"browser":null,"host":null,"hosturl":null,"db_version":null,"significant":null,"time":"2013-06-04T09:51:32.462-04:00","transId":null,"type":"SetSkin","user":"admin","user_agent":null,"eventTime":null,"id":null,"last_access":null,"refer":null,"session_id":null,"start":null},{"ShowAll":null,"app_version":"6.0.1.130526","filterTransId":"none","groups":"dba","skin":null,"lang":"en","pub":[],"browser":"Firefox","host":"http://localhost:4444","hosturl":"http://localhost:4444/LiveContent/","db_version":"1.4.1.0","significant":null,"time":"2013-06-04T09:51:32.462-04:00","transId":null,"type":"startAuditTrail","user":"admin","user_agent":"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0","eventTime":null,"id":"Session","last_access":"2013-06-03T16:16:16.407-04:00","refer":"","session_id":"rg7y77g6r10g","start":"2013-06-04T09:51:32.462-04:00"},{"ShowAll":null,"app_version":null,"filterTransId":"none","groups":"dba","skin":null,"lang":null,"pub":[],"browser":null,"host":null,"hosturl":null,"db_version":null,"significant":null,"time":"2013-06-04T09:51:32.462-04:00","transId":null,"type":"login","user":"admin","user_agent":null,"eventTime":null,"id":null,"last_access":null,"refer":null,"session_id":null,"start":null},{"ShowAll":"all","app_version":null,"filterTransId":"none","groups":"dba","skin":null,"lang":null,"pub":[],"browser":null,"host":null,"hosturl":null,"db_version":null,"significant":"1","time":"2013-06-04T09:51:32.462-04:00","transId":null,"type":"ViewPubs","user":"admin","user_agent":null,"eventTime":null,"id":null,"last_access":null,"refer":null,"session_id":null,"start":null}]}}
	** 
	** @Permissions: @include_PermManageApp
	** 
	** @ExplRequest: <p><b>GET </b> <codeph>@include_ExampleURL/audit/{session_id}</codeph></p>
	** 
	** @ExplResponse: {@code  <Audit aggregated="no" session_id="rg7y77g6r10g" time="2013-06-04T09:51:32.462-04:00" hostid="283a0990-1d98-4d85-b3f8-63194b7aa9c1" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0" browser="Firefox"><event filterTransId="none" groups="dba" skin="base" time="2013-06-04T09:51:32.462-04:00" type="SetSkin" user="admin"/><event app_version="6.0.1.130526" filterTransId="none" groups="dba" lang="en" browser="Firefox" host="http://localhost:4444" hosturl="http://localhost:4444/LiveContent/" db_version="1.4.1.0" time="2013-06-04T09:51:32.462-04:00" type="startAuditTrail" user="admin" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0" id="Session" last_access="2013-06-03T16:16:16.407-04:00" refer="" session_id="rg7y77g6r10g" start="2013-06-04T09:51:32.462-04:00"/><event filterTransId="none" groups="dba" time="2013-06-04T09:51:32.462-04:00" type="login" user="admin"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-06-04T09:51:32.462-04:00" type="ViewPubs" user="admin"/></Audit>}
	** 
	**/
	
	@GET 
	@Path("{session_id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public AuditTrailModel getAuditTrail(@PathParam("session_id") String session_id) {
		logger.debug("[SDL LCR] GET /v2/audit/{session_id}. \nPath params: N/A. \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Manage app"); 
		AuditTrailDao dao = new AuditTrailDao(request.getSession(true)); 
		if(dao.containsIllegalCharacters(session_id)){
			logger.debug("[SDL LCR] API fail: PUT /v2/audit/{session_id}. ApiResultModel returned to be marshaled.");
			throw new ApiException(404, 404, "FAILED!!  'session_id' can only contain letters and numbers.", true, user, request);
		}
		logger.debug("[SDL LCR] API succeed: GET /v2/AuditTrail. AuditTrail returned as an AuditTrailModel.");
		AuditTrailModel auditMod = dao.getAuditTrail(session_id);
		if (auditMod == null){
			throw new ApiException(404, 404, "FAILED!!  Could not find specified Audit Trail.", true, user, request);
		}
		return auditMod;
	}
	
	
		/** 
	** 
	** @APIGUID: GUID-CAB2B784-0000-0140-AC7E-1A93FFFF8003
	** 
	** @APITitle: Fetch Audits
	** 
	** @ShortDesc: Fetches the list of all assembled audit trails.
	** 
	** @Desc: 
	** 
	** @URI: {@code /v2/audit?scope=<value>&start=<value>&length=<value>}
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>Param Name: </b>
	** 
	** @QueryParam: <b>Param Name: </b>
	** 
	** @RespSuccessXML: {@code <Audits><Audit aggregated="no" session_id="1b74l42pik7za" time="2013-07-29T11:25:36.91-04:00" hostid="283a0990-1d98-4d85-b3f8-63194b7aa9c1" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36" browser="Chrome"><event filterTransId="none" groups="dba" skin="base" time="2013-07-29T11:25:36.91-04:00" type="SetSkin" user="admin"/><event app_version="6.0.1.130526" filterTransId="none" groups="dba" lang="en" browser="Chrome" host="http://localhost:4444" hosturl="http://localhost:4444/LiveContent/" db_version="1.4.1.0" time="2013-07-29T11:25:36.91-04:00" type="startAuditTrail" user="admin" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36" id="Session" last_access="2013-07-26T13:02:16.745-04:00" refer="" session_id="1b74l42pik7za" start="2013-07-29T11:25:36.91-04:00"/><event filterTransId="none" groups="dba" time="2013-07-29T11:25:36.91-04:00" type="login" user="admin"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-07-29T11:25:36.91-04:00" type="ViewPubs" user="admin"/></Audit><Audit aggregated="no" session_id="174oaus0e6qly" time="2013-08-15T11:15:52.103-04:00" hostid="283a0990-1d98-4d85-b3f8-63194b7aa9c1" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36" browser="Chrome"><event filterTransId="none" groups="dba" skin="base" time="2013-08-15T11:15:52.103-04:00" type="SetSkin" user="admin"/><event app_version="6.0.1.130526" filterTransId="none" groups="dba" lang="en" browser="Chrome" host="http://localhost:4444" hosturl="http://localhost:4444/LiveContent/" db_version="1.4.1.0" time="2013-08-15T11:15:52.103-04:00" type="startAuditTrail" user="admin" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36" id="Session" last_access="2013-08-15T11:11:50.427-04:00" refer="http://localhost:4444/LiveContent/web/ui.xql?action=html&amp;resource=publist_home.html" session_id="174oaus0e6qly" start="2013-08-15T11:15:52.103-04:00"/><event filterTransId="none" groups="dba" time="2013-08-15T11:15:52.103-04:00" type="login" user="admin"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-08-15T11:15:52.103-04:00" type="ViewPubs" user="admin" eventTime="8.174"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-08-15T11:16:00.277-04:00" type="ViewPubs" user="admin" eventTime="5.042"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-08-15T11:16:05.319-04:00" type="ViewPubs" user="admin" eventTime="7.904"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-08-15T11:16:13.223-04:00" type="ViewPubs" user="admin"/></Audit></Audits>}
	** 
	** @RespSuccessJSON: {@code {"Audit":[null,{"aggregated":"no","session_id":"1b74l42pik7za","time":"2013-07-29T11:25:36.91-04:00","hostid":"283a0990-1d98-4d85-b3f8-63194b7aa9c1","user_agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36","browser":"Chrome","event":[{"ShowAll":null,"app_version":null,"filterTransId":"none","groups":"dba","skin":"base","lang":null,"pub":[],"browser":null,"host":null,"hosturl":null,"db_version":null,"significant":null,"time":"2013-07-29T11:25:36.91-04:00","transId":null,"type":"SetSkin","user":"admin","user_agent":null,"eventTime":null,"id":null,"last_access":null,"refer":null,"session_id":null,"start":null},{"ShowAll":null,"app_version":"6.0.1.130526","filterTransId":"none","groups":"dba","skin":null,"lang":"en","pub":[],"browser":"Chrome","host":"http://localhost:4444","hosturl":"http://localhost:4444/LiveContent/","db_version":"1.4.1.0","significant":null,"time":"2013-07-29T11:25:36.91-04:00","transId":null,"type":"startAuditTrail","user":"admin","user_agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36","eventTime":null,"id":"Session","last_access":"2013-07-26T13:02:16.745-04:00","refer":"","session_id":"1b74l42pik7za","start":"2013-07-29T11:25:36.91-04:00"},{"ShowAll":null,"app_version":null,"filterTransId":"none","groups":"dba","skin":null,"lang":null,"pub":[],"browser":null,"host":null,"hosturl":null,"db_version":null,"significant":null,"time":"2013-07-29T11:25:36.91-04:00","transId":null,"type":"login","user":"admin","user_agent":null,"eventTime":null,"id":null,"last_access":null,"refer":null,"session_id":null,"start":null},{"ShowAll":"all","app_version":null,"filterTransId":"none","groups":"dba","skin":null,"lang":null,"pub":[],"browser":null,"host":null,"hosturl":null,"db_version":null,"significant":"1","time":"2013-07-29T11:25:36.91-04:00","transId":null,"type":"ViewPubs","user":"admin","user_agent":null,"eventTime":null,"id":null,"last_access":null,"refer":null,"session_id":null,"start":null}]},null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]}}
	** 
	** @RespFailedXML: {@code <result message="Not Found" messageKey="Not Found" status="FAIL" statusCode="404" />}
	** 
	** @RespFailedJSON: {@code {"statusCode":404,"messageKey":"Not Found","message":"Not Found","status":"FAIL","info":[]}}
	** 
	** @Permissions:  @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>GET </b> <codeph>@include_ExampleURL/audit?{@code scope=all&start=5&length=10}</codeph></p>
	** 
	** @ExplResponse: {@code <Audits><Audit aggregated="no" session_id="1b74l42pik7za" time="2013-07-29T11:25:36.91-04:00" hostid="283a0990-1d98-4d85-b3f8-63194b7aa9c1" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36" browser="Chrome"><event filterTransId="none" groups="dba" skin="base" time="2013-07-29T11:25:36.91-04:00" type="SetSkin" user="admin"/><event app_version="6.0.1.130526" filterTransId="none" groups="dba" lang="en" browser="Chrome" host="http://localhost:4444" hosturl="http://localhost:4444/LiveContent/" db_version="1.4.1.0" time="2013-07-29T11:25:36.91-04:00" type="startAuditTrail" user="admin" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36" id="Session" last_access="2013-07-26T13:02:16.745-04:00" refer="" session_id="1b74l42pik7za" start="2013-07-29T11:25:36.91-04:00"/><event filterTransId="none" groups="dba" time="2013-07-29T11:25:36.91-04:00" type="login" user="admin"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-07-29T11:25:36.91-04:00" type="ViewPubs" user="admin"/></Audit><Audit aggregated="no" session_id="174oaus0e6qly" time="2013-08-15T11:15:52.103-04:00" hostid="283a0990-1d98-4d85-b3f8-63194b7aa9c1" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36" browser="Chrome"><event filterTransId="none" groups="dba" skin="base" time="2013-08-15T11:15:52.103-04:00" type="SetSkin" user="admin"/><event app_version="6.0.1.130526" filterTransId="none" groups="dba" lang="en" browser="Chrome" host="http://localhost:4444" hosturl="http://localhost:4444/LiveContent/" db_version="1.4.1.0" time="2013-08-15T11:15:52.103-04:00" type="startAuditTrail" user="admin" user_agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36" id="Session" last_access="2013-08-15T11:11:50.427-04:00" refer="http://localhost:4444/LiveContent/web/ui.xql?action=html&amp;resource=publist_home.html" session_id="174oaus0e6qly" start="2013-08-15T11:15:52.103-04:00"/><event filterTransId="none" groups="dba" time="2013-08-15T11:15:52.103-04:00" type="login" user="admin"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-08-15T11:15:52.103-04:00" type="ViewPubs" user="admin" eventTime="8.174"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-08-15T11:16:00.277-04:00" type="ViewPubs" user="admin" eventTime="5.042"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-08-15T11:16:05.319-04:00" type="ViewPubs" user="admin" eventTime="7.904"/><event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-08-15T11:16:13.223-04:00" type="ViewPubs" user="admin"/></Audit></Audits>}
	** 
	**/ 
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public AuditTrailListModel getAuditTrailList( @QueryParam("scope") @DefaultValue( "all" ) String scope,  @QueryParam("start") @DefaultValue( "0" )  int start,  @QueryParam("length") @DefaultValue( "0" )  int length) {
		logger.debug("[SDL LCR] GET /v2/audit/{scope}/{start}/{length}. \nPath params: N/A. \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Manage app"); 
		AuditTrailDao dao = new AuditTrailDao(request.getSession(true)); 
		if(dao.containsIllegalCharacters(scope)){
			logger.debug("[SDL LCR] API fail: PUT /v2/audit/{scope}/{start}/{length}. Scope contains illegal characters Returned null.");
			throw new ApiException(200, 404, "FAILED!!  session_id cannot contain illegal characters!", true, user, request);
		}
		if(scope.equalsIgnoreCase("all") == false && scope.equalsIgnoreCase("aggregated") == false && scope.equalsIgnoreCase("aggregate") == false){
			logger.debug("[SDL LCR] API fail: PUT /v2/audit/{scope}/{start}/{length}. Scope must be either 'all' or 'aggregated'.");
			throw new ApiException(200, 404, " Scope must be either 'all' or 'aggregated'.", true, user, request);
		}
		if(start < 0){
			logger.debug("[SDL LCR] API fail: PUT /v2/audit/{scope}/{start}/{length}. Start must be a positive number less than the number of audit trails.");
			throw new ApiException(200, 404, " FAILED!!  The start parameter must be positive and less than the number of audit trails.", true, user, request);
		}
		if(dao.isTooLong(scope, length)){
			throw new ApiException(200, 404, " FAILED!!  The length cannot be longer than the number of matching audit trails", true, user, request);
		}
		logger.debug("[SDL LCR] API succeed: GET /v2/audit/{scope}/{start}/{length}. Audit Trails within specified range are returned");
		//run the getAuditTrails method from the database access object
		//if the length is the default value i.e. zero then return all the audit trails.
		//otherwise if length is specified only return the specified audit trails.
		AuditTrailListModel auditlist = dao.getAuditTrails(scope, start, length);
		if( auditlist != null){
			return auditlist;
		}else{
			logger.debug("[SDL LCR] API succeed: GET /v2/audit/{scope}/{start}/{length}. Failed!! No audit trails match the requirements specified.");
			throw new ApiException(200, 404, "FAILED!!  No audit trails match the requirements specified.", true, user, request);
		}
	}
	
	
	/** 
	** 
	** @APIGUID: GUID-CAB2B784-0000-0140-AC7E-1A93FFFF8004
	** 
	** @APITitle: Create an Audit
	** 
	** @ShortDesc: Creates an audit.
	** 
	** @Desc: 
	** 
	** @URI: /v2/audit/{session_id}
	** 
	** @HTTPMethod: POST
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>Param Name: </b>
	** 
	** @QueryParam: <b>Param Name: </b>
	** 
	** @RespSuccessXML: {@code <result message="Action = create_AuditTrail" messageKey="Action = create_AuditTrail" status="SUCCESS" statusCode="0" />}
	** 
	** @RespSuccessJSON: {@code {"statusCode":0,"messageKey":"Action = create_AuditTrail","message":"Action = create_AuditTrail","status":"SUCCESS","info":[]}}
	** 
	** @RespFailedXML: {@code <result message="Not Found" messageKey="Not Found" status="FAIL" statusCode="404" />}
	** 
	** @RespFailedJSON: {@code {"statusCode":404,"messageKey":"Not Found","message":"Not Found","status":"FAIL","info":[]}}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>POST </b> <codeph>@include_ExampleURL/audit/{session_id}</codeph></p>
	** 
	** @ExplResponse:{@code <result message="Action = create_AuditTrail" messageKey="Action = create_AuditTrail" status="SUCCESS" statusCode="0" />}
	** 
	**/
	@POST
	@Path("{session_id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel createAuditTrail(@PathParam("session_id") String session_id) {
		logger.debug("[SDL LCR] GET /v2/audit/{session_id}. \nPath params: N/A. \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Use application"); 
		AuditTrailDao dao = new AuditTrailDao(request.getSession(true)); 
		ApiResultModel ar = new ApiResultModel();
		if(dao.containsIllegalCharacters(session_id)){
			ar.init(1251, "invalid.characters.alphanumeric", false, user); 
			logger.debug("[SDL LCR] API fail: PUT /v2/audit/{session_id}. ApiResultModel returned to be marshaled.");
			return ar;
		}
		if(dao.createAuditTrail(session_id, request)){
			logger.debug("[SDL LCR] API succeed: POST /v2/audit/{session_id}. ApiResultModel returned to be marshaled.");
			ar.init(0, "Action = create_AuditTrail", true, user);
			return ar;
		}else{
			logger.debug("[SDL LCR] API failed: POST /v2/audit/{session_id}. an error occured.");
			ar.init(404, "Not Found", true, user);
			return ar;
		}
	}
	
	
		
	/**
	** 
	** @APIGUID: GUID-CAB2B784-0000-0140-AC7E-1A93FFFF8005
	** 
	** @APITitle: Create an Event in the Current Audit 
	** 
	** @ShortDesc: Creates an event item in the audit record with the specified session_id.
	** 
	** @Desc: 
	** 
	** @URI: /v2/audit/{session_id}/{event}
	** 
	** @HTTPMethod: POST 
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>Param Name: </b>session_id the session id of the audit to create
	** 
	** @QueryParam: <b>Param Name: </b>event the event to insert into the session
	** 
	** @PostDataXML: {@code <event ShowAll="all" filterTransId="none" groups="dba" significant="1" time="2013-08-20T11:42:04.339-04:00" type="ViewPubs" user="admin"/>}
	** 
	** @PostDataJSON: {@code {"aggregated":"no","session_id":"1j0ixpkybitj2","time":"2013-08-20T13:55:08.002-04:00","hostid":"283a0990-1d98-4d85-b3f8-63194b7aa9c1","user_agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36","browser":"chrome","event":[]}}
	** 
	** @RespSuccessXML: {@code <result message="Action = create_Event" messageKey="Action = create_Event" status="SUCCESS" statusCode="0" />}
	** 
	** @RespSuccessJSON: {@code {"statusCode":0,"messageKey":"Action = create_AuditTrail","message":"Action = create_AuditTrail","status":"SUCCESS","info":[]}}
	** 
	** @RespFailedXML: {@code <result message="Not Found" messageKey="Not Found" status="FAIL" statusCode="404" />}
	** 
	** @RespFailedJSON: {@code {"statusCode":404,"messageKey":"Not Found","message":"Not Found","status":"FAIL","info":[]}}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>POST</b> <codeph>@include_ExampleURL/audit/{session_id}/{event}</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = create_Event" messageKey="Action = create_Event" status="SUCCESS" statusCode="0" />}
	** 
	**/ 
	
	@POST 
	@Path("{session_id}/{event}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel createEvent(EventModel event, @PathParam("session_id") String session_id) {
		logger.debug("[SDL LCR] GET /v2/audit/{session_id}/{event}. \nPath params: N/A. \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Use application"); 
		AuditTrailDao dao = new AuditTrailDao(request.getSession(true)); 
		ApiResultModel ar = new ApiResultModel();
		logger.debug("[SDL LCR] The session_id inside the resource class: " + session_id);
		if(dao.containsIllegalCharacters(session_id)){
			ar.init(1251, "invalid.characters.alphanumeric", false, user); 
			logger.debug("[SDL LCR] API fail: PUT /v2/audit/{session_id}. ApiResultModel returned to be marshaled.");
			return ar;
		}
		if(dao.createEvent(session_id, event)){
			logger.debug("[SDL LCR] API succeed: POST /v2/audit/{session_id}/{event}. ApiResultModel returned to be marshaled.");
			ar.init(0, "Action = create_Event", true, user);
			return ar;
		}else{
			logger.debug("[SDL LCR] API failed: POST /v2/audit/{session_id}/{event}. an error occured.");
			ar.init(404, "Not Found", true, user);
			return ar;
		}
	}
	
}
