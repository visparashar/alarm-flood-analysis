function createRCACluster(clusterdiv ,clusterdatasource){

	var diameter = 300, //max size of the bubbles
	color = d3.scale.category20b(); //color category

	var bubble = d3.layout.pack().sort(null).size([ diameter, diameter ])
	.padding(1.5);

	var svg = d3.select(clusterdiv).append("svg")
	.attr("width", diameter)
	.attr("height", diameter)
//	.attr("preserveAspectRatio","xMinYMin meet")
	.attr("class", "bubble")
	.attr("viewBox","0 0 300 300")
	.classed("svg-content",true);


	d3.csv(clusterdatasource, function(error, data) {
		console.log(data);
		//convert numerical values from strings to numbers
		data = data.map(function(d) {
			d.value = +d["score"];
			return d;
		}

		);
		//		creating tooltips for bubble
		var tooltip =d3.select(clusterdiv)
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