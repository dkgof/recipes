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

        <script type="text/javascript">
            $(function() {
                $("div#createIngredientDialog").dialog({
                    autoOpen: false,
                    title: "Add ingredient",
                    width: 600,
                    buttons: {
                        "Add": function() {
                            $.post("actions/createIngredient.jsp", $("#addIngredientsForm").serialize());
                            
                            $("input#name").val("");
                            $("input#weightToVolume").val("");
                            $("input#energyPerHundred").val("");
                            $("select#preferedMeasure").val("");
                            $(this).dialog("close");
                        },
                        "Cancel": function() {
                            $("input#name").val("");
                            $("input#weightToVolume").val("");
                            $("input#energyPerHundred").val("");
                            $("select#preferedMeasure").val("");
                            $(this).dialog("close");
                        }
                    }
                });
                
                $("div#calculateDensityDialog").dialog({
                    autoOpen: false,
                    title: "Calculate Density",
                    width: 600,
                    buttons: {
                        "Calculate": function() {
                            $(this).dialog("close");
                        },
                        "Cancel": function() {
                            $("input#calculateDensityWeight").val("");
                            $("input#calculateDensityVolume").val("");
                            $("select#calculateDensityVolumeUnit").val("");
                            $("select#calculateDensityWeightUnit").val("");
                            $(this).dialog("close");
                        }
                    }
                });

                $("#addIngredientButton").on("click", function() {
                    $("div#createIngredientDialog").dialog("open");
                    return false;
                });

                $("#calculateDensityButton").on("click", function() {
                    $("div#calculateDensityDialog").dialog("open");
                    return false;
                });
            });
        </script>
        
        <title>Recipes - Ingredients</title>
    </head>
    <body>
        <h1>Recipes- Ingredients</h1>

        <h2>Current ingredients</h2>
        <%
            for (Ingredient ingredient : Ingredient.getAll()) {
                out.println("<div class='ingredients'>");
                out.println(ingredient.toHtml());
                out.println("</div>");
            }
        %>

        <a href="" id="addIngredientButton">Add ingredient</a>
        <div id="createIngredientDialog">
            <form id="addIngredientsForm">
                <label for="name">Name:</label><input type="text" id="name" name="name" /><br />
                <label for="weightToVolume">Weight in grams of 1L:</label><input type="number" step="any" min="0" id="weightToVolume" name="weightToVolume" /><a href="" id="calculateDensityButton">Calculate</a><br />
                <label for="energyPerHundred">Kilojoule in 100g:</label><input type="number" step="any" min="0" id="energyPerHundred" name="energyPerHundred" /><br />
                Custom measures: <a href="">Add</a><br />
                <label for="preferedMeasure">Preferred measure:</label><select id="preferedMeasure" name="preferedMeasure">
                    <%
                        out.println("<option disabled>---Weight---</option>");
                        for (Measure.Weight w : Measure.Weight.values()) {
                            out.println("<option value=\"" + w.toString() + "\">" + w.getSymbol() + "</option>");
                        }
                        out.println("<option disabled>---Volume---</option>");
                        for (Measure.Volume v : Measure.Volume.values()) {
                            out.println("<option value=\"" + v.toString() + "\">" + v.getSymbol() + "</option>");
                        }
                    %>
                </select>
           </form>
        </div>
        <div id="calculateDensityDialog">
            <form id="calculateDensityForm">
                <label for="calculateDensityWeight">Weight:</label><input type="number" step="any" min="0" id="calculateDensityWeight" name="calculateDensityWeight" />
                <select id="calculateDensityWeightUnit" name="calculateDensityWeightUnit">
                    <%
                        for (Measure.Weight w : Measure.Weight.values()) {
                            out.println("<option value=\"" + w.toString() + "\">" + w.getSymbol() + "</option>");
                        }
                    %>
                </select><br />
                <label for="calculateDensityVolume">Volume:</label><input type="number" step="any" min="0" id="calculateDensityVolume" name="calculateDensityVolume" />
                <select id="calculateDensityVolumeUnit" name="calculateDensityVolumeUnit">
                    <%
                        for (Measure.Volume v : Measure.Volume.values()) {
                            out.println("<option value=\"" + v.toString() + "\">" + v.getSymbol() + "</option>");
                        }
                    %>
                </select><br />
           </form>
        </div>
    </body>
</html>
