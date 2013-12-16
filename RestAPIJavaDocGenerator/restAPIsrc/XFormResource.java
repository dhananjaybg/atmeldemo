package com.sdl.cd.livecontent.services.rest.resources;
/* $Id: src/com/sdl/cd/livecontent/services/rest/resources/XFormResource.java 1.23 2013/08/05 17:27:41EDT vpevunov Exp  $ */

import com.sdl.cd.livecontent.services.rest.RestServiceBase;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.sdl.cd.livecontent.dao.exist.XFormTemplateDao;
import com.sdl.cd.livecontent.model.ApiResultModel;
import com.sdl.cd.livecontent.model.XFormTemplateModel;
import com.sdl.cd.livecontent.model.XFormTemplateListModel;
import com.sdl.cd.livecontent.services.rest.exceptions.ApiException;

/** 
** 
** @CLASSGUID: GUID-2E323793-5327-4F9D-9435-CD23BE487091
** 
** @ClassTitle: XForms - User Generated Content V2
** 
** @ShortDesc: Resources for creating, updating, deleting, and fetching XForm templates.
** 
**/

@Path("/xforms/config")
public class XFormResource extends RestServiceBase {
	
    /** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8020
	** 
	** @APITitle: Fetch XForm Template Configurations 
	** 
	** @ShortDesc: Fetches the list of all XForm template configurations.
	** 
	** @URI: /v2/xforms/config
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @RespSuccessXML:  {@code <configuration><xform xformfile="xform.issue_report.html" type="floating" status_options="" public="false" icon="issue_report.png" id="xform.issue_report" allow_status="0" allow_replies="0" allow_edits="1"/><xform xformfile="xform.comment.html" type="doc" status_options="xform.status.accepted|xform.status.rejected|xform.status.completed|" public="true" icon="comment.png" id="xform.comment" allow_status="1" allow_replies="1" allow_edits="1"/></configuration>}
	** 
	** @RespSuccessJSON:  {@code {"xform":[{"id":"xform.issue_report", "type":"floating", "public":false, "allow_edits":1, "allow_replies":0, "allow_status":0, "icon":"issue_report.png", "reply_status_options":null, "status_options":"", "xformfile":"xform.issue_report.html"},{"id":"xform.comment", "type":"doc", "public":true,"allow_edits":1,"allow_replies":1,"allow_status":1,"icon":"comment.png", "reply_status_options":null,"status_options":"xform.status.accepted|xform.status.rejected|xform.status.completed|", "xformfile":"xform.comment.html"}]}}
	** 
	** @RespFailedXML:  {@code <result statusCode="404" status="FAIL" messageKey="Config resource was not found.", message="Config resource was not found."/>}
	** 
	** @RespFailedJSON:  {@code {"statusCode":404,"messageKey":"Config resource was not found.", "message":"Config resource was not found.", "status":"FAIL", "info":[]}}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/xforms/config</codeph></p>
	** 
	** @ExplResponse: {@code <configuration><xform xformfile="xform.issue_report.html" type="floating" status_options="" public="false" icon="issue_report.png" id="xform.issue_report" allow_status="0" allow_replies="0" allow_edits="1"/><xform xformfile="xform.comment.html" type="doc" status_options="xform.status.accepted|xform.status.rejected|xform.status.completed|" public="true" icon="comment.png" id="xform.comment" allow_status="1" allow_replies="1" allow_edits="1"/></configuration>}
	** 
	**/
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public XFormTemplateListModel getXformsDefList()  throws ApiException  {
		logger.debug("[SDL LCR] GET /v2/xforms/config. \nPath params: N/A. \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		// permission check
		this.assertPermission(request, "Develop app");
		XFormTemplateDao dao = new XFormTemplateDao(request.getSession(true));
		XFormTemplateListModel xfList = dao.getXFormTemplateList();
		if (xfList == null) {
			logger.debug("[SDL LCR] API failed: GET /v2/xforms/config. 404 - Config resource is not found! ApiException thrown.");
			throw new ApiException(200, 404, "Config resource was not found.", true, user, request);
		}
		logger.debug("[SDL LCR] API succeed: GET /v2/xforms/config. XFormTemplateListModel returned to be marsheled.");
		return xfList;
	}
	
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8021
	** 
	** @APITitle: Fetch an XForm Template Configuration 
	** 
	** @ShortDesc: Fetches a single XForm template configuration identified by its ID.
	** 
	** @URI: /v2/xforms/config/{id}
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>id: </b>The ID of the XForm template configuration to fetch.
	** 
	** @RespSuccessXML: {@code <xform type="doc" status_options="xform.status.accepted|xform.status.rejected|xform.status.completed|" reply_status_options="xform.status.new|xform.status.accepted|xform.status.rejected|xform.status.completed|" public="true" id="xform.comment" allow_status="1" allow_replies="1" allow_edits="0"/>}
	** 
	** @RespSuccessJSON:  {@code {"id":"xform.comment", "type":"doc", "public":true,"allow_edits":0,"allow_replies":1,"allow_status":1,"icon":null,"reply_status_options":"xform.status.new|xform.status.accepted|xform.status.rejected|xform.status.completed|", "status_options":"xform.status.accepted|xform.status.rejected|xform.status.completed|", "xformfile":null}} 
	** 
	** @RespFailedXML:  {@code <result statusCode="404" status="FAIL" messageKey="XForm template configuration was not found." message="XForm template configuration was not found."/>}
	** 
	** @RespFailedJSON:  {@code {"statusCode":404,"messageKey":"XForm template configuration was not found.", "message":"XForm template configuration was not found.", "status":"FAIL", "info":[]}}
	** 
	** @Permissions: @include_PermUseApp 
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/xforms/config/xform.comment</codeph></p>
	** 
	** @ExplResponse: {@code <xform type="doc" status_options="xform.status.accepted|xform.status.rejected|xform.status.completed|" reply_status_options="xform.status.new|xform.status.accepted|xform.status.rejected|xform.status.completed|" public="true" id="xform.comment" allow_status="1" allow_replies="1" allow_edits="0"/>}
	** 
	**/
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public XFormTemplateModel getXFormTemplateModel(@PathParam("id") String id) {
		logger.debug("[SDL LCR] GET /v2/xforms/config/{id}. \nPath params: id=" + id + ". \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		// permission check
		this.assertPermission(request, "Develop app");
		XFormTemplateDao dao = new XFormTemplateDao(request.getSession(true));
		XFormTemplateListModel xfList = dao.getXFormTemplateList();
		if (xfList == null || xfList.getXFormTemplate(id) == null){
			logger.debug("[SDL LCR] API failed: GET /v2/xforms/config/{id}. 404 - XForm template configuration was not found! ApiException thrown.");
			throw new ApiException(200, 404, "XForm template configuration was not found.", true, user, request);
		}
		logger.debug("[SDL LCR] API succeed: GET /v2/xforms/config/{id}. XFormTemplateModel returned to be marsheled.");
		return xfList.getXFormTemplate(id);
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8022
	** 
	** @APITitle: Create or Update an XForm Template Configuration
	** 
	** @ShortDesc: Creates or updates an XForm template configuration.  
	** 
	** @URI: /v2/xforms/config/{id}
	** 
	** @HTTPMethod: PUT
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>id: </b>The ID of the XForm template configuration to create or update.
	** 
	** @PostDataXML:  {@code <xform id="xform.comment" type="doc" public="true" allow_edits="0" allow_replies="1" allow_status="1" reply_status_options="xform.status.new|xform.status.accepted|xform.status.rejected|xform.status.completed|" status_options="xform.status.accepted|xform.status.rejected|xform.status.completed|"/>} 
	** 
	** @PostDataJSON:  {@code {"id":"xform.comment", "type":"doc", "public":"true", "allow_edits":"0", "allow_replies":"1", "allow_status":"1", "reply_status_options":"xform.status.new|xform.status.accepted|xform.status.rejected|xform.status.completed|", "status_options":"xform.status.accepted|xform.status.rejected|xform.status.completed|"}}
	** 
	** @RespSuccessXML: {@code <result statusCode="0" status="SUCCESS" messageKey="Successfully created/updated XForm template configuration." message="Successfully created/updated XForm template configuration."/>}
	** 
	** @RespSuccessJSON: {@code {"statusCode":0,"messageKey":"Successfully created/updated XForm template configuration.", "message":"Successfully created/updated XForm template configuration.", "status":"SUCCESS", "info":[]}} 
	** 
	** @RespFailedXML:  {@code <result statusCode="500" status="FAIL" messageKey="Failed to create/update XForm template id: xform.comment"  message="Failed to create/update XForm template id: xform.comment" />}
	** 
	** @RespFailedJSON:  {@code {"statusCode":500,"messageKey":"Failed to create/update XForm template id: xform.comment" ,"message":"Failed to create/update XForm template id: xform.comment" ,"status":"FAIL", "info":[]}}
	** 
	** @Permissions: @include_PermDevelopApp
	** 
	** @ExplRequest: <p><b>PUT</b> <codeph>@include_ExampleURL/xforms/config/xform.comment</codeph></p>
	**					<p><b>Accept:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Content-Type:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Request Body:</b>  <codeph>{@code <xform allow_edits="1" allow_replies="1" allow_status="1" id="xform.comment" icon="comment.png" public="true" status_options="xform.status.accepted|xform.status.rejected|xform.status.completed|" type="doc" xformfile="xform.comment.html"/>}</codeph></p>
	** 
	** @ExplResponse: {@code <result statusCode="0" status="SUCCESS" messageKey="Successfully created/updated XForm template configuration." message="Successfully created/updated XForm template configuration."/>} 
	** 
	**/
	@PUT
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel createXformDef(XFormTemplateModel xfModal) {
		logger.debug("[SDL LCR] PUT /v2/xforms/config/{id}. \nPath params: id - FOR_PATTERN_ONLY. \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		// permission check
		this.assertPermission(request, "Develop app");

		XFormTemplateDao dao = new XFormTemplateDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();

		// build our result -- include an <info/> if successful
		if(dao.addXFormTemplate(xfModal)) {
			ar.init(0, "Successfully created/updated XForm template configuration.", true, user);
			logger.debug("[SDL LCR] API succeed: PUT /v2/xforms/config/{id}. ApiResultModel returned to be marsheled.");
		} else {
			// return the generic 500 error
			ar.init(500, "Failed to create/update XForm template id: " + xfModal.getId(), true, user);
			logger.debug("[SDL LCR] API failed: PUT /v2/xforms/config/{id}. 500 - Failed to create/update XForm template! ApiResultModel returned to be marsheled.");
		}
		return ar;
	}

	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8023
	** 
	** @APITitle: Delete an XForm Template Configuration 
	** 
	** @ShortDesc: Deletes the XForm template configuration identified by its ID.
	** 
	** @Desc: If you delete an XForm template configuration, all instances of that form (comments, for example) will also be deleted.
	** 
	** @URI: /v2/xforms/config/{id}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>id: </b>The ID of the XForm template configuration to delete.
	** 
	** @RespSuccessXML: {@code <result statusCode="0" status="SUCCESS" messageKey="Successfully deleted XForm template configuration." message="Successfully deleted XForm template configuration."/>}
	** 
	** @RespSuccessJSON: {@code {"statusCode":0,"messageKey":"Successfully deleted XForm template configuration.", "message":"Successfully deleted XForm template configuration.", "status":"SUCCESS", "info":[]}} 
	** 
	** @RespFailedXML:  {@code <result statusCode="500" status="FAIL" messageKey="msg.delete.fail" message="Failed to delete these items."/>}
	** 
	** @RespFailedJSON:  {@code {"statusCode":500,"messageKey":"msg.delete.fail", "message":"Failed to delete these items.", "status":"FAIL", "info":[]}}
	**
	** @Permissions: @include_PermDevelopApp 
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/xforms/config/xform.custom</codeph></p>
	** 
	** @ExplResponse: {@code <result statusCode="0" status="SUCCESS" messageKey="Successfully deleted XForm template configuration." message="Successfully deleted XForm template configuration."/>}
	** 
	**/
	@DELETE
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel deleteXformDef(@PathParam("id") String id) {
		logger.debug("[SDL LCR] DELETE /v2/xforms/config/{id}. \nPath params: id=" + id + ". \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		// permission check
		this.assertPermission(request, "Develop app");

		ApiResultModel ar = new ApiResultModel();
		XFormTemplateDao dao = new XFormTemplateDao(request.getSession(true));
		if(dao.deleteXFormTemplate(id)) {
			ar.init(0, "Successfully deleted XForm template configuration.", true, user);
			logger.debug("[SDL LCR] API succeed: DELETE /v2/xforms/config/{id}. ApiResultModel returned to be marsheled.");
		} else {
			// return the generic 300 error
			ar.init(500, "msg.delete.fail", false, user);
			logger.debug("[SDL LCR] API failed: DELETE /v2/xforms/config/{id}. 500 - Failed to delete these items! ApiResultModel returned to be marsheled.");
		}
		return ar;
	}

}
