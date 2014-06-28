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
                            var data = new FormData($("#addIngredientsForm")[0]);
                            var html = new XMLHttpRequest();
                            html.open("POST", "actions/createIngredient.jsp", true);
                            html.send(data);
                            $(html).on("readystatechanged", function() {
                                if(html.readyState === 4) {
                                    if(html.status === 200) {
                                        console.log("Ingredient added successfully");
                                    } else {
                                        console.log("Error adding ingredient: ", html.responseText);
                                    }
                                }
                            });
                            $(this).dialog("close");
                        },
                        "Cancel": function() {
                            $("input#name").val("");
                            $("input#weightToVolume").val("");
                            $("input#energyPerHundred").val("");
                            $("input#preferedMeasure").val("");
                            $(this).dialog("close");
                        }
                    }
                });
                
                $("#addIngredientButton").on("click", function() {
                    $("div#createIngredientDialog").dialog("open");
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
            <form action="actions/createIngredient.jsp" method="POST" id="addIngredientsForm">
                <label for="name">Name:</label><input type="text" id="name" name="name" /><br />
                <label for="weightToVolume">Weight in grams of 1L:</label><input type="number" step="any" min="0" id="weightToVolume" name="weightToVolume" /><a href="">Calculate</a><br />
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
    </body>
</html>