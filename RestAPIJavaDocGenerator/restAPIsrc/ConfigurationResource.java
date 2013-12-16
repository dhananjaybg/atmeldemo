package com.sdl.cd.livecontent.services.rest.resources;
/* $Id: src/com/sdl/cd/livecontent/services/rest/resources/ConfigurationResource.java 1.20 2013/08/08 18:42:26EDT vpevunov Exp  $ */
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sdl.cd.livecontent.config.ConfigFacade;
import com.sdl.cd.livecontent.dao.exist.ConfigurationDao;
import com.sdl.cd.livecontent.model.ApiResultModel;
import com.sdl.cd.livecontent.model.ConfigurationItemModel;
import com.sdl.cd.livecontent.model.ConfigurationModel;
import com.sdl.cd.livecontent.services.rest.RestServiceBase;
import com.sdl.cd.livecontent.services.rest.exceptions.ApiException;

/** 
** 
** @CLASSGUID: GUID-A779FDAB-D480-4001-ADB3-D4A60214D2F7
** 
** @ClassTitle: Global Configuration V2
** 
** @ShortDesc: Resources to create, update, delete, and fetch the global configuration.
** 
**/

@Path("/config")
public class ConfigurationResource extends RestServiceBase{
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8001
	** 
	** @APITitle: Fetch Global Configuration
	** 
	** @ShortDesc: Fetches the entire global configuration resource.
	** 
	** @URI: /v2/config
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @RespSuccessXML:  {@code <configuration><configitem value="yes" name="audit.enable"/><configitem value="en" name="app.language"/></configuration>} 
	** 
	** @RespSuccessJSON:  {@code {"audit.enable":"yes", "app.language":"en"}}
	** 
	** @RespFailedXML: {@code <result message="Your account does not have permission to perform this action." messageKey="unauthorized.noauth" status="FAIL" statusCode="5000"/>}
	** 
	** @RespFailedJSON: {"statusCode":5000,"messageKey":"unauthorized.noauth","message":"Your account does not have permission to perform this action.","status":"FAIL","info":[]}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/config</codeph></p>
	** 
	** @ExplResponse: {@code <configuration><configitem value="yes" name="audit.enable"/><configitem value="en" name="app.language"/></configuration>}
	** 
	**/
	@GET
	@Produces(MediaType.APPLICATION_XML) 
	public ConfigurationModel getConfig() {
		// permission check
		this.assertPermission(request, "Use application");
		// return the full configuration
		ConfigurationDao dao = new ConfigurationDao(request.getSession(true));
		return dao.load();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getConfig(@QueryParam("format") String format) {
		// permission check
		this.assertPermission(request, "Use application");
		return sendGZipJSON(request, ConfigFacade.getAppConfigJson());
	}

	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8002
	** 
	** @APITitle: Fetch a Global Configuration Item
	** 
	** @ShortDesc: Fetches a single global configuration item.
	** 
	** @URI: /v2/config/{item_name}
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>item_name: </b> The name of the global configuration item to fetch.
	** 
	** @RespSuccessXML: {@code <configitem name="app.example" value="example value"/>}
	** 
	** @RespSuccessJSON: {@code {"name":"app.example", "value":"example value"}}
	** 
	** @RespFailedXML:  {@code <result statusCode="404" status="FAIL" messageKey="msg.missingresource" message="Unable to locate the requested resource."/>}
	** 
	** @RespFailedJSON:  {@code {"statusCode":404,"messageKey":"msg.missingresource", "message":"Unable to locate the requested resource.", "status":"FAIL", "info":[]}}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/config/app.skin</codeph></p>
	** 
	** @ExplResponse:  {@code <configitem value="base" name="app.skin"/>}
	** 
	**/
	@GET
	@Path("{name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ConfigurationItemModel getConfigItem(@PathParam("name") String name) throws ApiException {
		// permission check
		this.assertPermission(request, "Use application");

		ConfigurationDao dao = new ConfigurationDao(request.getSession(true));
		ConfigurationModel cm = dao.load();
		ConfigurationItemModel cim = cm.get(name);
		if(cim != null) {
			return cim;
		} else {
			throw new ApiException(200, 404, "msg.missingresource", false, user, request);
		}
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8003
	** 
	** @APITitle: Create or Update a Global Configuration Item
	** 
	** @ShortDesc: Creates or updates a global configuration item.
	** 
	** @Desc: The values of some global configuration items (such as the application skin and application language)
	** 		  are store in user's sessions.  Therefore users who are currently logged in may not see the 
	**		  change in value until they have logged out and logged back in. 
	** 
	** @URI: /v2/config/{item_name}
	** 
	** @HTTPMethod: PUT
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>item_name: </b> The name of the global configuration item to create or update. Used to identify the request on the server. Actual value never used.
	** 
	** @PostDataXML: {@code <configitem name="app.example" value="example value"/>}
	** 
	** @PostDataJSON: {@code {"name":"app.example", "value":"example value"}}
	** 
	** @RespSuccessXML: {@code <result statusCode="0" status="SUCCESS" messageKey="Action = add config item" message="Action = add config item"/>}
	** 
	** @RespSuccessJSON: {@code {"statusCode":0,"messageKey":"Action = add config item", "message":"Action = add config item", "status":"SUCCESS", "info":[]}} 
	** 
	** @RespFailedXML:  {@code <result statusCode="500" status="FAIL" messageKey="Failed, Action add config item" message="Failed, Action add config item."/>}
	** 
	** @RespFailedJSON:  {@code {"statusCode":500,"messageKey":"Failed, Action add config item", "message":"Failed, Action add config item", "status":"FAIL", "info":[]}}
	** 
	** @Permissions: @include_PermManageApp
	** 
	** @ExplRequest: <p><b>PUT</b> <codeph>@include_ExampleURL/config/app.example</codeph></p>
	**					<p><b>Accept:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Content-Type:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Request Body:</b>  <codeph>{@code <configitem name="app.example" value="example value"/>}</codeph></p>
	** 
	** @ExplResponse: {@code {"statusCode":0,"messageKey":"Action = add config item", "message":"Action = add config item", "status":"SUCCESS", "info":[]}}
	** 
	**/
	@PUT @Path("{name}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel setConfigItem(ConfigurationItemModel configItem)  throws ApiException {
		// permission check
		this.assertPermission(request, "Manage application");

		ConfigurationDao dao = new ConfigurationDao(request.getSession(true));
		boolean check = dao.addItem(configItem);
		ApiResultModel ar = new ApiResultModel();

		// build our result -- include an <info/> if successful
		if(check) { 
			ar.init(0, "Action = add config item", true, user);
		} else {
			// return the generic 500 error
			throw new ApiException(200, 500, "Failed, Action add config item", true, user, request);
		}
		return ar;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8004
	** 
	** @APITitle: Delete a Global Configuration Item
	** 
	** @ShortDesc: Deletes a single global configuration item identified by name.
	** 
	** @Desc: The following global configuration items cannot be deleted: "app.skin", "app.language", "audit.enable", "cache.data.enable", 
	**	"cache.xsl.enable", "search.filter.enable", "audit.aggregation.enable", 
	**	"audit.garbagecollection.enable", "xforms.aggregation.enable", 
	**	"analytics.optimization.enable", "social.enable", "audit.aggregation.age", "index.binary.formats", "search.ignore", "prepared", "working_status".
	** 
	** @URI: /v2/config/{item_name} 
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>item_name: </b> The name of the global configuration name to delete.
	** 
	** @RespSuccessXML:  {@code <result statusCode="0" status="SUCCESS" messageKey="Action = delete config item" message="Action = delete config item"><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" value="1" name="Deleted objects"/></result>} 
	** 
	** @RespSuccessJSON:  {@code {"statusCode":0,"messageKey":"Action = delete config item", "message":"Action = delete config item", "status":"SUCCESS", "info":[{"name":"Deleted objects", "value":"1"}]}} 
	** 
	** @RespFailedXML:  {@code <result statusCode="500" status="FAIL" messageKey="Failed, Action delete config item" message="Failed, Action delete config item"/>} 
	** 
	** @RespFailedJSON:  {@code {"statusCode":500,"messageKey":"Failed, Action delete config item", "message":"Failed, Action delete config item", "status":"FAIL", "info":[]}}
	** 
	** @Permissions: @include_PermManageApp
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/config/app.example</codeph></p>
	** 
	** @ExplResponse: {@code <result statusCode="0" status="SUCCESS" messageKey="Action = delete config item" message="Action = delete config item"><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" value="1" name="Deleted objects"/></result>}
	** 
	**/
	@DELETE 
	@Path("{name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel deleteConfigItem(@PathParam("name") String name) throws ApiException {
		// permission check
		this.assertPermission(request, "Manage application");

		ApiResultModel ar = new ApiResultModel();
		// insure that this is not on the protected list for the configuration (no delete):
		if(ConfigurationModel.checkProtected(name)) {
			throw new ApiException(200, 301, "msg.delete.deny", false, user, request);
		} else {
			ConfigurationDao dao = new ConfigurationDao(request.getSession(true));
			if(dao.deleteItem(name)) {
				ar.init(0, "Action = delete config item", true, user);
				// to be backwards-compat, include this listing of the number of deleted objects
				ar.addChildObject("Deleted objects", "1");
			} else {
				// return the generic 500 error
				throw new ApiException(200, 301, "msg.delete.deny", false, user, request);
			}
		}
		return ar;
	}
}
