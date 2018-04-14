var cluster1datasource="";
var cluster2datasource="";
var cluster3datasource="";
function createCluster(){

	$.ajax({
		type: "GET",
//		contentType: "application/json",
		url: "/getFileNames",
		cache: false,
//		timeout: 600000,
		success: function (data) {
			var str ="Similarity Index Range ";
			var tempData =data;
			var si0 =tempData[0].match(/~~(.*)~~/).pop();
			si0= str+si0;
			var si1=tempData[1].match(/~~(.*)~~/).pop();
			si1= str+si1;
			var si2 =tempData[2].match(/~~(.*)~~/).pop();
			si2= str+si2;

			tempData =[si0,si1,si2];
			setClusterDataPath(data);
			plotCluster(tempData);

			console.log("SUCCESS : ", data);

		},
		error: function (e) {
			console.log("ERROR : ", e);

		}
	});

}
function setClusterDataPath(di){
	cluster1datasource=di[0];
	cluster2datasource=di[1];
	cluster3datasource=di[2];
}

function plotCluster(data){
	createCluster1(data[0]);
	createCluster2(data[1]);
	createCluster3(data[2]);


}

function createCluster1(d){

	$('#cluster1-header').text(d);

	var diameter = 300, //max size of the bubbles
	color = d3.scale.category20b(); //color category

	var bubble = d3.layout.pack().sort(null).size([ diameter, diameter ])
	.padding(1.5);

	var svg = d3.select("#cluster1").append("svg")
//	.attr("width", diameter)
//	.attr("height", diameter)
	.attr("preserveAspectRatio","xMinYMin meet")
	.attr("class", "bubble")
	.attr("viewBox","0 0 300 300")
	.classed("svg-content",true);

	d3.csv(cluster1datasource, function(error, data) {
		console.log(data);
		//convert numerical values from strings to numbers
		data = data.map(function(d) {
			d.value = +d["score"];
			return d;
		}

		);
		if(jQuery.isEmptyObject(data)){
			$('#cluster1parentdiv').remove();
			$('#cluster2parentdiv').removeClass("col-md-4");
			$('#cluster2parentdiv').addClass("col-md-6");
			$('#cluster3parentdiv').removeClass("col-md-4");
			$('#cluster3parentdiv').addClass("col-md-6");
		}

		//		creating tooltips for bubble
		var tooltip =d3.select("#cluster1")
		.append("div")
		.style("position","absolute")
		.style("z-index","100")
		.style("visibility","hidden")
		.style("color","white")
		.style("padding","5px")
		.style("background-color","rgba(0,0,0,0.75)")
		.style("border-radius","6px")
		.style("font","12px sans-serif")
		.text("tooltip");

		//bubbles needs very specific format, convert data to this.
		var nodes = bubble.nodes({
			children : data
		}).filter(function(d) {
			return !d.children;
		});

		//setup the chart
		var bubbles = svg.append("g").attr("transform", "translate(0,0)")
		.selectAll(".bubble").data(nodes).enter();

		//create the bubbles
		bubbles.append("circle").attr("r", function(d) {
			return d.r;
		}).attr("cx", function(d) {
			return d.x;
		}).attr("cy", function(d) {
			return d.y;
		}).style("fill", function(d) {
			return color(d.value);
		})
		.on("mouseover",function(d){
			tooltip.text(d["a"]+"-"+d["b"]+": "+d["score"]);
			tooltip.style("visibility","visible");
		})
		.on("mousemove",function(){
			return tooltip.style("top",(d3.event.pageY-400)+"px").style("left",(d3.event.pageX-450)+"px");
		})
		.on("mouseout",function(){return tooltip.style("visibility","hidden");});

		//format the text for each bubble

	})
}
//cluster 2

function createCluster2(d){

	$('#cluster2-header').text(d);
	var diameter = 300, //max size of the bubbles
	color = d3.scale.category20b(); //color category

	var bubble = d3.layout.pack().sort(null).size([ diameter, diameter ])
	.padding(1.5);

	var tooltip2 =d3.select("#cluster2")
	.append("div")
	.style("position","absolute")
	.style("z-index","100")
	.style("visibility","hidden")
	.style("color","white")
	.style("padding","5px")
	.style("background-color","rgba(0,0,0,0.75)")
	.style("border-radius","6px")
	.style("font","12px sans-serif")
	.text("tooltip");
	var svg1  = d3.select("#cluster2").append("svg")
//	.attr("width", diameter)
//	.attr("height", diameter)
	.attr("preserveAspectRatio","xMinYMin meet")
	.attr("class", "bubble")
	.attr("viewBox","0 0 300 300")
	.classed("svg-content",true);

	d3.csv(cluster2datasource, function(error, data) {

		//convert numerical values from strings to numbers
		data = data.map(function(d) {
			d.value = +d["score"];
			return d;
		});

		if(jQuery.isEmptyObject(data)){
			$('#cluster2parentdiv').remove();
			$('#cluster1parentdiv').removeClass("col-md-4");
			$('#cluster1parentdiv').addClass("col-md-6");
			$('#cluster3parentdiv').removeClass("col-md-4");
			$('#cluster3parentdiv').addClass("col-md-6");
		}

		//bubbles needs very specific format, convert data to this.
		var nodes = bubble.nodes({
			children : data
		}).filter(function(d) {
			return !d.children;
		});

		//setup the chart
		var bubbles = svg1.append("g").attr("transform", "translate(0,0)")
		.selectAll(".bubble").data(nodes).enter();

		//create the bubbles
		bubbles.append("circle").attr("r", function(d) {
			return d.r;
		}).attr("cx", function(d) {
			return d.x;
		}).attr("cy", function(d) {
			return d.y;
		}).style("fill", function(d) {
			return color(d.value);
		}).on("mouseover",function(d){
			tooltip2.text(d["a"]+"-"+d["b"]+": "+d["score"]);
			tooltip2.style("visibility","visible");
		})
		.on("mousemove",function(){
			return tooltip2.style("top",(d3.event.pageY-400)+"px").style("left",(d3.event.pageX-700)+"px");
		})
		.on("mouseout",function(){return tooltip2.style("visibility","hidden");});

		//format the text for each bubble
		/*bubbles.append("text").attr("x", function(d) {
			return d.x;
		}).attr("y", function(d) {
			return d.y + 5;
		}).attr("text-anchor", "middle").text(function(d) {
			var text = d["a"];
			var length=7;
			var trimmed = text.substring(0,length);
			return trimmed;
		}).style({
			"fill" : "white",
			"font-family" : "Helvetica Neue, Helvetica, Arial, san-serif",
			"font-size" : "12px"
		});*/
	})
}

//cluster 3

function createCluster3(da){
	$('#cluster3-header').text(da);
	var diameter = 300, //max size of the bubbles
	color = d3.scale.category20b(); //color category

	var bubble = d3.layout.pack().sort(null).size([ diameter, diameter ])
	.padding(1.5);
	var tooltip3 =d3.select("#cluster3")
	.append("div")
	.style("position","absolute")
	.style("z-index","100")
	.style("visibility","hidden")
	.style("color","white")
	.style("padding","5px")
	.style("background-color","rgba(0,0,0,0.75)")
	.style("border-radius","6px")
	.style("font","12px sans-serif")
	.text("tooltip");
	var svg2 =  d3.select("#cluster3").append("svg")
//	.attr("width", diameter)
//	.attr("height", diameter)
	.attr("preserveAspectRatio","xMinYMin meet")
	.attr("class", "bubble")
	.attr("viewBox","0 0 300 300")
	.classed("svg-content",true);
	d3.csv(cluster3datasource, function(error, data) {

		//convert numerical values from strings to numbers
		data = data.map(function(d) {
			d.value = +d["score"];
			return d;
		});
		if(jQuery.isEmptyObject(data)){
			$('#cluster3parentdiv').remove();
			$('#cluster1parentdiv').removeClass("col-md-4");
			$('#cluster1parentdiv').addClass("col-md-6");
			$('#cluster2parentdiv').removeClass("col-md-4");
			$('#cluster2parentdiv').addClass("col-md-6");
		}

		//bubbles needs very specific format, convert data to this.
		var nodes = bubble.nodes({
			children : data
		}).filter(function(d) {
			return !d.children;
		});

		//setup the chart
		var bubbles = svg2.append("g").attr("transform", "translate(0,0)")
		.selectAll(".bubble").data(nodes).enter();

		//create the bubbles
		bubbles.append("circle").attr("r", function(d) {
			return d.r;
		}).attr("cx", function(d) {
			return d.x;
		}).attr("cy", function(d) {
			return d.y;
		}).style("fill", function(d) {
			return color(d.value);
		}).on("mouseover",function(d){
			tooltip3.text(d["a"]+"-"+d["b"]+": "+d["score"]);
			tooltip3.style("visibility","visible");
		})
		.on("mousemove",function(){
			return tooltip3.style("top",(d3.event.pageY-400)+"px").style("left",(d3.event.pageX-950)+"px");
		})
		.on("mouseout",function(){return tooltip3.style("visibility","hidden");});

		/*	//format the text for each bubble
		bubbles.append("text").attr("x", function(d) {
			return d.x;
		}).attr("y", function(d) {
			return d.y + 5;
		}).attr("text-anchor", "middle").text(function(d) {
			return d["a"];
		}).style({
			"fill" : "white",
			"font-family" : "Helvetica Neue, Helvetica, Arial, san-serif",
			"font-size" : "12px"
		});*/
	})
}

