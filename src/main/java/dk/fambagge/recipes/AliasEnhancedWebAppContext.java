/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes;

import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.jetty.webapp.WebAppContext;

public class AliasEnhancedWebAppContext extends WebAppContext {

    @Override
    public String getResourceAlias(String alias) {

        @SuppressWarnings("unchecked")
        Map<String, String> resourceAliases =
                getResourceAliases();

        if (resourceAliases == null) {
            return null;
        }

        for (Entry<String, String> oneAlias : 
            resourceAliases.entrySet()) {

            if (alias.startsWith(oneAlias.getKey())) {
                return alias.replace(oneAlias.getKey(), oneAlias.getValue());
            }
        }

        return null;
    }
}