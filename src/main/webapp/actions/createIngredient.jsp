<%-- 
    Document   : createIngredient
    Created on : Mar 4, 2014, 2:13:59 PM
    Author     : Gof
--%>

<%@page import="dk.fambagge.recipes.domain.Measure"%>
<%@page import="dk.fambagge.recipes.db.MeasureType"%>
<%@page import="dk.fambagge.recipes.domain.Ingredient"%>
<%@page import="dk.fambagge.recipes.web.CreateIngredientData"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id="postData" scope="page" class="dk.fambagge.recipes.web.CreateIngredientData">
    <jsp:setProperty name="postData" property="*" />
</jsp:useBean>

<%
    Measure preferredMeasure = MeasureType.getTypeFromString(postData.getPreferedMeasure());
    
    Ingredient ingredient = new Ingredient();
    ingredient.setName(postData.getName());
    ingredient.setEnergyPerHundred(postData.getEnergyPerHundred());
    ingredient.setWeightToVolume(postData.getWeightToVolume());
    ingredient.setPreferredMeasure(preferredMeasure);
    ingredient.save();
    
    response.sendRedirect("../index.jsp");
%>