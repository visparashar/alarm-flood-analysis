function processData(allRows) {
	var ss = allRows.split(",");
	var x1 = [];
	var y1 = [];
	var z1 = [];

	for (var i = 0; i < ss.length; i++) {
		var eachRow = ss[i].split("#~#");
		if(i==0){
			y1.push(eachRow[0]);
			x1.push(eachRow[0]);
		}
		if (x1.indexOf(eachRow[0]) == -1){
			if((y1.indexOf(eachRow[0]) > -1)
			y1.push(eachRow[0]);
			if((x1.indexOf(eachRow[0]) > -1)
			x1.push(eachRow[0]);
		}
		if(i==ss.length-1){
			if((y1.indexOf(eachRow[1]) > -1)
			y1.push(eachRow[1]);
			if((x1.indexOf(eachRow[1]) > -1)
			x1.push(eachRow[1]);
		}
		if (y1.indexOf(eachRow[1]) == -1){
			if((y1.indexOf(eachRow[1]) > -1)
			y1.push(eachRow[1]);
			if((x1.indexOf(eachRow[1]) > -1)
			x1.push(eachRow[1]);
		}
	}
	var tempArr = [];
	var n=0;
	var m=0;
	var temp=ss[0].split("#~#")[0];
	for(var u=0;u<x1.length;u++){
		for(var v=0;u<y1.length;v++){
			for(var w = 0; w < ss.length; w++){
				var row = ss[w].split("#~#");
				if(x1[u]==y1[u]){
					tempArr[n]=1;
					n=n+1;
				}
				else{((x1[u]==row[0] & y1[v]==row[1]) | (x1[u]==row[1] & y1[v]==row[0])){
					if()
					tempArr[n]=row[2];
					n=n+1;
				}
				}
				
			}
		}
		z1[m]= tempArr;
		m=m+1;
		tempArr = [];
		n=0;
	}
	
	var data = [ {
		z : z1,
		x : x1,
		y : y1,
		type : 'heatmap'
	} ];
	var layout = {
			title : 'Similarity Matrix',
			showlegend : true
	};

	Plotly.newPlot('plotsimilarity', data, layout, {
		displayModeBar : false
	});
} 