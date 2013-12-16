<?xml version="1.0" encoding="utf-8" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<!-- $Id: restClass.xsl 1.12 2013/06/25 16:07:54EDT milind Exp  $ -->
	<xsl:output  method="xml" encoding="UTF-8" media-type="application/xml" indent="yes"/>
	<xsl:preserve-space elements="*"/>
	<xsl:include href="includes.xsl"/>

	<xsl:template match="/CLASS_WRAPPER">
		<reference> 
			<xsl:attribute name="id">
				<xsl:apply-templates select="CLASSGUID"/>
			</xsl:attribute>
			<xsl:apply-templates select="ClassTitle"/>
			<xsl:apply-templates select="ShortDesc"/>
			<xsl:if test="count(IndexTerm)">
				<prolog><metadata><keywords>
					<xsl:apply-templates select="IndexTerm"/>
				</keywords></metadata></prolog>
			</xsl:if>
			<refbody>
				<section>
					<xsl:apply-templates select="Desc"/>
					<p>
						<table> 
							<tgroup cols="6">
								<colspec colnum="1" colname="col1" colwidth="*"/>
								<colspec colnum="2" colname="col2" colwidth="*"/>
								<colspec colnum="3" colname="col3" colwidth="*"/>
								<colspec colnum="4" colname="col4" colwidth="*"/>
								<colspec colnum="5" colname="col5" colwidth="*"/>
								<colspec colnum="6" colname="col6" colwidth="*"/>
								<thead> 
									<row> 
									  <entry colname="col1"> API Call </entry> 
									  <entry colname="col2"> HTTP Method </entry> 
									  <entry colname="col3"> URL </entry> 
									  <entry colname="col4"> Format </entry> 
									  <entry colname="col5"> Form Data </entry> 
									  <entry colname="col6"> Permissions </entry> 
									</row>
								</thead> 
								<tbody> 
									<xsl:apply-templates select="API_WRAPPER">
										 <xsl:sort select="APITitle"/>
									</xsl:apply-templates>
								</tbody> 
							</tgroup> 
						</table> 
					</p> 
				</section> 
			</refbody>
		</reference>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API WRAPPER                   -->
<!-- ============================================= -->
	<xsl:template match="API_WRAPPER">
		<row> 
			<entry colname="col1">
				<xref format="dita" scope="local" type="reference">
					<xsl:attribute name="href"><xsl:apply-templates select="APIGUID"/>#<xsl:apply-templates select="APIGUID"/></xsl:attribute>
					<xsl:apply-templates select="APITitle"/>
				</xref>
			</entry>
			<entry colname="col2"><codeph><xsl:apply-templates select="HTTPMethod"/></codeph></entry> 
			<entry colname="col3"><filepath><xsl:apply-templates select="URI"/></filepath></entry> 
			<entry colname="col4"><codeblock><xsl:apply-templates select="Accept"/></codeblock></entry> 
			<entry colname="col5"><codeblock><xsl:apply-templates select="PostDataJSON"/></codeblock></entry>
			<entry colname="col6"><uicontrol><xsl:apply-templates select="Permissions"/></uicontrol></entry> 
		</row> 
	</xsl:template>

<!-- ============================================= -->
<!-- 			API GUID                   -->
<!-- ============================================= -->
	<xsl:template match="APIGUID">
		 <xsl:apply-templates/>
	</xsl:template>

<!-- ============================================= -->
<!-- 			zStyling Tags   -->
<!-- ============================================= -->        
	<xsl:template match="b | i | u | p">
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			 <xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="*" mode="copy">
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates mode="copy"/>
		</xsl:copy>
	</xsl:template>

<!-- ============================================= -->
<!-- 			CLASS Title                  -->
<!-- ============================================= -->
	<xsl:template match="ClassTitle">
		<title><xsl:apply-templates/></title>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API Title                  -->
<!-- ============================================= -->
	<xsl:template match="APITitle">
		<xsl:apply-templates/>
	</xsl:template>
	
<!-- ============================================= -->
<!-- 			Class Short Description            -->
<!-- ============================================= -->
	<xsl:template match="ShortDesc">
		<shortdesc><xsl:apply-templates/></shortdesc>
	</xsl:template>
	
<!-- ============================================= -->
<!-- 			Class Description            -->
<!-- ============================================= -->
	<xsl:template match="Desc">
		<p><xsl:apply-templates/></p>
	</xsl:template>

<!-- ============================================= -->
<!-- 			Class Index Terms            -->
<!-- ============================================= -->
	<xsl:template match="IndexTerm">
        <indexterm><xsl:apply-templates/></indexterm>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API URI            -->
<!-- ============================================= -->
	<xsl:template match="URI">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="HTTPMethod">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="Accept">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="PostDataJSON">
        <xsl:apply-templates/>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API Permissions					   -->
<!-- ============================================= -->
	<xsl:template match="Permissions">
		<xsl:apply-templates/>
	</xsl:template>


</xsl:stylesheet>
