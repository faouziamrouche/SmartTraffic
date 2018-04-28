$(document).ready(function(){
    //var chart1 = $("#chart1")
    // Plotly.plot("chart2", [{
    //     x: [1,2,3,4,5],
    //     y: [1,2,3,4,5]
    // }]) ;


    // function rand() {
    //     return Math.random();
    //   }
      
    //   var time = new Date();
      
    //   var data = [{
    //     x: [time],
    //     y: [0],
    //     mode: 'lines',
    //     line: {color: '#80CAF6'}
    //   }]
      
    //   Plotly.plot('chart1', data);
      
    //   var cnt = 0;
      
    //   var interval = setInterval(function() {
        
    //     $.ajax({
    //         type: "GET",
    //         contentType: "application/json",
    //         url: "/monitoring",
    //         dataType: "json",
    //         success: function(result) {
    //             console.log("Measure received") ;
    //             console.log(result.temperature)
    //             var time = new Date();
                
    //               var update = {
    //               x:  [[time]],
    //               y: [[result.temperature]]
    //               }
                
    //               var olderTime = time.setMinutes(time.getMinutes() - 1);
    //               var futureTime = time.setMinutes(time.getMinutes() + 1);
                
    //               var minuteView = {
    //                     xaxis: {
    //                       type: 'date',
    //                       range: [olderTime,futureTime]
    //                     }
    //                   };
                
    //               Plotly.relayout('chart1', minuteView);
    //               Plotly.extendTraces('chart1', update, [0])
                
    //               if(cnt === 100) clearInterval(interval);
    //         }
    //     })
      
        
    //   }, 2000);
    
    var time = new Date();
    
    var trace1 = {
      x: [],
      y: [],
      mode: 'lines',
      line: {
        color: '#80CAF6',
        shape: 'spline'
      }
    }
    
    var trace2 = {
      x: [],
      y: [],
      xaxis: 'x2',
      yaxis: 'y2',
      mode: 'lines',
      line: {color: '#DF56F1'}
    };
    
    var layout = {
      xaxis: {
        type: 'date',
        domain: [0, 1],
        showticklabels: false
      },
      yaxis: {domain: [0.6,1]},
      xaxis2: {
        type: 'date',
        anchor: 'y2',
        domain: [0, 1]
      },
      yaxis2: {
        anchor: 'x2',
        domain: [0, 0.4]},
    }
    
    var data = [trace1,trace2];
    
    Plotly.plot('chart1', data, layout);
    
    var cnt = 0;
    
    var interval = setInterval(function() {
      $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/monitoring",
        dataType: "json",
        success: function(result) {
            console.log(result) ;
            var time = new Date();
            
              var update = {
                x: [[time], [time]],
                y: [[result.temperature], [result.humidity]]
              }
            
              Plotly.extendTraces('chart1', update, [0,1])
            
              if(cnt === 100) clearInterval(interval);
        }
    })
    
    }, 2000);


    var interval2 = setInterval(function(){
      $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/monitoring",
        dataType: "json",
        success: function(result) {
          console.log(result) ;
          $("#chart2 h1").text(result.number + " voitures passées") ;
        }
      })
    }, 2000) ;
}) ;