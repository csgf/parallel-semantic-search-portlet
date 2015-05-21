(function(){
    Renderer = function(canvas)
    {
        var canvas = $(canvas).get(0);
        var ctx = canvas.getContext("2d");
        var gfx = arbor.Graphics(canvas);
        var particleSystem;
        var dom = $(canvas);
        var that = {
            init:function(system){
                //INIZIALIZZAZIONE
                // alert("INIT1");
                particleSystem = system;
                particleSystem.screenSize(canvas.width, canvas.height); 
                particleSystem.screenPadding(80);
                that.initMouseHandling();
            },
       
            redraw:function(){
                //IN FASE DI REDRAW, EFFETTUO IL REFRESH
                ctx.fillStyle = "white";
                ctx.fillRect(0,0, canvas.width, canvas.height); //COLORO L'AREA INTERA
                var nodeBoxes = {};
    
  
                particleSystem.eachNode( //OGNI VERTICE
                    function(node, pt){  //OTTENGO IL TOP E IL PUNTO 
                        //                        var w = 10;   //LARGHEZZA
                        //                        nodeBoxes[node.name] = [pt.x-w/2, pt.y-w/2, w,w]
                        if (node.data.alpha===0) return
                        //                        
                        //                         var label = node.data.label||"";
                        //                        ctx.fillStyle = "blue"; //COLORE CHIARO
                        //                        ctx.fillRect(pt.x-w/2, pt.y-w/2, w,w); //DISEGNO
                        //                        ctx.fillStyle = "black"; //COLORE DEL FRONT
                        //                        ctx.font = 'italic 11px sans-serif'; //CARATTERE
                        //                        ctx.fillText (node.name, pt.x+8, pt.y+8); //SCRIVO IL NOME DI OGNI NODO

                        //Codice copiato da render statico
                
                        var label = node.data.label||""
                        var w = ctx.measureText(""+label).width + 10
                        if (!(""+label).match(/^[ \t]*$/)){
                            pt.x = Math.floor(pt.x)
                            pt.y = Math.floor(pt.y)
                        }else{
                            label = null
                        }

                        // draw a rectangle centered at pt
                        if (node.data.color) ctx.fillStyle = node.data.color
                        else ctx.fillStyle = "rgba(0,0,0,.2)"
                        if (node.data.color=='none') ctx.fillStyle = "white"

                        if (node.data.shape=='dot'){
                            gfx.oval(pt.x-w/2, pt.y-w/2, w,w, {
                                fill:ctx.fillStyle
                            })
                            nodeBoxes[node.name] = [pt.x-w/2, pt.y-w/2, w,w]
                        }else{
                            gfx.rect(pt.x-w/2, pt.y-10, w,20, 4, {
                                fill:ctx.fillStyle
                            })
                            nodeBoxes[node.name] = [pt.x-w/2, pt.y-11, w, 22]
                        }
                  
                        //                        gfx.rect(pt.x-w/2, pt.y-10, w,20, 4, {
                        //                            fill:ctx.fillStyle
                        //                        })
                        //                        nodeBoxes[node.name] = [pt.x-w/2, pt.y-11, w, 22]
                    

                        // draw the text
                        if (label){
                            ctx.font = "12px Helvetica"
                            ctx.textAlign = "center"
                            ctx.fillStyle = "black"
                            if (node.data.color=='none') ctx.fillStyle = '#333333'
                            ctx.fillText(label||"", pt.x, pt.y+4)
                            ctx.fillText(label||"", pt.x, pt.y+4)
                        }



                    }); 
    
                particleSystem.eachEdge( //PER OGNI BORDO
                    function(edge, pt1, pt2){ //LAVORERÀ CON TUTTI I BORDI E PUNTI DI INIZIO E FINE
      
                        if (edge.source.data.alpha * edge.target.data.alpha == 0) return
                        
                        var tail = intersect_line_box(pt1, pt2, nodeBoxes[edge.source.name])
                        var head = intersect_line_box(tail, pt2, nodeBoxes[edge.target.name])
      
      
                        //                        ctx.strokeStyle = "rgba(0,0,0, .333)"; //LE FACCE SARANNO NERE CON UNA SFUMATURA
                        //                        ctx.lineWidth = 1; //1 PIXEL
                        //                        ctx.beginPath();  //INIZIO DEL DISEGNO
                        //                        ctx.moveTo(pt1.x, pt1.y); //DAL PUNTO A
                        //                        ctx.lineTo(pt2.x, pt2.y); //AL PUNTO B
                        //                        ctx.stroke();
                        //                        
                        //                        ctx.fillStyle = "black";
                        //                        ctx.font = 'italic 13px sans-serif';
                        //                        ctx.fillText (edge.data.name, (pt1.x + pt2.x) / 2, (pt1.y + pt2.y) / 2);


                        //codice nuovo copiato da render statico
                       ctx.lineWidth = 1 + 4*edge.data.weight
                        ctx.save() 
                        ctx.beginPath()
                       // ctx.lineWidth = (!isNaN(weight)) ? parseFloat(weight) : 3
                        ctx.strokeStyle = (color) ? color : "#708090"
                        ctx.fillStyle = "black";

                        ctx.moveTo(tail.x, tail.y)
                        ctx.lineTo(head.x, head.y)
                        ctx.stroke()
            
                        
                    
                        if(edge.data.connectionclass)
                        {
                            ctx.fillStyle = "blue";
                            ctx.font = 'italic 15px sans-serif ';
                            ctx.fillText (edge.data.name, (pt1.x + pt2.x) / 2, (pt1.y + pt2.y) / 2);
            
                            
                        }
                        else
                        {
                            ctx.fillStyle = edge.data.color;
                            ctx.font = 'italic 12px sans-serif';
                            ctx.fillText (edge.data.name, (pt1.x + pt2.x) / 2, (pt1.y + pt2.y) / 2);

                        }

                        ctx.restore() 
      
                        // draw an arrowhead if this is a -> style edge
                        if (edge.data.directed){
                            //alert("DENTRO DIRECTED");
                            ctx.save()
                        
                            var weight = edge.data.weight;
                            var color = edge.data.color;
                        
        
                            // move to the head position of the edge we just drew
                            var wt = !isNaN(weight) ? parseFloat(weight) : 1
                            var arrowLength = 10 + wt
                            var arrowWidth = 6 + wt
                            ctx.fillStyle = (color) ? color : "#708090"
                            ctx.translate(head.x, head.y);
                            ctx.rotate(Math.atan2(head.y - tail.y, head.x - tail.x));
                            //
                            // delete some of the edge that's already there (so the point isn't hidden)
                            ctx.clearRect(-arrowLength/2,-wt/2, arrowLength/2,wt)
                            // draw the chevron
                            ctx.beginPath();
                            ctx.moveTo(-arrowLength, arrowWidth);
                            ctx.lineTo(0, 0);
                            ctx.lineTo(-arrowLength, -arrowWidth);
                            ctx.lineTo(-arrowLength * 0.8, -0);
                            ctx.closePath();
                            ctx.fill();
                            ctx.restore()
                        }
      
      
      
      
                    });
    
    
            },
   
            switchSection:function(newSection){
                 //alert("DENTRO SWITCH SECTION");
                var parent = particleSystem.getEdgesFrom(newSection)[0].source;
                // alert("PARENT: "+parent);
                var children = $.map(particleSystem.getEdgesFrom(newSection), function(edge){
                    return edge.target
                })
                 //alert("CHILDREN: "+children);
        
                particleSystem.eachNode(function(node){
                   // alert("DENTRO 2 ALPHA: "+node.data.alpha)
                    
                    if (node.data.alpha===1) return // skip all but leafnodes
                    //alert("NAME: "+node.name)
                    var nowVisible = ($.inArray(node, children)>=0)
                    //alert("Figlio VISIBLE: "+nowVisible);
                    var newAlpha = (nowVisible) ? 1 : 0
                    // alert("NUOVA ALFA: "+newAlpha);
                    var dt = (nowVisible) ? .5 : .5
                    particleSystem.tweenNode(node, dt, {
                        alpha:newAlpha
                    })
                    //
                    if (newAlpha==1){
                        var nowVisible = ($.inArray(node, parent)>=0)
                    //                            var newAlpha = (nowVisible) ? 1 : 0
                    //                            var dt = (nowVisible) ? .5 : .5
                    //                            particleSystem.tweenNode(node, dt, {
                    //                                alpha:newAlpha
                    //                            })
                    }
                })
            },
   
            initMouseHandling:function(){ //EVENTO DEL MOUSE
                var dragged = null;   //IL VERTICE CHE SI MUOVE
                var _section = null
                var handler = {
                    clicked:function(e){ //PREMUTO
                        
                        var pos = $(canvas).offset(); //OTTENGO LA POSIZIONE DEL CANVAS
                        _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top); //E LA POSIZIONE RELATIVA AL PUNTO PREMUTO SUL CANVAS
                        dragged = particleSystem.nearest(_mouseP); //DETERMINA IL VERTICA PIU' VICINO
      
                        // alert("CLICKED:"+dragged.node.data.label)
                        if (dragged.node.name!=_section){
                            _section = dragged.node.name;
                            that.switchSection(_section)
                        }
                        if(dragged.node.data.typeDescription==true)
                        {
                           // alert("DENTRO!!!");
                            var description=dragged.node.data.description;
                            alert("DESCRIPTION: "+description);
                           // var Mytitle=dragged.node.data.title;
                           // alert("TITLE: "+MyTitle);
                            
//                            $( "#dialogDescription" ).dialog({
//                                title: "Mytitle" , 
//                                width: 500, 
//                                position: [e.pageX-pos.left,e.pageY-pos.top],
//                                hide: { effect: 'drop', direction: "down" }
//
//                            });
//                            $( "#dialogDescription" ).text(description);
                            
                           
                        }
                        if(dragged.node.data.typeTitle===true)
                        {
                            
                            var title=dragged.node.data.title;
                                
                            
                            //$('#dialogTitle').dialog('widget').find(".ui-dialog-titlebar").hide();     
                            $( "#dialogTitle" ).dialog({
                                
                                width: 500, 
                                height:150,
                                title:"TITLE",
                                position: [e.pageX-pos.left,e.pageY-pos.top],
                                buttons: [
                                {
                                    text: "Ok",
                                    click: function() {
                                        $(this).dialog("close");
                                    }
                                }
                                ],
                                hide: { effect: 'drop', direction: "down" },
                                resizable: false
                                

                            });
                            $( "#dialogTitle" ).text(title);
                           
                        }
                        
      
                        if (dragged && dragged.node !== null){
                            dragged.node.fixed = true; //RILASCIO
                        }
                        $(canvas).bind('mousemove', handler.dragged); //HANDLER DEL MOUSE MOVE
                        $(window).bind('mouseup', handler.dropped);  //E DEL MOUSE UP
                        return false;
                    },
                    dragged:function(e){ //RITORNO AL TOP
                        var pos = $(canvas).offset();
                        var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top);
  
                        if (dragged && dragged.node !== null){
                            var p = particleSystem.fromScreen(s);
                            dragged.node.p = p; //PUNTARE LA PARTE BASSA DEL MOUSE
                        }
  
                        return false;
                    },
                    dropped:function(e){ //RELASCIO
                        if (dragged===null || dragged.node===undefined) return; //SE NON SI MUOVE, NULL
                        if (dragged.node !== null) dragged.node.fixed = false; //SE SI MUOVE, INIZIA
                        dragged = null; //PULISCO TUTTO
                        $(canvas).unbind('mousemove', handler.dragged); //FERMO TUTTO GLI EVENTI
                        $(window).unbind('mouseup', handler.dropped);
                        _mouseP = null;
                        return false;
                    }
                }
                // ASCOLTO GLI EVENTI DEL MOUSE
                $(canvas).mousedown(handler.clicked);
            }
       
        }
  
        // helpers for figuring out where to draw arrows (thanks springy.js)
        var intersect_line_line = function(p1, p2, p3, p4)
        {
            var denom = ((p4.y - p3.y)*(p2.x - p1.x) - (p4.x - p3.x)*(p2.y - p1.y));
            if (denom === 0) return false // lines are parallel
            var ua = ((p4.x - p3.x)*(p1.y - p3.y) - (p4.y - p3.y)*(p1.x - p3.x)) / denom;
            var ub = ((p2.x - p1.x)*(p1.y - p3.y) - (p2.y - p1.y)*(p1.x - p3.x)) / denom;

            if (ua < 0 || ua > 1 || ub < 0 || ub > 1)  return false
            return arbor.Point(p1.x + ua * (p2.x - p1.x), p1.y + ua * (p2.y - p1.y));
        }

        var intersect_line_box = function(p1, p2, boxTuple)
        {
            var p3 = {
                x:boxTuple[0], 
                y:boxTuple[1]
            },
            w = boxTuple[2],
            h = boxTuple[3]

            var tl = {
                x: p3.x, 
                y: p3.y
            };
            var tr = {
                x: p3.x + w, 
                y: p3.y
            };
            var bl = {
                x: p3.x, 
                y: p3.y + h
            };
            var br = {
                x: p3.x + w, 
                y: p3.y + h
            };

            return intersect_line_line(p1, p2, tl, tr) ||
            intersect_line_line(p1, p2, tr, br) ||
            intersect_line_line(p1, p2, br, bl) ||
            intersect_line_line(p1, p2, bl, tl) ||
            false
        }
  
  
        return that;
    }
})()