/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.fambagge.recipes.domain.Recipe;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rolf
 */
@WebServlet("/json")
public class JsonServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public JsonServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setHeaders(resp);

        Set<Recipe> recipes = Recipe.getAll();

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode recipesJsonArray = mapper.createArrayNode();

        recipes.forEach((recipe) -> {
            ObjectNode recipesJson = recipesJsonArray.addObject();

            recipesJson.put("id", recipe.getId());
            recipesJson.put("name", recipe.getName());
            recipesJson.put("servings", recipe.getServings());
            recipesJson.put("calories", recipe.getEnergyInCalories());
            try {
                recipesJson.put("media", recipe.getPrimaryMedia().getThumbnailUrl(256));
            } catch (Exception e) {

            }

            ArrayNode ingredientsJsonArray = recipesJson.putArray("ingredients");

            recipe.getIngredients().forEach((ingredient) -> {
                ObjectNode ingredientJson = ingredientsJsonArray.addObject();
                ingredientJson.put("name", ingredient.getIngredient().getName());
                ingredientJson.put("amount", ingredient.getAmount());
                ingredientJson.put("unit", ingredient.getMeasure().getSymbol());
            });

            ArrayNode stepsJsonArray = recipesJson.putArray("steps");

            recipe.getSteps().stream().sorted((rs1, rs2) -> {
                return Integer.compare(rs1.getSortOrder(), rs2.getSortOrder());
            }).forEachOrdered((step) -> {
                ObjectNode stepJson = stepsJsonArray.addObject();
                stepJson.put("description", step.getDescription());
            });
        });

        String jsonString = recipesJsonArray.toPrettyString();

        resp.getWriter().println(jsonString);
        resp.getWriter().flush();
    }

    //for Preflight
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setHeaders(HttpServletResponse resp) {
        resp.setHeader("Content-Type", "application/json; charset=utf-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET");
    }
}
