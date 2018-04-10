$(document).ready(function() {

	$("#train-form").submit(function(event) {

		// stop submit the form, we will post it manually.
		event.preventDefault();
		fire_ajax_submit();

	});

});

function fire_ajax_submit() {
	debugger;
	$.ajax({
		type : "GET",
		// contentType: "application/json",
		url : "/training",
		cache : false,
		// timeout: 600000,
		success : function(data) {

			getDataAndPlot();
			console.log("SUCCESS : ", data);

		},
		error : function(e) {
			console.log("ERROR : ", e);

		}
	});

}

function getDataAndPlot() {
	$.ajax({
		type : "GET",
		// contentType: "application/json",
		url : "/getCsv",
		cache : false,
		// timeout: 600000,
		success : function(data) {
			processData(data);
			console.log("SUCCESS : ", data);

		},
		error : function(e) {
			console.log("ERROR : ", e);

		}
	});

	// Plotly.d3.csv("data/mswcluster_similaritymatrix/result.csv",
	// function(data){ processData(data) } );

}

function processData(allRows) {
	var ss = allRows.split(",");
	var x1 = [];
	var y1 = [];
	//var z1 = [][];
	var i;
	for (i = 0; i < ss.length; i++) {
		var eachRow = ss[i].split("#~#");
		if(x1.indexOf(eachRow[0])==-1)
		x1.push(eachRow[0]);
		if(y1.indexOf(eachRow[1])==-1)
		y1.push(eachRow[1]);
	}
	
	// var x1=['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];
	// var y1=['Morning', 'Afternoon'];
	var z1 = [ [ 1, 20, 30, 50, 1 ], [ 20, 1, 60, 80, 30 ] ,[ 20, 1, 60, 80, 30 ]];
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

	Plotly.newPlot('sim', data, layout, {
		displayModeBar : false
	});
}

/*
 * console.log(allRows); var x = [], y = [], z = [];
 * 
 * for (var i=0; i<allRows.length; i++) { row = allRows[i]; if ( x.indexOf(
 * row['a']) == -1 ){ x.push( row['a'] ); } if ( y.indexOf( row['b']) == -1 ){
 * y.push( row['b'] ); }
 * 
 *  } console.log( 'X',x, 'Y',y, 'SD',z ); makePlotly( x, y, z );
 */

