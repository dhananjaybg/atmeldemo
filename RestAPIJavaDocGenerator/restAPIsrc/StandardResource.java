package com.sdl.cd.livecontent.services.rest.resources;
/* $Id: src/com/sdl/cd/livecontent/services/rest/resources/StandardResource.java 1.16 2013/08/05 17:27:42EDT vpevunov Exp  $ */
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType; 

import com.sdl.cd.livecontent.dao.exist.StandardDao;
import com.sdl.cd.livecontent.model.ApiResultModel;
import com.sdl.cd.livecontent.model.StandardListModel;
import com.sdl.cd.livecontent.model.StandardModel;
import com.sdl.cd.livecontent.model.StandardItemModel;
import com.sdl.cd.livecontent.services.rest.RestServiceBase;
import com.sdl.cd.livecontent.services.rest.exceptions.ApiException;
 
/** 
** 
** @CLASSGUID: GUID-AAA95141-0000-013F-A0E7-2C91FFFF8000
** 
** @ClassTitle: Standards Configuration V2 
** 
** @ShortDesc: Create, Fetch or Delete Standards supported by @include_AppName
** 
**/

 
@Path("/standards")
public class StandardResource extends RestServiceBase{

	
	/** 
	** 
	** @APIGUID: GUID-AAA95141-0000-013F-A0E7-2C91FFFF8001
	** 
	** @APITitle: Fetch Standards
	** 
	** @ShortDesc: Fetches the list of Standards
	** 
	** @URI: v2/standards
	** 
	** @HTTPMethod: GET
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @RespSuccessXML:  {@code <configuration><standards><standard id="DITA"/></standards></configuration>}
	**
	** @RespSuccessJSON: {@code {"standards":[{"standard":[{"id":"DITA"}]}]}   }
	** 
	** @RespFailedXML: {@code <result message="Standard Configuration resource is not found!" messageKey="Standard Configuration resource is not found!" status="FAIL" statusCode="404" /> }
	** 
	** @RespFailedJSON: {@code {"statusCode":404,"messageKey":"Standard Configuration resource is not found!", "message":"Standard Configuration resource is not found!", "status":"FAIL", "info":[]} }
	** 
	** @Permissions: @include_PermDevelopApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/standards</codeph></p>
	** 
	** @ExplResponse:  {@code <configuration><standards><standard id="DITA"/></standards></configuration>}
	** 
	**/	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public StandardListModel getStandardList() throws ApiException {
		this.assertPermission(request, "Use application"); 
		StandardDao dao = new StandardDao(request.getSession(true));
		StandardListModel standardModel = dao.getStandardList();
		if (standardModel == null) throw new ApiException(200, 404, "Standard Configuration resource is not found!", true, user, request);
		return standardModel; 
	}

	
	
	/** 
	**  
	** @APIGUID:GUID-AAA95141-0000-013F-A0E7-2C91FFFF8002
	** 
	** @APITitle: Create a Standard 
	** 
	** @ShortDesc: Creates a new Standard.
	** 
	** @Desc:
	** 
	** @URI: /v2/standards/{name}
	** 
	** @HTTPMethod: POST
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>name: </b> The name of the standard to be created. 
	** 
	** @RespSuccessXML:  {@code <result message="Action = create_standard" messageKey="Action = create_standard" status="SUCCESS" statusCode="0" /> }
	** 
	** @RespSuccessJSON: {@code {"statusCode":0,"messageKey":"Action = create_standard","message":"Action = create_standard","status":"SUCCESS","info":[]} } 
	** 
	** @RespFailedXML:  {@code  <result message="Action = create_standard" messageKey="Action = create_standard" status="FAIL" statusCode="400"/> }
	** 
	** @RespFailedJSON:  {@code {"statusCode":400,"messageKey":"Action = create_standard", "message":"Action = create_standard", "status":"FAIL", "info":[]}}
	** 
	** @Permissions: @include_PermManageApp
	** 
	** @ExplRequest: <p><b>POST</b> <codeph>@include_ExampleURL/standards/exampleID</codeph></p>
	**					<p><b>Accept:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Content-Type:</b> <codeph>applicaton/xml</codeph></p>
	**					
	** 
	** @ExplResponse:  {@code <result message="Action = create_standard" messageKey="Action = create_standard" status="SUCCESS" statusCode="0" /> }
	** 
	**/   
	@POST
	@Path("{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel createStandardItem (@PathParam("name") String standID){
		ApiResultModel ar = new ApiResultModel();
		
		standID = standID.toUpperCase();
		this.assertPermission(request, "Manage app");
		StandardDao dao = new StandardDao(request.getSession(true));
		if (standID.length() <= 0) {
			ar.init(1000, "msg.missingparams", false, user);
			return ar;
		} else if (dao.isSupportedStandard(standID)) { 
			ar.init(100, "msg.duplicate", false, user);
			return ar;
		} else if (dao.containsIllegalCharacters(standID)) {
			ar.init(1251, "invalid.characters.alphanumeric", false, user);
			return ar;
		} else if (dao.createStandardItem(standID)) { 
			ar.init(0, "Action = add standard", true, user);
			return ar;
		} else {
			ar.init(400, "msg.server.error", false, user);
			return ar;
		}
	}

 
	
	/** 
	** 
	** @APIGUID:GUID-AAA95141-0000-013F-A0E7-2C91FFFF8003
	** 
	** @APITitle: Delete a Standard
	** 
	** @ShortDesc: Deletes a Standard from the list of standards.
	** 
	** @Desc: 
	** 
	** @URI: /v2/standards/{name}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>name: </b> The name of the standard item to delete.
	** 
	** @RespSuccessXML:  {@code <result message="Action = delete_standard" messageKey="Action = delete_standard" status="SUCCESS" statusCode="0" /> }
	** 
	** @RespSuccessJSON:  {@code {"statusCode":0,"messageKey":"Action = delete_standard","message":"Action = delete_standard","status":"SUCCESS","info":[]}}
	** 
	** @RespFailedXML:  {@code <result message="Not Found" messageKey="Not Found" status="FAIL" statusCode="404" /> }
	** 
	** @RespFailedJSON:  {@code {"statusCode":404,"messageKey":"Not Found", "message":"Not Found", "status":"FAIL", "info":[]} }
	** 
	** @Permissions: @include_PermManageApp
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/standards/exampleID</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = delete_standard" messageKey="Action = delete_standard" status="SUCCESS" statusCode="0" /> }
	** 
	**/
	@DELETE
	@Path("{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel deleteStandardItem(@PathParam("name") String standardId){
		standardId = standardId.toUpperCase();
		this.assertPermission(request, "Manage app");
		StandardDao dao = new StandardDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();
		if (dao.containsIllegalCharacters(standardId)){
			ar.init(1251, "invalid.characters.alphanumeric", false, user);
			return ar;
		}else if(dao.deleteStandardItem(standardId)){
			ar.init(0, "Action = delete_standard", true, user);
		}else{
			ar.init(404, "Not Found", false, user);
		}
		return ar;
	}
	

}
