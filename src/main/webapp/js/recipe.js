function initDND() {
    console.log("Setting up DND!");
    
    jQuery("#recipeForm\\:recipeSteps").find(".draggable").draggable({
        helper: "clone",
        revert: "true",
        cursorAt: {
            left: 150,
            top: 15
        },
        start: function (e, ui)
        {
            $(ui.helper).addClass("ui-draggable-helper");
        }
        //zIndex: ++PrimeFaces.zindex
    });

    jQuery("#recipeForm\\:recipeSteps").find(".draggable").droppable({
        accept: "#recipeForm\\:recipeSteps .draggable",
        activeClass: 'dragActiveTest',
        hoverClass: 'dragHoverTest',
        drop: function (event, ui) {
             doDropStep([
             {name: 'from', value: jQuery(ui.draggable).data("id")},
             {name: 'to', value: jQuery(this).data("id")}
             ]);
        }
    });
}

function initGroupDND() {
    console.log("Group DND!");
    
    jQuery("#recipeForm .ingredientItem.draggable").draggable({
        helper: "clone",
        revert: "true",
        cursorAt: {
            left: 150,
            top: 15
        },
        start: function (e, ui)
        {
            $(ui.helper).addClass("ui-draggable-helper");
        }
    });

    jQuery("#recipeForm .recipeGroup").droppable({
        accept: "#recipeForm .ingredientItem",
        activeClass: 'dragActiveTest',
        hoverClass: 'dragHoverTest',
        drop: function (event, ui) {
            doDropIngredient([
                {name: 'ingredient', value: jQuery(ui.draggable).find(".ingredientId").data("id")},
                {name: 'group', value: jQuery(this).find(".groupId").data("id")}
            ]);
        }
    });
    
    jQuery("#recipeForm\\:ingredientList").droppable({
        accept: "#recipeForm .ingredientItem",
        activeClass: 'dragActiveTest',
        hoverClass: 'dragHoverTest',
        drop: function (event, ui) {
            console.log("Drop on me!");
            
            doDropIngredient([
                {name: 'ingredient', value: jQuery(ui.draggable).find(".ingredientId").data("id")},
                {name: 'group', value: -1}
            ]);
        }
    });
}
