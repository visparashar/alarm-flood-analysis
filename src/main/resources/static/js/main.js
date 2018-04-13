$(document).ready(function () {

	$("#train-form").submit(function (event) {

		//stop submit the form, we will post it manually.
		event.preventDefault();
//		callResizePlolyFun();
		var col =$("#leftpanel");
		var originalWidth = col.width();
		var originalHeight = col.height();
		 if (col.hasClass("shrunk")) {
		        col.removeClass("shrunk")
		            .stop(true)
		            .animate({width: originalWidth});
		 } else {
		        col.addClass("shrunk")
		            .stop(true, true)
		            .animate({width: 370}, 100);
		    }
		 var update ={
				 width:320,
		 		height:originalHeight
		 };
		 Plotly.relayout('myDiv',update); 
		$("#uploadstatus").hide();
		$("#messageboxid").append(
				"<div class='well well-lg' >" +
				"<div class='row' id='trainuploaddiv'>" +
				"<div class='col-md-3'><B>Upload Test Flood</B></div>"
				+"<div class='col-md-5'>" +
				"<form id='fileinfo' enctype='multipart/form-data' action='/test' method='post' name='fileinfo'>" +
				"<div class='col-sm-6'>" +
				"<input type='file' name='file' accept='.csv' /></div>" +
				"</form>" +
				"<div class='col-sm-4'>" +
				"<input type='submit' onclick='uploadTestDataUsingJqueryForm()' id='uploadtestbtn' class='btn btn-success btn-sm' value='Submit' />" +
				"</div>" +
				
				"</div></div></div>"
		);
		fire_ajax_submit();
		createCluster();
		$("#trainingset-container-id *").attr('disabled','disabled');



	});





});


function fire_ajax_submit() {
//	alert("in fire_ajax");
//	debugger;
	$.ajax({
		type: "GET",
//		contentType: "application/json",
		url: "/training",
		cache: false,
//		timeout: 600000,
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
	/*function getDataAndPlot() {
	Plotly.d3.csv("result.csv", function(data){ processData(data) } );

}*/
}

function processData(allRows) {
	var ss = allRows.split(",");
	var x1 = [];
	var y1 = [];
	var z1 = [];
	var i;
	var j;
	var k;
	var f=0;
	var n=0;
	for (i = 0; i < ss.length; i++) {
		var eachRow = ss[i].split("#~#");
		if (x1.indexOf(eachRow[0]) == -1)
			x1.push(eachRow[0]);
		if (y1.indexOf(eachRow[1]) == -1)
			y1.push(eachRow[1]);
	}
	var tempArr = [];
	var temp=ss[0].split("#~#")[0];
	for (j = 0; j < ss.length; j++) {
		var row = ss[j].split("#~#");
		if (temp == row[0]) {
			tempArr[f] = row[2];
			f=f+1;
		} else {
			z1[n] = tempArr;
			n=n+1;
			temp = row[0];
			f=0;
			tempArr = [];
			tempArr[f] = row[2];
			f=1;
		}

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

/*function processData(allRows) {

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

	Plotly.newPlot('plotsimilarity', data, layout, {
		displayModeBar : false
	});}*/

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

/* $("#fileinfo").on('submit', function(e){ 
		 e.preventDefault();
		 debugger;
		var fd = new FormData($("#fileinfo"));
		//fd.append("CustomField", "This is some extra data");
		alert('inside');
		$.ajax({
			url: '/upload',  
			type: 'GET',
			data: fd,
			success:function(data){
//				$('#output').html(data);
				alert(data);
			},
			cache: false,
			contentType: false,
			processData: false
		});
	});*/


/*$(document).ready(function() { 
	// bind 'myForm' and provide a simple callback function 
	$('#fileinfo').ajaxForm(function() { 
		alert("Thank you for your comment!"); 
	}); 
}); */

function uploadTestDataUsingJqueryForm(){
//	alert("hit");
//	$("#training-container").slideDown();
	$('#test-container').show();
	
	$("#fileinfo").ajaxForm({
		success: function(data){
			 document.getElementById("fileinfo").reset();
			 var d =data;
			 var s =d.split(",")
			 if(s[0] === '1'){
				 $('#uploadedtestdatastatus').html("<center><b>Status:</b>True Flood</center>");	 
			 }else{
				 $('#uploadedtestdatastatus').html("<center><b>Status:</b>False Flood, Please Upload new File for test </center>");	 
			 }
		},
		dataType:"text"
	}).submit();
	
}

