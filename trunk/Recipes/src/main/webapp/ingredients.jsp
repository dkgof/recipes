<%-- 
    Document   : index
    Created on : Mar 4, 2014, 2:21:32 PM
    Author     : Gof
--%>

<%@page import="dk.fambagge.recipes.domain.Measure"%>
<%@page import="dk.fambagge.recipes.domain.Ingredient"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link href="js/libs/jqueryui/css/base/minified/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
        
        <script src="js/libs/jquery/jquery.min.js" type="text/javascript"></script>
        <script src="js/libs/jqueryui/jquery-ui.min.js" type="text/javascript"></script>
        
        <title>Recipes - Ingredients</title>
    </head>
    <body>
        <h1>Recipes- Ingredients</h1>
        
        <h2>Current ingredients</h2>
        <%
            for(Ingredient ingredient : Ingredient.getAll()) {
                out.println("<div class='ingredients'>");
                out.println(ingredient.toHtml());
                out.println("</div>");
            }
        %>
        
        
        <h2>Add new ingredient</h2>
        <form action="actions/createIngredient.jsp" method="POST">
            Name: <input type="text" name="name" /><br />
            Weight in grams of 1L: <input type="number" step="any" min="0" name="weightToVolume" /><a href="">Calculate</a><br />
            Kilojoule in 100g: <input type="number" step="any" min="0" name="energyPerHundred" /><br />
            Custom measures: <a href="">Add</a><br />
            Preferred measure: <select name="preferedMeasure">
            <%
                out.println("<option disabled>---Weight---</option>");
                for(Measure.Weight w : Measure.Weight.values()) {
                    out.println("<option value=\""+w.toString()+"\">"+w.getSymbol()+"</option>");
                }
                out.println("<option disabled>---Volume---</option>");
                for(Measure.Volume v : Measure.Volume.values()) {
                    out.println("<option value=\""+v.toString()+"\">"+v.getSymbol()+"</option>");
                }
            %>
            </select>
            <br />
            <input type="submit" value="Add Ingredient" />
        </form>
    </body>
</html>
