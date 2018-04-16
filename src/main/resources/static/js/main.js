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
			.animate({width: 390}, 6000);
		}
		var update ={
				width:340,
				height:originalHeight
		};
		setTimeout(function(){
			Plotly.relayout('myDiv',update);
		}, 3000);
		
		

		/* $('#rightpanelbody').css({
			 "width":originalHeight
		 })*/

		fire_ajax_submit();
		createCluster();
		$("#trainingset-container-id *").attr('disabled','disabled');
		$("#uploadstatus").hide();
		$("#messageboxid").append(
				"<div class='well well-lg' >" +
				"<div class='row' id='trainuploaddiv'>" +
				"<div class='col-md-3'><B>Upload Test Flood</B></div>"
				+"<div class='col-md-5'>" +
				"<form id='fileinfo' enctype='multipart/form-data'  action='/test' method='post' name='fileinfo'>" +
				"<div class='col-sm-6'>" +
				"<input type='file' id='message' name='file' accept='.csv' /></div>" +
				"</form>" +
				"<div class='col-sm-4'>" +
				"<input type='submit' onclick='return uploadTestDataUsingJqueryForm();' id='uploadtestbtn' class='btn btn-success btn-sm' value='Submit' />" +
				"</div>" +

				"</div></div></div>"
		);



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
			$('#loading').hide();
			getDataAndPlot();
			
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

/*function processData(allRows) {
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
			showlegend : true,
			yaxis:{
				ticklen:10
			}
	};

	Plotly.newPlot('plotsimilarity', data, layout, {
		displayModeBar : false
	});
}*/

function processData(allRows) {
	var ss = allRows.split(",");
	var x1 = [];
	var y1 = [];
	var z1 = [];
	for (var i = 0; i < ss.length; i++) {
		var eachRow = ss[i].split("#~#");
		
		if(x1.indexOf(eachRow[0]) == -1){
			x1.push(eachRow[0]);
		}
		if(x1.indexOf(eachRow[1]) == -1){
			x1.push(eachRow[1]);
		}
		if(y1.indexOf(eachRow[0]) == -1){
			y1.push(eachRow[0]);
		}
		if(y1.indexOf(eachRow[1]) == -1){
			y1.push(eachRow[1]);
		}
	}
	var tempArr = [];
	var n=0;
	var m=0;
	var temp=ss[0].split("#~#")[0];
	for(var u=0;u<x1.length;u++){
		for(var v=0;v<y1.length;v++){
			for(var w = 0; w < ss.length; w++){
				var row = ss[w].split("#~#");
				if(x1[u]==y1[v]){
					tempArr[n]=1;
					n=n+1;
					break;
				}
				else if((x1[u]==row[0] & y1[v]==row[1]) | (x1[u]==row[1] & y1[v]==row[0])){
					tempArr[n]=row[2];
					n=n+1;
					break;
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


function uploadTestDataUsingJqueryForm(){
	if ($.trim($("#message").val()) === "") {
		alert('Please select a file to upload');
		return false;
	}else{
		var strlength =$.trim($("#message").val()).length;
		var str =$.trim($("#message").val());
		if(str.substring(strlength-4,strlength) ==='.csv'){
			$('#test-container').show();

			$("#fileinfo").ajaxForm({
				success: function(data){
					document.getElementById("fileinfo").reset();
					var d =data;
					var s =d.split(",")
					if(s[0] === '1'){
						$('#uploadedtestdatastatus').html("<center><b>Status:</b>True Flood</center>");
						createClusterAndGetRCAOfCluster();
					}else{
						$('#uploadedtestdatastatus').html("<center><b>Status:</b>False Flood, Please Upload new File for test </center>");	
						$("#testrcacluster").html('');
						$("#testrcsdiv").html('');
					}
//					call another function for get the cluster to be created and the RCA 

				},
				dataType:"text"
			}).submit();
			return true;
		}else{
			alert("Please Upload CSV files only");
			return false;
		}
	}
}


function onRCA1(){
	$('#rca2').hide();
	$('#rca3').hide();

	var path ="/getPostCsvData?path=data/frequencycount_outputfolder/Cluster1_Frequency.csv&&fileFlag=check_for_cluster_reco_done.txt"
		readCsv(path ,"rca1");
}

function readCsv(path , id){
	$.ajax({
		type: "GET",
//		contentType: "application/json",
		url: path,
		cache: false,
		dataType:"text",
		success: function (data) {
//			alert(data)
			drawTable(data ,id)
			console.log("SUCCESS : ", data);
		},
		error: function (e) {
			console.log("ERROR : ", e);

		}
	});
}

function drawTable(data ,id){
	var rec_data = data.split(",");
	var table_data = '<table style="height: 20px;overflow:scroll;" class="table table-bordered table-striped">';
	for(var count = 0; count<rec_data.length; count++)
	{
		var cell_data = rec_data[count].split("#~#");
		table_data += '<tr>';
		for(var cell_count=0; cell_count<cell_data.length; cell_count++)
		{
			if(count === 0)
			{
				table_data += '<th>'+cell_data[cell_count]+'</th>';
			}
			else
			{
				table_data += '<td>'+cell_data[cell_count]+'</td>';
			}
		}
		table_data += '</tr>';
	}
	table_data += '</table>';
	$('#'+id).html(table_data);
	$('#'+id).css({"height":"150px" ,"overflow-y":"scroll"});
	$('#'+id).show();
}
function onRCA2(){
	$('#rca1').hide();
	$('#rca3').hide();

	var path ="/getPostCsvData?path=data/frequencycount_outputfolder/Cluster2_Frequency.csv&&fileFlag=check_for_cluster_reco_done1.txt"
		readCsv(path ,"rca2");
}

function onRCA3(){
	$('#rca1').hide();
	$('#rca2').hide();
	var path ="/getPostCsvData?path=data/frequencycount_outputfolder/Cluster3_Frequency.csv&&fileFlag=check_for_cluster_reco_done2.txt"
		readCsv(path ,"rca3");
}

function createClusterAndGetRCAOfCluster(){
	var restpath = "/getRecoCluster?path=data/testrca_output_folder/Recommendation.csv&&fileFlag=check_for_test_rca_done.txt";
	$.ajax({
		type: "GET",
		url: restpath,
		cache: false,
		dataType:"text",
		success: function (data) {
			drawRecoCluster(data);
			console.log("SUCCESS : ", data);
		},
		error: function (e) {
			console.log("ERROR : ", e);

		}
	});

}

function drawRecoCluster(data)
{
	var dec = data.split("#~#");
	var clustername =dec[0];
	var similarityIndex =dec[1];
	var divname ="testrcsdiv";
	var clusid ="#testrcacluster";
	if(clustername.toLowerCase() === 'cluster1'){
		createRCACluster(clusid,cluster1datasource);
		var csvname = "Cluster1_Frequency.csv";
		var filename ="check_for_cluster_reco_done.txt";
		createTestRCAReco(csvname,filename,divname);

	}else if(clustername.toLowerCase() === 'cluster2'){
		createRCACluster(clusid,cluster2datasource);
		var csvname = "Cluster2_Frequency.csv";
		var filename ="check_for_cluster_reco_done1.txt";
		createTestRCAReco(csvname,filename,divname);
	}else {
		createRCACluster(clusid,cluster3datasource);
		var csvname = "Cluster3_Frequency.csv";
		var filename ="check_for_cluster_reco_done2.txt";
		createTestRCAReco(csvname,filename,divname);
	}
}

function createTestRCAReco(csvname,filename,id){
	var path ="/getPostCsvData?path=data/frequencycount_outputfolder/"+csvname+"&&fileFlag="+filename;
	readCsv(path ,id);
}







