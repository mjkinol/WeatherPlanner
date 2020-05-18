function validateForm() {
    var enteredLocation = document.getElementById("locationInput").value;
    var enteredUnit = document.getElementById("unit").checked ? "Fahrenheit" : "Celsius";
    var history = localStorage.getItem("history");
    var histUnit = document.getElementById("unit").checked ? "F" : "C";
    
    if(enteredLocation == ""){
    	displayError();
    	return false;
    }
    
    var requestUrl = "/simpleweather?loc=" + enteredLocation + "&unit=" + enteredUnit;
    var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
	    	var response = JSON.parse(this.responseText);
	    	console.log(response);
	    	updateUI(response);
	    }
		else if(this.status == 400){
			displayError();
		}
	};
	
	xhttp.open("GET", requestUrl, true);
	xhttp.send();

	
	if(history == "false"){
		//add to history

		//
		var requestUrlb = "/history?location=" + enteredLocation + "," + histUnit;
	    var xhttpb = new XMLHttpRequest();
		xhttpb.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
		    
		    }
			else if(this.status == 400){
				displayError();
			}
		};
		
		xhttpb.open("POST", requestUrlb, true);
		xhttpb.send();
		//
		
		
	}
	localStorage.setItem("history", "false");
	
}

function updateUI(response){
	var locationName = response.city + ", " + response.country;
	var temp = response.currentTemp;
	var desc = response.weather;
	var x = document.getElementById("weatherdata");
	var enteredUnit = document.getElementById("unit").checked ? "°F" : "°C";
	
	document.getElementById("date").innerHTML = (new Date()).toLocaleDateString()
	document.getElementById("location").innerHTML = locationName;
	document.getElementById("weather").innerHTML = temp + enteredUnit;
	document.getElementById("description").innerHTML = desc;
	
	var img = document.getElementById("weatherIcon");
	img.src = response.weatherGraphicStatic;
	img.onmouseover = setSrc(img, response.weatherGraphicAnimate);
	img.onmouseout = setSrc(img, response.weatherGraphicStatic);
}

function setSrc(img, src){
	return function(){
		img.src = src;
	};
}

function displayError(){
	var weatherDisplay = document.getElementById("weatherdata");
	weatherDisplay.innerHTML = "<p class=\"error\">No weather data found.</p>"
}

function checkHistory(){
	//Query database to check users search history and then populate table if so
	var requestUrl = "/history";
    var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			//console.log(this.responseText);
	    	if(this.responseText == "NoUser" || this.responseText == "[]"){
	    		
	    	}
	    	else{
	    		var response = JSON.parse(this.responseText);
	    		updateHistory(response);
	    	}
	    }
		else if(this.status == 400){
			displayError();
		}
	};
	
	xhttp.open("GET", requestUrl, true);
	xhttp.send();
}

function showHistory(){
	var index = arguments[0];
	var location = histResp[index].location;
	var unit = "°" + histResp[index].unit;
	
	document.getElementById("locationInput").value = location;
	if(unit == "°F"){
		document.getElementById("unit").checked = true;
	}
	else{
		document.getElementById("unit").checked = false;
	}
	
	localStorage.history = "true";
	validateForm();
}

var histResp;

function updateHistory(response){
	if(response)
	histResp = response;
	document.getElementById("miniTitle").hidden = false;
	document.getElementById("historyResults").hidden = false;
	var inner;
	inner = "<thead><tr><th>Location</th><th>Unit</th><th></th></tr></thead><tbody>";
	var i = 0;
	for(result of response){
		inner = inner + "<tr><td>" + result.location + "</td><td>°" + result.unit + "</td><td><button type='button' onclick='showHistory("+ i + ")'>Go</button></td></tr>";
		i++;
	}
	inner = inner + "</tbody></table>";
	document.getElementById("table").innerHTML = inner;
		
}
