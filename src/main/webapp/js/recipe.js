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
        activeClass: 'dragActiveTest',
        hoverClass: 'dragHoverTest',
        drop: function (event, ui) {
             doDrop([
             {name: 'from', value: jQuery(ui.draggable).data("id")},
             {name: 'to', value: jQuery(this).data("id")}
             ]);
        }
    });
}
