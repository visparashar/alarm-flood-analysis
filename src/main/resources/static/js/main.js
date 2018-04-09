$(document).ready(function () {

    $("#train-form").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();
        alert("in main");
        fire_ajax_submit();

    });

});


function fire_ajax_submit() {
alert("in fire_ajax");
debugger;
    $.ajax({
        type: "GET",
//        contentType: "application/json",
        url: "/training",
        cache: false,
//        timeout: 600000,
        success: function (data) {

        	getDataAndPlot();
            console.log("SUCCESS : ", data);

        },
        error: function (e) {
                      console.log("ERROR : ", e);

        }
    });

}

function getDataAndPlot() {
	  Plotly.d3.csv("data/mswcluster_similaritymatrix/result.csv", function(data){ processData(data) } );

	}
	
	function processData(allRows) {
		console.log(allRows);
		var data = [
			  {
			    z: [[1, 20, 30, 50, 1], [20, 1, 60, 80, 30], [30, 60, 1, -10, 20]],
			    x: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'],
			    y: ['Morning', 'Afternoon', 'Evening'],
			    type: 'heatmap'
			  }
			];
		var layout = {
				title : 'Similarity Matrix',
				showlegend : true
			};

			Plotly.newPlot('sim', data, layout, {
				displayModeBar : false
	});}

		 /* console.log(allRows);
		  var x = [], y = [], z = [];

		  for (var i=0; i<allRows.length; i++) {
		    row = allRows[i];
		    if ( x.indexOf( row['a']) == -1 ){
		    	 x.push( row['a'] );
		    }
		    if ( y.indexOf( row['b']) == -1 ){
		    	 y.push( row['b'] );
		    }
		   
		   
		  }
		  console.log( 'X',x, 'Y',y, 'SD',z );
		  makePlotly( x, y, z );*/
		




		

