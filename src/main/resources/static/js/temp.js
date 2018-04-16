function processData(allRows) {
       var ss = allRows.split(",");
       var x1 = [];
       var y1 = [];
       var z1 = [];
       var i;
       var j;
       var k;
       var f=1;
       var n=0;
       var s=0;
       var z=1;
       var t=0;
       for (i = 0; i < ss.length; i++) {
              var eachRow = ss[i].split("#~#");
              if(i==0){
                     y1.push(eachRow[0]);
              }
              if (x1.indexOf(eachRow[0]) == -1){
                     x1.push(eachRow[0]);
              }
              if(i==ss.length-1){
                     x1.push(eachRow[1]);
              }
              if (y1.indexOf(eachRow[1]) == -1){
                     y1.push(eachRow[1]);
              }
       }
       var tempArr = [];
       tempArr[0]=1;
       var temp=ss[0].split("#~#")[0];
       for (j = 0; j < ss.length; j++) {
              var row = ss[j].split("#~#");
              if (temp == row[0]) {
                     tempArr[f] = row[2];
                     f=f+1;
              }
              else {
                     z1[n] = tempArr;
                     alert(tempArr);
                     n=n+1;
                     temp = row[0];
                     f=1;
                     tempArr = [];
                     
                     for(var g=0;g<z;g++){
                           temp[g]=z1[g][g+1+t];
                           
                     }
                     t=t+1;
                     temp[z]=1;
                     z=z+1;
                     
                     
                     tempArr[0]=1;
                     tempArr[f] = row[2];
                     f=2;
              }
              if(j==ss.length-1){
                     z1[n] = tempArr;
                     n=n+1;
                     f=1;
                     tempArr = [];
                     tempArr[0]=1;
                     z1[n] = tempArr;
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
