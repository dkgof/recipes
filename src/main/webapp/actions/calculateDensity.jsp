<%-- 
    Document   : createIngredient
    Created on : Mar 4, 2014, 2:13:59 PM
    Author     : Gof
--%>

<%@page import="dk.fambagge.recipes.domain.RecipeIngredient"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="dk.fambagge.recipes.domain.Measure"%>
<%@page import="dk.fambagge.recipes.db.MeasureType"%>
<%@page import="dk.fambagge.recipes.domain.Ingredient"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<jsp:useBean id="postData" scope="page" class="dk.fambagge.recipes.web.CalculateDensityData">
    <jsp:setProperty name="postData" property="*" />
</jsp:useBean>
<%
    Logger.getLogger("Recepies").info("getCalculateDensityVolumeUnit "+postData.getCalculateDensityVolumeUnit());
    Logger.getLogger("Recepies").info("getCalculateDensityWeightUnit "+postData.getCalculateDensityWeightUnit());
    Logger.getLogger("Recepies").info("getCalculateDensityVolume "+postData.getCalculateDensityVolume());
    Logger.getLogger("Recepies").info("getCalculateDensityWeight "+postData.getCalculateDensityWeight());
    
    Measure weightMeassure = MeasureType.getTypeFromString(postData.getCalculateDensityWeightUnit());
    Measure volumeMeassure = MeasureType.getTypeFromString(postData.getCalculateDensityVolumeUnit());
    
    double grams = weightMeassure.convertTo(postData.getCalculateDensityWeight(), Measure.Weight.GRAM);
    double liters = volumeMeassure.convertTo(postData.getCalculateDensityVolume(), Measure.Volume.LITER);
    
    double density = grams / liters;
    
    out.println("{\"density\":"+density+"}");
%>