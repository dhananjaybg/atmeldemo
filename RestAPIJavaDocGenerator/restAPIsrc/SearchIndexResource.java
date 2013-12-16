package com.sdl.cd.livecontent.services.rest.resources;
/* $Id: src/com/sdl/cd/livecontent/services/rest/resources/SearchIndexResource.java 1.19 2013/08/05 17:27:42EDT vpevunov Exp  $ */
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sdl.cd.livecontent.dao.exist.SearchIndexDao;
import com.sdl.cd.livecontent.model.ApiResultModel;
import com.sdl.cd.livecontent.model.SearchIndexListModel;
import com.sdl.cd.livecontent.model.IndexItemModel;
import com.sdl.cd.livecontent.services.rest.RestServiceBase;

/** 
** 
** @CLASSGUID: GUID-D4FFAE43-88A5-4531-A56B-422EC6FC77E5
** 
** @ClassTitle: Search Index Management V2
** 
** @ShortDesc: Resources for creating, updating, deleting, and fetching search indexes.
** 
**/

@Path("/searchindex") 
public class SearchIndexResource extends RestServiceBase{
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8018
	** 
	** @APITitle: Fetch Search Indexes
	** 
	** @ShortDesc: Fetches the full list of search indexes.
	** 
	** @URI: /v2/searchindex
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @RespSuccessXML:  {@code <configuration><indexes indexed="true" indexed_audit="true"><index attribute="" id="search.category.indexterms" ignore="" inline="" standards="DITA" tags="" xpath="//indexterm[not(parent::indexterm)]" xpath_end="indexterm"/><index attribute="" id="search.category.figure.title" ignore="" inline="" standards="DITA" tags="" xpath="//fig/title" xpath_end="title"/><index attribute="" id="search.category.table.title" ignore="" inline="" standards="DITA,S1000D" tags="" xpath="//table/title" xpath_end="title"/><index attribute="" id="examplename" ignore="" inline="" standards="DITA" tags="" xpath="//ben" xpath_end="ben"/></indexes></configuration> }
	** 
	** @RespSuccessJSON:  {@code {"indexes":[{"indexed":"true", "indexed_audit":"true", "index":[{"attribute":"", "id":"search.category.indexterms", "ignore":"", "inline":"", "standards":"DITA", "tags":"", "xpath":"//indexterm[not(parent::indexterm)]", "xpath_end":"indexterm"},{"attribute":"", "id":"search.category.figure.title", "ignore":"", "inline":"", "standards":"DITA", "tags":"", "xpath":"//fig/title", "xpath_end":"title"},{"attribute":"", "id":"search.category.table.title", "ignore":"", "inline":"", "standards":"DITA,S1000D", "tags":"", "xpath":"//table/title", "xpath_end":"title"},{"attribute":"", "id":"examplename", "ignore":"", "inline":"", "standards":"DITA", "tags":"", "xpath":"//ben", "xpath_end":"ben"}]}]} }
	** 
	** @RespFailedXML: {@code <result message="Not Found" messageKey="Not Found" status="FAIL" statusCode="404" /> }
	** 
	** @RespFailedJSON: {@code {"statusCode":404,"messageKey":"Not Found", "message":"Not Found", "status":"FAIL", "info":[]} }
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/searchindex</codeph></p>
	** 
	** @ExplResponse: {@code <configuration><indexes indexed="true" indexed_audit="true"><index attribute="" id="search.category.indexterms" ignore="" inline="" standards="DITA" tags="" xpath="//indexterm[not(parent::indexterm)]" xpath_end="indexterm"/><index attribute="" id="search.category.figure.title" ignore="" inline="" standards="DITA" tags="" xpath="//fig/title" xpath_end="title"/><index attribute="" id="search.category.table.title" ignore="" inline="" standards="DITA,S1000D" tags="" xpath="//table/title" xpath_end="title"/><index attribute="" id="examplename" ignore="" inline="" standards="DITA" tags="" xpath="//ben" xpath_end="ben"/></indexes></configuration> }
	** 
	** 
	**/
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public SearchIndexListModel getSearchIndex(){
		this.assertPermission(request, "Use application");
		SearchIndexDao dao = new SearchIndexDao(request.getSession(true));
		SearchIndexListModel searchModel = dao.getSearchIndex();
		return searchModel;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8019
	** 
	** @APITitle: Create Search Index
	** 
	** @ShortDesc: Creates a search index item in an indexes element.
	** 
	** @Desc:  Creating a new search index does not force a reindex of the database. therefore, after creating a search index it is important to reindex the database.
	** 
	** @URI: /v2/searchindex/{name}
	** 
	** @HTTPMethod: PUT
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>name: </b> The name of the search index to be created. Just to identify the proper resource to be called. The actual value never used.
	** 
	** @PostDataXML:  {@code <index attribute="" id="exampleID" ignore="" inline="" standards="DITA" tags="" xpath="//fig/title" xpath_end="title"/>}
	** 
	** @PostDataJSON: {@code {"attribute":"", "id":"exampleID", "ignore":"", "inline":"", "standards":"DITA", "tags":"", "xpath":"//indexterm[not(parent::indexterm)]", "xpath_end":"indexterm"} }
	** 
	** @RespSuccessXML:  {@code <result message="Action = create_index" messageKey="Action = create_index" status="SUCCESS" statusCode="0"/> }
	** 
	** @RespSuccessJSON: {@code {"statusCode":0,"messageKey":"Action = create_index", "message":"Action = create_index", "status":"SUCCESS", "info":[]} } 
	** 
	** @RespFailedXML:  {@code  <result message="Action = create_index" messageKey="Action = create_index" status="FAIL" statusCode="500"/> }
	** 
	** @RespFailedJSON:  {@code {"statusCode":500,"messageKey":"Action = create_index", "message":"Action = create_index", "status":"FAIL", "info":[]}}
	** 
	** @Permissions: @include_PermManageApp
	** 
	** @ExplRequest: <p><b>PUT</b> <codeph>@include_ExampleURL/searchindex/exampleID</codeph></p>
	**					<p><b>Accept:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Content-Type:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Request Body:</b> <codeph>{@code <index attribute="" id="exampleID" ignore="" inline="" standards="DITA" tags="" xpath="//fig/title" xpath_end="title"/>}</codeph></p>
	** 
	** @ExplResponse:  {@code <result message="Action = create_index" messageKey="Action = create_index" status="SUCCESS" statusCode="0"/> }
	** 
	**/
	@PUT
	@Path("{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel createSearchIndex(IndexItemModel indexItem){
		this.assertPermission(request, "Manage app");
		SearchIndexDao dao = new SearchIndexDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();
		if(dao.createSearchIndex(indexItem)){
			ar.init(0, "Action = create_index", true, user);
		}else{
			ar.init(500, "msg.delete.fail", false, user);
		}
		return ar;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF801A
	** 
	** @APITitle: Delete Search Index
	** 
	** @ShortDesc: Deletes a search index from an indexes element.
	** 
	** @Desc: Deleting a search index does not force a reindex of the database; therefore, after deleting a search index it is important to reindex the database.
	** 
	** @URI: /v2/searchindex/{name}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>name: </b> The name of the index item to delete.
	** 
	** @RespSuccessXML:  {@code <result message="Action = delete_index" messageKey="Action = delete_index" status="SUCCESS" statusCode="0" /> }
	** 
	** @RespSuccessJSON:  {@code {"statusCode":0,"messageKey":"Action = delete_index", "message":"Action = delete_index", "status":"SUCCESS", "info":[]} }
	** 
	** @RespFailedXML:  {@code <result message="Not Found" messageKey="Not Found" status="FAIL" statusCode="404" /> }
	** 
	** @RespFailedJSON:  {@code {"statusCode":404,"messageKey":"Not Found", "message":"Not Found", "status":"FAIL", "info":[]} }
	** 
	** @Permissions: @include_PermManageApp
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/searchindex/exampleID</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = delete_index" messageKey="Action = delete_index" status="SUCCESS" statusCode="0" /> }
	** 
	**/
	@DELETE
	@Path("{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel deleteSearchIndex(@PathParam("name") String indexId){
		this.assertPermission(request, "Manage app");
		SearchIndexDao dao = new SearchIndexDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();
		if(dao.deleteSearchIndex(indexId)){
			ar.init(0, "Action = delete_index", true, user);
		}else{
			ar.init(404, "Not Found", true, user);
		}
		return ar;
	}

}
