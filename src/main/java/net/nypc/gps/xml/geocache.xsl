<xsl:stylesheet version="2.0" 
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:groundspeak="http://www.groundspeak.com/cache/1/0/1"
>
    
    <xsl:output  method="xml" indent="yes" />
      
   
    <xsl:template match="gpx">
       <xsl:variable name="this" select="." />
       <Geocaches xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
           xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:groundspeak="http://www.groundspeak.com/cache/1/0/1"
       >
           <name><xsl:value-of select="$this/name" /></name> 
           <desc><xsl:value-of select="$this/desc" /></desc>
           <author><xsl:value-of select="author" /></author>
           <email><xsl:value-of select="email" /></email>
           <time><xsl:value-of select="time" /></time> 
           <keywords><xsl:value-of select="keywords" /></keywords>
           <bounds><xsl:value-of select="bounds" /></bounds>
           
           <xsl:apply-templates select="wpt" />
           <xsl:apply-templates select="groundspeak:cache"/>
           <xsl:apply-templates select="*"/>
       </Geocaches>
    </xsl:template>
    
 
	<xsl:template match="wpt" >
	<xsl:variable name="this" select="." />
	<geocache> 	
    <latitude><xsl:value-of select="@lat" /></latitude>
    <longitude><xsl:value-of select="@long" /></longitude>
    <gccode><xsl:value-of select="name" /></gccode>
    <desc><xsl:value-of select="desc" /></desc>
    <url><xsl:value-of select="url" /></url>
    <urlname><xsl:value-of select="urlname" /></urlname>
    <sym><xsl:value-of select="sym" /></sym>
    <type><xsl:value-of select="type" /></type> 
    </geocache>
     
    </xsl:template>
    
     <xsl:template match="groundspeak:cache" xmlns:groundspeak="http://www.groundspeak.com/cache/1/0/1">
    <xsl:variable name="this" select="." />
      <groundspeak_id><xsl:value-of select="@id" /></groundspeak_id>
      <groundspeak_name><xsl:value-of select="name" /></groundspeak_name>
      <groundspeak_placed_by><xsl:value-of select="placed_by" /></groundspeak_placed_by>
      <groundspeak_owner ><xsl:value-of select="owner" /></groundspeak_owner>
      <groundspeak_type><xsl:value-of select="type" /></groundspeak_type>
      <groundspeak_container><xsl:value-of select="container" /></groundspeak_container>
       
      <groundspeak_difficulty><xsl:value-of select="difficulty" /></groundspeak_difficulty>
      <groundspeak_terrain><xsl:value-of select="terrain" /></groundspeak_terrain>
      <groundspeak_country><xsl:value-of select="country" /></groundspeak_country>
      <groundspeak_state><xsl:value-of select="state" /></groundspeak_state>
      <groundspeak_shortdesc><xsl:value-of select="short_description" /></groundspeak_shortdesc>
      <groundspeak_longdesc><xsl:value-of select="long_description" /></groundspeak_longdesc>
      <xsl:apply-templates select="attributes" />
      <xsl:apply-templates select="logs" />
      <xsl:apply-templates select="*" />
    </xsl:template>
    
    <xsl:template match="groundspeak:attributes" >
    
    </xsl:template>
    
    <xsl:template match="groundspeak:attribute" >
    
    </xsl:template>
    <xsl:template match="groundspeak:logs" >
    
    </xsl:template>
    
    <xsl:template match="groundspeak:log" >
    
    </xsl:template>
    
    
       
    <!-- <xsl:template match="*">     
       <xsl:apply-templates /> 
    </xsl:template>  --> 
     
</xsl:stylesheet>