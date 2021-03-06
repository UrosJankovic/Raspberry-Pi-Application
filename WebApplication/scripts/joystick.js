var counter;        // Counts middle putton press
var chart;          // Chart.js object
var chartContext;   // Context of chart

var timer;          // Request timer

const getDataURL = '../server/joystick_via_deamon.php?id=get'
const resetCounterURL = '../server/joystick_via_deamon.php?id=rst'

$(document).ready(function(){
    currentPoint = [{x: 0, y: 0}];

    $(".joysticktable").width($(window).width() / 2);

    // Initialize chart
    chartInit();
})

/**
 * @brief Sets coordinates to (0,0) and resets counter
 */
function resetAll(){
    ajaxGetJSON(resetCounterURL);
}

/**
* @brief Start request timer
*/
function startTimer(){
    if(timer == null)
    	timer = setInterval(ajaxGetJSON, 100, getDataURL);
}

/**
* @brief Stop request timer
*/
function stopTimer(){
	if(timer != null) {
		clearInterval(timer);
		timer = null;
	}
}

/**
* @brief Send HTTP GET request to IoT server
*/
function ajaxGetJSON(url){
	$.getJSON(url, function(data){	
		handleData(data);
	})
}

/**
 * @brief Handle received data
 * @param receivedData object with received data
 */
function handleData(receivedData){    
    if(receivedData){
        // Update counter value
        counter = receivedData.Counter;
        $("#counterValue").html(counter);

        // Change position of datapoint
        chart.data.datasets[0].data[0].x = receivedData.X;
        chart.data.datasets[0].data[0].y = receivedData.Y;
        chart.update();
    }
}

function chartInit(){
    chartContext = $("#chart")[0].getContext('2d');

    Chart.defaults.global.elements.point.radius = 5;

	chart = new Chart(chartContext, {
		// The type of chart: linear plot
		type: 'scatter',

		data: {
			datasets: [
			{
				label: 'Joystick coordinates',
				backgroundColor: 'rgba(255, 255, 255, 1)',
				borderColor: 'rgba(255, 255, 255, 0.75)',
				data: [{x: 0, y: 0}]
			}
			]
		},

		// Configuration options
		options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                xAxes: [{
                    gridLines: {
                        color: 'rgba(255, 255, 255, 0.5)',
                        zeroLineColor: 'rgba(255, 255, 255, 1)'
                    },
                    ticks:{
                        stepSize: 1,
                        callback: function(value){if (value % 1 === 0){return value;}},
                        suggestedMin: -5,
						suggestedMax: 5
                    }
                }],
                yAxes: [{
                    gridLines: {
                        color: 'rgba(255, 255, 255, 0.5)',
                        zeroLineColor: 'rgba(255, 255, 255, 1)'
                    },
                    ticks:{
                        stepSize: 1,
                        callback: function(value){if (value % 1 === 0){return value;}},
                        suggestedMin: -5,
						suggestedMax: 5 
                    }
                }]
            }
        }
        
    });
}